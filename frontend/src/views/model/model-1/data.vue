<template>
  <div style="display: flex;">
    <!-- 第一块，分为三个区域，使用 ItemWrap 组件 -->
    <div class="block block-1">
      <ItemWrap class="block-1-area area-1" title="基础信息" style="height: 500px;">
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
      <ItemWrap class="block-1-area area-2" title="今日健康预警">
        <HWarning></HWarning>
      </ItemWrap>
      <ItemWrap class="block-1-area area-3" title="BMI">
        <BMIChart :bmi="this.bmi"></BMIChart>
      </ItemWrap>
    </div>

    <!-- 剩余2/3的区域 -->
    <div class="right">
      <ItemWrap class="block block-2" title="各项健康指标">
        <IChart></IChart>
      </ItemWrap>
      <ItemWrap class="block block-3" title="健康数据监控">
        <HRader></HRader>
      </ItemWrap>
      <ItemWrap class="block block-4" title="健康数据对比图">

        <CompareChart></CompareChart>
      </ItemWrap>
      <ItemWrap class="block block-5" title="环境数据监控">
        <EnvRader></EnvRader>
      </ItemWrap>
    </div>
  </div>
</template>

<script>
import IChart from "./indicator-chart.vue";
import CompareChart from "./compare-chart.vue";
import HRader from "./health-rader.vue";
import EnvRader from "./env-rader.vue";
import HWarning from "./health-warning.vue";
import BMIChart from "./bmi-chart.vue";
import axios from "axios";
import { baseUrl } from "@/api/api";
export default {
  components: {
    IChart, CompareChart, HRader, EnvRader, HWarning, BMIChart
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

<style lang="scss" scoped>
.block {
  display: flex;
  align-items: center;
  font-size: 24px;
  color: #fff;
  justify-content: space-around;
}

/* 第一块 */
.block-1 {
  display: flex;
  flex-direction: column;
  width: 25%;
}

/* 每个区域 */
.block-1 .block-1-area {
  flex: 1;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.user-info {
  text-align: left;
  font-size: 18px;
  line-height: 1.5;
  color: #fff;
  padding-left: 120px;
  padding-top: 20px;
}

/* 右侧2/3区域的容器 */
.right {
  display: grid;
  grid-template-columns: 1fr 1fr;
  grid-template-rows: 1fr 1fr;
  gap: 40px;
  width: 75%;
  height: 90%;
  padding-left: 30px;
}

/* 修改下拉框样式 */
:deep(.el-input__inner) {
  background-color: transparent !important;
  color: #d9d9d9;
  height: 40px;
}

/* 修改边框和字体颜色 */
:deep(.el-select) {
  position: relative;
  width: 150px;

  .el-input {
    input {
      height: 40px;
      border-color: rgba(44, 137, 229, 0.5);
      color: #fff;
    }
  }
}

/**修改下拉图标颜色 */
:deep(.el-input__suffix .el-input__suffix-inner .el-icon-arrow-up:before) {
  color: rgba(44, 137, 229, 0.5);
  padding-left: 0.11rem;
}

@keyframes rotating {
  0% {
    -webkit-transform: rotate(0) scale(1);
    transform: rotate(0) scale(1);
  }

  50% {
    -webkit-transform: rotate(180deg) scale(1.1);
    transform: rotate(180deg) scale(1.1);
  }

  100% {
    -webkit-transform: rotate(360deg) scale(1);
    transform: rotate(360deg) scale(1);
  }
}
</style>
