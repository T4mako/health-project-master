<template>
  <div>
    <Echart id="leftCenter" :options="option" class="left_center_inner" v-if="true" ref="charts" width="430px"
      height="300px" />
  </div>
</template>

<script>
import axios from "axios";
import {baseUrl} from "@/api/api";

export default {
  data() {
    return {
      option: {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          borderWidth: 0,
          left: 'left',
          textStyle: {
            color: '#fff',
            fontSize: 14
          }
        },
        series: [
          {
            name: '健康预警',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
            },
            label: {
              show: false,
              color:'white'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 18,
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: 3, name: '安全' },
              { value: 5, name: '一级预警' },
              { value: 1, name: '二级预警' },
              { value: 1, name: '紧急预警' },
            ]
          }
        ]
      }

    };
  },
  created() {
    this.userId = this.$route.query.id    
    axios.get(`${baseUrl}/user/warning`,{  params:{id:this.userId}}).then(response => {
        const warningData = response.data;
        console.log(warningData);
        
        const counts = {
            '安全': 0,
            '一级预警': 0,
            '二级预警': 0,
            '三级预警': 0,
            '数据缺失':0
          };

          // 统计每种预警级别的数量
          warningData.forEach(item => {
            const level = item.split(': ')[1];
            if (counts.hasOwnProperty(level)) {
              counts[level]++;
            }
          });

          // 更新 ECharts 数据
          this.option.series[0].data = [
            { value: counts['安全'], name: '安全' },
            { value: counts['一级预警'], name: '一级预警' },
            { value: counts['二级预警'], name: '二级预警' },
            { value: counts['三级预警'], name: '紧急预警' },
            { value: counts['数据缺失'], name: '数据未测量' },

          ];
            
        if (response.code === "200") {
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