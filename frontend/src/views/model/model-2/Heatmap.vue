<template>
  <div>
    <Echart :options="option" class="left_center_inner" ref="charts" width="900px" height="800px" />
  </div>
</template>

<script>
import axios from "axios";
import { baseUrl } from "@/api/api";

export default {
  name: 'Heatmap',
  data() {
    return {
      option: {
        tooltip: {
          position: 'top'
        },
        grid: {
          height: '80%',
          width: '80%',
          top: '10%'
        },
        xAxis: {
          axisLine: {
            lineStyle: { color: '#FFFFFF' }
          },
          type: 'category',
          data: ['中风', '糖尿病', '心脏病'],
          splitArea: {
            show: true
          }
        },
        yAxis: {
          type: 'category',
          axisLine: {
            lineStyle: { color: '#FFFFFF' }
          },
          data: [],
          splitArea: {
            show: true
          }
        },
        visualMap: {
          min: 0,
          max: 100,
          calculable: true,
          orient: 'horizontal',
          left: 'center',
          bottom: '0%',
          textStyle: {
            color: '#fff',         
            fontSize: 16          
          }
        },
        series: [
          {
            type: 'heatmap',
            data: [],
            itemStyle: {
              color:'#fff'
            },
            label: {
              show: true
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      }
    };
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
          // Extract prediction data for each disease
          const heartDiseaseData = results[0]; // Heart disease prediction data
          const diabetesData = results[1]; // Diabetes prediction data
          const strokeData = results[2]; // Stroke prediction data

          // Combine prediction results based on user IDs
          heartDiseaseData.forEach((item, index) => {
            const id = item.id;
            const heartDiseaseProbability = item.probability;

            // Find corresponding diabetes and stroke probabilities by ID
            const diabetesProbability = diabetesData.find(d => d.id === id) ? diabetesData.find(d => d.id === id).probability : 0;
            const strokeProbability = strokeData.find(s => s.id === id) ? strokeData.find(s => s.id === id).probability : 0;

            // Add result to res array
            res.push({
              id: id,
              heart_disease_probability: (heartDiseaseProbability * 100).toFixed(2), // Multiply by 100 and round to 2 decimal places
              stroke_probability: (strokeProbability * 100).toFixed(2),             // Multiply by 100 and round to 2 decimal places
              diabetes_probability: (diabetesProbability * 100).toFixed(2)          // Multiply by 100 and round to 2 decimal places
            });
          });

          // Process the results into the heatmap option structure
          this.processHeatmapData(res);
        });
    },
    processHeatmapData(res) {
      // Prepare data for the heatmap
      const heatmapData = [];
      const userIds = [];

      // Populate yAxis with user IDs
      res.forEach(item => {
        if (!userIds.includes(item.id)) {
          userIds.push(item.id);
        }

        // Push heatmap data points: [userIndex, diseaseIndex, probability]
        heatmapData.push([0,userIds.indexOf(item.id), item.heart_disease_probability]); // Heart disease
        heatmapData.push([1,userIds.indexOf(item.id), item.diabetes_probability]);     // Diabetes
        heatmapData.push([2,userIds.indexOf(item.id), item.stroke_probability]);       // Stroke
      });

      // Update the heatmap option with the new data
      this.option.yAxis.data = userIds;  // Set user IDs as yAxis labels
      this.option.series[0].data = heatmapData;  // Set the heatmap data
      console.log(this.option.series[0].data);
      
    }
  },
  created() {
    this.userId = this.$route.query.id;
    axios.get(`${baseUrl}/user/communityAllInfo`, { params: { id: this.userId } })
      .then(response => {
        const data = response.data;
        if (response.code === "200") {
          // Fetch disease predictions
          this.getPredictData(data);
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
};
</script>

<style scoped>
.heatmap {
  width: 100%;
  height: 400px;
}
</style>
