<template>
  <div class="platform-container">
    <!-- 背景图片容器 -->
    <div class="background">
      <!-- 标题 -->
      <h1 class="title-text">数字健康家庭服务城市平台</h1>

      <!-- 图片展示区 -->
      <div class="images-row">
        <div class="image-container" v-for="(image, index) in images" :key="index" @click="goTo(image.path)">
          <img :src="image.src" :alt="image.alt" class="image-item" />
          <p class="image-caption">{{ image.caption }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "HomeIndex",
  data() {
    return {
      images: [
        { src: require('@/assets/img/back1.png'), alt: "City Health - Person", caption: "城市家庭人员健康", path: "/index" },
        { src: require('@/assets/img/back2.png'), alt: "City Health - Building", caption: "城市健康建筑环境" },
        { src: require('@/assets/img/back3.png'), alt: "Active Health - City Map", caption: "主动健康城市图谱" },
        { src: require('@/assets/img/back4.png'), alt: "Active Health - Model", caption: "主动健康模型" },
      ]
    };
  },
  methods: {
    goTo(path) {
      this.$router.push(path);
    }
  }
};
</script>

<style scoped>
.platform-container {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100vh;
  overflow: hidden;
}

.background {
  position: relative;
  width: 100%;
  height: 100%;
  background-image: url('~@/assets/img/home-background.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.title {
  color: white;
  font-size: 2.5rem;
  text-align: center;
  margin-top: 1rem;
}

.images-row {
  display: flex;
  justify-content: space-around; /* 修改为 space-around，使图片均匀分布在容器内 */
  width: 80%; /* 设置宽度，确保图片容器左右留出足够空间 */
  margin-top: 10rem;
}

.image-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 0 1rem;
}

.image-item {
  width: 250px;
  height: 250px;
  object-fit: cover;
  margin-bottom: 0.5rem;
  cursor: pointer;
  border-radius: 10px;
}

.image-caption {
  margin-top: 5px;
  color: white;
  font-size: 1.25rem;
  text-align: center;
  width: 100%; /* 使文字在图片下方居中 */
  background: linear-gradient(92deg, #0072FF 0%, #00EAFF 48.8525390625%, #01AAFF 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
.title-text {
  font-size: 38px;
  font-weight: 900;
  background: linear-gradient(92deg, #0072FF 0%, #00EAFF 48.8525390625%, #01AAFF 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
</style>
