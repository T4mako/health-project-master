<template>
  <div>

    <Echart id="leftCenter" 
    :options="option" 
    class="left_center_inner" 
    v-if="true" 
    ref="charts" 
    width="650px"
    height="380px" />
  </div>
</template>

<script>
export default {
  data() {
    return {
      option: {
        title: {
          textStyle:{
            color:'white'
          }
        },
        grid: {
          show: false
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          textStyle: {
            color: '#FFFFFF',  // legend 字体颜色
            fontSize: '14px'
        },
          data: ['呼吸率', '收缩压', '舒张压', '血氧', '温度', '心率', '血糖'],
          bottom: '5%'
        },
        xAxis: {
          type: 'category',
          axisLine:{
            lineStyle:{
              color: '#FFFFFF'
            }
          },
          nameTestStyle:{
            color: 'white'
          },
          boundaryGap: false,
          data: ['10-25', '10-26', '10-27', '10-28', '10-29', '10-30', '10-31']
        },
        yAxis: {
          type: 'value',
          axisLine:{
            lineStyle:{
              color: '#FFFFFF'
            }
          }
        },
        series: [
          {
            name: '呼吸率',
            type: 'line',

            data: [120, 132, 101, 134, 90, 230, 210]
          },
          {
            name: '收缩压',
            type: 'line',

            data: [220, 182, 191, 234, 290, 330, 310]
          },
          {
            name: '舒张压',
            type: 'line',

            data: [150, 232, 201, 154, 190, 330, 410]
          },
          {
            name: '血氧',
            type: 'line',

            data: [320, 332, 301, 334, 390, 330, 320]
          },
          {
            name: '温度',
            type: 'line',

            data: [820, 932, 901, 934, 1290, 1330, 1320]
          },
          {
            name: '心率',
            type: 'line',

            data: [200, 530, 458, 666, 777, 887, 1235]
          },
          {
            name: '血糖',
            type: 'line',
            stack: 'Total',
            data: [123, 321, 654, 425, 425, 466, 63]
          }
        ]
      }

    };
  }
};
</script>

<style scoped>
/* 可根据需要添加样式 */
</style>