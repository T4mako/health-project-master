<template>
  <div>
    <Echart id="leftCenter" :options="option" class="left_center_inner" ref="charts" width="450px" height="380px" />
  </div>
</template>

<script>
import axios from "axios";
import { baseUrl } from "@/api/api";
export default {
  data() {
    return {
      option: {
        legend: {
          bottom: 5,
          data: ['安全指标', '实际指标'],
          itemGap: 20,
          textStyle: {
            color: '#fff',
            fontSize: 14
          }
        },
        tooltip: {
          trigger: 'item'
        },
        radar: {
          indicator: [
            { name: '呼吸率', max: 50, min: 0 },
            { name: '收缩压', max: 200, min: 50 },
            { name: '舒张压', max: 160, min: 30 },
            { name: '血氧', max: 100, min: 80 },
            { name: '温度', max: 45, min: 32 },
            { name: '心率', max: 220, min: 50 },
            { name: '血糖', max: 20, min: 0 }
          ],
          splitNumber: 5,
          axisName: {
            color: 'white',
            fontSize: '16px'
          },
          splitLine: {
            lineStyle: {}
          },
          splitArea: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: 'white'
            }
          }
        },
        series: [
          {
            type: 'radar',
            data: [
              {
                value: [5, 140, 90, 95, 37.3, 100, 7.1],
                name: '安全指标',
                lineStyle: {
                  color: 'green',
                  width: 2
                },
                itemStyle: {
                  color: 'green'
                },
                symbol: 'none'
              },
              {
                value: [], // 将通过接口数据填充
                name: '实际指标',
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
    this.userId = this.$route.query.id;
    axios
        .get(`${baseUrl}/user/dayHData`, { params: { id: this.userId } })
        .then(response => {
          if (response.code === "200") {
          const healthData = response.data;
          // 使用返回的数据填充“实际指标”值
          this.option.series[0].data[1].value = [
              healthData[0].breath_rate,
              healthData[0].systolic,
              healthData[0].diastolic,
              healthData[0].blood_oxygen,
              healthData[0].temperature,
              healthData[0].heart_rate,
              healthData[0].blood_glucose
            ];

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
  },
};
</script>

<style scoped>
/* 可根据需要添加样式 */
</style>
