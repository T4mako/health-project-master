<!--
 * @Author: daidai
 * @Date: 2022-03-01 15:27:58
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-05-07 11:24:14
 * @FilePath: \web-pc\src\pages\big-screen\view\indexs\right-center.vue
-->
<template>
  <div v-if="pageflag" class="right_center_wrap beautify-scroll-def" :class="{ 'overflow-y-auto': !sbtxSwiperFlag }" style="padding-top: 20px;">
    <component :is="components" :data="list" :class-option="defaultOption">
      <ul class="right_center ">
        <li class="right_center_item" v-for="(item, i) in healthData" :key="i" v-on:click="toPeople(item)">
          <span class="orderNum">{{ i + 1 }}</span>
          <div class="inner_right">
            <div class="dibu"></div>
            <div class="flex">
              <div class="info">
                <span class="labels ">姓名：</span>
                <span class="contents zhuyao"> {{ item.researched_person_id }}</span>
              </div>
              <div class="info">
                <span class="labels"> 住址：</span>
                <span class="contents ciyao" style="font-size:12px"> {{ item.family_user_id }}</span>
              </div>
            </div>


            <div class="flex">              
              <div class="info time">
                <span class="labels">时间：</span>
                <span class="contents" style="font-size:12px"> {{ item.create_time }}</span>
              </div>

            </div>
            <div class="flex">

              <div class="info">
                <span class="labels">体温：</span>
                <span class="contents ciyao" :class="{ warning: item.alertdetail }"> {{ item.temperature+"°C" || '无'
                }}</span>
              </div>
              <div class="info">
                <span class="labels">心率：</span>
                <span class="contents ciyao" :class="{ warning: item.alertdetail }"> {{ item.heart_rate+"" || '无'
                }}</span>
              </div>
              <div class="info">
                <span class="labels">呼吸率：</span>
                <span class="contents ciyao" :class="{ warning: item.alertdetail }"> {{ item.breath_rate || '无'
                }}</span>
              </div>
            </div>
          </div>
        </li>
      </ul>
    </component>
  </div>
  <Reacquire v-else @onclick="getData" style="line-height:200px" />

</template>

<script>
import { currentGET } from 'api/modules'
import vueSeamlessScroll from 'vue-seamless-scroll'  // vue2引入方式
import Kong from '../../components/kong.vue'
export default {
  components: { vueSeamlessScroll, Kong },

  data() {
    return {
      healthData:[],
      list: [],
      pageflag: true,
      defaultOption: {
        ...this.$store.state.setting.defaultOption,
        limitMoveNum: 3,
        singleHeight: 250,
        step:0,
      }

    };
  },
  computed: {
    sbtxSwiperFlag() {
      let ssyjSwiper = this.$store.state.setting.ssyjSwiper
      if (ssyjSwiper) {
        this.components = vueSeamlessScroll
      } else {
        this.components = Kong
      }
      return ssyjSwiper
    }
  },
  created() {
    this.getData();
    const apiUrl = "http://www.pahealthsys.cn/device/deviceData/getDataByType";

    const requestData = {
      current: 1, // 第几页
      size: 10, // 每页多少条
      startDate: "", // 开始日期，可以根据需要填写
      endDate: "", // 结束日期，可以根据需要填写
      type: "temperature", // 数据类型
    };

    const requestOptions = {
      method: "POST",
      headers: {
        Authorization: "Bearer 83e60402-b255-4f7d-87ac-139c6564f250",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestData),
    };

    fetch(apiUrl, requestOptions)
      .then((response) => response.json())
      .then((data) => {
        if (data.code === 0) {
          const healthData = data.data.records;
          console.log(healthData); // 在这里处理健康数据
          this.healthData=healthData;
        } else {
          console.error("请求失败:", data.msg);
        }
      })
      .catch((error) => {
        console.error("请求失败:", error);
      });
  },

  mounted() { },
  methods: {
    getData() {
      this.pageflag = true
      // this.pageflag =false
      currentGET('big5', { limitNum: 50 }).then(res => {
        console.log('实时预警', res);
        if (res.success) {
          this.list = res.data.list
          let timer = setTimeout(() => {
              clearTimeout(timer)
              this.defaultOption.step=this.$store.state.setting.defaultOption.step
          }, this.$store.state.setting.defaultOption.waitTime);
        } else {
          this.pageflag = false
          this.$Message.warning(res.msg)
        }
      })
    },
    // 跳转到具体人
    toPeople(item) {
      this.$router.push({ path: `/people/${item.researched_person_id}`, query: { data: JSON.stringify(item) } });
    }
  },
};
</script>
<style lang='scss' scoped>
.right_center {
  width: 100%;
  height: 100%;

  .right_center_item {
    display: flex;
    align-items: center;
    //justify-content: center;
    margin-left: 40px;
    height: auto;
    padding: 10px;
    font-size: 14px;
    color: #fff;
    text-align: left;
    .orderNum {
      margin: 0 20px 0 -20px;
    }


    .inner_right {
      position: relative;
      height: 100%;
      width: 90%;
      flex-shrink: 0;
      line-height: 1.5;

      .dibu {
        position: absolute;
        height: 2px;
        width: 104%;
        background-image: url("../../assets/img/zuo_xuxian.png");
        bottom: -12px;
        left: -2%;
        background-size: cover;
      }
    }

    .info {
      margin-right: 10px;
      display: flex;
      align-items: center;

      .labels {
        flex-shrink: 0;
        font-size: 12px;
        color: rgba(255, 255, 255, 0.6);
      }

      .zhuyao {
        color: $primary-color;
        font-size: 15px;
      }

      .ciyao {
        color: rgba(255, 255, 255, 0.8);
      }

      .warning {
        color: #E6A23C;
        font-size: 15px;
      }
    }

  }
}

.right_center_wrap {
  overflow: hidden;
  width: 100%;
  height: 830px;
}

.overflow-y-auto {
  overflow-y: auto;
}
</style>
