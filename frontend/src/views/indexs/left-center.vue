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

import axios from "axios";
import {baseUrl} from "@/api/api";

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
      colors: ["#ECA444", "#33A1DB", "#56B557"], // 固定颜色数组
      countUserNumData: {
        lockNum: 902.8,
        onlineNum: 1316.3,
        offlineNum: 1274.2,
        totalNum: 0
      },
      dataItems: [],
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
    province(newVal) {
      if (!newVal || newVal === "中国") {  // 当 province 为空或等于 "中国" 时，获取全国数据
        this.getData();
      } else {  // 当 province 有值且不等于 "中国" 时，获取该省份的数据
        this.getProvinceData(newVal);
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
      this.pageflag = true;
      axios.get(`${baseUrl}/city/getCityNameAndNum`).then(response => {
            const data = response.data;
        if (response.code === "200") {
              // 使用返回的数据更新 dataItems
          this.dataItems = data.map((item, index) => ({
            value: item.total_count,
            name: item.city_name,
            label: {
              shadowColor: this.colors[index % this.colors.length],
            }
          }));

          this.init();  // 初始化图表
            } else {
              this.pageflag = false;
              this.$Message({
                text: response.data.msg,
                type: 'warning'
              });
            }
          })
          .catch(error => {
            console.error(error);
            this.pageflag = false;
            this.$Message({
              text: '获取数据失败',
              type: 'error'
            });
          });
    },
    // 根据 province 获取特定省份的数据
    getProvinceData(provinceName) {
      if (!provinceName) return; // 确保省份名称有效
      axios.get(`${baseUrl}/city/getNumByCityName?cityName=${provinceName}`).then(response => {
            const data  = response.data;
            if (response.code === "200") {
              this.dataItems = [{
                value: data,
                name: provinceName,
                label: {
                  shadowColor: this.colors[Math.floor(Math.random() * this.colors.length)],
                }
              }]
              this.init();  // 重新初始化图表
            } else {
              this.pageflag = false;
              this.$Message({
                text: response.data.msg,
                type: 'warning'
              });
            }
          })
          .catch(error => {
            console.error(error);
            this.pageflag = false;
            this.$Message({
              text: '获取省份数据失败',
              type: 'error'
            });
          });
    },
    init: function () {
      const data = this.dataItems;
      let total = 0
      data.forEach(d => total += d.value)
      let colors = this.colors
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
