<template>
  <div class="center_bottom" id="center_bottom" ref="centerBottom">

  </div>
</template>

<script>

import * as echarts from 'echarts/core';
import {
  ToolboxComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent
} from 'echarts/components';
import { BarChart } from 'echarts/charts';
import { CanvasRenderer } from 'echarts/renderers';
import axios from 'axios';  // 导入 axios 用于请求后端接口
import { baseUrl } from "@/api/api";

export default {
  name: "centerBottom",
  data() {
    return {
      options: {},
      dataItem: {
        title: [],
        normal: [],
        oneLevel: [],
        twoLevel: [],
        threeLevel: []
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
    this.fetchNationalData();  // 初始加载全国数据
    this.getData();
  },
  watch: {
    // 监听 province 属性的变化并根据新值获取相应的数据
    province(newVal) {
      if (!newVal || newVal === "中国") {  // 当 province 为空或等于 "中国" 时，获取全国数据
        this.fetchNationalData();
      } else {  // 当 province 有值且不等于 "中国" 时，获取该省份的数据
        this.fetchProvinceData(newVal);
      }
    }
  },
  methods: {
    // 获取全国数据并更新 dataItem
    fetchNationalData() {
      axios.get(`${baseUrl}/city/getHealthStatus`).then(response => {
        if (response.code === "200") {
          const data = response.data;
          this.updateDataItem(data);  // 更新 dataItem
        } else {
          console.warn("Failed to fetch national data:", responsemsg);
        }
      }).catch(error => {
        console.error("Error fetching national data:", error);
      });
    },

    // 获取指定省份的数据并更新 dataItem
    fetchProvinceData(provinceName) {
      axios.get(`${baseUrl}/city/getHealthStatusByCity`, {
        params: { cityName: provinceName }
      }).then(response => {
        if (response.code === "200") {
          const data = response.data;  // 已是数组格式
          this.updateDataItem(data);  // 更新 dataItem
        } else {
          console.warn("Failed to fetch province data:", response.msg);
        }
      }).catch(error => {
        console.error("Error fetching province data:", error);
      });
    },

    // 更新 dataItem 的数据
    updateDataItem(data) {
      const isNationalData = data[0] && data[0].hospital_name !== undefined;

      this.dataItem.title = data.map(item => {
        const name = isNationalData ? item.hospital_name : item.name;
        return name ? (name.length > 4 ? name.slice(0, 4) + "..." : name) : "未知区域";
      });

      this.dataItem.normal = data.map(item => Number(item.normal) || 0);
      this.dataItem.oneLevel = data.map(item => Number(item.warn_level1) || 0);
      this.dataItem.twoLevel = data.map(item => Number(item.warn_level2) || 0);
      this.dataItem.threeLevel = data.map(item => Number(item.warn_level3) || 0);

      this.getData();  // 调用原始 getData 方法刷新图表
    },
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
                fontSize: 16
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
