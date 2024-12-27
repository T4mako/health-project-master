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
import { FBXLoader } from 'three/examples/jsm/loaders/FBXLoader';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import axios from "axios";
import { baseUrl } from "@/api/api";
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
      mixer: null,
      controls: null,
      userId: 302,
    };
  },
  mounted() {
    this.init();
    this.animate(); // 调用 animate 方法
  },
  methods: {
    init() {
      this.createScene(); // 创建场景
      this.loadRoom(); // 加载房间模型
      this.loadTask(); // 加载人物模型
      this.createCamera(); // 创建相机
      this.createRender(); // 创建渲染器
      this.createLight(); // 创建光源
      this.createControls(); // 创建控制器
    },
    createScene() {
      this.scene = new THREE.Scene();
      // this.scene.background = new THREE.Color();
    },
    loadRoom() {
      const stlLoader = new STLLoader();
      stlLoader.load(
        `${this.publicPath}models/isometric room.stl`, // 房间STL文件路径
        (geometry) => {
          // 创建线框材质
          const material = new THREE.MeshBasicMaterial({
            color: 0x00ffff, // 线框颜色
            wireframe: true, // 启用线框模式
          });

          const roomMesh = new THREE.Mesh(geometry, material);

          // 调整房间尺寸和位置
          roomMesh.scale.set(100, 100, 100);
          roomMesh.position.set(0, 0, 0);
          roomMesh.rotation.x = -Math.PI / 2;

          this.scene.add(roomMesh);
        },
        (xhr) => {
          console.log((xhr.loaded / xhr.total * 100) + '% loaded');
        },
        (error) => {
          console.error('STL加载出错:', error);
        }
      );
    },
    loadTask() {
      const fbxLoader = new FBXLoader();
      fbxLoader.load(
        `${this.publicPath}models/Walk In Circle.fbx`, // 人物FBX文件路径
        (object) => {
          this.mixer = new THREE.AnimationMixer(object);

          if (object.animations.length > 0) {
            const action = this.mixer.clipAction(object.animations[0]);
            action.timeScale = 0.5; // 减慢动画速度
            action.play();
          }

          // 将人物模型的所有子对象改为线框风格
          object.traverse((child) => {
            if (child.isMesh) {
              child.material = new THREE.MeshBasicMaterial({
                color: 0x0000ff, // 线框颜色
                wireframe: true, // 启用线框模式
              });
            }
          });

          // 调整人物模型的尺寸和位置
          object.scale.set(1.8, 1.8, 1.8);
          object.position.set(350, 0, 200);
          object.rotation.y = Math.PI;

          this.scene.add(object);
        },
        (xhr) => {
          console.log((xhr.loaded / xhr.total * 100) + '% loaded');
        },
        (error) => {
          console.error('FBX加载出错:', error);
        }
      );
    },
    createLight() {
      const hemiLight = new THREE.HemisphereLight(0x00ffff, 0x00ffff);
      hemiLight.position.set(0, 200, 0);
      this.scene.add(hemiLight);

      const dirLight = new THREE.DirectionalLight(0x00ffff);
      dirLight.position.set(0, 200, 100);
      dirLight.castShadow = true;
      this.scene.add(dirLight);
    },
    createCamera() {
      const container = this.$refs.centerContainer;
      const width = container.clientWidth;
      const height = container.clientHeight;

      this.camera = new THREE.PerspectiveCamera(35, width / height, 1, 5000);
      this.camera.position.set(1000, 1000, 1000);
      this.camera.lookAt(0, 100, 0);
    },
    createRender() {
      const container = this.$refs.centerContainer;
      this.renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
      this.renderer.setSize(container.clientWidth, container.clientHeight);
      this.renderer.shadowMap.enabled = true;
      container.appendChild(this.renderer.domElement);
    },
    createControls() {
      this.controls = new OrbitControls(this.camera, this.renderer.domElement);
      this.controls.target.set(0, 100, 0);
      this.controls.update();
    },
    animate() {
      requestAnimationFrame(this.animate);

      if (this.mixer) {
        this.mixer.update(0.01);
      }

      this.controls.update();
      this.renderer.render(this.scene, this.camera);
    },

  },
  created() {
    this.userId = this.$route.params.peopleId;    
    axios.get(`${baseUrl}/hData/personLatestHData`, { params: { id: this.userId } })
      .then(response => {
        if (response.code === "200") {
          const data = response.data;
          console.log(data);
          this.leftData = [
          { label: "体温", value: `${data.temperature}°C` },
          { label: "呼吸", value: `${data.breath_rate}` },
          { label: "血糖", value: `${data.blood_glucose}` },
          { label: "心率", value: `${data.heart_rate}` },
          { label: "血氧", value: `${data.blood_oxygen}%` },
          { label: "收缩压", value: `${data.systolic}` },
          { label: "舒张压", value: `${data.diastolic}` },
        ];
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
  flex-grow: 1;
}

.section-title {
  font-size: 20px;
  color: white;
  text-align: center;
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