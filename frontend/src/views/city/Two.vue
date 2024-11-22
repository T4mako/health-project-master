<template>
  <div id="two">
    <Echart
        id="leftCenter"
        :options="options"
        class="left_center_inner"
        v-if="pageflag"
        ref="charts"
        width="300px"
        height="300px"
        style="left: 40px;padding: 0;"
    />
    <Reacquire v-else @onclick="getData" style="line-height:200px">
      重新获取
    </Reacquire>
  </div>
</template>

<script>
import axios from "axios";
import { baseUrl } from "@/api/api";
export default {
  name: "Two",
  props: {
    communityName: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      options: {},
      countUserNumData: {},
      pageflag: true,
      timer: null
    };
  },
  created() {
    this.getData()
  },
  mounted() {
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
      axios.get(`${baseUrl}/city/getHealthDataByCommunity`, {
        params: { communityName: this.communityName }
      })
          .then((res) => {
            if (res.code === "200") {
              const data = res.data;
              console.log("Fetched Data:", data); // 调试用
              this.countUserNumData = {
                lockNum: parseInt(data.male) || 0, // 确保为整数
                onlineNum: parseInt(data.female) || 0, // 确保为整数
                totalNum: (parseInt(data.male) || 0) + (parseInt(data.female) || 0), // 确保总数正确
              };
              this.init();
            } else {
              this.pageflag = false;
              this.$Message.warning(res.msg);
            }
          })
          .catch((error) => {
            console.error("API Error:", error);
            this.pageflag = false;
          });
    },

    init() {
      let total = this.countUserNumData.totalNum;
      let colors = ["#ECA444", "#33A1DB"]; // 男性和女性颜色
      let piedata = {
        name: "用户总览",
        type: "pie",
        radius: ["42%", "65%"],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 4,
          borderColor: "rgba(0,0,0,0)",
          borderWidth: 2,
        },
        color: colors,
        data: [
          {
            value: this.countUserNumData.lockNum,
            name: "男性",
            label: {
              shadowColor: colors[0], // 修复颜色访问
            },
          },
          {
            value: this.countUserNumData.onlineNum,
            name: "女性",
            label: {
              shadowColor: colors[1], // 修复颜色访问
            },
          },
        ],
      };

      this.options = {
        title: {
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
          {
            ...piedata,
            tooltip: { show: true },
            label: {
              formatter: "{per|{d}%}",
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
              length: 20,
              length2: 36,
              show: false,
            },
          },
        ],
      };
    },

  },
}
</script>

<style lang='scss' scoped>
</style>
