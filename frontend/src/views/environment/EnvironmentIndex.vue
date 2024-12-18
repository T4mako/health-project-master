<template>
  <div class="contents">
    <div class="content_left">
      <!-- 地图 -->
      <CenterMap @childNeedChangeData="childNeedChangeData($event)"
                 :isEnvironment="isEnvironment"
                 @communityClick="handleCommunityClick" />
<!--      人物数据展示-->
      <div class="personInfo" :data="personData">
        <ul class="right_center">
          <li class="right_center_item" v-for="(item, i) in personData" :key="i" @click="getPersonDataById(item.id)">
            <span class="orderNum">{{ i + 1 }}</span>
            <div class="inner_right">
              <div class="dibu"></div>
              <div class="flex">
                <div class="info">
                  <span class="labels ">ID：</span>
                  <span class="contents zhuyao">{{ item.id }}</span>
                </div>
                <div class="info">
                  <span class="labels">性别：</span>
                  <span class="contents">{{ item.gender }}</span>
                </div>
                <div class="info">
                  <span class="labels">年龄：</span>
                  <span class="contents warning">{{ item.age }}</span>
                </div>
              </div>
              <div class="flex">
                <div class="info">
                  <span class="labels">住址：</span>
                  <span class="contents ciyao" style="font-size:12px">{{ item.address }}</span>
                </div>
              </div>
              <div class="flex">
                <div class="info">
                  <span class="labels">身体状况：</span>
                  <span class="contents ciyao" :class="{ warning: item.alertLevel }">{{ item.alertLevel || '无' }}</span>
                </div>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </div>
    <div class="content-right">
      <!-- 环境数据 -->
      <div class="env">
        <span class="titletext">环境数据</span>
        <div class="env-data">
          <div class="data-item"
               v-for="(value, key) in environmentData"
               :key="key"
               v-if="key !== 'count'">
            <span class="data-label">{{ labels[key] || key }}</span>
            <span class="data-value">{{ value || 0 }}</span>
            <span class="data-unit" v-if="units[key]">{{ units[key] }}</span>
          </div>
        </div>
        <span class="hint">{{ hintMessage }}</span>
      </div>
      <div class="analysis" style="margin-top: 20px">
        <span class="titletext">数据评估与分析</span>
        <div style="display: flex;margin-top: 20px">
        <!-- 雷达图 -->
          <div id="radarChart" style="width: 500px; height: 520px; flex: 1;"></div>
          <div style="flex: 1; padding: 10px;width: 550px">
            <div class="titletext" style="text-align: center">分析结果</div>
            <p class="titletext" style="line-height: 1.5;font-size: 20px;margin-top: 20px"> {{ analysisResults }}</p>
            <!-- 表格部分 -->
            <table style="margin: 60px auto; border-collapse: collapse; width: 100%; text-align: center;">
              <thead>
              <tr style="background-color: #f5f5f5;">
                <th style="padding: 10px; border: 1px solid #ddd;">属性</th>
                <th style="padding: 10px; border: 1px solid #ddd;">值</th>
                <th style="padding: 10px; border: 1px solid #ddd;">状态</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(status, key) in tableData" :key="key">
                <td style="padding: 10px; border: 1px solid #ddd;">{{ labels[key] || key }}</td>
                <td style="padding: 10px; border: 1px solid #ddd;">{{ environmentData[key] || '无数据' }}</td>
                <td style="padding: 10px; border: 1px solid #ddd;">{{ status }}</td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<script>

import CenterMap from "../indexs/center-map.vue";
import axios from 'axios';
import { baseUrl } from "@/api/api";
import * as echarts from 'echarts';

