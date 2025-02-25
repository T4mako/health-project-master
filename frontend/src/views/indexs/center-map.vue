
<template>
  <div class="centermap">
    <div class="maptitle">
      <div class="zuo"></div>
      <span class="titletext">{{ maptitle }}</span>
      <div class="you"></div>
    </div>
    <div class="mapwrap">
      <dv-border-box-13>
        <div class="quanguo" @click="toChina()" v-if="code !== 'china'" >
          中国
        </div>

        <Echart id="CenterMap" :options="options" ref="CenterMap" width="800px" height="590px" style="left: -50px"/>
      </dv-border-box-13>
    </div>
    <div
        class="apartmentDot"
        v-for="val in apartments"
        :style="position(val)"
        v-on:click="toApartment(val)"
    >
      {{ val.value }}
    </div>
  </div>
</template>

<script>
import {mylog} from "@/utils";
import xzqCode from "../../utils/map/xzqCode";
import {currentGET} from "api/modules";
import * as echarts from "echarts";
import {GETNOBASE} from "api";

export default {
  props: {
    isEnvironment: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      maptitle: "分布图",
      options: {},
      code: "china", //china 代表中国 其他地市是行政编码
      echartBindClick: false,
      isSouthChinaSea: true, //是否要展示南海群岛  修改此值请刷新页面
      name: "中国", //判断当前处于哪个地区
      newData: [], // 地图上标的点
      apartments: [],// 小区的点
    };
  },
  mounted() {
    this.getData("china");
  },
  methods: {
    toChina() {
      this.getData('china');
      this.$emit('childNeedChangeData', "中国");
    },
    getData(code) {
      currentGET("big8", {regionCode: code}).then((res) => {
        const data = res.data.dataList;
        if (res.success) {
          this.getGeojson(res.data.regionCode, res.data.dataList);
          this.mapclick();
        } else {
          this.$Message.warning(res.msg);
        }
      });
    },
    /**
     * @description: 获取geojson
     * @param {*} name china 表示中国 其他省份行政区编码
     * @param {*} mydata 接口返回列表数据
     * @return {*}
     */
    async getGeojson(name, mydata) {
      this.code = name;
      //如果要展示南海群岛并且展示的是中国的话
      let geoname = name
      if (this.isSouthChinaSea && name == "china" ) {
        geoname = "chinaNanhai";
      }
      //如果有注册地图的话就不用再注册 了
      let mapjson = echarts.getMap(name);
      if (mapjson) {
        mapjson = mapjson.geoJSON;
      } else {
        mapjson = await GETNOBASE(`./map-geojson/${geoname}.json`).then((res) => {
          return res;
        });
        echarts.registerMap(name, mapjson);
      }
      let cityCenter = {};
      let arr = mapjson.features;
      //根据geojson获取省份中心点
      arr.map((item) => {
        cityCenter[item.properties.name] =
            item.properties.centroid || item.properties.center;
      });

      this.newData = [];
      this.apartments = [];
      // 具体市的点
      if (this.name === "中国" || name === 'china') {
        this.newData.push({name: "徐州市", value: [117.184811, 34.261792, 1]});
        this.newData.push({name: "郑州市", value: [113.477391, 34.626256, 1]});
        this.newData.push({name: "西安市", value: [108.797426, 34.10671, 1]});
      } else if (this.name === "陕西省" || this.name === '西安市') {
        // this.newData.push({name: "西安市", value: [108.797426, 34.10671, 1]});
        this.apartments = [
          {
            id: 22,
            value: "东关南街社区",
            top: 365,
            left: 200
          },
          {
            id: 23,
            value: "长安路社区",
            top: 355,
            left: 430
          },
          {
            id: 24,
            value: "长乐坊社区",
            top: 314,
            left: 585
          },
          {
            id: 25,
            value: "小寨永松路社区",
            top: 320,
            left: 330
          },
        ]
      } else if (this.name === "河南省" || this.name === '郑州市') {
        // this.newData.push({name: "郑州市", value: [113.477391, 34.626256, 1]});
        this.apartments = [
          {
            id: 16,
            value: "九街示范区",
            top: 365,
            left: 200
          },
          {
            id: 17,
            value: "城南示范区",
            top: 355,
            left: 430
          },
          {
            id: 18,
            value: "城北示范区",
            top: 314,
            left: 585
          },
          {
            id: 19,
            value: "西站示范区",
            top: 320,
            left: 330
          },
        ]
      } else if (this.name === "江苏省" || this.name === '徐州市') {
        // this.newData.push({name: "徐州市", value: [117.184811, 34.261792, 1]});
        this.apartments = [
          {
            id: 13,
            value: "永安社区",
            top: 365,
            left: 200
          },
          {
            id: 26,
            value: "泉山社区",
            top: 355,
            left: 430
          },
          {
            id: 27,
            value: "黄山社区",
            top: 314,
            left: 585
          },
          {
            id: 28,
            value: "铜沛社区",
            top: 320,
            left: 330
          },
        ]
      }
      // 小区的点
      this.$nextTick(() => {
        this.init(name, mydata);
      });
    },
    init(name, data) {
      let top = 45;
      let zoom = 1.05;
      let option = {
        backgroundColor: "rgba(0,0,0,0)",
        tooltip: {
          show: false,
        },
        legend: {
          show: false,
        },

        geo: {
          map: name,
          roam: false,
          selectedMode: false, //是否允许选中多个区域
          zoom: zoom,
          top: top,
          // aspectScale: 0.78,
          show: false,
        },
        series: [
          {
            name: "MAP",
            type: "map",
            map: name,
            // aspectScale: 0.78,
            data: data,
            // data: [1,100],
            selectedMode: false, //是否允许选中多个区域
            zoom: zoom,
            geoIndex: 1,
            top: top,
            tooltip: {
              show: true,
              formatter: function (params) {
                if (params.data) {
                  return params.name + "：" + params.data["value"];
                } else {
                  return params.name;
                }
              },
              backgroundColor: "rgba(0,0,0,.6)",
              borderColor: "rgba(147, 235, 248, .8)",
              textStyle: {
                color: "#FFF",
              },
            },
            label: {
              show: false,
              color: "#000",
              // position: [-10, 0],
              formatter: function (val) {
                // console.log(val)
                if (val.data !== undefined) {
                  return val.name.slice(0, 2);
                } else {
                  return "";
                }
              },
              rich: {},
            },
            emphasis: {
              label: {
                show: false,
              },
              itemStyle: {
                areaColor: "#389BB7",
                borderWidth: 1,
              },
            },
            itemStyle: {
              borderColor: "rgba(147, 235, 248, .8)",
              borderWidth: 1,
              areaColor: {
                type: "radial",
                x: 0.5,
                y: 0.5,
                r: 0.8,
                colorStops: [
                  {
                    offset: 0,
                    color: "rgba(147, 235, 248, 0)", // 0% 处的颜色
                  },
                  {
                    offset: 1,
                    color: "rgba(147, 235, 248, .2)", // 100% 处的颜色
                  },
                ],
                globalCoord: false, // 缺为 false
              },
              shadowColor: "rgba(128, 217, 248, .3)",
              shadowOffsetX: -2,
              shadowOffsetY: 2,
              shadowBlur: 10,
            },
          },
          {
            data: this.newData,
            type: "effectScatter",
            coordinateSystem: "geo",
            symbolSize: function (val) {
              return 4;
              // return val[2] / 50;
            },
            legendHoverLink: true,
            showEffectOn: "render",
            rippleEffect: {
              // period: 4,
              scale: 6,
              color: "rgba(255,255,255, 1)",
              brushType: "fill",
            },
            tooltip: {
              show: true,
              formatter: function (params) {
                if (params.data) {
                  return params.name + "：" + params.data["value"][2];
                } else {
                  return params.name;
                }
              },
              backgroundColor: "rgba(0,0,0,.6)",
              borderColor: "rgba(147, 235, 248, .8)",
              textStyle: {
                color: "#FFF",
              },
            },
            label: {
              formatter: (param) => {
                return param.name.slice(0, 2);
              },
              fontSize: 11,
              offset: [0, 2],
              position: "bottom",
              textBorderColor: "#fff",
              textShadowColor: "#000",
              textShadowBlur: 10,
              textBorderWidth: 0,
              color: "#FFF",
              show: true,
            },
            // colorBy: "data",
            itemStyle: {
              color: "rgba(255,255,255,1)",
              borderColor: "rgba(2255,255,255,2)",
              borderWidth: 4,
              shadowColor: "#000",
              shadowBlur: 10,
            },
          },
        ],

      };
      this.options = option;
    },
    message(text) {
      this.$Message({
        text: text,
        type: "warning",
      });
    },
    mapclick() {
      if (this.echartBindClick) return;
      //单击切换到级地图，当mapCode有值,说明可以切换到下级地图
      this.$refs.CenterMap.chart.on("click", (params) => {
        let xzqData = xzqCode[params.name];
        this.name = params.name;
        const isProvince = this.name.indexOf("省")
        if (isProvince) {
          console.log("开始传递省份");
          // 传递数据给父组件通知进行数据修改
          this.$emit('childNeedChangeData', this.name);
        }
        switch (xzqData.adcode) {
          case '320000':
            xzqData.adcode = '320300';
            break;
          case '610000':
            xzqData.adcode = '610100';
            break;
          case '410000':
            xzqData.adcode = '410100';
            break;
        }
        if (xzqData) {
          const tempName = params.name;
          this.getData(xzqData.adcode);
        } else {
          // 判断当前具体是哪个地区
          mylog(this.name)
        }
      });
      this.echartBindClick = true;
    },
    // 跳转到具体小区
    toApartment(val) {
      if (this.isEnvironment === true){
        this.$router.push({name:'city',params: {cityName:val.value}})
      }else {
        // 当 isEnvironment 为 false 时，执行其他操作
        this.handleCommunityClick(val.value);
      }
    },
    //向environment传递社区名字
    handleCommunityClick(value) {
        // 触发自定义事件，将社区名传递出去
        this.$emit('communityClick', value);
    },
    /**
     * 小区点位
     * @param val
     * @returns {string}
     */
    position(val) {
      return "top: " + val.top + "px;" + "left: " + val.left + "px;";
    },
  },
};
</script>
<style lang="scss" scoped>
.apartmentDot {
  background: #fff;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  z-index: 9999;
  position: absolute;
  cursor: pointer;
  //top: 10px;
  //left: 10px;
}

