<template>
  <div id="one">
    <ul class="user_Overview flex" v-if="pageflag">
      <li class="user_Overview-item" style="color: #00fdfa">
        <div class="user_Overview_nums allnum ">
          <dv-digital-flop :config="config" style="width:100%;height:100%;" />
        </div>
        <p>小区人数</p>
      </li>
      <li class="user_Overview-item" style="color: #07f7a8">
        <div class="user_Overview_nums online">
          <dv-digital-flop :config="onlineconfig" style="width:100%;height:100%;" />
        </div>
        <p>总户数</p>
      </li>

    </ul>
    <Reacquire v-else @onclick="getData" line-height="200px">
      重新获取
    </Reacquire>
  </div>
</template>

<script>

import axios from "axios";
import { baseUrl } from "@/api/api";
let style = {
  fontSize: 24
}
export default {
  name: "One",
  props: {
    communityName: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      options: {},
      pageflag: true,
      timer: null,
      config: {
        number: [],
        content: '{nt}',
        style: {
          ...style,
          // stroke: "#00fdfa",
          fill: "#00fdfa",
        },
      },
      onlineconfig: {
        number: [0],
        content: '{nt}',
        style: {
          ...style,
          // stroke: "#07f7a8",
          fill: "#07f7a8",
        },
      },
    };
  },
  filters: {
    numsFilter(msg) {
      return msg || 0;
    },
  },
  created() {
    this.getData()
  },
  mounted() {
  },
  beforeDestroy() {
    this.clearData()

  },
  methods: {
    clearData() {
      if (this.timer) {
        clearInterval(this.timer)
        this.timer = null
      }
    },
    getData() {
        axios.get(`${baseUrl}/city/getHealthDataByCommunity`, {
          params: { communityName: this.communityName }
        }).then(res => {
          if (res.code === "200") {
            this.config = { ...this.config, number: [Number(res.data.family_num)] };
            this.onlineconfig = { ...this.onlineconfig, number: [Number(res.data.total)] };
          } else {
            this.pageflag = false;
            this.$Message.warning(res.msg);
          }
        })
    },

  },
}
</script>

<style lang="scss" scoped>
.user_Overview {
  position: relative;
  li {
    flex: 1;

    p {
      text-align: center;
      height: 16px;
      font-size: 16px;
    }

    .user_Overview_nums {
      width: 100px;
      height: 100px;
      text-align: center;
      line-height: 100px;
      font-size: 22px;
      margin: 50px auto 30px;
      background-size: cover;
      background-position: center center;
      position: relative;

      &::before {
        content: '';
        position: absolute;
        width: 100%;
        height: 100%;
        top: 0;
        left: 0;
      }

      &.bgdonghua::before {
        animation: rotating 14s linear infinite;
      }
    }

    .allnum {

      // background-image: url("../../assets/img/left_top_lan.png");
      &::before {
        background-image: url("../../assets/img/left_top_lan.png");

      }
    }

    .online {
      &::before {
        background-image: url("../../assets/img/left_top_lv.png");

      }
    }

    .offline {
      &::before {
        background-image: url("../../assets/img/left_top_huang.png");

      }
    }

    .laramnum {
      &::before {
        background-image: url("../../assets/img/left_top_hong.png");

      }
    }
  }
}
.user_Overview li {
  margin-top: 50px;
}
</style>
