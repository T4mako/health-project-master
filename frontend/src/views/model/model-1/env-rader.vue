<template>
  <div>
    <Echart id="leftCenter" :options="option" class="left_center_inner" v-if="true" ref="charts" width="600px"
      height="380px" />
  </div>
</template>

<script>
import axios from "axios";
import { baseUrl } from "@/api/api";
export default {
  data() {
    return {
      option:{
        legend: {
          bottom: 5,
          data: ['环境数值'],
          itemGap: 20,
          textStyle: {
            color: '#fff',
            fontSize: 14
          }
        },
        tooltip: {
          trigger: 'item' // 设置触发类型为 'item'，在鼠标悬停时显示值
        },
        radar: {
          indicator: [
            { name: 'CO2', max: 1500, min: 400 },
            { name: 'TVOC', max: 1000, min: 0 },
            { name: '光照', max: 60000, min: 0 },
            { name: 'PM2.5', max: 500, min: 0 },
            { name: '声音', max: 100, min: 0 },
            { name: '温度', max: 50, min: 0 },
            { name: '湿度', max: 95, min: 20 },
          ],
          splitNumber: 5,
          axisName: {
            color: 'white',
            fontSize: '16px'
          },
          splitLine: {
            lineStyle: {
              // color: [
              //   'rgba(238, 197, 102, 0.1)',
              //   'rgba(238, 197, 102, 0.2)',
              //   'rgba(238, 197, 102, 0.4)',
              //   'rgba(238, 197, 102, 0.6)',
              //   'rgba(238, 197, 102, 0.8)',
              //   'rgba(238, 197, 102, 1)'
              // ].reverse()
            }
          },
          splitArea: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: 'white)'
            }
          }
        },
        series: [
          {
            type: 'radar',
            data: [
              {
                value: [518, 170, 28695, 38, 36, 26, 80],
                name: '环境数值',
                itemStyle: {
                  color: '#F9713C'
                },
                areaStyle: {
                  color: 'rgba(249, 113, 60, 0.3)'
                },
                symbol: 'none'
              }
            ]
          }
        ]
      }
    };
  },
  created() {
    this.userId = this.$route.query.id
    axios.get(`${baseUrl}/env/userEnv`, { params: { id: this.userId } }).then(response => {
      if (response.code === "200") {
        const data = response.data;
        
        // 将数据映射到雷达图的value
        const radarValues = [
            data.co2 + 650,          // CO2
            data.tvoc + 400,         // TVOC
            data.light + 30000,        // 光照
            data.pm25 + 50,         // PM2.5
            data.db,           // 声音
            data.temperature,  // 温度
            data.humidity      // 湿度
          ];

          this.option.series[0].data[0].value = radarValues;
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
  }
};
</script>

<style scoped>
/* 可根据需要添加样式 */
</style>