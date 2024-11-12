<template>
  <div>
    <div>
      <span style="font-size: 18px;">地区：</span>
      <el-select v-model="areaChoose" placeholder="请选择" @change="fetchData">
        <el-option v-for="item in areaOptions" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
      <span style="font-size: 18px; margin-left: 50px;">健康指标：</span>
      <el-select v-model="indicatorChoose" placeholder="请选择" @change="fetchData">
        <el-option v-for="item in indicatorOptions" :key="item.value" :label="item.label" :value="item.value">
        </el-option>
      </el-select>
    </div>
    <Echart id="leftCenter" :options="option" class="left_center_inner" ref="charts" width="650px" height="380px" />
  </div>
</template>

<script>
import axios from "axios";
import { baseUrl } from "@/api/api";

export default {
  data() {
    return {
      userHealthData:[],
      option: {
        grid: { show: false },
        tooltip: { trigger: 'axis' },
        xAxis: {
          name: '年龄',
          scale: true,
          axisLine: {
            lineStyle: { color: '#FFFFFF' }
          }
        },
        yAxis: {
          name: '指标值',
          scale: true,
          axisLine: {
            lineStyle: { color: '#FFFFFF' }
          }
        },
        series: [{
          type: 'scatter',
          itemStyle: { color: 'rgba(249, 113, 60, 0.8)' },
          data: []
        }, {
          type: 'effectScatter',
          symbolSize: 20,
          data: [
          ],
          itemStyle: {
            color: 'red' // 设置点的颜色为红色
          },
          rippleEffect: {
            color: 'red', // 设置涟漪的颜色为红色
            scale: 2.5, // 可以调整涟漪的大小，默认是2.5
            brushType: 'stroke' // 设置涟漪效果为边框，不填充
          }
        }]
      },
      areaOptions: [
        { value: 'community', label: '本社区' },
        { value: 'city', label: '本城市' },
        // { value: 'all', label: '全部' }
      ],
      areaChoose: 'community',
      indicatorOptions: [
        { value: 'breath_rate', label: '呼吸率' },
        { value: 'systolic', label: '收缩压' },
        { value: 'diastolic', label: '舒张压' },
        { value: 'blood_oxygen', label: '血氧' },
        { value: 'temperature', label: '温度' },
        { value: 'heart_rate', label: '心率' },
        { value: 'blood_glucose', label: '血糖' }
      ],
      indicatorChoose: 'systolic'
    };
  },
  created() {
    this.userId = this.$route.query.id
    
    this.fetchData();
  },
  methods: {
    fetchData() {
      const formattedIndicator = encodeURIComponent(this.indicatorChoose);
      axios
        .get(`${baseUrl}/hData/areaHDataAge`, {
          params: {
            id: this.userId,
            area: this.areaChoose,
            indicator: formattedIndicator // 编码后的指标名称
          }
        })
        .then(response => {
          if (response.code === "200") {
            const data = response.data;
            this.updateChart(data);
          } else {
            this.$Message({
              text: response.msg,
              type: 'warning'
            });
          }
        })
        .catch(error => {
          console.error(error);
          this.$Message({
            text: '获取数据失败',
            type: 'error'
          });
        });
      
        axios
        .get(`${baseUrl}/user/ageIndicator`, {
          params: {
            id: this.userId,
            indicator:this.indicatorChoose
          }
        })
        .then(response => {
          if (response.code === "200") {
            const data = response.data;
            if(data[0]){
              this.option.series[1].data = [[data[0].age,data[0][this.indicatorChoose]]]
            }
          } else {
            this.$Message({
              text: response.msg,
              type: 'warning'
            });
          }
        })
        .catch(error => {
          console.error(error);
          this.$Message({
            text: '获取数据失败',
            type: 'error'
          });
        });
    },
    updateChart(data) {
      this.option.series[0].data = data.map(item => [item.age, item[this.indicatorChoose]]);
    }
  }
};
</script>

<style scoped>
/* 可根据需要添加样式 */
</style>
