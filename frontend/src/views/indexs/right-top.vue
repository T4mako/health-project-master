<!--
 * @Author: daidai
 * @Date: 2022-03-01 14:13:04
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-09-27 15:04:49
 * @FilePath: \web-pc\src\pages\big-screen\view\indexs\right-top.vue
-->
<template>
  <Echart
    id="rightTop"
    :options="option"
    class="right_top_inner"
    v-if="pageflag"
    ref="charts"
  />
  <Reacquire v-else @onclick="getData" style="line-height: 200px">
    重新获取
  </Reacquire>
</template>

<script>
import axios from 'axios';  // 导入 axios 用于请求后端接口
import { baseUrl } from "@/api/api";
import {graphic} from "echarts"
import {mylog} from "@/utils";
export default {
  props: {
    province: {
      type: String,
      default: '',
    }
  },
  data() {
    return {
      option: {},
      pageflag: false,
      timer: null,
      dataItem: {
        title: ['2021-11', '2021-12', '2022-01', '2022-02', '2022-03','2022-04'],
        oneLevel: [270, 49, 670, 123, 684, 747],
        twoLevel: [488, 6, 771, 465, 315, 14],
        threeLevel: [120, 423, 123, 676, 342, 543]
      }
    };
  },
  created() {

  },

  mounted() {
    this.updateData();
  },
  beforeDestroy() {
    this.clearData();
  },
  watch: {
    province() {
      this.updateData();
    }
  },
  methods: {
    // 根据省份判断调用哪个接口
    updateData() {
      if (this.province && this.province !== "中国") {
        this.getProvinceData();
      } else {
        this.getData();
      }
    },
// 获取全国数据
    getData() {
      this.pageflag = true;
      axios.get(`${baseUrl}/city/getHealthLevel`).then((response) => {
        if (response.code === "200") {
          this.updateDataItem(response.data);
        } else {
          this.pageflag = false;
          this.$Message({
            text: response.msg,
            type: "warning",
          });
        }
      }).catch((error) => {
        console.error("Error fetching data:", error);
        this.pageflag = false;
      });
    },

    // 获取省份数据
    getProvinceData() {
      this.pageflag = true;
      axios.get(`${baseUrl}/city/getHealthLevelByCity?cityName=${this.province}`).then((response) => {
        if (response.code === "200") {
          this.updateDataItem(response.data);
        } else {
          this.pageflag = false;
          this.$Message({
            text: response.msg,
            type: "warning",
          });
        }
      }).catch((error) => {
        console.error("Error fetching data:", error);
        this.pageflag = false;
      });
    },

    // 更新图表数据项
    updateDataItem(data) {
      this.dataItem.title = data.map(item => item.month);
      this.dataItem.oneLevel = data.map(item => item.L1_count);
      this.dataItem.twoLevel = data.map(item => item.L2_count);
      this.dataItem.threeLevel = data.map(item => item.L3_count);

      this.init(); // 调用图表初始化方法
    },
    clearData() {
      if (this.timer) {
        clearInterval(this.timer);
        this.timer = null;
      }
    },

    init() {
      this.option = {
        xAxis: {
          type: "category",
          data: this.dataItem.title,
          boundaryGap: false, // 不留白，从原点开始
          splitLine: {
            show: true,
            lineStyle: {
              color: "rgba(31,99,163,.2)",
            },
          },
          axisLine: {
            // show:false,
            lineStyle: {
              color: "rgba(31,99,163,.1)",
            },
          },
          axisLabel: {
            color: "#7EB7FD",
            fontWeight: "500",
          },
        },
        yAxis: {
          type: "value",
          splitLine: {
            show: true,
            lineStyle: {
              color: "rgba(31,99,163,.2)",
            },
          },
          axisLine: {
            lineStyle: {
              color: "rgba(31,99,163,.1)",
            },
          },
          axisLabel: {
            color: "#7EB7FD",
            fontWeight: "500",
          },
        },
        tooltip: {
          trigger: "axis",
          backgroundColor: "rgba(0,0,0,.6)",
          borderColor: "rgba(147, 235, 248, .8)",
          textStyle: {
            color: "#FFF",
          },
        },
        grid: {
          //布局
          show: true,
          left: "10px",
          right: "30px",
          bottom: "10px",
          top: "28px",
          containLabel: true,
          borderColor: "#1F63A3",
        },
        series: [
          {
            data: this.dataItem.oneLevel,
            type: "line",
            smooth: true,
            symbol: "none", //去除点
            name: "一级预警数",
            color: "rgba(252,144,16,.7)",
            areaStyle: {
                //右，下，左，上
                color: new graphic.LinearGradient(
                  0,
                  0,
                  0,
                  1,
                  [
                    {
                      offset: 0,
                      color: "rgba(252,144,16,.7)",
                    },
                    {
                      offset: 1,
                      color: "rgba(252,144,16,.0)",
                    },
                  ],
                  false
                ),
            },
            markPoint: {
              data: [
                {
                  name: "最大值",
                  type: "max",
                  valueDim: "y",
                  symbol: "rect",
                  symbolSize: [60, 26],
                  symbolOffset: [0, -20],
                  itemStyle: {
                    color: "rgba(0,0,0,0)",
                  },
                  label: {
                    color: "#FC9010",
                    backgroundColor: "rgba(252,144,16,0.1)",
                    borderRadius: 6,
                    padding: [7, 14],
                    borderWidth: 0.5,
                    borderColor: "rgba(252,144,16,.5)",
                    formatter: "一级预警：{c}",
                  },
                },
                {
                  name: "最大值",
                  type: "max",
                  valueDim: "y",
                  symbol: "circle",
                  symbolSize: 6,
                  itemStyle: {
                    color: "#FC9010",
                    shadowColor: "#FC9010",
                    shadowBlur: 8,
                  },
                  label: {
                    formatter: "",
                  },
                },
              ],
            },
          },
          {
            data: this.dataItem.twoLevel,
            type: "line",
            smooth: true,
            symbol: "none", //去除点
            name: "二级预警次数",
            color: "rgba(9,202,243,.7)",
            areaStyle: {
                //右，下，左，上
                color: new graphic.LinearGradient(
                  0,
                  0,
                  0,
                  1,
                  [
                    {
                      offset: 0,
                      color: "rgba(9,202,243,.7)",
                    },
                    {
                      offset: 1,
                      color: "rgba(9,202,243,.0)",
                    },
                  ],
                  false
                ),
            },
            markPoint: {
              data: [
                {
                  name: "最大值",
                  type: "max",
                  valueDim: "y",
                  symbol: "rect",
                  symbolSize: [60, 26],
                  symbolOffset: [0, -20],
                  itemStyle: {
                    color: "rgba(0,0,0,0)",
                  },
                  label: {
                    color: "#09CAF3",
                    backgroundColor: "rgba(9,202,243,0.1)",

                    borderRadius: 6,
                    borderColor: "rgba(9,202,243,.5)",
                    padding: [7, 14],
                    formatter: "二级预警：{c}",
                    borderWidth: 0.5,
                  },
                },
                {
                  name: "最大值",
                  type: "max",
                  valueDim: "y",
                  symbol: "circle",
                  symbolSize: 6,
                  itemStyle: {
                    color: "#09CAF3",
                    shadowColor: "#09CAF3",
                    shadowBlur: 8,
                  },
                  label: {
                    formatter: "",
                  },
                },
              ],
            },
          },
          {
            data: this.dataItem.threeLevel,
            type: "line",
            smooth: true,
            symbol: "none", //去除点
            name: "三级级预警次数",
            color: "rgba(212,0,0,0.7)",
            areaStyle: {
              //右，下，左，上
              color: new graphic.LinearGradient(
                  0,
                  0,
                  0,
                  1,
                  [
                    {
                      offset: 0,
                      color: "rgba(231,22,22,0.7)",
                    },
                    {
                      offset: 1,
                      color: "rgba(9,202,243,.0)",
                    },
                  ],
                  false
              ),
            },
            markPoint: {
              data: [
                {
                  name: "最大值",
                  type: "max",
                  valueDim: "y",
                  symbol: "rect",
                  symbolSize: [60, 26],
                  symbolOffset: [0, -20],
                  itemStyle: {
                    color: "rgba(0,0,0,0)",
                  },
                  label: {
                    color: "#ff276d",
                    backgroundColor: "rgba(255,14,14,0.1)",

                    borderRadius: 6,
                    borderColor: "rgba(255,46,46,0.5)",
                    padding: [7, 14],
                    formatter: "三级预警：{c}",
                    borderWidth: 0.5,
                  },
                },
                {
                  name: "最大值",
                  type: "max",
                  valueDim: "y",
                  symbol: "circle",
                  symbolSize: 6,
                  itemStyle: {
                    color: "#ff0000",
                    shadowColor: "#ff3434",
                    shadowBlur: 8,
                  },
                  label: {
                    formatter: "",
                  },
                },
              ],
            },
          },
        ],
      };
    },
  },
};
</script>
<style lang='scss' scoped>
.right_top_inner {
  margin-top: -8px;
}
</style>
