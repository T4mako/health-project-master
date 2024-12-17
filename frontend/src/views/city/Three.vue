<template>
  <div id="three" ref="three" style="top:-90px">

  </div>
</template>

<script>
import * as echarts from "echarts";
import axios from "axios";
import { baseUrl } from "@/api/api";
export default {
  name: "Three",
  props: {
    communityName: {
      type: String,
      required: true,
    },
  },
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
      axios.get(`${baseUrl}/city/getHealthDataByCommunity`, {
            params: { communityName: this.communityName }
          })
          .then((res) => {
            if (res.code === "200") {
              const data = res.data;
              // 将接口返回的数据格式化为图表需要的格式
              const ageData = [
                { value: data.age_30 || 0, name: '30岁以下' },
                { value: data.age_30_40 || 0, name: '30~40岁' },
                { value: data.age_40_50 || 0, name: '40~50岁' },
                { value: data.age_50_60 || 0, name: '50~60岁' },
                { value: data.age_60_70 || 0, name: '60~70岁' },
                { value: data.age_70 || 0, name: '70岁以上' }
              ];
              // 调用 init 方法，传入格式化后的数据
              this.init(ageData);
            } else {
              console.error("数据获取失败:", res.msg);
            }
          })
          .catch((error) => {
            console.error("API Error:", error);
          });
    },
    init(ageData) {
      // Echarts 初始化
      let chartDom = this.$refs.three;
      let myChart = echarts.init(chartDom);
      let option = {
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
            data: ageData, // 使用接口返回的年龄数据
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

      // 设置图表选项
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
