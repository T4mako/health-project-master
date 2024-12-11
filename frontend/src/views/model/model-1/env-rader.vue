<template>
  <div>
    <!-- 第一个空的 div，用于显示警告文字 -->
    <div style="font-size: 18px;color: rgb(255, 220, 96); margin: 8px;">天气：{{ this.wealth }}
    </div>
    <Echart id="leftCenter" :options="option" class="left_center_inner" v-if="true" ref="charts" width="600px"
      height="360px" />
  </div>
</template>

<script>
import axios from "axios";
import { baseUrl } from "@/api/api";
export default {
  data() {
    return {
      wealth: '',
      option: {
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
            { name: 'CO', max: 2, min: 0 },
            { name: '气压', max: 2000, min: 0 },
            { name: '天气', max: 60000, min: 0 },
            { name: 'PM2.5', max: 100, min: 0 },
            { name: 'PM10', max: 500, min: 0 },
            { name: '温度', max: 50, min: -20 },
            { name: '湿度', max: 100, min: 0 },
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
                value: [0, 0, 0, 0, 0, 0, 0],
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
        console.log(data);
        
        if (data != null) {
          // 将数据映射到雷达图的value
          const radarValues = [
            data.co,
            data.pressure=0?800:data.pressure,
            0,    // 日照   
            data.pm25,
            data.pm10,
            data.temperature=0?-20:data.temperature,
            data.humidity
          ];
          this.wealth = data.light
          this.option.series[0].data[0].value = radarValues;
        }

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