<template>
  <div>
    <div>
      <span style="font-size: 18px;">时间段：</span>
      <el-select v-model="timeChoose" placeholder="请选择" @change="fetchData">
        <el-option v-for="item in timeOptions" :key="item.value" :label="item.label" :value="item.value"></el-option>
      </el-select>
    </div>
    <div>
      <Echart id="leftCenter" :options="option" class="left_center_inner" ref="charts" width="650px" height="380px" />
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { baseUrl } from "@/api/api";

export default {
  data() {
    return {
      option: {
        title: {
          textStyle: {
            color: 'white'
          }
        },
        grid: {
          show: false
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          textStyle: {
            color: '#FFFFFF',
            fontSize: '14px'
          },
          data: ['呼吸率', '收缩压', '舒张压', '血氧', '温度', '心率', '血糖'],
          bottom: '5%'
        },
        xAxis: {
          type: 'category',
          axisLine: {
            lineStyle: {
              color: '#FFFFFF'
            }
          },
          nameTestStyle: {
            color: 'white'
          },
          boundaryGap: false,
          data: []  // 将会通过接口填充
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#FFFFFF'
            }
          }
        },
        series: [
          { name: '呼吸率', type: 'line', smooth: true, data: [] },
          { name: '收缩压', type: 'line', smooth: true, data: [] },
          { name: '舒张压', type: 'line', smooth: true, data: [] },
          { name: '血氧', type: 'line', smooth: true, data: [] },
          { name: '温度', type: 'line', smooth: true, data: [] },
          { name: '心率', type: 'line', smooth: true, data: [] },
          { name: '血糖', type: 'line', smooth: true, data: [] }
        ]
      },
      timeOptions: [
        { value: 'week', label: '过去一周' },
        { value: 'month', label: '过去一月' },
        { value: 'year', label: '过去一年' },
        // { value: 'all', label: '全部' },
      ],
      timeChoose: 'week',
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData() {
      this.userId = this.$route.query.id
      axios.get(`${baseUrl}/user/dateHData`, { params: { id: this.userId, date: this.timeChoose } })
        .then(response => {
          if (response.code === "200") {
            const data = response.data;

            // 使用接口数据填充 xAxis 和 series 数据
            this.option.xAxis.data = data.map(item => item.create_time);
            this.option.series[0].data = data.map(item => item.breath_rate);
            this.option.series[1].data = data.map(item => item.systolic);
            this.option.series[2].data = data.map(item => item.diastolic);
            this.option.series[3].data = data.map(item => item.blood_oxygen);
            this.option.series[4].data = data.map(item => item.temperature);
            this.option.series[5].data = data.map(item => item.heart_rate);
            this.option.series[6].data = data.map(item => item.blood_glucose);
          } else {
            this.$Message({
              text: response.data.msg,
              type: 'warning'
            });
          }
        })
        .catch(error => {
          console.error(error);
          this.$Message({
            text: '获取数据失败',
            type: 'error'
          });
        });
    }
  }
};
</script>

<style scoped>
/* 可根据需要添加样式 */
</style>
