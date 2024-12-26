<template>
  <ItemWrap style="height: 90%;">
    <div style="margin: 30px;">
  <!-- 下拉框 -->
  <el-select 
        v-model="currentCity" 
        placeholder="选择城市" 
        @change="updateGraph"
        class="custom-select">
        <el-option 
          label="徐州" 
          value="xuzhou" 
          class="custom-option"
          style="font-size: 22px;">
        </el-option>
        <el-option 
          label="郑州" 
          value="zhengzhou" 
          class="custom-option"
          style="font-size: 22px;"
          >
        </el-option>
        <el-option 
          label="西安" 
          value="xian" 
          class="custom-option"
          style="font-size: 22px;">
        </el-option>
      </el-select>
</div>

    <Echart :options="option" class="left_center_inner" v-if="true" ref="charts" :width="chartWidth" :height="chartHeight"/>
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
      currentCity: "xuzhou", // 默认城市
      timing: null,
      loading: true,
      dateDay: null,
      dateYear: null,
      dateWeek: null,
      weekday: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
      input: '',
      path: '',
      chartWidth: '1800px', // 将 width 和 height 改为 data 属性
      chartHeight: '850px',
      option: {  // The chart option goes here
        title: {
          text: '建筑环境与老年健康关系图谱',
          left: 'center',
          top: '0%',
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
          left: '5%',
          top: '0%',
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
              { name: 'PM10', category: 0, value: 70, symbolSize: 30, x: 310, y: 600 },
              { name: '日照', category: 0, value: 60, symbolSize: 30, x: 330, y: 650 },
              { name: 'PM2.5', category: 0, value: 90, symbolSize: 30, x: 350, y: 700 },
              { name: '气压', category: 0, value: 60, symbolSize: 30, x: 330, y: 750 },
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
            links: [],
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
    this.updateGraph(); // Initialize graph with default city
  },
  beforeDestroy() {
    clearInterval(this.timing);
  },
  methods: {
    initChart() {
      // Initialize ECharts instance and set option
      const chart = this.$refs.charts;
      if (chart && chart.$el) {
        this.myChart = echarts.init(chart.$el);
        this.myChart.setOption(this.option);
      }
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
    },
    updateGraph() {
      const cityWeights = {
        xuzhou: {
          'PM2.5': { '血氧': 0.9, '呼吸率': 0.8, '血糖': 0.5 },
          'PM10': { '血氧': 0.8, '呼吸率': 0.7 },
          '温度': { '血糖': 0.6, '心率': 0.7, '收缩压': 0.6, '舒张压': 0.5, '体温': 0.7 },
          '湿度': { '血糖': 0.5, '心率': 0.6, '体温': 0.6, '舒张压': 0.5, '收缩压': 0.6 },
          '气压': { '血糖': 0.4, '呼吸率': 0.5, '体温': 0.4 },
          'CO': { '血氧': 0.9, '呼吸率': 0.8 },
          '日照': { '血糖': 0.7, '心率': 0.7, '舒张压': 0.6, '收缩压': 0.6 }
        },
        zhengzhou: {
          'PM2.5': { '血氧': 0.9, '呼吸率': 0.8, '血糖': 0.5 },
          'PM10': { '血氧': 0.8, '呼吸率': 0.7 },
          '温度': { '血糖': 0.6, '心率': 0.7, '收缩压': 0.6, '舒张压': 0.5, '体温': 0.7 },
          '湿度': { '血糖': 0.5, '心率': 0.6, '体温': 0.6, '舒张压': 0.5, '收缩压': 0.6 },
          '气压': { '血糖': 0.4, '呼吸率': 0.5, '体温': 0.4 },
          'CO': { '血氧': 0.9, '呼吸率': 0.8 },
          '日照': { '血糖': 0.7, '心率': 0.7, '舒张压': 0.6, '收缩压': 0.6 }
        },
        xian: {
          'PM2.5': { '血氧': 0.8, '呼吸率': 0.7, '血糖': 0.4 },
          'PM10': { '血氧': 0.7, '呼吸率': 0.6 },
          '温度': { '血糖': 0.7, '心率': 0.6, '收缩压': 0.7, '舒张压': 0.6, '体温': 0.8 },
          '湿度': { '血糖': 0.5, '心率': 0.7, '体温': 0.7, '舒张压': 0.6, '收缩压': 0.7 },
          '气压': { '血糖': 0.3, '呼吸率': 0.4, '体温': 0.3 },
          'CO': { '血氧': 0.9, '呼吸率': 0.8 },
          '日照': { '血糖': 0.8, '心率': 0.7, '舒张压': 0.7, '收缩压': 0.7 }
        }
      };

      const city = this.currentCity;
      const weights = cityWeights[city];

      const links = [];

      for (const env in weights) {
        for (const health in weights[env]) {
          links.push({
            source: env,
            target: health,
            value: weights[env][health],
            lineStyle: { width: 2, color: 'red' },
            label: { show: true, formatter: `${health} ${weights[env][health] > 0.5 ? '正相关' : '负相关'}`, color: '#ffffff', fontSize: 18 }
          });
        }
      }

      // 健康指标之间的关系
      const healthRelations = [
        { source: '血糖', target: '血氧', value: 0.7, lineStyle: { width: 2, color: 'blue' } },
        { source: '血糖', target: '呼吸率', value: 0.6, lineStyle: { width: 2, color: 'blue' } },
        { source: '血糖', target: '体温', value: 0.5, lineStyle: { width: 2, color: 'blue' } },
        { source: '收缩压', target: '心率', value: 0.8, lineStyle: { width: 2, color: 'blue' } },
        { source: '舒张压', target: '心率', value: 0.8, lineStyle: { width: 2, color: 'blue' } },
        { source: '血氧', target: '心率', value: 0.7, lineStyle: { width: 2, color: 'blue' } },
        { source: '血氧', target: '舒张压', value: 0.6, lineStyle: { width: 2, color: 'blue' } },
        { source: '血氧', target: '收缩压', value: 0.6, lineStyle: { width: 2, color: 'blue' } },
        { source: '呼吸率', target: '收缩压', value: 0.5, lineStyle: { width: 2, color: 'blue' } },
        { source: '呼吸率', target: '舒张压', value: 0.5, lineStyle: { width: 2, color: 'blue' } },
        { source: '运动停留时间', target: '血糖', value: 0.7, lineStyle: { width: 2, color: 'blue' } },
        { source: '运动停留时间', target: '血氧', value: 0.6, lineStyle: { width: 2, color: 'blue' } },
        { source: '运动停留时间', target: '呼吸率', value: 0.6, lineStyle: { width: 2, color: 'blue' } }
      ];

      links.push(...healthRelations);

      this.option.series[0].links = links;
      if (this.myChart) {
        this.myChart.setOption(this.option);
      }
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

::v-deep .el-input__inner {
  background-color: transparent !important;
  //   border-color: rgba(255, 255, 255, 0.5);
  color: #d9d9d9;
  height: 40px;
}

/**修改边框和字体颜色 */
::v-deep .el-select {
  position: relative;
  width: 450px;
  .el-input {
    input {
      height: 40px;
      border-color: rgba(44, 137, 229, 0.5);
      color: #fff;
    }
  }
}

/**修改下拉图标颜色 */
::v-deep .el-input__suffix {
  .el-input__suffix-inner {
    .el-icon-arrow-up:before {
      color: rgba(44, 137, 229, 0.5);
      padding-left: 0.11rem;
    }
  }
}
::v-deep .custom-select {
  .el-input__inner {
    font-size: 26px; /* 选择框的字体大小 */
  }
}

::v-deep .custom-option {
  font-size: 22px; /* 选项的字体大小 */
}
</style>