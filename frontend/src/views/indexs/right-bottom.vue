<template>
  <div v-if="pageflag" class="right_center_wrap beautify-scroll-def" :class="{ 'overflow-y-auto': !sbtxSwiperFlag }" style="padding-top: 20px;">
    <component :is="components" :data="list" :class-option="defaultOption">
      <ul class="right_center">
        <li class="right_center_item" v-for="(item, i) in list" :key="i" v-on:click="toPeople(item.id)">
          <span class="orderNum">{{ i + 1 }}</span>
          <div class="inner_right">
            <div class="dibu"></div>
            <div class="flex">
              <div class="info">
                <span class="labels ">ID：</span>
                <span class="contents zhuyao">{{ item.id }}</span>
              </div>
              <div class="info">
                <span class="labels">性别：</span>
                <span class="contents">{{ item.gender }}</span>
              </div>
              <div class="info">
                <span class="labels">年龄：</span>
                <span class="contents warning">{{ item.age }}</span>
              </div>
            </div>
            <div class="flex">
              <div class="info">
                <span class="labels">住址：</span>
                <span class="contents ciyao" style="font-size:12px">{{ item.address }}</span>
              </div>
            </div>
            <div class="flex">
              <div class="info">
                <span class="labels">身体状况：</span>
                <span class="contents ciyao" :class="{ warning: item.alertLevel }">{{ item.alertLevel || '无' }}</span>
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
import axios from "axios";
import vueSeamlessScroll from 'vue-seamless-scroll';
import Kong from '../../components/kong.vue';
import { baseUrl } from "@/api/api";

export default {
  components: { vueSeamlessScroll, Kong },
  props: {
    communityName: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      list: [],
      pageflag: true,
      defaultOption: {
        ...this.$store.state.setting.defaultOption,
        limitMoveNum: 3,
        singleHeight: 250,
        step: 0,
      },
    };
  },
  computed: {
    sbtxSwiperFlag() {
      let ssyjSwiper = this.$store.state.setting.ssyjSwiper;
      this.components = ssyjSwiper ? vueSeamlessScroll : Kong;
      return ssyjSwiper;
    }
  },
  created() {
    this.getData();
  },
  methods: {
    getData() {
      axios.get(`${baseUrl}/city/getHealthDataByCommunityAll`, {
            params: { communityName: this.communityName }
          })
          .then((res) => {
            if (res.code === "200") {
              // 格式化数据
              this.list = res.data.map(item => ({
                id: item.id,
                gender: item.gender,
                age: item.age,
                address: item.address,
                // 将 L1, L2, L3 拼接成身体状况显示
                alertLevel: [
                  item.L1.length ? `${item.L1.join(', ')} 处于一级预警状态` : '',
                  item.L2.length ? `${item.L2.join(', ')} 处于二级预警状态` : '',
                  item.L3.length ? `${item.L3.join(', ')} 处于三级预警状态` : ''
                ].filter(Boolean).join('；') || '无'
              }));

              let timer = setTimeout(() => {
                clearTimeout(timer);
                this.defaultOption.step = this.$store.state.setting.defaultOption.step;
              }, this.$store.state.setting.defaultOption.waitTime);
            } else {
              this.pageflag = false;
              this.$Message.warning(res.msg);
            }
          })
          .catch((error) => {
            console.error("API Error:", error);
            this.pageflag = false;
          });
    },
    toPeople(peopleId) {
      this.$router.push({ path: '/people/' + peopleId });
    }
  }
};
</script>

<style lang='scss' scoped>
.right_center {
  width: 100%;
  height: 100%;

  .right_center_item {
    display: flex;
    align-items: center;
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
