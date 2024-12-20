<template>
  <div class="container">
    <!-- 左侧区域 -->
    <div class="left">
      <div class="section-title">健康信息</div>
      <Statistic
        v-for="(item, index) in leftData"
        :key="index"
        :title="item.label"
        :value="item.value"
      />
    </div>

    <!-- 中间区域 -->
    <div class="center" ref="centerContainer">
      <div id="stl-viewer"></div>
    </div>

    <!-- 右侧区域 -->
    <div class="right">
      <div class="section-title">环境信息</div>
      <Statistic
        v-for="(item, index) in rightData"
        :key="index"
        :title="item.label"
        :value="item.value"
      />
    </div>
  </div>
</template>

<script>
import * as THREE from 'three';
import Statistic from "@/components/Statistic";
import { STLLoader } from 'three/examples/jsm/loaders/STLLoader';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';

export default {
  components: { Statistic },
  data() {
    return {
      publicPath: process.env.BASE_URL,
      leftData: [
        { label: "体温", value: "25°C" },
        { label: "呼吸", value: "12" },
        { label: "血糖", value: "13" },
        { label: "心率", value: "65" },
        { label: "血氧", value: "95%" },
        { label: "收缩压", value: "86" },
        { label: "舒张压", value: "135" },
      ],
      rightData: [
        { label: "CO", value: "400 μg/m³" },
        { label: "天气", value: "晴天" },
        { label: "PM2.5", value: "10 μg/m³" },
        { label: "PM10", value: "20 μg/m³" },
        { label: "O₃", value: "120 μg/m³" },
        { label: "SO₂", value: "15 μg/m³" },
        { label: "NO₂", value: "25 μg/m³" },
      ],
      scene: null,
      camera: null,
      renderer: null,
      mesh: null, // 线框网格
    };
  },
  mounted() {
    this.init();
  },
  methods: {
    init() {
      this.createScene(); // 创建场景
      this.loadSTL(); // 加载STL模型
      this.createCamera(); // 创建相机
      this.createRender(); // 创建渲染器
      this.createLight(); // 创建光源
      this.createControls(); // 创建控制器
      this.render(); // 渲染
    },
    createScene() {
      this.scene = new THREE.Scene();
    },
    // 加载STL模型
    loadSTL() {
      const THIS = this;
      const loader = new STLLoader();
      loader.load(
        `${THIS.publicPath}models/isometric room2.stl`, // STL 文件路径
        (geometry) => {
          // 创建线框材质
          const material = new THREE.MeshBasicMaterial({
            color: 0x00ffff, // 线框颜色
            wireframe: true, // 启用线框模式
            wireframeLinewidth: 2, // 线宽
          });

          // 创建线框网格
          this.mesh = new THREE.Mesh(geometry, material);

          // 调整模型位置与缩放
          this.mesh.rotation.x = -Math.PI / 2; // 沿 X 轴旋转 90 度
          this.mesh.scale.set(35, 35, 35); // 缩放模型
          geometry.center(); // 居中模型

          // 添加到场景
          this.scene.add(this.mesh);
        },
        undefined,
        (error) => {
          console.error('STL 文件加载失败:', error);
        }
      );
    },
    // 创建光源
    createLight() {
      // 环境光
      const ambientLight = new THREE.AmbientLight(0xffffff, 0.1); // 创建环境光
      this.scene.add(ambientLight); // 将环境光添加到场景

      // 聚光灯
      const spotLight = new THREE.SpotLight(0xffffff); // 创建聚光灯
      spotLight.position.set(150, 150, 150);
      spotLight.castShadow = true;
      this.scene.add(spotLight);
    },
    // 创建相机
    createCamera() {
      const container = this.$refs.centerContainer;
      const width = container.clientWidth; // 窗口宽度
      const height = container.clientHeight; // 窗口高度
      const k = width / height; // 窗口宽高比

      // PerspectiveCamera( fov, aspect, near, far )
      this.camera = new THREE.PerspectiveCamera(30, k, 0.4, 2500);
      this.camera.position.set(500, 100, 300); // 设置相机位置
      this.camera.lookAt(new THREE.Vector3(0, 0, 0)); // 设置相机方向
      this.scene.add(this.camera);
    },
    // 创建渲染器
    createRender() {
      const container = this.$refs.centerContainer;
      this.renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
      this.renderer.setClearAlpha(0.2);
      this.renderer.setSize(container.clientWidth, container.clientHeight); // 设置渲染区域尺寸
      this.renderer.shadowMap.enabled = true; // 显示阴影
      this.renderer.shadowMap.type = THREE.PCFSoftShadowMap;
      this.renderer.setClearColor(0x3f3f3f, 0); // 设置背景颜色
      container.appendChild(this.renderer.domElement);
    },
    // 渲染循环
    render() {
      this.renderer.render(this.scene, this.camera);
      requestAnimationFrame(this.render);
    },
    // 创建控件对象
    createControls() {
      this.controls = new OrbitControls(this.camera, this.renderer.domElement);
    },
  },
};
</script>

<style scoped>
.container {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

.left,
.right {
  width: 25%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 10px;
  box-sizing: border-box;
  flex-grow: 1; /* 使得左侧和右侧区域填满剩余空间 */
}

.section-title {
  font-size: 20px;
  color: white;
  text-align: center; /* Ensures title is centered */
  margin-bottom: 20px;
}

.center {
  width: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
}

#stl-viewer {
  width: 100%;
  height: 100%;
}

.section-title {
  font-size: 32px;
  color: rgb(84, 95, 255);
  margin-top: 20px;
}
</style>