<template>
  <div id="people">
    <div class="left">
      <div class="peopleInfo">
        <!-- <div class="peopleImg">
          <img src="../../assets/img/people/people.jpg">
        </div> -->
        <div class="info">
          个人信息:
          <div>姓名：张三</div>
          <div>性别：男</div>
          <div>年龄：84</div>
        </div>
      </div>
      <div class="leftbox" v-for="data in leftData">

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
            <!-- <el-progress :text-inside="false" :stroke-width=" 25" :percentage="data.currentNumber"
                         status="success"></el-progress> -->
            <el-progress :text-inside="true" :stroke-width=" 25" :percentage="data.currentNumber"
                         status="success" :show-text="false" color="rgb(0, 205, 255)"></el-progress>
          </div>
        </div>

      </div>

    </div>

    <div class="center">
<!--      <div class="center container" id="container" ref="container"></div>-->
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

    <div class="right">
      <div class="palpitateTitle">
        <div class="titleOne">节律</div>
        <div class="titleTwo">3</div>
        <div class="titleThree">s</div>
      </div>
      <div id="palpitate" ref="totalFlowRate" style="height: 100px"></div>
      <div class="leftbox" v-for="data in rightData">

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
import * as echarts from "echarts";
import * as THREE from 'three'
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader'
import { STLLoader } from 'three/examples/jsm/loaders/STLLoader'
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls'
import {mylog} from "@/utils";
export default {
  name: "index",
  data() {
    return {
      publicPath: process.env.BASE_URL,
      mesh: null,
      camera: null,
      scene: null,
      renderer: null,
      controls: null,
      peopleName: '',
      leftData: [
        {
          icon: require('../../assets/img/people/temperature.jpg'),
          title: '体温',
          currentNumber: 38.5,
          unit: '℃',
          maxMumber: 42
        },
        {
          icon: require('../../assets/img/people/sleep.jpg'),
          title: '睡眠呼吸暂停综合征',
          currentNumber: 20,
          unit: '次/小时',
          maxMumber: 30
        },
        {
          icon: require('../../assets/img/people/temperature.jpg'),
          title: 'BMI',
          currentNumber: 21,
          unit: '',
          maxMumber: 40
        },
        {
          icon: require('../../assets/img/people/Blood sugar.jpg'),
          title: '血糖',
          currentNumber: 25,
          unit: 'mmol/L',
          maxMumber: 40
        },
        {
          icon: require('../../assets/img/people/TC.jpg'),
          title: 'TC',
          currentNumber: 3,
          unit: 'mmol/L',
          maxMumber: 10
        },
      ],
      rightData: [
        {
          icon: require('../../assets/img/people/sphygmus.jpg'),
          title: '脉搏',
          unit: 'bpm',
          currentNumber: 70
        },
        {
          icon: require('../../assets/img/people/Blood oxygen.jpg'),
          title: '血氧',
          unit: '%',
          currentNumber: 90
        },
        {
          icon: require('../../assets/img/people/SBP.jpg'),
          title: '收缩压',
          unit: 'mmHg',
          currentNumber: 90
        },
        {
          icon: require('../../assets/img/people/DBP.jpg'),
          title: '舒张压',
          unit: 'mmHg',
          currentNumber: 120
        },
      ],
      bottomData: [
        {
          title: 'CO₂',
          data: '800',
          unit: 'ppm'
        },
        {
          title: 'TVOC',
          data: '1000',
          unit: 'ug/m³'
        },
        {
          title: '光照',
          data: '500',
          unit: 'lx'
        },
        {
          title: 'PM2.5',
          data: '201',
          unit: 'μg/m³'
        },
        {
          title: '声音',
          data: '60',
          unit: '分贝'
        },
        {
          title: '温度',
          data: '25',
          unit: '°C'
        },
        {
          title: '湿度',
          data: '40',
          unit: '%'
        },
      ],
    }
  },
  created() {
    this.peopleName = this.$route.params.peopleName;
    // this.getGl()
  },
  mounted() {
    document.getElementById("search").style.display = 'none';
    this.getPalpitate();
    this.init();
  },
  methods: {
    init() {
      this.createScene() // 创建场景
      this.loadSTL() // 加载STL模型
      // this.createLight() // 创建光源
      this.createCamera() // 创建相机
      this.createRender() // 创建渲染器
      // this.createControls() // 创建控件对象
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
      this.renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true })
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




    // 节律
    getPalpitate() {
      var totalFlowRate = echarts.init(this.$refs.totalFlowRate);
      var xAxisData = [];
      var yAxisData = [];
      for (var i = 500; i > 0; i--) {
        xAxisData.push(i + "秒前");
      }
      for (i = 1; i < 501; i++) {
        yAxisData.push(null);
      }
      var totalFlowRateOption = {
        animation: false,
        title: {
          show: false,
          text: '节律（s）'/*,
        left:"110px"*/
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {type: 'cross'}
        },
        grid: {
          left: 50/*"50px"*/,
          right: 15/*"15px"*/
        },
        legend: {
          show: false,
          data: ['当前节律']
        },
        xAxis: {
          boundaryGap: false,
          data: xAxisData,
          show: false
        },
        yAxis: {boundaryGap: false, show: false},
        series: {
          symbol: "none",/*去掉小圆点*/
          name: '当前节律',
          type: 'line',
          data: yAxisData,/*,             smooth:true//显示为平滑的曲线*/
          color: "rgb(0, 205, 255)"
        }
      };

      totalFlowRate.setOption(totalFlowRateOption);

      setInterval(function () {
        yAxisData.push(Math.round(Math.random() * 3000));
        if (yAxisData.length > 500) {
          yAxisData.shift();
        }
        totalFlowRate.setOption(totalFlowRateOption);
      }, 100);
    }
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
  height: 165px;
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
  height: 165px;
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
