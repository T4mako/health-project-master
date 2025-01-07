<template>
  <div id="people">
    <div class="left" >
      <div class="peopleInfo" >
        <div class="info" >
          个人信息:
          <div>ID ：{{ peopleId }}</div>
          <div>性别：{{ gender }}</div>
          <div>年龄：{{ age }}</div>
          <div>身高：{{ height}} </div>
          <div>体重：{{ weight}} </div>
        </div>
        <div style="margin-top: 150px;">
          <el-button type="primary" @click="goRoom()">房间数字孪生</el-button>
        </div>
      </div>
      <div style="margin-top: 90px"><div class="leftbox" v-for="data in leftData" >
        <div class="lefticon">
          <img :src="data.icon" alt="">
        </div>
        <div class="boxright">
          <div class="lefttitle">{{ data.title }}</div>
          <div class="leftdata">
            <div class="leftdatanumber">{{ data.currentNumber }}</div>
            <span class="numberunit">{{ data.unit }}</span>
          </div>
          <div class="leftprocess">
            <el-progress :text-inside="true" :stroke-width=" 25" :percentage="data.currentNumber"
                         status="success" :show-text="false" color="rgb(0, 205, 255)"></el-progress>
          </div>
        </div>

      </div></div>

    </div>

    <div class="center">
      <div class="peopelGl container" id="container" ref="container">

      </div>
      <div class="centerBottom">
        <div class="centerBottomBox" v-for="d in bottomData">
          <div class="bottomTitle">{{d.title}}</div>
          <div class="bottomContextBox">
            <div class="bottomContext">{{d.data}}</div>
            <div class="bottomunit">{{d.unit}}</div>
          </div>
        </div>
      </div>
    </div>

    <div class="right" >
      <div style="font-size: 26px;width: 400px;line-height: 40px;margin-bottom: 45px">
        <div>信息测量时间：{{ p_create_time}} </div>
      </div>
      <div class="leftbox" v-for="data in rightData" :key="data.title">
        <div class="righticon">
          <img :src="data.icon" alt="">
        </div>
        <div class="boxright">
          <div class="lefttitle">{{ data.title }}</div>
          <div class="leftdata" style="margin-top: 40px">
            <div class="leftdatanumber">{{ data.currentNumber }}</div>
            <span class="numberunit">{{ data.unit }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script type="module">
import * as THREE from 'three'
import { STLLoader } from 'three/examples/jsm/loaders/STLLoader'
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls'
import axios from 'axios';
import { baseUrl } from "@/api/api";

export default {
  name: "index",
  data() {
    return {
      peopleId: '',
      gender: '', // 性别初始化
      age: 0, // 年龄初始化
      height: 0, // 身高初始化
      weight: 0, // 体重初始化
      p_create_time: '', // 个人测量时间初始化
      e_create_time: '', // 环境测量时间初始化
      publicPath: process.env.BASE_URL,
      mesh: null,
      camera: null,
      scene: null,
      renderer: null,
      controls: null,
      leftData: [
        {
          icon: require('../../assets/img/people/temperature.jpg'),
          title: '体温',
          currentNumber: 0,
          unit: '℃',
          maxMumber: 42
        },
        {
          icon: require('../../assets/img/people/sleep.jpg'),
          title: '呼吸',
          currentNumber: 0,
          unit: '次/小时',
          maxMumber: 30
        },
        {
          icon: require('../../assets/img/people/temperature.jpg'),
          title: 'BMI',
          currentNumber: 0,
          unit: '',
          maxMumber: 40
        },
        {
          icon: require('../../assets/img/people/Blood sugar.jpg'),
          title: '血糖',
          currentNumber: 0,
          unit: 'mmol/L',
          maxMumber: 40
        },
      ],
      rightData: [
        {
          icon: require('../../assets/img/people/sphygmus.jpg'),
          title: '心率',
          unit: 'bpm',
          currentNumber: 0
        },
        {
          icon: require('../../assets/img/people/Blood oxygen.jpg'),
          title: '血氧',
          unit: '%',
          currentNumber: 0
        },
        {
          icon: require('../../assets/img/people/SBP.jpg'),
          title: '收缩压',
          unit: 'mmHg',
          currentNumber: 0
        },
        {
          icon: require('../../assets/img/people/DBP.jpg'),
          title: '舒张压',
          unit: 'mmHg',
          currentNumber: 0
        },
      ],
      bottomData: [
        { title: 'CO', data: 0, unit: 'ppm' },
        { title: '气压', data: 0, unit: 'ug/m³' },
        { title: '光照', data: 0, unit: 'lx' },
        { title: 'PM2.5', data: 0, unit: 'μg/m³' },
        { title: 'PM10', data: 0, unit: 'μg/m³' },
        { title: '温度', data: 0, unit: '°C' },
        { title: '湿度', data: 0, unit: '%' }
      ],
    }
  },
  created() {
    this.peopleId = this.$route.params.peopleId;
  },
  mounted() {
    // 调用接口获取个人健康数据
    axios.get(`${baseUrl}/city/getPersonalHealthData?id=${this.peopleId}`).then(response => {
          const data = response.data;
          if (data.p_temperature === -999){
            data.p_temperature = '未测量'
          }
          if(data.breath_rate === 0){
            data.breath_rate = '未测量'
          }
          if(data.blood_oxygen === 0){
            data.blood_oxygen = '未测量'
          }
          if(data.heart_rate === 0){
            data.heart_rate = '未测量'
          }
          if(data.bmi === 0){
            data.bmi = '未测量'
          }
          if(data.blood_glucose === 0){
            data.blood_glucose = '未测量'
          }
          if(data.systolic ===0){
            data.systolic ='未测量'
          }
          if(data.diastolic === 0){
            data.diastolic = '未测量'
          }
          // 更新左侧数据
          this.leftData = [
            { icon: require('../../assets/img/people/temperature.jpg'), title: '体温', currentNumber: data.p_temperature, unit: '℃', maxMumber: 42 },
            { icon: require('../../assets/img/people/sleep.jpg'), title: '呼吸', currentNumber: data.breath_rate, unit: '次/小时', maxMumber: 30 },
            { icon: require('../../assets/img/people/temperature.jpg'), title: 'BMI', currentNumber: data.bmi, unit: '', maxMumber: 40 },
            { icon: require('../../assets/img/people/Blood sugar.jpg'), title: '血糖', currentNumber: data.blood_glucose, unit: 'mmol/L', maxMumber: 40 },
          ];

          // 更新右侧数据
          this.rightData = [
            { icon: require('../../assets/img/people/sphygmus.jpg'), title: '心率', unit: 'bpm', currentNumber: data.heart_rate },
            { icon: require('../../assets/img/people/Blood oxygen.jpg'), title: '血氧', unit: '%', currentNumber: data.blood_oxygen },
            { icon: require('../../assets/img/people/SBP.jpg'), title: '收缩压', unit: 'mmHg', currentNumber: data.systolic },
            { icon: require('../../assets/img/people/DBP.jpg'), title: '舒张压', unit: 'mmHg', currentNumber: data.diastolic },
          ];
          if (data.e_temperature === -999){
            data.e_temperature = '未测量'
          }
          if(data.co === 0){
            data.co ='未测量'
          }
          if(data.pm10=== 0){
            data.pm10 = '未测量'
          }
          if(data.pm25 === 0){
            data.pm25 = '未测量'
          }
          if(data.pressure === 0){
            data.pressure ='未测量'
          }
          if(data.humidity === 0){
            data.humidity = '未测量'
          }
          if (data.light === '0'){
            data.light = '未测量'
          }
          // 更新底部数据
          this.bottomData = [
            { title: 'CO', data: data.co, unit: 'ug/m³' },
            { title: '气压', data: data.pressure, unit: 'hPa' },
            { title: '天气', data: data.light, unit: '天' },
            { title: 'PM2.5', data: data.pm25, unit: 'μg/m³' },
            { title: 'PM10', data: data.pm10, unit: 'μg/m³' },
            { title: '温度', data: data.e_temperature, unit: '°C' },
            { title: '湿度', data: data.humidity, unit: '%' }
          ];

          // 更新个人信息
          this.age = data.age;
          this.gender = data.gender;
          this.height = data.height;
          this.weight = data.weight;
          this.e_create_time = data.e_create_time ? data.e_create_time.split(' ')[0] : '';
          //创建时间如果接受为0，则显示未测量
          if (data.p_create_time === '0') {
            this.p_create_time = '未测量'
          }else {
            this.p_create_time = data.p_create_time;
          }
        })
        .catch(error => {
          console.error("获取个人健康数据失败:", error);
        });
    this.init();
  },
  methods: {
    goRoom(){
      this.$router.push({path:'/room/' + this.peopleId})
    },
    init() {
      this.createScene() // 创建场景
      this.loadSTL() // 加载STL模型
      this.createCamera() // 创建相机
      this.createRender() // 创建渲染器
      this.render() // 渲染
    },
    createScene() {
      this.scene = new THREE.Scene()
    },
    // 加载STL模型
    loadSTL() {
      const THIS = this
      const loader = new STLLoader()
      loader.load(
          `${THIS.publicPath}models/person.stl`,
          geometry => {
            // 创建材质
            const material = new THREE.PointsMaterial({
              color: 0x00CCFF,
              size: 3,
              opacity: 1,
              transparent: false,
              blending: THREE.AdditiveBlending,
              depthTest: true,
              map: this.generateSprite()
            });
            this.mesh = new THREE.Points(geometry, material)
            this.mesh.rotation.x = -0.5 * Math.PI
            this.mesh.scale.set(20, 20, 20)
            geometry.center()
            this.scene.add(this.mesh)
          }
      )
    },

    // 使用canvas生成粒子的纹理
    generateSprite() {
      const canvas = document.createElement('canvas');
      canvas.width = 16;
      canvas.height = 16;

      const context = canvas.getContext('2d');
      const gradient = context.createRadialGradient(canvas.width / 2, canvas.height / 2, 0, canvas.width / 2, canvas.height / 2, canvas.width / 2);
      gradient.addColorStop(0, 'rgba(255,255,255,1)');
      gradient.addColorStop(0.2, 'rgba(0,255,255,1)');
      gradient.addColorStop(0.4, 'rgba(0,0,64,1)');
      gradient.addColorStop(1, 'rgba(0,0,0,1)');

      context.fillStyle = gradient;
      context.fillRect(0, 0, canvas.width, canvas.height);

      const texture = new THREE.Texture(canvas);
      texture.needsUpdate = true;
      return texture;
    },

    // 创建光源
    createLight() {
      // 环境光
      const ambientLight = new THREE.AmbientLight(0xffffff, 0.1) // 创建环境光
      this.scene.add(ambientLight) // 将环境光添加到场景

      const spotLight = new THREE.SpotLight(0xffffff) // 创建聚光灯
      spotLight.position.set(150, 150, 150)
      spotLight.castShadow = true
      this.scene.add(spotLight)
    },
    // 创建相机
    createCamera() {
      const element = document.getElementById('container')
      const width = element.clientWidth // 窗口宽度
      console.log(width)
      const height = element.clientHeight // 窗口高度
      console.log(height)
      const k = width / height // 窗口宽高比
      // PerspectiveCamera( fov, aspect, near, far )
      this.camera = new THREE.PerspectiveCamera(28, k, 0.4, 2000)
      this.camera.position.set(250, 120, 100) // 设置相机位置
      this.camera.lookAt(new THREE.Vector3(10, 10, 0)) // 设置相机方向
      this.scene.add(this.camera)
    },
    // 创建渲染器
    createRender() {
      const element = document.getElementById('container')
      this.renderer = new THREE.WebGLRenderer({antialias: true, alpha: true})
      this.renderer.setClearAlpha(0.2);
      this.renderer.setSize(element.clientWidth, element.clientHeight) // 设置渲染区域尺寸
      this.renderer.shadowMap.enabled = true // 显示阴影
      this.renderer.shadowMap.type = THREE.PCFSoftShadowMap
      this.renderer.setClearColor(0x3f3f3f, 0) // 设置背景颜色
      element.appendChild(this.renderer.domElement)
    },
    render() {
      if (this.mesh) {
        this.mesh.rotation.z += 0.006
      }
      this.renderer.render(this.scene, this.camera)
      requestAnimationFrame(this.render)
    },
    // 创建控件对象
    createControls() {
      this.controls = new OrbitControls(this.camera, this.renderer.domElement)
    },
    }
  }

</script>

<style scoped>
#container {
  position: absolute;
  width: 43%;
  height: 60%;
  margin: 0;
  overflow: hidden;
  /* background: url("../../assets/img/pageBg.png") center no-repeat; */
  background-size:cover;
}
.palpitateTitle {
  position: absolute;
  top: 4px;
  left: 10px;
  font-size: 24px;
  text-align: center;
}

