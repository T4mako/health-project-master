<!--
 * @Author: daidai
 * @Date: 2022-02-28 16:16:42
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-09-27 15:16:12
 * @FilePath: \web-pc\src\pages\big-screen\view\indexs\left-center.vue
-->
<template>
  <Echart
      id="leftCenter"
      :options="options"
      class="left_center_inner"
      v-if="pageflag"
      ref="charts"
      width="500px"
  />
  <Reacquire v-else @onclick="getData" style="line-height:200px">
    重新获取
  </Reacquire>
</template>

<script>
import {currentGET} from 'api/modules'
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
      options: {},
      countUserNumData: {
        lockNum: 902.8,
        onlineNum: 1316.3,
        offlineNum: 1274.2,
        totalNum: 0
      },
      dataItems: [{
        value: 902.8,
        name: "徐州",
        label: {
          shadowColor: "#ECA444",
        },
      }, {
        value: 1316.3,
        name: "西安",
        label: {
          shadowColor: "#33A1DB",
        },
      }, {
        value: 1274.2,
        name: "郑州",
        label: {
          shadowColor: "#56B557"
        },
      }],
      pageflag: true,
      timer: null
    };
  },
  created() {
    this.getData()
  },
  mounted() {

  },
  watch: {
    province(newVal, oldVal) {
      this.province = newVal;
      if (newVal.indexOf("省") >= 0) {
        this.dataItems = [
          {
            value: 902.8,
            name: "徐州",
            label: {
              shadowColor: "#ECA444",
            },
          },
          {
            value: 3,
            name: "小区个数",
            label: {
              shadowColor: "#e04444"
            }
          }
        ]
        this.init();
      }else {
        console.log("进中国");
        this.dataItems = [{
          value: 902.8,
          name: "徐州",
          label: {
            shadowColor: "#ECA444",
          },
        }, {
          value: 1316.3,
          name: "西安",
          label: {
            shadowColor: "#33A1DB",
          },
        }, {
          value: 1274.2,
          name: "郑州",
          label: {
            shadowColor: "#56B557"
          },
        }];
      }
    }

  },
  beforeDestroy() {
    this.clearData()

  },
  methods: {
    clearData() {
      if (this.timer) {
        clearInterval(this.timer)
        this.timer = null
      }
    },
    getData() {
      this.pageflag = true
      // this.pageflag =false

      currentGET('big1').then(res => {
        //只打印一次
        if (!this.timer) {
          console.log("设备总览", res);
        }
        if (res.success) {
          let data = this.countUserNumData;
          data.totalNum = data.lockNum + data.onlineNum + data.offlineNum;
          this.countUserNumData = data
          this.$nextTick(() => {
            this.init()
            this.switper()
          })

        } else {
          this.pageflag = false
          this.$Message({
            text: res.msg,
            type: 'warning'
          })
        }
      })
    },
    //轮询
    switper() {
      if (this.timer) {
        return
      }
      let looper = (a) => {
        this.getData()
      };
      this.timer = setInterval(looper, this.$store.state.setting.echartsAutoTime);
      let myChart = this.$refs.charts.chart
      myChart.on('mouseover', params => {
        this.clearData()
      });
      myChart.on('mouseout', params => {
        this.timer = setInterval(looper, this.$store.state.setting.echartsAutoTime);
      });
    },
    init: function () {
      const data = this.dataItems;
      let total = 0
      data.forEach(d => total += d.value)
      let colors = ["#ECA444", "#33A1DB", "#56B557"];
      let piedata = {
        name: "数据",
        type: "pie",
        radius: ["42%", "65%"],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 4,
          borderColor: "rgba(0,0,0,0)",
          borderWidth: 2,
        },

        color: colors,
        data
      };
      this.options = {
        title: {
          // zlevel: 0,
          text: ["{value|" + total + "}", "{name|总数}"].join("\n"),
          top: "center",
          left: "center",
          textStyle: {
            rich: {
              value: {
                color: "#ffffff",
                fontSize: 24,
                fontWeight: "bold",
                lineHeight: 20,
              },
              name: {
                color: "#ffffff",
                lineHeight: 20,
              },
            },
          },
        },
        tooltip: {
          trigger: "item",
          backgroundColor: "rgba(0,0,0,.6)",
          borderColor: "rgba(147, 235, 248, .8)",
          textStyle: {
            color: "#FFF",
          },
        },
        legend: {
          show: false,
          top: "5%",
          left: "center",
        },
        series: [
          //展示圆点
          {
            ...piedata,
            tooltip: {show: true},
            label: {
              formatter: "{per|{d}%}",
              //   position: "outside",
              rich: {
                b: {
                  color: "#fff",
                  fontSize: 12,
                  lineHeight: 26,
                },
                c: {
                  color: "#31ABE3",
                  fontSize: 14,
                },
                per: {
                  color: "#31ABE3",
                  fontSize: 14,
                },
              },
            },
            labelLine: {
              length: 20, // 第一段线 长度
              length2: 36, // 第二段线 长度
              show: true,

            },
            emphasis: {
              show: true,
            },
          },
          {
            ...piedata,
            tooltip: {show: true},
            itemStyle: {},
            label: {
              backgroundColor: "inherit", //圆点颜色，auto：映射的系列色
              height: 0,
              width: 0,
              lineHeight: 0,
              borderRadius: 2.5,
              shadowBlur: 8,
              shadowColor: "auto",
              padding: [2.5, -2.5, 2.5, -2.5],
            },
            labelLine: {
              length: 20, // 第一段线 长度
              length2: 36, // 第二段线 长度
              show: false,
            },
          },
        ],
      };
    },
  },
};
</script>
<style lang='scss' scoped>
</style>
