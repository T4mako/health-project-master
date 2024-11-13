<template>
    <div ref="riskForecast" class="risk-forecast"></div>
  </template>
  
  <script>
  import * as echarts from 'echarts';
  
  export default {
    name: 'RiskForecast',
    mounted() {
      this.initChart();
    },
    methods: {
      initChart() {
        let chart = echarts.init(this.$refs.riskForecast);
        let option = {
          title: { text: '风险预测', left: 'center' },
          tooltip: { trigger: 'axis' },
          legend: {
            data: ['心脏病', '糖尿病', '高血压', '关节炎', '老年痴呆', '肺病', '骨质疏松']
          },
          xAxis: {
            type: 'category',
            data: ['用户一', '用户二', '用户三', '用户四', '用户五']
          },
          yAxis: {
            type: 'value'
          },
          series: [
            { name: '心脏病', type: 'bar', data: [3, 5, 2, 4, 1] },
            { name: '糖尿病', type: 'bar', data: [4, 2, 5, 3, 6] },
            { name: '高血压', type: 'bar', data: [6, 3, 4, 2, 5] },
            { name: '关节炎', type: 'bar', data: [2, 4, 3, 5, 1] },
            { name: '老年痴呆', type: 'bar', data: [1, 3, 2, 4, 6] },
            { name: '肺病', type: 'bar', data: [5, 2, 3, 6, 4] },
            { name: '骨质疏松', type: 'bar', data: [4, 5, 3, 2, 1] }
          ]
        };
        chart.setOption(option);
      }
    }
  };
  </script>
  
  <style scoped>
  .risk-forecast {
    width: 100%;
    height: 400px;
  }
  </style>
  