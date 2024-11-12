<template>
    <div ref="heatmap" class="heatmap"></div>
  </template>
  
  <script>
  import * as echarts from 'echarts';
  
  export default {
    name: 'Heatmap',
    mounted() {
      this.initChart();
    },
    methods: {
      initChart() {
        let chart = echarts.init(this.$refs.heatmap);
        let option = {
          title: { text: '风险分析', left: 'center' },
          tooltip: {},
          xAxis: {
            type: 'category',
            data: ['心脏病', '糖尿病', '高血压', '关节炎', '老年痴呆', '肺病', '骨质疏松']
          },
          yAxis: {
            type: 'category',
            data: ['用户一', '用户二', '用户三', '用户四', '用户五']
          },
          visualMap: {
            min: 0,
            max: 10,
            calculable: true,
            orient: 'horizontal',
            left: 'center',
            bottom: '15%'
          },
          series: [{
            name: '风险评分',
            type: 'heatmap',
            data: [
              [0, 0, 5], [1, 0, 3], [2, 0, 1], [3, 0, 2], [4, 0, 6], [5, 0, 4], [6, 0, 0],
              [0, 1, 2], [1, 1, 5], [2, 1, 3], [3, 1, 4], [4, 1, 7], [5, 1, 1], [6, 1, 2]
            ],
            label: { show: true }
          }]
        };
        chart.setOption(option);
      }
    }
  };
  </script>
  
  <style scoped>
  .heatmap {
    width: 100%;
    height: 400px;
  }
  </style>