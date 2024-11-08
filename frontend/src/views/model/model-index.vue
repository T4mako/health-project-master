<!--
 * @Author: Author T4mako
 * @Date: 2024-11-05 13:23:59
-->
<template>
  <ScaleScreen :width="1920" :height="1080" class="scale-wrap">
    <div class="bg">
      <dv-loading v-if="loading">Loading...</dv-loading>
      <div v-else class="host-body">
        <!-- 头部 s -->
        <div class="d-flex jc-center title_wrap" style="z-index: 999">
          <div class="zuojuxing"></div>
          <div class="youjuxing"></div>
          <div class="guang"></div>
          <div class="d-flex jc-center">
            <div class="title">
              <span class="title-text" v-on:click="() => { this.$router.push({ path: '/' }) }">数字健康家庭服务城市平台</span>
            </div>
          </div>
          <div class="timers">
            {{ dateYear }} {{ dateWeek }} {{ dateDay }}
          </div>
        </div>

        <!-- 主体 -->
        <div class="content">
          <ItemWrap class="wrap" title="老年人信息">
            <UserSelect></UserSelect>
          </ItemWrap>
          <ItemWrap class="wrap" style="text-align: center;" title="模型选择">
            <div style="margin-left: 160px;">
              <div @click="goTo('/model')">
                <ModleBtn name="健康数据分析图" ></ModleBtn>
              </div>
              <ModleBtn name="疾病预测"></ModleBtn>
              <ModleBtn name="摔倒监测报警"></ModleBtn>
              <ModleBtn name="健康数字人"></ModleBtn>
            </div>

          </ItemWrap>
        </div>
        <!-- <router-view></router-view> -->
      </div>
    </div>
  </ScaleScreen>

</template>

<script>
import { formatTime } from "@/utils/index.js";
import ScaleScreen from "@/components/scale-screen/scale-screen.vue";
import ItemWrap from "@/components/item-wrap/item-wrap.vue";
import UserSelect from "./user-select.vue"
import ModleBtn from "./model-btn.vue"
export default {
  components: { ScaleScreen, UserSelect, ModleBtn },
  data() {
    return {
      timing: null,
      loading: true,
      dateDay: null,
      dateYear: null,
      dateWeek: null,
      weekday: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
      input: '',
      path: '',
    };
  },
  filters: {
    numsFilter(msg) {
      return msg || 0;
    },
  },
  computed: {},
  created() {
    this.path = this.$route.path
    console.log(this.$route.path);
  },
  mounted() {
    this.timeFn();
    this.cancelLoading();
  },
  beforeDestroy() {
    clearInterval(this.timing);
  },
  methods: {
    timeFn() {
      this.timing = setInterval(() => {
        this.dateDay = formatTime(new Date(), "HH: mm: ss");
        this.dateYear = formatTime(new Date(), "yyyy-MM-dd");
        this.dateWeek = this.weekday[new Date().getDay()];
      }, 1000);
    },
    cancelLoading() {
      let timer = setTimeout(() => {
        this.loading = false;
        clearTimeout(timer);
      }, 500);
    },
    goTo(path) {
      this.$router.push(path);
    }
  },
};
</script>

<style lang="scss" scoped>
.bg {
  width: 100%;
  height: 100%;
  padding: 16px 16px 10px 16px;
  box-sizing: border-box;
  background-size: cover;
  background-position: center center;
}

.content {
  display: flex;
  width: 100%;
  height: 90%;

  .wrap {
    margin: 15px;
  }

  font-size: 24px;

}

@import "model.scss";
</style>
