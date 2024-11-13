<template>
  <div ref="lineChart" class="line-chart"></div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'BarChart',
  mounted() {
    this.initChart();
  },
  methods: {
    initChart() {
      let chart = echarts.init(this.$refs.lineChart);
      let option = {
        title: { text: '', left: 'center' },
        tooltip: { trigger: 'axis' },
        xAxis: {
          type: 'category',
          data: ['01月', '02月', '03月', '04月', '05月', '06月', '07月', '08月', '09月', '10月', '11月', '12月']
        },
        yAxis: {
          type: 'value',
          min: 0,
          max: 2000, // 设置最大值
          axisLabel: {
            formatter: '{value}'
          }
        },
        series: [
          {
            name: '人数',
            type: 'line', // 改为折线图
            data: [500, 700, 800, 600, 900, 1100, 1500, 1300, 1200, 1000, 900, 800],
            smooth: true, // 使曲线平滑
            areaStyle: {
              color: 'rgba(0, 150, 255, 0.3)' // 设置填充颜色
            },
            itemStyle: {
              color: 'blue' // 设置线条颜色
            }
          }
        ]
      };
      chart.setOption(option);
    }
  }
};
</script>

<style scoped>
.line-chart {
  width: 100%;
  height: 400px;
}
</style>