<template>
  <div class="container">
    <!-- 中间区域 -->
    <div class="center" ref="centerContainer">
      <div id="stl-viewer"></div>
    </div>

    <!-- 滑动条 -->
    <div class="slider-container">
      <el-slider v-model="sliderValue" :min="0" :max="dataLength - 1" @input="updateValues" />
      <div class="time-display">测量时间：{{ currentTime }}</div>
    </div>

    <!-- 摄像机旋转开关 -->
    <div class="camera-control">
      <el-switch v-model="isCameraRotating" active-text="旋转摄像机" inactive-text="停止旋转" />
    </div>
  </div>
</template>

<script>
import * as THREE from 'three';
import { STLLoader } from 'three/examples/jsm/loaders/STLLoader';
import { FBXLoader } from 'three/examples/jsm/loaders/FBXLoader';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls';
import { CSS2DRenderer, CSS2DObject } from 'three/examples/jsm/renderers/CSS2DRenderer';
import axios from "axios";
import { baseUrl } from "@/api/api";

export default {
  data() {
    return {
      publicPath: process.env.BASE_URL,
      scene: null,
      camera: null,
      renderer: null,
      mixer: null,
      controls: null,
      userId: 302,
      sliderValue: 0,
      dataLength: 2,
      currentTime: '',
      healthLabels: [],
      environmentLabels: [],
      healthData: {
        temperature: 25,
        breath_rate: 12,
        blood_glucose: 13,
        heart_rate: 65,
        blood_oxygen: 95,
        systolic: 86,
        diastolic: 135,
      },
      environmentData: {
        CO: 400,
        light: "晴",
        pressure: 1025,
        PM25: 45,
        PM10: 105,
        humidity: 45,
        temperature: 1,
      },
      labelRenderer: null, // 用于存储 CSS2DRenderer 实例
      character: null,
      buildingBox: null, // 用于存储建筑方框
      data: [], // 存储接口返回的数据
      isCameraRotating: true, // 控制摄像机是否旋转
      cameraAngle: 0, // 摄像机旋转角度
    };
  },
  mounted() {
    this.init();
    this.animate();
  },
  beforeDestroy() {
    this.cleanup(); // 组件销毁时清理资源
  },
  methods: {
    init() {
      this.createScene();
      this.loadRoom();
      this.loadTask();
      this.createCamera();
      this.createRender();
      this.createLight();
      this.createControls();
      this.createLabels();
    },
    cleanup() {
      // 移除 CSS2DRenderer 的 DOM 元素
      if (this.labelRenderer && this.labelRenderer.domElement) {
        document.body.removeChild(this.labelRenderer.domElement);
      }

      // 清理 Three.js 场景
      if (this.scene) {
        this.scene.traverse((object) => {
          if (object instanceof CSS2DObject && object.element) {
            object.element.remove(); // 移除所有 CSS2DObject 的 DOM 元素
          }
        });
      }

      // 停止动画循环
      cancelAnimationFrame(this.animationFrameId);

      // 清理其他资源
      if (this.renderer) {
        this.renderer.dispose();
      }
      if (this.controls) {
        this.controls.dispose();
      }
    },
    createScene() {
      this.scene = new THREE.Scene();
    },
    loadRoom() {
      const stlLoader = new STLLoader();
      stlLoader.load(
        `${this.publicPath}models/isometric room.stl`,
        (geometry) => {
          const material = new THREE.MeshBasicMaterial({
            color: 0x00ffff,
            wireframe: true,
          });

          const roomMesh = new THREE.Mesh(geometry, material);
          roomMesh.scale.set(100, 100, 100);
          roomMesh.position.set(0, 0, 0);
          roomMesh.rotation.x = -Math.PI / 2;

          this.scene.add(roomMesh);

          // 创建建筑方框
          const boxGeometry = new THREE.BoxGeometry(1300, 600, 1600); // 自定义尺寸
          const edges = new THREE.EdgesGeometry(boxGeometry); // 创建边界几何体
          const lineMaterial = new THREE.LineBasicMaterial({ color: 0x00ff00 }); // 绿色
          this.buildingBox = new THREE.LineSegments(edges, lineMaterial); // 用 LineSegments 表示边框
          this.buildingBox.position.set(200, 250, -50); // 设置建筑方框的位置
          this.scene.add(this.buildingBox); // 添加到场景
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
        `${this.publicPath}models/Walk In Circle.fbx`,
        (object) => {
          this.mixer = new THREE.AnimationMixer(object);

          if (object.animations.length > 0) {
            const action = this.mixer.clipAction(object.animations[0]);
            action.timeScale = 0.5;
            action.play();
          }

          object.traverse((child) => {
            if (child.isMesh) {
              child.material = new THREE.MeshBasicMaterial({
                color: 0x0000ff,
                wireframe: true,
              });
            }
          });

          object.scale.set(1.8, 1.8, 1.8);
          object.position.set(350, 0, 200);
          object.rotation.y = Math.PI;

          this.scene.add(object);
          this.character = object; // 保存人物模型

          // 创建方框
          const boxGeometry = new THREE.BoxGeometry(135, 350, 135); // 自定义尺寸
          const edges = new THREE.EdgesGeometry(boxGeometry); // 创建边界几何体
          const lineMaterial = new THREE.LineBasicMaterial({ color: 0xfad700 }); // 黄色
          this.boundingBox = new THREE.LineSegments(edges, lineMaterial); // 用 LineSegments 表示边框
          this.scene.add(this.boundingBox); // 添加到场景
        },
        (xhr) => {
          console.log((xhr.loaded / xhr.total * 100) + '% loaded');
        },
        (error) => {
          console.error('FBX加载出错:', error);
        }
      );
    },
    animate() {
      this.animationFrameId = requestAnimationFrame(this.animate);

      if (this.mixer) {
        this.mixer.update(0.01);
      }

      if (this.character && this.boundingBox) {
        const position = new THREE.Vector3();
        this.character.traverse((child) => {
          if (child.isBone && child.name === "mixamorig1Hips") { // 替换为实际的根骨骼名称
            child.getWorldPosition(position);
            // 将方框的位置和旋转与人物模型同步
            this.boundingBox.position.copy(position);
            this.boundingBox.rotation.copy(position);

            // 更新健康数据标签的位置
            this.healthLabels.forEach((label, index) => {
              label.position.set(position.x - 150, position.y + index * 20, position.z);
            });
          }
        });
      }

      // 摄像机旋转逻辑
      if (this.isCameraRotating) {
        const radius = 2250; // 摄像机旋转半径
        const speed = 0.002; // 摄像机旋转速度
        this.cameraAngle += speed; // 更新旋转角度

        // 计算摄像机的新位置
        this.camera.position.x = radius * Math.sin(this.cameraAngle);
        this.camera.position.z = radius * Math.cos(this.cameraAngle);
        this.camera.lookAt(0, 100, 0); // 让摄像机始终看向场景中心
      }

      this.controls.update();
      this.renderer.render(this.scene, this.camera);

      if (this.labelRenderer) {
        this.labelRenderer.render(this.scene, this.camera);
      }
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
    createLabels() {
      // 创建 CSS2DRenderer
      this.labelRenderer = new CSS2DRenderer();
      this.labelRenderer.setSize(window.innerWidth, window.innerHeight);
      this.labelRenderer.domElement.style.position = 'absolute';
      this.labelRenderer.domElement.style.top = '0';
      this.labelRenderer.domElement.style.pointerEvents = 'none';
      this.labelRenderer.domElement.style.zIndex = '9999'; // 确保标签在最上层
      document.body.appendChild(this.labelRenderer.domElement);

      // 健康数值标签
      const healthFields = [
        { label: '体温', field: 'hd_temperature', unit: '°C' },
        { label: '呼吸率', field: 'hd_breath_rate', unit: '次每分钟' },
        { label: '收缩压', field: 'hd_systolic', unit: 'mm Hg' },
        { label: '舒张压', field: 'hd_diastolic', unit: 'mm Hg' },
        { label: '血氧', field: 'hd_blood_oxygen', unit: '百分比' },
        { label: '心率', field: 'hd_heart_rate', unit: 'Bpm' },
        { label: '血糖', field: 'hd_blood_glucose', unit: 'mmol/L' },
      ];

      healthFields.forEach((item, index) => {
        const healthLabel = new CSS2DObject(document.createElement('div'));
        healthLabel.element.className = 'label';
        healthLabel.element.textContent = `${item.label}: ${this.healthData[item.field] || '未测量'}${item.unit}`;
        healthLabel.element.style.color = '#fad700';
        healthLabel.element.style.fontSize = '12px';
        healthLabel.element.style.fontWeight = 'bold';
        healthLabel.element.style.backgroundColor = 'rgba(0, 0, 0, 0)';
        healthLabel.element.style.padding = '5px';
        healthLabel.position.set(0, 0, 0); // 初始位置设置为 (0, 0, 0)
        this.scene.add(healthLabel);
        this.healthLabels.push(healthLabel);
      });

      // 环境数值标签
      const environmentFields = [
        { label: 'CO', field: 'ev_co', unit: ' mg/m³' },
        { label: '气压', field: 'ev_pressure', unit: ' hPa' },
        { label: '天气', field: 'ev_light', unit: '' },
        { label: 'PM2.5', field: 'ev_pm25', unit: ' μg/m³' },
        { label: 'PM10', field: 'ev_pm10', unit: ' μg/m³' },
        { label: '温度', field: 'ev_temperature', unit: ' °C' },
        { label: '湿度', field: 'ev_humidity', unit: ' %' },
      ];

      environmentFields.forEach((item, index) => {
        const environmentLabel = new CSS2DObject(document.createElement('div'));
        environmentLabel.element.className = 'label';
        environmentLabel.element.textContent = `${item.label}: ${this.environmentData[item.field] || '未测量'}${item.unit}`;
        environmentLabel.element.style.color = '#00ff00';
        environmentLabel.element.style.fontSize = '12px';
        environmentLabel.element.style.fontWeight = 'bold';
        environmentLabel.element.style.backgroundColor = 'rgba(0, 0, 0, 0)';
        environmentLabel.element.style.padding = '5px';
        environmentLabel.position.set(-500, 300 - index * 30, 400); // 初始位置设置为建筑方框旁边
        this.scene.add(environmentLabel);
        this.environmentLabels.push(environmentLabel);
      });
    },
    updateValues() {
      if (!this.data || this.data.length === 0) {
        return; // 如果数据未加载，直接返回
      }
      const index = this.sliderValue;
      const currentData = this.data[index];

      // 更新当前时间
      this.currentTime = currentData.ev_create_time;

      // 更新健康数据标签
      this.healthLabels[0].element.textContent = `体温: ${currentData.hd_temperature || '未测量'} °C`;
      this.healthLabels[1].element.textContent = `呼吸率: ${currentData.hd_breath_rate || '未测量'} 次每分钟`;
      this.healthLabels[2].element.textContent = `收缩压: ${currentData.hd_systolic || '未测量'} mm Hg`;
      this.healthLabels[3].element.textContent = `舒张压: ${currentData.hd_diastolic || '未测量'} mm Hg`;
      this.healthLabels[4].element.textContent = `血氧: ${currentData.hd_blood_oxygen || '未测量'} %`;
      this.healthLabels[5].element.textContent = `心率: ${currentData.hd_heart_rate || '未测量'} Bpm`;
      this.healthLabels[6].element.textContent = `血糖: ${currentData.hd_blood_glucose || '未测量'} mmol/L`;

      // 更新环境数据标签
      this.environmentLabels[0].element.textContent = `CO: ${currentData.ev_co || '未测量'} `;
      this.environmentLabels[1].element.textContent = `气压: ${currentData.ev_pressure || '未测量'}hPa`;
      this.environmentLabels[2].element.textContent = `天气: ${currentData.ev_light || '未测量'}`;
      this.environmentLabels[3].element.textContent = `PM2.5: ${currentData.ev_pm25 || '未测量'}μg/m³`;
      this.environmentLabels[4].element.textContent = `PM10: ${currentData.ev_pm10 || '未测量'}μg/m³`;
      this.environmentLabels[5].element.textContent = `温度: ${currentData.ev_temperature || '未测量'}°C`;
      this.environmentLabels[6].element.textContent = `湿度: ${currentData.ev_humidity || '未测量'}%`;
    },
  },
  created() {
    this.userId = this.$route.params.peopleId;
    axios.get(`${baseUrl}/hData/allHealthAndEnvData`, { params: { id: this.userId } })
      .then(response => {
        if (response.code == "200") {
          this.data = response.data;
          this.dataLength = this.data.length;
          this.sliderValue = this.dataLength - 1;
          this.currentTime = this.data[0].ev_create_time;
          this.updateValues();
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

.center {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.slider-container {
  position: absolute;
  bottom: 20px;
  width: 60%;
  /* 设置滑动条的宽度 */
  left: 50%;
  /* 将容器的左边缘定位到页面中间 */
  transform: translateX(-50%);
  /* 通过负的 50% 平移，使容器居中 */
  text-align: center;
}

.time-display {
  color: white;
  font-size: 16px;
  margin-top: 10px;
}

.label {
  color: white;
  font-size: 16px;
  background-color: rgba(0, 0, 0, 0.5);
  padding: 5px;
  border-radius: 5px;
  pointer-events: none;
  /* 防止标签拦截鼠标事件 */
}

.camera-control {
  position: absolute;
  top: 20px;
  left: 20px;
  z-index: 1000;
}
</style>