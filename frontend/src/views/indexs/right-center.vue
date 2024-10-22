<!--
 * @Author: daidai
 * @Date: 2022-03-01 15:51:43
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-09-29 15:12:46
 * @FilePath: \web-pc\src\pages\big-screen\view\indexs\right-bottom.vue
-->
<template>
  <div class="right_ottom" ref="rightBottom">
<!--    <dv-capsule-chart :config="config" style="width:100%;height:260px" />-->
  </div>
</template>

<script>
import { currentGET } from 'api/modules'
import {mylog} from "@/utils";
import * as echarts from 'echarts';
export default {
  props: {
    province: {
      type: String,
      default: '',
    }
  },
  name: "rightCenter",
  data() {
    return {
      gatewayno: '',
      config: {
        showValue: true,
        unit: "人",
        data: []
      },
      dataItem: [
        { value: 1048, name: '30岁以下' },
        { value: 735, name: '30~40岁' },
        { value: 580, name: '40~50岁' },
        { value: 484, name: '50~60岁' },
        { value: 300, name: '60~70岁' },
        { value: 300, name: '70岁以上' }
      ]
    };
  },
  computed: {
  },
  mounted() {
    this.getData();
  },
  watch: {
    province(newVal, oldVal) {
      this.province = newVal;
      if (newVal.indexOf("省") >= 0) {

        this.dataItem = [
          { value: 234, name: '30岁以下' },
          { value: 111, name: '30~40岁' },
          { value: 342, name: '40~50岁' },
          { value: 212, name: '50~60岁' },
          { value: 321, name: '60~70岁' },
          { value: 654, name: '70岁以上' }
        ]
        this.getData();
      }else {
        this.dataItem = [
          { value: 1048, name: '30岁以下' },
          { value: 735, name: '30~40岁' },
          { value: 580, name: '40~50岁' },
          { value: 484, name: '50~60岁' },
          { value: 300, name: '60~70岁' },
          { value: 300, name: '70岁以上' }
        ]
        this.getData();
      }
    }
  },
  methods: {
    getData() {
      let app = {};

      let chartDom = this.$refs.rightBottom;
      let myChart = echarts.init(chartDom);
      let option;

      option = {

        tooltip: {
          trigger: 'item',
        },
        legend: {
          orient: 'vertical',
          left: 'left',
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
            data: this.dataItem,
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
};
</script>
<style lang='scss' scoped>
.right_ottom {
  height: 500px;
}

</style>
