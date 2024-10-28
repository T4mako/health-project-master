<template>
  <div class="platform-container">
    <!-- 背景图片容器 -->
    <div class="background">
      <!-- 标题 -->
      <h1 class="title">数字健康家庭服务城市平台</h1>

      <!-- 图片展示区 -->
      <div class="images-row">
        <div class="image-container" v-for="(image, index) in images" :key="index" @click="goTo(image.path)">
          <img :src="image.src" :alt="image.alt" class="image-item" />
          <p class="image-caption">{{ image.caption }}</p>
        </div>
        <!-- 空位置留空 -->
        <div class="image-container">
          <p class="image-caption">空位</p>
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
      // 使用 require 引入背景图片路径
      images: [
        { src: require('@/assets/img/back1.png'), alt: "City Health - Person", caption: "城市家庭人员健康",path:"/index" },
        { src: require('@/assets/img/back2.png'), alt: "City Health - Building", caption: "城市健康建筑环境" },
        { src: require('@/assets/img/back3.png'), alt: "Active Health - City Map", caption: "主动健康城市图谱" }
      ]
    };
  },
  methods:{
    goTo(path) {
      // 使用 Vue Router 跳转到指定页面
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
  justify-content: center;
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
  margin-left: 4rem;
  margin-bottom: 0.5rem;
  cursor: pointer;
}

.image-caption {
  color: white;
  font-size: 1rem;
  text-align: center;
}
</style>
