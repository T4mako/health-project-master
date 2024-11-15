<template>
  <div class="content">
    <div class="left">
      <div class="top">
        <div class="top-left">
          <ItemWrap title="基础信息">
            <div class="user-info">
              <p>ID：{{ userInfo.id }}</p>
              <p>性别：{{ userInfo.gender }}</p>
              <p>年龄：{{ userInfo.age }} 岁</p>
              <p>城市：{{ userInfo.city }}</p>
              <p>社区：{{ userInfo.deptName }}</p>
              <p>身高：{{ userInfo.height }} cm</p>
              <p>体重：{{ userInfo.weight }} kg</p>
            </div>
          </ItemWrap>
        </div>
        <div class="top-right">
          <ItemWrap title="健康信息">
            <HealthRader></HealthRader>
          </ItemWrap>
        </div>
      </div>
      <div class="bottom">
        <ItemWrap title="疾病预测">
          <BarChart></BarChart>
        </ItemWrap>
      </div>
    </div>
    <div class="right">
      <ItemWrap title="社区疾病预测对比">
        <Heatmap></Heatmap>
      </ItemWrap>
    </div>
  </div>
</template>

<script>
import ItemWrap from '@/components/item-wrap/item-wrap.vue';
import BarChart from './BarChart.vue';
import Heatmap from './Heatmap.vue';
import HealthRader from './health-rader.vue';
import axios from "axios";
import { baseUrl } from "@/api/api";
export default {
  components: {
    ItemWrap,
    BarChart,
    Heatmap,
    HealthRader
  },
  data() {
    return {
      userId: null,
      userInfo: {
        id: "",
        gender: "",
        age: 0,
        city: "",
        deptName: "",
        height: 0,
        weight: 0,
        bmi: 0,
      },
    };
  },
  created() {
    this.userId = this.$route.query.id
    axios.get(`${baseUrl}/user/info`, { params: { id: this.userId } }).then(response => {
      if (response.code === "200") {
        const data = response.data;
        this.userInfo = data.pd;
        this.userInfo.city = data.city;
        this.bmi = data.pd.bmi
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
.content {
  display: flex;
  height: 90%;
  color: #fff;
  font-size: 24px;
  justify-content: space-around;
}

.left {
  width: 50%; /* Set the left part to 45% width */
  display: flex;
  flex-direction: column;
}

.right {
  width: 50%; /* Set the right part to 55% width */
  display: flex;
  flex-direction: column;
}

.left .top {
  display: flex;
  flex: 1;
  margin-right: 20px;
}

.left .top-left,
.left .top-right {
  flex: 1;
  margin: 0 10px;
  align-items: center
}

.left .bottom {
  flex: 1;
  margin-right: 20px;
}

.top {
  margin-bottom: 20px;
}

.mid,
.right {
  display: flex;
  align-items: center;
}

.user-info {
  text-align: left;
  font-size: 24px;
  line-height: 2;
  color: #fff;
  padding-left: 90px;
  padding-top: 20px;
}

</style>
