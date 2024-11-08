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
      option: {
        legend: {
          bottom: 5,
          data: ['安全指标', '实际指标'],
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
            { name: '呼吸率', max: 50, min: 0 },
            { name: '收缩压', max: 200, min: 50 },
            { name: '舒张压', max: 160, min: 30 },
            { name: '血氧', max: 100, min: 80 },
            { name: '温度', max: 45, min: 32 },
            { name: '心率', max: 220, min: 50 },
            { name: '血糖', max: 20, min: 0 }
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
                value: [5, 140, 90, 95, 37.3, 100, 7.1],
                name: '安全指标',
                lineStyle: {
                  color: 'green',
                  width: 2,
                },
                itemStyle: {
                  color: 'green'
                },
                symbol: 'none'
              },
              {
                value: [8, 136, 85, 96, 36.5, 75, 6],
                name: '实际指标',
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