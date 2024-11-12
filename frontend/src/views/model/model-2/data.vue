<template>
  <div class="dashboard">
    <div class="left-column">
      <!-- 家庭信息 -->
      <ItemWrap title="家庭信息" style="height: 500px; flex: 2; margin-bottom: 20px;">
        <div class="family-info">
          <p>家庭成员：{{ familyInfo.members }}</p>
          <p>遗传病史：{{ familyInfo.geneticHistory }}</p>
        </div>
      </ItemWrap>

      <!-- 个人信息 -->
      <ItemWrap title="个人信息" style="height: 500px; flex: 2; margin-bottom: 20px;">
        <div class="personal-info">
          <ul>
            <li>姓名：{{ familyInfo.name }}</li>
            <li>年龄：{{ familyInfo.age }}</li>
            <li>性别：{{ familyInfo.gender }}</li>
            <li>职业：{{ familyInfo.occupation }}</li>
            <li>作息习惯：{{ familyInfo.sleepHabits }}</li>
            <li>饮食习惯：{{ familyInfo.dietaryHabits }}</li>
            <li>联系方式：{{ familyInfo.contact }}</li>
          </ul>
        </div>
      </ItemWrap>

      <!-- 病例史 -->
      <ItemWrap title="病例史" style="height: 500px; flex: 2; margin-bottom: 20px;">
        <div class="medical-history">
          <p>家庭成员病例史：</p>
          <ul>
            <li v-for="(record, index) in familyInfo.medicalRecords" :key="index">
              {{ record }}
            </li>
          </ul>
          <p>个人病例史：</p>
          <ul>
            <li v-for="(record, index) in familyInfo.personalMedicalRecords" :key="index">
              {{ record }}
            </li>
          </ul>
        </div>
      </ItemWrap>
    </div>

    <div class="middle-column">
      <!-- 男女比例和社区人数调查 -->
      <ItemWrap title="风险指标" style="height: 500px; flex: 2; margin-bottom: 20px;">
        <GenderRatio /> <!-- 假设 GenderRatio 是一个已定义的组件 -->
      </ItemWrap>
      <ItemWrap title="社区人数调查" style="height: 500px; flex: 1; margin-top: -10px;">
        <BarChart /> <!-- 假设 BarChart 是一个已定义的组件 -->
      </ItemWrap>
    </div>

    <div class="right-column">
      <!-- 热力图和条形图 -->
      <ItemWrap title="热力图" style="height: 500px; flex: 1;">
        <Heatmap /> <!-- 假设 Heatmap 是一个已定义的组件 -->
      </ItemWrap>
      <ItemWrap title="风险预测" style="height: 500px; flex: 1;">
        <RiskForecast /> <!-- 假设 RiskForecast 是一个已定义的组件 -->
      </ItemWrap>
    </div>
  </div>
</template>

<script>
import ItemWrap from '@/components/item-wrap/item-wrap.vue';
import GenderRatio from './GenderRatio.vue';
import BarChart from './BarChart.vue';
import Heatmap from './Heatmap.vue';
import RiskForecast from './RiskForecast.vue';

export default {
  components: {
    ItemWrap,
    GenderRatio,
    BarChart,
    Heatmap,
    RiskForecast
  },
  data() {
    return {
      familyInfo: {
        name: '张三',
        age: 30,
        gender: '男',
        members: '父母、兄弟姐妹',
        geneticHistory: '无',
        occupation: '软件工程师',
        sleepHabits: '早上7点起床，睡眠时间不规律，经常熬夜',
        dietaryHabits: '相对均衡饮食，偶尔不吃早晚饭，喜欢蔬菜和水果',
        contact: '1234567890',
        medicalRecords: [
          '父亲：高血压，糖尿病',
          '母亲：无',
          '兄弟：哮喘',
          '姐妹：无'
        ],
        personalMedicalRecords: [
          '无'
        ]
      }
    };
  }
};
</script>

<style lang="scss" scoped>
.dashboard {
  display: flex;
  height: 100%;
}

/* 左侧列 */
.left-column {
  display: flex;
  flex-direction: column;
  width: 25%;
}

/* 中间列 */
.middle-column {
  display: flex;
  flex-direction: column;
  width: 25%;
}

/* 右侧列 */
.right-column {
  display: flex;
  flex-direction: column;
  width: 50%;
}

/* 每个区域 */
.item-wrap {
  flex: 1;
}

/* 家庭信息样式 */
.family-info {
  text-align: left;
  font-size: 18px;
  line-height: 1.5;
  color: #fff;
  padding-left: 20px;
  padding-top: 20px;
}

/* 个人信息样式 */
.personal-info {
  text-align: left;
  font-size: 18px;
  line-height: 1.5;
  color: #fff;
  padding-left: 20px;
  padding-top: 20px;
}

/* 病例史样式 */
.medical-history {
  text-align: left;
  font-size: 18px;
  line-height: 1.5;
  color: #fff;
  padding-left: 20px;
  padding-top: 20px;
}
</style>