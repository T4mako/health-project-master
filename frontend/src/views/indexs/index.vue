<template>
  <div class="contents">
    <div class="contetn_left">
      <div class="pagetab">
      </div>

      <ItemWrap
          class="contetn_left-center contetn_lr-item"
          title="市人数(单位人)"
          style="height: 460px"
      >
        <LeftCenter :province="province"/>
      </ItemWrap>
      <ItemWrap
        class="contetn_left-bottom contetn_lr-item"
        title="男女比例(单位人)"
        style="padding: 0 10px 16px 10px; height: 500px;"
      >
        <LeftTop :province="province"/>
      </ItemWrap>
    </div>
    <div class="contetn_center">
<!--      地图-->
      <CenterMap class="contetn_center_top" @childNeedChangeData="childNeedChangeData($event)" :isEnvironment="isEnvironment"/>
<!--      健康状况-->
      <ItemWrap class="contetn_center-bottom" title="健康状况(单位人)" :province="province">
        <CenterBottom :province="province"/>
      </ItemWrap>
    </div>

<!--    年龄分布-->
    <div class="contetn_right">
      <ItemWrap
        class="contetn_left-bottom contetn_lr-item"
        title="年龄分布(单位人)"
        style="padding: 0 10px 16px 10px; height: 500px;"
      >
        <RightCenter :province="province"/>
      </ItemWrap>
      <ItemWrap
          class="contetn_left-bottom contetn_lr-item"
          title="健康状况"
          style="height: 460px"
      >
        <RightTop :province="province"/>
      </ItemWrap>
    </div>
  </div>
</template>

<script>
import LeftTop from './left-top.vue'
import LeftCenter from "./left-center.vue";
import LeftBottom from "./left-bottom.vue";
import CenterMap from "./center-map.vue";
import CenterBottom from "./center-bottom.vue";
import RightTop from "./right-top.vue";
import RightCenter from "./right-center.vue";
import RightBottom from "./right-bottom.vue";

export default {
  components: {
    LeftTop,
    LeftCenter,
    LeftBottom,
    CenterMap,
    RightTop,
    RightCenter,
    RightBottom,
    CenterBottom,
  },
  data() {
    return {
      province:'',
      isEnvironment:true,
    };
  },
  filters: {
    numsFilter(msg) {
      return msg || 0;
    },
  },
  created() {
  },

  mounted() {},
  methods: {
    childNeedChangeData(name) {
      this.province = name;
      console.log(this.province);
    }
  },
};
</script>
<style lang="scss" scoped>
// 内容
.contents {
  .contetn_left,
  .contetn_right {
    width: 540px;
    box-sizing: border-box;
    // padding: 16px 0;
  }

  .contetn_center {
    width: 720px;
  }

  //左右两侧 三个块
  .contetn_lr-item {
    height: 310px;
  }

  .contetn_center_top {
    width: 100%;
  }

  // 中间
  .contetn_center {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
  }

  .contetn_center-bottom {
    height: 315px;
  }


  //左边 右边 结构一样
  .contetn_left,
  .contetn_right {
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    position: relative;
  }
}



@keyframes rotating {
    0% {
        -webkit-transform: rotate(0) scale(1);
        transform: rotate(0) scale(1);
    }
    50% {
        -webkit-transform: rotate(180deg) scale(1.1);
        transform: rotate(180deg) scale(1.1);
    }
    100% {
        -webkit-transform: rotate(360deg) scale(1);
        transform: rotate(360deg) scale(1);
    }
}
</style>