export default {
  components: {
    CenterMap,
  },
  computed: {
    hintMessage() {
      // 如果 province 为空，显示默认提示，否则显示具体省份名称
      return this.province ? `此数据是 ${this.province} 的数据` : '此数据是 总体 数据';
    },
  },

  data() {
    return {
      province: '', // 当前选中的省份
      isEnvironment: false, // 控制地图是否跳转
      environmentData: {}, // 动态环境数据
      tableData: {}, // 表格数据
      personData:[],
      communityName:'',
      units: {
        co: 'ppm',
        pressure: 'µg/m³',
        light: '天',
        pm25: 'µg/m³',
        pm10: 'µg/m³',
        temperature: '°C',
        humidity: '%',
        latest_date:'年-月-日'
      }, // 单位信息
      labels: {
        co: 'CO',
        pressure: '气压',
        light: '光照',
        pm25: 'PM2.5',
        pm10: 'PM10',
        temperature: '温度',
        humidity: '湿度',
        latest_date:'日期'
      } ,// 显示用的中文标签
      analysisResults: '', // 分析结果文本
      thresholds: {
        co: { normal: 400, high: 800 }, // CO阈值
        pressure: { normal: 500, high: 1500 }, //
        pm25: { normal: 35, high: 75 }, // PM2.5阈值
        pm10: { normal: 50, high: 100 }, //
        temperature: { normal: 22, high: 30 }, // 温度阈值
        humidity: { normal: 40, high: 70 } // 湿度阈值
      }
    };
  },
  filters: {
    numsFilter(msg) {
      return msg || 0;
    },
  },
  created() {
    // 页面加载时查询所有环境数据
    this.fetchEnvironmentData();
    this.getPersonData()
  },
  watch: {
    // 监听 province 的变化
    province(newProvince) {
      if (!newProvince || newProvince === '中国') {
        // 没有选择省份或选择的是 "中国"，调用全国范围方法
        this.fetchEnvironmentData();
        this.getPersonData();
      } else if (newProvince.includes('区')) {
        // 包含“区”，调用社区范围方法
        console.log('调用社区范围接口:', newProvince);
        this.getDataByCommunityName(newProvince);
        this.getPersonInfoByCommunityName(newProvince)
      } else {
        // 其他情况调用城市范围方法
        console.log('调用城市范围接口:', newProvince);
        this.fetchEnvironmentDataByCity(newProvince);
        this.getPersonDataByCity(newProvince);
      }
    },
    environmentData: {
      handler() {
        this.initRadarChart();
        this.analyzeData();
      },
      deep: true
    }
  },
  mounted() {
    this.initRadarChart();
  },
  methods: {
    // 获取所有环境数据
    async fetchEnvironmentData() {
      try {
        const response = await axios.get(`${baseUrl}/city/getEnvironmentData`);
        if (response.code === '200') {
          this.environmentData = response.data;
        } else {
          console.error('获取所有环境数据失败', response.msg);
        }
      } catch (error) {
        console.error('请求所有环境数据出错：', error);
      }
    },
    // 获取指定省份环境数据
    async fetchEnvironmentDataByCity(cityName) {
      try {
        const response = await axios.get(`${baseUrl}/city/getEnvironmentDataByCity?cityName=${cityName}`);
        if (response.code === '200') {
          this.environmentData = response.data;
        } else {
          console.error(`获取 ${cityName} 数据失败`, response.msg);
        }
      } catch (error) {
        console.error(`请求 ${cityName} 数据出错：`, error);
      }
    },
    // 获取指定社区环境数据
    getDataByCommunityName(communityName){
      axios.get(`${baseUrl}/city/getCommunityEnvironmentDataByCity`,{
        params:{communityName:communityName}
      }).then(res =>{
        if (res.code === '200'){
          this.environmentData = res.data
        } else {
          this.$Message.warning(res.msg);
        }
      })
    },
    // 根据居民id获取环境数据
    getPersonDataById(userId){
      axios.get(`${baseUrl}/city/getEnviromentByUserId`,{
        params:{Id:userId}
      }).then(res =>{
        if(res.code === '200'){
          // 格式化数据
          this.environmentData = res.data
        }else {
          this.$Message.warning(res.msg);
        }
      })
    },
    // 获取居民数据
    getPersonData(){
      axios.get(`${baseUrl}/city/getHealthDataAll`).then((res) => {
          if (res.code === "200") {
            // 格式化数据
            this.personData = res.data.map(item => ({
              id: item.id,
              gender: item.gender,
              age: item.age,
              address: item.address,
              // 将 L1, L2, L3 拼接成身体状况显示
              alertLevel: [
                item.L1.length ? `${item.L1.join(', ')} 处于一级预警状态` : '',
                item.L2.length ? `${item.L2.join(', ')} 处于二级预警状态` : '',
                item.L3.length ? `${item.L3.join(', ')} 处于三级预警状态` : ''
              ].filter(Boolean).join('；') || '无'
            }));
          } else {
            this.$Message.warning(res.msg);
          }
        })
    },
    //根据城市名字获取居民数据
    getPersonDataByCity(provinceName){
      axios.get(`${baseUrl}/city/getHealthDataAllByCityName`,{
        params:{cityName:provinceName}
      }).then(res =>{
        if (res.code === "200") {
          // 格式化数据
          this.personData = res.data.map(item => ({
            id: item.id,
            gender: item.gender,
            age: item.age,
            address: item.address,
            // 将 L1, L2, L3 拼接成身体状况显示
            alertLevel: [
              item.L1.length ? `${item.L1.join(', ')} 处于一级预警状态` : '',
              item.L2.length ? `${item.L2.join(', ')} 处于二级预警状态` : '',
              item.L3.length ? `${item.L3.join(', ')} 处于三级预警状态` : ''
            ].filter(Boolean).join('；') || '无'
          }));
        } else {
          this.$Message.warning(res.msg);
        }
      })
    },
    //根据社区名字获取居民信息
    getPersonInfoByCommunityName(communityName){
      axios.get(`${baseUrl}/city/getHealthDataByCommunityAll`,{
        params:{communityName:communityName}
      }).then(res =>{
        if (res.code === "200") {
          // 格式化数据
          this.personData = res.data.map(item => ({
            id: item.id,
            gender: item.gender,
            age: item.age,
            address: item.address,
            // 将 L1, L2, L3 拼接成身体状况显示
            alertLevel: [
              item.L1.length ? `${item.L1.join(', ')} 处于一级预警状态` : '',
              item.L2.length ? `${item.L2.join(', ')} 处于二级预警状态` : '',
              item.L3.length ? `${item.L3.join(', ')} 处于三级预警状态` : ''
            ].filter(Boolean).join('；') || '无'
          }));
        } else {
          this.$Message.warning(res.msg);
        }
      })
    },
    // 数据分析拼接成字符串输出
    analyzeData() {
      const results = {
        normal: [],
        moderate: [],
        high: []
      };
      const table = {};
      for (const [key, value] of Object.entries(this.environmentData)) {
        if (this.thresholds[key]) {
          const { normal, high } = this.thresholds[key];
          if (value <= normal) {
            results.normal.push(this.labels[key] || key);
            table[key] = '正常';
          } else if (value > normal && value <= high) {
            results.moderate.push(this.labels[key] || key);
            table[key] = '较高';
          } else {
            results.high.push(this.labels[key] || key);
            table[key] = '高';
          }
        }
      }
      // 更新表格数据
      this.tableData = table;
      // 根据结果生成分析文本
      let analysisText = '';
      if (results.normal.length) {
        analysisText += `${results.normal.join('，')}处于正常水平。`;
      }
      if (results.moderate.length) {
        analysisText += `${results.moderate.join('，')}处于较高水平。`;
      }
      if (results.high.length) {
        analysisText += `${results.high.join('，')}处于高水平。`;
      }

      this.analysisResults = analysisText || '暂无数据可分析。';
    },
    initRadarChart() {
      // 获取容器
      const radarChart = echarts.init(document.getElementById('radarChart'));

      // 过滤掉 latest_date 字段
      const filteredKeys = Object.keys(this.environmentData).filter(key => key !== 'latest_date'&&key !== 'light');

      // 准备雷达图数据
      const indicators = filteredKeys.map(key => ({
        name: this.labels[key] || key,
        max: this.getMaxValue(key) // 动态设置每项的最大值
      }));

      const dataValues = filteredKeys.map(key => this.environmentData[key]);

      // 配置雷达图
      const option = {
        tooltip: {},
        radar: {
          indicator: indicators,
          radius: '80%',
          name: {
            textStyle: {
              fontSize: 18, // 调整文字大小
              fontWeight: 'bold', // 文字加粗
              color: '#ff7f50'
            }
          }
        },
        series: [{
          name: '环境数据',
          type: 'radar',
          data: [
            {
              value: dataValues,
              name: '环境数据',
              lineStyle: {
                color: '#ff7f50', // 线条颜色（例如橙色）
                width: 3, // 线条宽度
              },
              itemStyle: {
                color: '#ff7f50', // 数据点的颜色
              },
              areaStyle: {
                opacity: 0.3, // 填充颜色透明度
              }
            }
          ]
        }]
      };

      // 使用配置生成雷达图
      radarChart.setOption(option);

      // 监听窗口大小变化，自动调整图表大小
      window.addEventListener('resize', () => radarChart.resize());
    },

    // 动态计算每项的最大值
    getMaxValue(key) {
      const predefinedMax = {
        co: 5,
        pressure: 1500,
        pm25: 100,
        pm10: 100,
        temperature: 50,
        humidity: 100
      };
      return predefinedMax[key] || 1000; // 默认最大值为 1000
    },
    handleCommunityClick(communityName) {
      // 在这里更新数据，例如记录当前选中的社区名
      this.province = communityName;
      console.log(this.province)
    },
    childNeedChangeData(name) {
      this.province = name;
      // console.log(this.province);
    }
  },
};
</script>
<style lang="scss" scoped>
@import "envIndex";
</style>
