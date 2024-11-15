<template>
  <div>
    <Echart :options="option" class="left_center_inner" ref="charts" width="920px" height="450px" />
  </div>
</template>

<script>
import axios from "axios";
import { baseUrl } from "@/api/api";
export default {
  data() {
    return {
      option: {
        tooltip: {
          trigger: 'item',  // Trigger the tooltip on hovering over the item (bar)
          formatter: '{b}: {c}',  // Display the category label (b) and value (c)
        },
        xAxis: {
          type: 'category',
          data: ['中风预警', '糖尿病预警', '心脏病预警']
        },
        yAxis: {
          type: 'value',
          min: 0, // Set the minimum value to 0
          max: 1, // Set the maximum value to 1
          name: '概率', // Set the label for the y-axis
          axisLabel: {
            formatter: '{value}' // To display the value on the axis
          }
        },
        series: [
          {
            data: [],
            type: 'bar',
            itemStyle: {
              // Dynamic color based on value
              normal: {
                color: function(params) {
                  return params.value > 0.5 ? 'red' : '#5470C6'; // Red for > 0.5, default blue
                }
              }
            }
          }
        ]
      }
    }
  },
  created() {
    this.userId = this.$route.query.id;
    axios.get(`${baseUrl}/user/allDayInfo`, { params: { id: this.userId } })
      .then(response => {
        if (response.code === "200") {
          const data = response.data;
          const mergedData = [{
            id: data.pd.id,
            ...data.pd,
            ...data.hd,
          }];
          this.getPredictData(mergedData);
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
  methods: {
    getPredictData(data) {
      let res = [];

      // Define disease prediction endpoints and corresponding labels
      const predictEndpoints = [
        { api: '/api/predict_heart', label: 'heart_disease_prediction' },
        { api: '/api/predict_diabetes', label: 'diabetes_prediction' },
        { api: '/api/predict_stroke', label: 'stroke_prediction' }
      ];

      // Make requests to all prediction endpoints
      Promise.all(predictEndpoints.map(endpoint => {
        return axios.post(endpoint.api, data)
          .then(response => response) // Return the response data
          .catch(error => {
            console.error(error);
            this.$Message({
              text: '获取数据失败',
              type: 'error'
            });
            return [];  // Return an empty array to avoid crashing
          });
      }))
        .then(results => {
          
          const heartDiseaseData = results[0]; // Heart disease prediction data
          const diabetesData = results[1]; // Diabetes prediction data
          const strokeData = results[2]; // Stroke prediction data
          this.option.series[0].data = [
            heartDiseaseData[0].probability.toFixed(3),
            diabetesData[0].probability.toFixed(3),
            strokeData[0].probability.toFixed(3)
          ];
        });
    },
  }
};
</script>

<style scoped></style>
