<template>
  <div class="center_bottom" id="center_bottom" ref="centerBottom">
<!--    <Echart-->
<!--      :options="options"-->
<!--      id="bottomLeftChart"-->
<!--      class="echarts_bottom"-->
<!--    ></Echart>-->
  </div>
</template>

<script>
import {mylog} from "@/utils";
import { currentGET } from "api";
import { graphic } from "echarts";
import * as echarts from 'echarts/core';
import {
  ToolboxComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent
} from 'echarts/components';
import { BarChart } from 'echarts/charts';
import { CanvasRenderer } from 'echarts/renderers';


export default {
  name: "centerBottom",
  data() {
    return {
      options: {},
      dataItem: {
        title: ['徐州', '西安', '郑州'],
        normal: [320, 332, 301, 334, 390],
        oneLevel: [120, 103, 101, 134, 190],
        twoLevel: [62, 184, 78, 15, 36],
        threeLevel: [258, 77, 122, 185, 164]
      }
    };
  },
  props: {
    province: {
      type: String,
      default: '',
    }
  },
  mounted() {
    this.getData();
  },
  watch: {
    province(newVal, oldVal) {
      this.province = newVal;
      if (newVal.indexOf("省") >= 0) {
        this.dataItem = {
          title: ["新福小区","阳光小区","德才小区"],
          normal: [100, 200, 300, 400],
          oneLevel: [200,100,300,500],
          twoLevel: [300,500,300,100],
          threeLevel: [300,100,400,300]
        }
        this.getData();
      }else {
        this.dataItem = {
          title: ['徐州', '西安', '郑州'],
          normal: [320, 332, 301, 334, 390],
          oneLevel: [120, 103, 101, 134, 190],
          twoLevel: [62, 184, 78, 15, 36],
          threeLevel: [258, 77, 122, 185, 164]
        }
        this.getData();
      }
    }
  },
  methods: {
    getData() {
      echarts.use([
        ToolboxComponent,
        TooltipComponent,
        GridComponent,
        LegendComponent,
        BarChart,
        CanvasRenderer
      ]);
      let app = {};

      let chartDom = this.$refs.centerBottom;
      let myChart = echarts.init(chartDom);
      let option;

      const posList = [
        'left',
        'right',
        'top',
        'bottom',
        'inside',
        'insideTop',
        'insideLeft',
        'insideRight',
        'insideBottom',
        'insideTopLeft',
        'insideTopRight',
        'insideBottomLeft',
        'insideBottomRight'
      ];
      app.configParameters = {
        rotate: {
          min: -90,
          max: 90
        },
        align: {
          options: {
            left: 'left',
            center: 'center',
            right: 'right'
          }
        },
        verticalAlign: {
          options: {
            top: 'top',
            middle: 'middle',
            bottom: 'bottom'
          }
        },
        position: {
          options: posList.reduce(function (map, pos) {
            map[pos] = pos;
            return map;
          }, {})
        },
        distance: {
          min: 0,
          max: 100
        }
      };
      app.config = {
        rotate: 90,
        align: 'left',
        verticalAlign: 'middle',
        position: 'insideBottom',
        distance: 15,
        onChange: function () {
          const labelOption = {
            rotate: app.config.rotate,
            align: app.config.align,
            verticalAlign: app.config.verticalAlign,
            position: app.config.position,
            distance: app.config.distance
          };
          myChart.setOption({
            series: [
              {
                label: labelOption
              },
              {
                label: labelOption
              },
              {
                label: labelOption
              },
              {
                label: labelOption
              }
            ]
          });
        }
      };
      const labelOption = {
        show: false,
        position: app.config.position,
        distance: app.config.distance,
        align: app.config.align,
        verticalAlign: app.config.verticalAlign,
        rotate: app.config.rotate,
        formatter: '{c}  {name|{a}}',
        fontSize: 14,
        rich: {
          name: {}
        }
      };
      option = {
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
          // feature: {
          //   mark: { show: true },
          //   dataView: { show: true, readOnly: false },
          //   magicType: { show: true, type: ['line', 'bar', 'stack'] },
          //   restore: { show: true },
          //   saveAsImage: { show: true }
          // }
        },
          xAxis: [
          {
            type: 'category',
            axisTick: { show: false },
            data: this.dataItem.title,
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

            data: this.dataItem.normal
          },
          {
            name: '一级预警',
            type: 'bar',
            label: labelOption,
            emphasis: {
              focus: 'series'
            },
            data: this.dataItem.oneLevel
          },
          {
            name: '二级预警',
            type: 'bar',
            label: labelOption,
            emphasis: {
              focus: 'series'
            },
            data: this.dataItem.twoLevel
          },
          {
            name: '三级预警',
            type: 'bar',
            label: labelOption,
            emphasis: {
              focus: 'series'
            },
            data: this.dataItem.threeLevel
          }
        ]
      };

      option && myChart.setOption(option);
    },
  },
};
</script>
<style lang="scss" scoped>
.center_bottom {
  width: 100%;
  height: 100%;

  .echarts_bottom {
    width: 100%;
    height: 100%;
  }
}
</style>
