<template>
  <div>
    <Echart :options="option" class="left_center_inner" v-if="true" ref="charts" width="430px"
      height="300px" />
  </div>
</template>

<script>
export default {
  props: ['bmi'],
  data() {
    return {
      option: {
        legend: {
          borderWidth: 0,
          left: 'left',
          textStyle: {
            color: '#fff',
            fontSize: 14
          }
        },
        series: [
          {
            type: 'gauge',
            startAngle: 200,
            endAngle: -20,
            min: 10,
            max: 50,
            splitNumber: 8,
            axisLine: {
              lineStyle: {
                width: 24,
                color: [
                  [0.2125, '#67e0e3'], // 体重过轻 (BMI < 18.5)
                  [0.35, '#91cc75'],   // 健康 (18.5 <= BMI < 24)
                  [0.5, '#fac858'],    // 轻度肥胖 (24 <= BMI < 30)
                  [1, '#fd666d']       // 重度肥胖 (BMI >= 35)
                ]
              }
            },
            pointer: {
              itemStyle: {
                color: 'auto'
              }
            },
            axisTick: {
              distance: 0,
              length: 8,
              lineStyle: {
                color: '#fff',
                width: 2
              }
            },
            splitLine: {
              distance: -30,
              length: 30,
              lineStyle: {
                color: '#fff',
                width: 0
              }
            },
            axisLabel: {
              color: 'inherit',
              distance: 40,
              fontSize: 10
            },
            detail: {
              valueAnimation: true,
              formatter: function (value) {
                let status = '';
                if (value < 18.5) {
                  status = '体重过轻';
                } else if (value < 24) {
                  status = '健康';
                } else if (value < 30) {
                  status = '轻度肥胖';
                } else {
                  status = '重度肥胖';
                }
                return `BMI: ${value.toFixed(1)}\n${status}`;
              },
              color: 'inherit',
              fontSize: 18,
              offsetCenter: [0, '70%']
            },
            data: [
              {
                value: this.bmi
              }
            ]
          }
        ]
      }
    };
  },
  watch: {
    bmi(newValue) {
      // 监控 bmi 值变化，并更新图表数据
      this.option.series[0].data[0].value = newValue;
    }
  },
  mounted() {
    // 初始化图表数据
    this.option.series[0].data[0].value = this.bmi;
  }
};
</script>

<style scoped>
/* 可根据需要添加样式 */
</style>
