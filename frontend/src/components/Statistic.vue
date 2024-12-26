<template>
  <div class="statistic">
    <div class="statistic-title">{{ title }}</div>
    <div class="statistic-value">{{ value }}</div>
  </div>
</template>

<script>
export default {
  name: "Statistic",
  props: {
    title: {
      type: String,
      required: true,
    },
    value: {
      type: [String, Number],
      required: true,
    },
  },
};
</script>

<style scoped>
.statistic {
  width: 90%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border: 2px solid rgb(84, 95, 255); /* 蓝色边框 */
  border-radius: 8px;
  margin: 16px; /* 间距 */
  padding: 16px;
  box-sizing: border-box;
  background: transparent;
}

.statistic-title {
  font-size: 16px;
  color: white;
  text-align: center;
  margin-bottom: 16px; /* 标题与正文的间距 */
}

.statistic-value {
  font-size: 24px;
  font-weight: bold;
  color: rgb(84, 95, 255); /* 蓝色正文 */
  text-align: center;
}
</style>
