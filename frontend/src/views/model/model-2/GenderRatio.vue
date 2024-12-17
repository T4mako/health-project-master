<template>
  <div class="gender-ratio-container">
      <div ref="genderRatio" class="gender-ratio"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'GenderRatio',
  mounted() {
      this.initChart();
  },
  methods: {
      initChart() {
          let chart = echarts.init(this.$refs.genderRatio);
          let option = {
              title: {
                  text: '',
                  left: 'center'
              },
              tooltip: {
                  trigger: 'item'
              },
              legend: {
                  data: ['个人指标', '社区平均指标', '健康指标'],
                  bottom: 10
              },
              xAxis: {
                  type: 'category',
                  data: ['血糖', '血脂', '血压']
              },
              yAxis: {
                  type: 'value'
              },
              series: [
                  {
                      name: '个人指标',
                      type: 'bar',
                      data: [70, 80, 75], // 示例数据
                      itemStyle: {
                          color: '#ff4d4f' // 红色
                      }
                  },
                  {
                      name: '社区平均指标',
                      type: 'bar',
                      data: [65, 70, 72], // 示例数据
                      itemStyle: {
                          color: '#2196F3' // 蓝色
                      }
                  },
                  {
                      name: '健康指标',
                      type: 'bar',
                      data: [75, 78, 76], // 示例数据
                      itemStyle: {
                          color: '#4caf50' // 绿色
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
.gender-ratio-container {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center; /* 垂直居中 */
  height: 100%; /* 确保容器有高度 */
}

.gender-ratio {
  width: 100%;
  height: 400px;
}
</style>