.right {
  position: relative;
  margin-top: 20px;
}

.rightTitle {
  font-size: 20px;
}

.righticon img {
  height: 112px;
}

.righticon {
  height: 112px;
  border: 2px solid  rgb(0, 205, 255);
  border-radius: 50%;
  overflow: hidden;
}

.leftprocess {
  margin-top: 16px;
}

.leftdata {
  display: flex;
}

.numberunit {
  font-size: 26px;
  color: white;
}

.leftdatanumber {
  font-size: 52px;
  color: white;
}

.lefttitle {
  font-size: 26px;
}

.lefticon img {
  height: 112px;
}

.lefticon {
  height: 112px;
  border: 2px solid rgb(0, 205, 255); 
}

.boxright {
  height: 142px;
  width: 348px;
}

.leftbox {
  width: 500px;
  height: 168px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

#people {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;

  /* background: url("../../assets/img/people/peopleBg.jpg") center no-repeat; */
}

.left, .center, .right {
  height: 80%;
  width: 500px;
}

.peopleInfo {
  font-size: 24px;
  width: 344px;
  height: 200px;
  position: absolute;
  top: 28px;
  left: 18px;
  display: flex;
  justify-content: space-between;
}

.peopleImg {
  height: 165px;
  width: 140px;
}

.peopleImg img {
  height: 165px;
  width: 140px;

}

.info {
  height: 200px;
  width: 184px;
  font-size: 26px;
  line-height: 42px;

}

.center {
  width: 800px;
}
.peopelGl {
  width: 800px;
  height: 722px;
}
.centerBottom {
  position: absolute;
  bottom: 82px;
  width: 800px;
  height: 120px;
  display: flex;
  justify-content: space-between;
}
.centerBottomBox {
  width: 104px;
}
.bottomContextBox {
  border: 2px solid rgb(0, 205, 255);
  height: 86px;
}
.bottomTitle {
  font-size: 22px;
}
.bottomContext {
  font-size: 30px;
  text-align: center;
  color: rgb(0, 205, 255);
  padding-top: 12px;
}
.bottomunit {
  font-size: 22px;
  text-align: center;
  color: rgb(0, 205, 255);
  margin-top: 6px;
}
</style>
