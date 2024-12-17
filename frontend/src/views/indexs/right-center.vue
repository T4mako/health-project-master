
<template>
  <div class="right_ottom" ref="rightBottom">

  </div>
</template>

<script>
import axios from 'axios';  // 导入 axios 用于请求后端接口
import { baseUrl } from "@/api/api";
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
      dataItem: []
    };
  },
  computed: {
  },
  mounted() {
    this.fetchData();  // 初次加载时根据 province 获取数据
  },
  watch: {
    // 监听 province 属性的变化并根据新值获取相应的数据
    province(newVal) {
      this.fetchData();
    }
  },
  methods: {
    fetchData() {
      if (!this.province || this.province === "中国") {
        this.fetchNationalData();
      } else {
        this.fetchProvinceData(this.province);
      }
    },
    // 获取全国数据并更新 dataItem
    fetchNationalData() {
      axios.get(`${baseUrl}/city/getHealthStatus`)
          .then(response => {
            if (response.code === "200") {
              const data = response.data;
              this.updateDataItem(data);  // 更新 dataItem
            } else {
              console.warn("Failed to fetch national data:", response.msg);
            }
          })
          .catch(error => {
            console.error("Error fetching national data:", error);
          });
    },

    // 获取指定省份的数据并更新 dataItem
    fetchProvinceData(provinceName) {
      axios.get(`${baseUrl}/city/getHealthStatusByCity`, {
        params: { cityName: provinceName }
      })
          .then(response => {
            if (response.code === "200") {
              const data = response.data;  // 数据已是数组格式
              this.updateDataItem(data);  // 更新 dataItem
            } else {
              console.warn("Failed to fetch province data:", response.msg);
            }
          })
          .catch(error => {
            console.error("Error fetching province data:", error);
          });
    },

    // 更新 dataItem 的数据
    updateDataItem(data) {
      const ageGroups = {
        '30岁以下': 0,
        '30~40岁': 0,
        '40~50岁': 0,
        '50~60岁': 0,
        '60~70岁': 0,
        '70岁以上': 0,
      };

      data.forEach(item => {
        ageGroups['30岁以下'] += item.age_30 || 0;
        ageGroups['30~40岁'] += item.age_30_40 || 0;
        ageGroups['40~50岁'] += item.age_40_50 || 0;
        ageGroups['50~60岁'] += item.age_50_60 || 0;
        ageGroups['60~70岁'] += item.age_60_70 || 0;
        ageGroups['70岁以上'] += item.age_70 || 0;
      });

      // 将数据格式更新为饼图所需的格式
      this.dataItem = Object.keys(ageGroups).map(key => ({
        value: ageGroups[key],
        name: key
      }));

      // 调用 getData 方法进行渲染
      this.getData();
    },
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
