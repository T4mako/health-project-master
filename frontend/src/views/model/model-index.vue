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
            <div style="padding: 10px;">
              <el-table ref="singleTable" :data="tableData" highlight-current-row @current-change="handleCurrentChange"
                style="width: 100%" size="medium" :lazy="true"
                :header-cell-style="{ background: 'rgb(0,0,0,0)', color: '#fff' }" height="875">
                <el-table-column type="index" width="60">
                </el-table-column>
                <el-table-column property="id" label="用户id" width="120">
                </el-table-column>
                <el-table-column property="gender" label="性别" width="120">
                </el-table-column>
                <el-table-column property="age" label="年龄" width="120">
                </el-table-column>
                <el-table-column property="family_user_id" label="家庭id">
                </el-table-column>
                <el-table-column property="city" label="城市">
                </el-table-column>
                <el-table-column property="dept_name" label="社区名">
                </el-table-column>
              </el-table>
            </div>
          </ItemWrap>
          <ItemWrap class="wrap" style="text-align: center;" title="模型选择">
            <div style="margin-left: 160px;">
              <div @click="goTo('/model?id=' + userid)">
                <ModleBtn name="健康数据分析图"></ModleBtn>
              </div>
              <div @click="goTo('/disease?id=' + userid)">
                <ModleBtn name="疾病预测"></ModleBtn>
              </div>
              <div @click="goTo('/video?id=' + userid)">
                <ModleBtn name="摔倒监测报警"></ModleBtn>
                <div @click="goTo('/medic')">
                  <ModleBtn name="健康数字人"></ModleBtn>
                </div>
              </div>
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
import ModleBtn from "./model-btn.vue";
import axios from "axios";
import { baseUrl } from "@/api/api";

export default {
  components: { ScaleScreen, ItemWrap, ModleBtn },
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
      tableData: [
        { id: 98, gender: '女', age: '59', family_user_id: '211', dept_name: '永安社区', city: '徐州' },
        { id: 99, gender: '女', age: '59', family_user_id: '211', dept_name: '永安社区', city: '徐州' },
        { id: 100, gender: '女', age: '59', family_user_id: '211', dept_name: '永安社区', city: '徐州' },
        { id: 101, gender: '女', age: '59', family_user_id: '211', dept_name: '永安社区', city: '徐州' }
      ],
      currentRow: null,
      userid: null,
    };
  },
  created() {
    this.path = this.$route.path;
    axios.get(`${baseUrl}/user/allUserBaseInfo`).then(response => {

      if (response.code === "200") {
        const data = response.data;
        this.tableData = data

      } else {
        this.pageflag = false;
        this.$Message({
          text: response.data.msg,
          type: 'warning'
        });
      }
    })
      .catch(error => {
        console.error(error);
        this.pageflag = false;
        this.$Message({
          text: '获取数据失败',
          type: 'error'
        });
      });
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
      if (this.userid) {
        this.$router.push(path);
      } else {
        this.$Message.warning("请选择用户")
      }
    },
    handleCurrentChange(row) {
      this.userid = row.id; // Update userid with selected user's id

    }
  }
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

<style lang="scss">
/* Most outer transparent layer */
.el-table,
.el-table__expanded-cell {
  color: #ffffff;
  background-color: transparent;
}

.el-table th.gutter {
  display: none;
  width: 0;
}

.el-table colgroup col[name='gutter'] {
  display: none;
  width: 0;
}

/* Background color for table cells */
.el-table th,
.el-table tr,
.el-table td {
  background-color: transparent;
}

/* Font color for table headers */
.el-table thead {
  color: #ffffff;
  font-weight: 500;
}

.el-table--enable-row-hover .el-table__body tr:hover>td {
  background-color: rgba(174, 209, 249, 0.25) !important;
}

.el-table__body tr.current-row>td {
  background-color: rgba(48, 141, 247, 0.75) !important;
}

/* 删除表格下最底层的横线 */
.el-table::before {
  left: 0;
  bottom: 0;
  width: 100%;
  height: 0px;
}

.el-table {
  border-bottom: 0;

  ::-webkit-scrollbar {
    width: 8px;
    height: 10px;
  }

  ::-webkit-scrollbar-track {
    background-color: transparent;
    -webkit-border-radius: 2em;
    -moz-border-radius: 2em;
    border-radius: 2em;
  }

  ::-webkit-scrollbar-thumb {
    background-color: rgb(1, 179, 255, 1);
    -webkit-border-radius: 2em;
    -moz-border-radius: 2em;
    border-radius: 2em;
  }
}
</style>
