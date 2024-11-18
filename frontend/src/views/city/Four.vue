<template>
  <div id="four">
    <div class="center_bottom" id="center_bottom" ref="centerBottom">
    </div>
  </div>
</template>

<script>

import * as echarts from 'echarts/core';
import axios from "axios";
import { baseUrl } from "@/api/api";
import {
  ToolboxComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent
} from 'echarts/components';
import { BarChart } from 'echarts/charts';
import { CanvasRenderer } from 'echarts/renderers';
export default {
  name: "Four",
  props: {
    communityName: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      options: {},
    };
  },
  mounted() {
    this.getData();
  },
  methods: {
    getData() {
      axios
          .get(`${baseUrl}/city/getHealthDataByCommunity`, {
            params: { communityName: this.communityName },
          })
          .then((res) => {
            if (res.code === "200") {
              const data = res.data;

              // 将接口数据映射为图表需要的格式
              const chartData = {
                normal: data.normal || 0,
                warn_level1: data.warn_level1 || 0,
                warn_level2: data.warn_level2 || 0,
                warn_level3: data.warn_level3 || 0,
              };

              // 调用 init 方法初始化图表
              this.init(chartData);
            } else {
              console.error("数据获取失败:", res.msg);
            }
          })
          .catch((error) => {
            console.error("API Error:", error);
          });
    },
    init(chartData) {
      echarts.use([
        ToolboxComponent,
        TooltipComponent,
        GridComponent,
        LegendComponent,
        BarChart,
        CanvasRenderer
      ]);

      let chartDom = this.$refs.centerBottom;
      let myChart = echarts.init(chartDom);

      const labelOption = {
        show: false,
        position: 'insideBottom',
        distance: 15,
        align: 'left',
        verticalAlign: 'middle',
        rotate: 90,
        formatter: '{c}  {name|{a}}',
        fontSize: 14,
        rich: {
          name: {}
        }
      };

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['正常', '一级预警', '二级预警', '三级预警'],
          textStyle: {
            fontSize: 14,
            color: "white"
          }
        },
        toolbox: {
          show: true,
          orient: 'vertical',
          left: 'right',
          top: 'center',
        },
        xAxis: [
          {
            type: 'category',
            axisTick: { show: false },
            data: ['小区'],
            axisLabel: {
              show: true,
              textStyle: {
                color: 'white',
                fontSize: 20
              }
            }
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: [
          {
            name: '正常',
            type: 'bar',
            barGap: 0,
            label: labelOption,
            emphasis: {
              focus: 'series'
            },
            data: [chartData.normal]
          },
          {
            name: '一级预警',
            type: 'bar',
            label: labelOption,
            emphasis: {
              focus: 'series'
            },
            data: [chartData.warn_level1]
          },
          {
            name: '二级预警',
            type: 'bar',
            label: labelOption,
            emphasis: {
              focus: 'series'
            },
            data: [chartData.warn_level2]
          },
          {
            name: '三级预警',
            type: 'bar',
            label: labelOption,
            emphasis: {
              focus: 'series'
            },
            data: [chartData.warn_level3]
          }
        ]
      };

      option && myChart.setOption(option);
    }
  },
}
</script>

<style lang="scss" scoped>
.center_bottom {
  padding-top: 22px;
  width: 100%;
  height: 350px;

  .echarts_bottom {
    width: 100%;
    height: 100%;
  }
}
</style>