.centermap {
  margin-bottom: 30px;
  overflow: hidden;
  position: relative;

  .maptitle {
    height: 60px;
    display: flex;
    justify-content: center;
    padding-top: 10px;
    box-sizing: border-box;

    .titletext {
      font-size: 28px;
      font-weight: 900;
      letter-spacing: 6px;
      background: linear-gradient(
              92deg,
              #0072ff 0%,
              #00eaff 48.8525390625%,
              #01aaff 100%
      );
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      margin: 0 10px;
    }

    .zuo,
    .you {
      background-size: 100% 100%;
      width: 29px;
      height: 20px;
      margin-top: 8px;
    }

    .zuo {
      background: url("../../assets/img/xiezuo.png") no-repeat;
    }

    .you {
      background: url("../../assets/img/xieyou.png") no-repeat;
    }
  }

  .mapwrap {
    height: 548px;
    width: 100%;
    // padding: 0 0 10px 0;
    box-sizing: border-box;
    position: relative;
    display: flex;
    justify-content: space-around;

    .quanguo {
      position: absolute;
      right: 20px;
      top: -46px;
      width: 80px;
      height: 28px;
      border: 1px solid #00eded;
      border-radius: 10px;
      color: #00f7f6;
      text-align: center;
      line-height: 26px;
      letter-spacing: 6px;
      cursor: pointer;
      box-shadow: 0 2px 4px rgba(0, 237, 237, 0.5),
      0 0 6px rgba(0, 237, 237, 0.4);
    }
  }
}
</style>
