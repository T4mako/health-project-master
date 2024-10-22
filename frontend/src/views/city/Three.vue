<template>
  <div id="three" ref="three" style="top:-90px">

  </div>
</template>

<script>
import * as echarts from "echarts";

export default {
  name: "Three",
  data() {
    return {
      gatewayno: '',
      config: {
        showValue: true,
        unit: "人",
        data: []
      },

    };
  },
  computed: {
  },
  mounted() {
    this.getData();
  },
  methods: {
    getData() {
      let app = {};

      let chartDom = this.$refs.three;
      let myChart = echarts.init(chartDom);
      let option;

      option = {

        tooltip: {
          trigger: 'item',
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          show: false,
          textStyle: {
            fontSize: 12,
            color: "white"
          }
        },

        series: [
          {
            name: '年龄',
            type: 'pie',
            radius: '50%',
            data: [
              { value: 1048, name: '30岁以下' },
              { value: 735, name: '30~40岁' },
              { value: 580, name: '40~50岁' },
              { value: 484, name: '50~60岁' },
              { value: 300, name: '60~70岁' },
              { value: 300, name: '70岁以上' }
            ],
            label: {
              color: 'white',
              fontSize: 14
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(255,255,255,0.5)',
              }
            },
          }
        ]
      };

      option && myChart.setOption(option);

    }
  },
}
</script>

<style lang='scss' scoped>
#three {
  height: 500px;
}

</style>
