<template>
  <div style="display: flex;">
    <!-- 第一块，分为三个区域，使用 ItemWrap 组件 -->
    <div class="block block-1">
      <ItemWrap class="block-1-area area-1" title="基础信息" style="height: 500px;">
        <div class="user-info">
          <p>ID：{{ userInfo.id }}</p>
          <p>性别：{{ userInfo.gender }}</p>
          <p>年龄：{{ userInfo.age }} 岁</p>
          <p>社区：{{ userInfo.community }}</p>
          <p>身高：{{ userInfo.height }} cm</p>
          <p>体重：{{ userInfo.weight }} kg</p>
        </div>
      </ItemWrap>
      <ItemWrap class="block-1-area area-2" title="今日健康预警">
        <HWarning></HWarning>
      </ItemWrap>
      <ItemWrap class="block-1-area area-3" title="BMI">
        <BMIChart></BMIChart>
      </ItemWrap>
    </div>

    <!-- 剩余2/3的区域 -->
    <div class="right">
      <ItemWrap class="block block-2" title="各项健康指标">
        <div>
          <span style="font-size: 18px;">时间段：</span>
          <el-select v-model="timeChoose" placeholder="请选择">
            <el-option v-for="item in timeOptions" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
        <IChart></IChart>
      </ItemWrap>
      <ItemWrap class="block block-3" title="健康数据监控">
        <HRader></HRader>
      </ItemWrap>
      <ItemWrap class="block block-4" title="健康数据对比图">
        <div>
          <span style="font-size: 18px;">地区：</span>
          <el-select v-model="areaChoose" placeholder="请选择">
            <el-option v-for="item in areaOptions" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </div>
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
export default {
  components: {
    IChart, CompareChart, HRader, EnvRader, HWarning, BMIChart
  },
  data() {
    return {
      userInfo: {
        id: "12345",
        gender: "男",
        age: 30,
        community: "和平小区",
        height: 175,
        weight: 70,
      },
      timeOptions: [
        { value: 'day', label: '过去一天' },
        { value: 'week', label: '过去一周' },
        { value: 'month', label: '过去一月' },
        { value: 'year', label: '过去一年' },
        { value: 'all', label: '全部' },
      ],
      timeChoose: 'day',
      areaOptions:[
      { value: 'community', label: '本社区' },
        { value: 'city', label: '本城市' },
        { value: 'all', label: '全部' },
      ],
      areaChoose: 'community',
    };
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
  font-size: 24px;
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
