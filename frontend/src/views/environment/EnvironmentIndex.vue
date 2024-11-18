<template>
  <div class="contents">
    <div class="content_left">
      <!-- 地图 -->
      <CenterMap class="content_left_top"
                 @childNeedChangeData="childNeedChangeData($event)"
                 :isEnvironment="isEnvironment"
                 @communityClick="handleCommunityClick" />
      <div> 总户数展示 </div>
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
      return this.province ? `此数据是 ${this.province} 数据的平均值` : '此数据是 中国 数据的平均值';
    },
  },

  data() {
    return {
      province: '', // 当前选中的省份
      isEnvironment: false, // 控制地图是否跳转
      environmentData: {}, // 动态环境数据
      tableData: {}, // 表格数据
      units: {
        co2: 'ppm',
        tvoc: 'µg/m³',
        light: 'lx',
        pm25: 'µg/m³',
        db: '分贝',
        temperature: '°C',
        humidity: '%',
      }, // 单位信息
      labels: {
        co2: 'CO₂',
        tvoc: 'TVOC',
        light: '光照',
        pm25: 'PM2.5',
        db: '声音',
        temperature: '温度',
        humidity: '湿度'
      } ,// 显示用的中文标签
      analysisResults: '', // 分析结果文本
      thresholds: {
        co2: { normal: 400, high: 800 }, // CO2阈值
        tvoc: { normal: 200, high: 600 }, // TVOC阈值
        light: { normal: 300, high: 700 }, // 光照阈值
        pm25: { normal: 35, high: 75 }, // PM2.5阈值
        db: { normal: 50, high: 80 }, // 声音阈值
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
  },
  watch: {
    // 监听 province 的变化
    province(newProvince) {
      if (!newProvince || newProvince === '中国') {
        this.fetchEnvironmentData();
      } else {
        this.fetchEnvironmentDataByCity(newProvince);
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

      // 准备雷达图数据
      const indicators = Object.keys(this.environmentData).filter(key => key !== 'count'&&key !== 'latest_time').map(key => ({
        name: this.labels[key] || key,
        max: this.getMaxValue(key) // 动态设置每项的最大值
      }));

      const dataValues = Object.keys(this.environmentData).filter(key => key !== 'count'&& key !== 'latest_time').map(key => this.environmentData[key]);

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
              color:'#ff7f50'
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
              lineStyle:{
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
        CO2: 1000,
        TVOC: 1200,
        light: 1000,
        PM25: 500,
        sound: 100,
        temperature: 50,
        humidity: 100
      };
      return predefinedMax[key] || 1000; // 默认最大值为 1000
    },
    handleCommunityClick(communityName) {
      // 在这里更新数据，例如记录当前选中的社区名
      this.province = communityName;

    },
    childNeedChangeData(name) {
      this.province = name;
      console.log(this.province);
    }
  },
};
</script>
<style lang="scss" scoped>
.contents {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;

  .content_left {
    width: 720px;
    box-sizing: border-box;
  }
  .content-right {
    width: 1400px;
    box-sizing: border-box;
    padding: 16px;
    margin-top: 52px;
    display: flex;
    flex-direction: column;
    justify-content: center; /* 垂直居中 */
    align-items: flex-start; /* 左对齐 */
    gap: 20px; /* 两部分之间的间距 */
  }

  .env, .analysis {
    width: 98%;
    padding: 20px;
    border-radius: 10px;
    position: relative;
    border: 1px solid #6586ec;
    display: flex;
    flex-direction: column;
    align-items: center; /* 水平居中内容 */

    .titletext {
      font-size: 24px;
      font-weight: 900;
      letter-spacing: 4px;
      background: linear-gradient(92deg, #0072ff 0%, #00eaff 48.85%, #01aaff 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      margin-bottom: 16px;
    }
  }
}
.hint{
  margin-top: 15px;
  letter-spacing: 4px;
  background: linear-gradient(92deg, #0072ff 0%, #00eaff 48.85%, #01aaff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin-bottom: 5px;
}
.env-data {
  display: flex;
  gap: 15px;
  margin-top: 15px;
}
.data-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px;
  border: 1px solid #00eded;
  border-radius: 5px;
  min-width: 80px;
  text-align: center;

  .data-label {
    font-size: 14px;
    color: #00eded;
    margin-bottom: 5px;
  }

  .data-value {
    font-size: 24px;
    font-weight: bold;
    color: #00eded;
  }

  .data-unit {
    font-size: 12px;
    color: #00eded;
  }
}
@keyframes rotating {
  0% {
    -webkit-transform: rotate(0) scale(1);
    transform: rotate(0) scale(1);
  }
  50% {
    -webkit-transform: rotate(180deg) scale(1.1);
    transform: rotate(180deg) scale(1.1);
  }
  100% {
    -webkit-transform: rotate(360deg) scale(1);
    transform: rotate(360deg) scale(1);
  }
}
table {
  margin: 20px auto; /* 表格水平居中 */
  border-collapse: collapse; /* 合并边框 */
  width: 100%; /* 表格宽度 */
  text-align: center; /* 文字居中 */
  color: #00eded; /* 文字颜色 */
}

th, td {
  padding: 10px;
  border: 1px solid #00eded; /* 边框颜色 */
}

thead tr {
  background-color: #002b36 !important; /* 添加 !important 确保样式优先 */
}

tbody tr:nth-child(even) {
  background-color: rgba(0, 237, 237, 0.05); /* 偶数行背景颜色 (透明度为 5%) */
}

tbody tr:hover {
  background-color: rgba(0, 237, 237, 0.2); /* 鼠标悬停高亮背景 (透明度为 20%) */
}

</style>
