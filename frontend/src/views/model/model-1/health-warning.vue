<template>
  <div>
    <Echart id="leftCenter" :options="option" class="left_center_inner" v-if="true" ref="charts" width="430px"
      height="300px" />
  </div>
</template>

<script>
export default {
  data() {
    return {
      option: {
        tooltip: {
          trigger: 'item'
        },
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
            name: '健康预警',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
            },
            label: {
              show: false,
              color:'white'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 18,
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 3, name: '安全' },
              { value: 5, name: '一级预警' },
              { value: 1, name: '二级预警' },
              { value: 1, name: '紧急预警' },
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