<template>
  <div>
    <!-- 第一个空的 div，用于显示警告文字 -->
    <div v-if="warnings" style="font-size: 18px;color: rgb(255, 220, 96); margin: 8px;">{{ warnings }}
      &nbsp;
      <el-tooltip content="使用用户最近数据" placement="top" @click.stop.prevent>
        <i class="el-icon-warning-outline" />
      </el-tooltip>
    </div>

    <Echart id="leftCenter" :options="option" class="left_center_inner" ref="charts" width="600px" height="360px" />
  </div>
</template>

<script>
import axios from "axios";
import { baseUrl } from "@/api/api";
export default {
  data() {
    return {
      warnings: "", // 用于存储警告文字
      option: {
        legend: {
          bottom: 5,
          data: ["安全指标", "实际指标"],
          itemGap: 20,
          textStyle: {
            color: "#fff",
            fontSize: 14
          }
        },
        tooltip: {
          trigger: "item"
        },
        radar: {
          indicator: [
            { name: "呼吸率", max: 35, min: 0 },
            { name: "收缩压", max: 200, min: 0 },
            { name: "舒张压", max: 160, min: 0 },
            { name: "血氧", max: 100, min: 0 },
            { name: "温度", max: 45, min: 0 },
            { name: "心率", max: 220, min: 0 },
            { name: "血糖", max: 33, min: 0 }
          ],
          splitNumber: 5,
          axisName: {
            color: "white",
            fontSize: "16px"
          },
          splitLine: {
            lineStyle: {}
          },
          splitArea: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: "white"
            }
          }
        },
        series: [
          {
            type: "radar",
            data: [
              {
                value: [5, 140, 90, 95, 37.3, 100, 7.1],
                name: "安全指标",
                lineStyle: {
                  color: "green",
                  width: 2
                },
                itemStyle: {
                  color: "green"
                },
                symbol: "none"
              },
              {
                value: [], // 将通过接口数据填充
                name: "实际指标",
                itemStyle: {
                  color: "#F9713C"
                },
                areaStyle: {
                  color: "rgba(249, 113, 60, 0.3)"
                },
                symbol: "none"
              }
            ]
          }
        ]
      },
      indicatorMap: {
        "breath_rate": "呼吸率",
        "systolic": "收缩压",
        "diastolic": "舒张压",
        "blood_oxygen": "血氧",
        "temperature": "温度",
        "heart_rate": "心率",
        "blood_glucose": "血糖"
      },
    };
  },
  created() {
    this.userId = this.$route.query.id;

    // 获取健康数据
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
            type: "warning"
          });
        }
      })
      .catch(error => {
        console.error(error);
        this.$Message({
          text: "获取数据失败",
          type: "error"
        });
      });

    // 获取预警信息
    axios
      .get(`${baseUrl}/user/warning`, { params: { id: this.userId } })
      .then(response => {
        if (response.code === "200") {
          const data = response.data;
          // 处理预警信息并生成警告文字
          this.warnings = this.formatWarnings(data);
        } else {
          this.$Message({
            text: response.data.msg,
            type: "warning"
          });
        }
      })
      .catch(error => {
        console.error(error);
        this.$Message({
          text: "获取数据失败",
          type: "error"
        });
      });
  },
  methods: {

    // 格式化预警信息
    formatWarnings(data) {
      return data
        .filter(item => !item.includes("安全")) // 排除“安全”的项
        .map(item => {
          const [indicator, level] = item.split(": "); // 拆分指标和级别
          const chineseIndicator = this.indicatorMap[indicator] || indicator; // 转换成中文指标
          return `${chineseIndicator}${level}`; // 格式化输出
        })
        .join("，"); // 拼接成字符串
    }
  }
};
</script>

<style scoped>
/* 可根据需要添加样式 */
</style>
