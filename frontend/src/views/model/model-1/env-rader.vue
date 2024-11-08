<template>
  <div>

    <Echart id="leftCenter" :options="option" class="left_center_inner" v-if="true" ref="charts" width="600px"
      height="380px" />
  </div>
</template>

<script>
export default {
  data() {
    return {
      option:{
        legend: {
          bottom: 5,
          data: ['环境数值'],
          itemGap: 20,
          textStyle: {
            color: '#fff',
            fontSize: 14
          }
        },
        tooltip: {
          trigger: 'item' // 设置触发类型为 'item'，在鼠标悬停时显示值
        },
        radar: {
          indicator: [
            { name: 'CO2', max: 1500, min: 400 },
            { name: 'TVOC', max: 1000, min: 0 },
            { name: '光照', max: 60000, min: 0 },
            { name: 'PM2.5', max: 500, min: 0 },
            { name: '声音', max: 100, min: 0 },
            { name: '温度', max: 50, min: 0 },
            { name: '湿度', max: 95, min: 20 },
          ],
          splitNumber: 5,
          axisName: {
            color: 'white',
            fontSize: '16px'
          },
          splitLine: {
            lineStyle: {
              // color: [
              //   'rgba(238, 197, 102, 0.1)',
              //   'rgba(238, 197, 102, 0.2)',
              //   'rgba(238, 197, 102, 0.4)',
              //   'rgba(238, 197, 102, 0.6)',
              //   'rgba(238, 197, 102, 0.8)',
              //   'rgba(238, 197, 102, 1)'
              // ].reverse()
            }
          },
          splitArea: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: 'white)'
            }
          }
        },
        series: [
          {
            type: 'radar',
            data: [
              {
                value: [518, 170, 28695, 38, 36, 26, 80],
                name: '环境数值',
                itemStyle: {
                  color: '#F9713C'
                },
                areaStyle: {
                  color: 'rgba(249, 113, 60, 0.3)'
                },
                symbol: 'none'
              }
            ]
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