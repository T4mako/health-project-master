<template>
  <ItemWrap style="height: 90%;">
    <Echart :options="option" class="left_center_inner" v-if="true" ref="charts" />
  </ItemWrap>
</template>

<script>
import * as echarts from 'echarts';  // Import ECharts
import { formatTime } from "@/utils/index.js";
import ScaleScreen from "@/components/scale-screen/scale-screen.vue";
import ItemWrap from "@/components/item-wrap/item-wrap.vue";

export default {
  components: { ScaleScreen },
  data() {
    return {
      timing: null,
      loading: true,
      dateDay: null,
      dateYear: null,
      dateWeek: null,
      weekday: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
      input: '',
      path: '',
      option: {  // The chart option goes here
        title: {
          text: '建筑环境与老年健康关系图谱',
          left: 'center',
          top: '5%',
          textStyle: {
            color: '#ffffff', // Set the title text color to white
            fontSize: 24 // Set the font size to 24
          },
        },
        tooltip: {
          formatter: function (params) {
            if (params.dataType === 'node') {
              return params.data.name; // 节点的提示信息
            } else if (params.dataType === 'edge') {
              return `${params.data.source} → ${params.data.target} <br/>权重: ${params.data.value}`;
            }
          }
        },
        legend: {
          data: ['建筑环境指标', '老年健康指标'],
          orient: 'vertical',
          left: '8%',
          top: '8%',
          textStyle: {
            color: '#ffffff', // Set legend text color to white
            fontSize: 18
          },
        },
        textStyle: {
          color: '#ffffff' // Set tooltip text color to white
        },
        series: [
          {
            type: 'graph',
            layout: 'none', // 使用固定布局
            roam: true,
            draggable: true,
            label: {
              show: true,
              position: 'right',
              formatter: '{b}', // 显示节点名称
              color: '#ffffff', // Set node label text color to white
              fontSize: 18
            },
            data: [
              // 环境数据节点
              { name: 'CO2浓度', category: 0, value: 80, symbolSize: 30, x: 290, y: 550 },
              { name: 'TVOC浓度', category: 0, value: 70, symbolSize: 30, x: 310, y: 600 },
              { name: '光照强度', category: 0, value: 60, symbolSize: 30, x: 330, y: 650 },
              { name: 'PM2.5浓度', category: 0, value: 90, symbolSize: 30, x: 350, y: 700 },
              { name: '噪音', category: 0, value: 60, symbolSize: 30, x: 330, y: 750 },
              { name: '温度', category: 0, value: 80, symbolSize: 30, x: 310, y: 800 },
              { name: '湿度', category: 0, value: 70, symbolSize: 30, x: 290, y: 850 },

              // 健康数据节点
              { name: '呼吸率', category: 1, value: 60, symbolSize: 40, x: -60, y: 500 },
              { name: '血氧', category: 1, value: 70, symbolSize: 40, x: -130, y: 600 },
              { name: '心率', category: 1, value: 70, symbolSize: 40, x: -120, y: 650 },
              { name: '舒张压', category: 1, value: 60, symbolSize: 40, x: -170, y: 700 },
              { name: '收缩压', category: 1, value: 60, symbolSize: 40, x: -70, y: 750 },
              { name: '体温', category: 1, value: 60, symbolSize: 40, x: -120, y: 850 },
              { name: '血糖', category: 1, value: 60, symbolSize: 40, x: 150, y: 880 },
              { name: '运动停留时间', category: 1, value: 60, symbolSize: 40, x: -30, y: 880 },
            ],
            links: [
              // 建筑环境 → 老年健康指标的关系
              { source: 'CO2浓度', target: '呼吸率', value: 0.7, lineStyle: { width: 2, color: 'red' }, label: { show: true, formatter: '呼吸率增加', color: 'ffffff', fontSize: 18 } },
              { source: 'CO2浓度', target: '血氧', value: 0.8, lineStyle: { width: 2, color: 'red' }, label: { show: true, formatter: '血氧下降', color: 'ffffff', fontSize: 18 } },
              { source: 'CO2浓度', target: '心率', value: 0.6, lineStyle: { width: 2, color: 'red' }, label: { show: true, formatter: '心率加快', color: 'ffffff', fontSize: 18 } },

              { source: 'TVOC浓度', target: '呼吸率', value: 0.6, lineStyle: { width: 2, color: 'red' }, label: { show: true, formatter: '呼吸率增加', color: 'ffffff', fontSize: 18 } },
              { source: 'TVOC浓度', target: '血氧', value: 0.7, lineStyle: { width: 2, color: 'red' }, label: { show: true, formatter: '血氧下降', color: 'ffffff', fontSize: 18 } },
              { source: 'TVOC浓度', target: '体温', value: 0.4, lineStyle: { width: 2, color: 'red' }, label: { show: true, formatter: '体温升高', color: 'ffffff', fontSize: 18 } },

              { source: '光照强度', target: '运动停留时间', value: 0.8, lineStyle: { width: 2, color: 'green' }, label: { show: true, formatter: '运动时间增加', color: 'ffffff', fontSize: 18 } },
              { source: '光照强度', target: '心率', value: 0.6, lineStyle: { width: 2, color: 'green' }, label: { show: true, formatter: '心率稳定', color: 'ffffff', fontSize: 18 } },
              { source: '光照强度', target: '舒张压', value: 0.5, lineStyle: { width: 2, color: 'green' }, label: { show: true, formatter: '舒张压降低', color: 'ffffff', fontSize: 18 } },

              { source: 'PM2.5浓度', target: '呼吸率', value: 0.9, lineStyle: { width: 2, color: 'red' }, label: { show: true, formatter: '呼吸率增加', color: 'ffffff', fontSize: 18 } },
              { source: 'PM2.5浓度', target: '血氧', value: 0.85, lineStyle: { width: 2, color: 'red' }, label: { show: true, formatter: '血氧下降', color: 'ffffff', fontSize: 18 } },
              { source: 'PM2.5浓度', target: '心率', value: 0.7, lineStyle: { width: 2, color: 'red' }, label: { show: true, formatter: '心率加快', color: 'ffffff', fontSize: 18 } },

              { source: '噪音', target: '心率', value: 0.6, lineStyle: { width: 2, color: 'red' }, label: { show: true, formatter: '心率加快', color: 'ffffff', fontSize: 18 } },
              { source: '噪音', target: '收缩压', value: 0.65, lineStyle: { width: 2, color: 'red' }, label: { show: true, formatter: '血压升高', color: 'ffffff', fontSize: 18 } },

              { source: '温度', target: '体温', value: 0.8, lineStyle: { width: 2, color: 'red' }, label: { show: true, formatter: '体温调节异常', color: 'ffffff', fontSize: 18 } },
              { source: '温度', target: '收缩压', value: 0.7, lineStyle: { width: 2, color: 'red' }, label: { show: true, formatter: '血压波动', color: 'ffffff', fontSize: 18 } },

              { source: '湿度', target: '呼吸率', value: 0.6, lineStyle: { width: 2, color: 'red' }, label: { show: true, formatter: '呼吸率增加', color: 'ffffff', fontSize: 18 } },
              { source: '湿度', target: '血氧', value: 0.5, lineStyle: { width: 2, color: 'red' }, label: { show: true, formatter: '血氧下降', color: 'ffffff', fontSize: 18 } },

              // 老年健康指标之间的关系
              { source: '呼吸率', target: '血氧', value: 0.85, lineStyle: { width: 2, color: 'blue' }, label: { show: true, formatter: '血氧下降', color: 'ffffff', fontSize: 18 } },

              { source: '收缩压', target: '心率', value: 0.7, lineStyle: { width: 2, color: 'blue' }, label: { show: true, formatter: '心率加快', color: 'ffffff', fontSize: 18 } },

              { source: '体温', target: '收缩压', value: 0.6, lineStyle: { width: 2, color: 'blue' }, label: { show: true, formatter: '血压升高', color: 'ffffff', fontSize: 18 } },
              { source: '体温', target: '舒张压', value: 0.5, lineStyle: { width: 2, color: 'blue' }, label: { show: true, formatter: '血压降低', color: 'ffffff', fontSize: 18 } },

              { source: '血糖', target: '心率', value: 0.6, lineStyle: { width: 2, color: 'blue' }, label: { show: true, formatter: '心率加快', color: 'ffffff', fontSize: 18 } },
              { source: '血糖', target: '心率', value: 0.7, lineStyle: { width: 2, color: 'blue' }, label: { show: true, formatter: '心率减慢', color: 'ffffff', fontSize: 18 } },

              { source: '收缩压', target: '舒张压', value: 0.8, lineStyle: { width: 2, color: 'blue' }, label: { show: true, formatter: '舒张压升高', color: 'ffffff', fontSize: 18 } },

              { source: '呼吸率', target: '心率', value: 0.75, lineStyle: { width: 2, color: 'blue' }, label: { show: true, formatter: '心率加快', color: 'ffffff', fontSize: 18 } }
            ],
            categories: [
              { name: '建筑环境指标' },
              { name: '老年健康指标' }
            ],
            lineStyle: {
              curveness: 0 // 连接线的弯曲度
            },
            emphasis: {
              focus: 'adjacency',
              label: {
                show: true,
                color: '#ffffff' // Set label text color to white on emphasis
              },
              lineStyle: {
                width: 10
              }
            }
          }
        ]
      }
    };
  },
  filters: {
    numsFilter(msg) {
      return msg || 0;
    },
  },
  computed: {},
  created() {
    this.path = this.$route.path
  },
  mounted() {
    this.timeFn();
    this.cancelLoading();
    this.initChart();  // Initialize chart on mount
  },
  beforeDestroy() {
    clearInterval(this.timing);
  },
  methods: {
    initChart() {
      // Initialize ECharts instance and set option
      const chart = this.$refs.charts;
      this.myChart = echarts.init(chart);
      this.myChart.setOption(this.option);
    },
    timeFn() {
      this.timing = setInterval(() => {
        this.dateDay = formatTime(new Date(), "HH: mm: ss");
        this.dateYear = formatTime(new Date(), "yyyy-MM-dd");
        this.dateWeek = this.weekday[new Date().getDay()];
      }, 1000);
    },
    cancelLoading() {
      let timer = setTimeout(() => {
        this.loading = false;
        clearTimeout(timer);
      }, 500);
    },
    goTo(path) {
      this.$router.push(path);
    }
  },
};
</script>

<style lang="scss" scoped>
.bg {
  width: 100%;
  height: 100%;
  padding: 16px 16px 10px 16px;
  box-sizing: border-box;
  background-size: cover;
  background-position: center center;
}

.content {
  display: flex;
  width: 100%;
  height: 90%;

  .wrap {
    margin: 15px;
  }

  font-size: 24px;
}
</style>
