<!--
 * @Author: daidai
 * @Date: 2022-02-28 16:16:42
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-07-20 17:57:11
 * @FilePath: \web-pc\src\pages\big-screen\view\indexs\left-center.vue
-->
<template>
    <ul class="user_Overview flex" v-if="pageflag">
        <li class="user_Overview-item" style="color: #00fdfa">
            <div class="user_Overview_nums allnum ">
                <dv-digital-flop :config="manNumber" style="width:100%;height:100%;" />
            </div>
            <p>男性人数</p>
        </li>
        <li class="user_Overview-item" style="color: #07f7a8">
            <div class="user_Overview_nums online">
                <dv-digital-flop :config="womanNumber" style="width:100%;height:100%;" />
            </div>
            <p>女性人数</p>
        </li>
        <li class="user_Overview-item" style="color: #e3b337">
            <div class="user_Overview_nums offline">
                <dv-digital-flop :config="proportion" style="width:100%;height:100%;" />
            </div>
            <p>男女比例</p>
        </li>
    </ul>
    <Reacquire v-else @onclick="getData" line-height="200px">
        重新获取
    </Reacquire>
</template>

<script>

import axios from "axios";
import {baseUrl} from "@/api/api";
let style = {
    fontSize: 24
}
export default {
  props: {
    province: {
      type: String,
      default: '',
    }
  },
    data() {
        return {
            options: {},
            pageflag: true,
            timer: null,
            // 男生数
            manNumber: {
                number: [],
                content: '{nt}',
                style: {
                    ...style,
                    // stroke: "#00fdfa",
                    fill: "#00fdfa",
                },
            },
            womanNumber: {
                number: [],
                content: '{nt}',
                style: {
                    ...style,
                    // stroke: "#07f7a8",
                    fill: "#07f7a8",
                },
            },
            proportion: {
                number: [],
                content: '{nt}',
                style: {
                    ...style,
                    // stroke: "#e3b337",
                    fill: "#e3b337",
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
       this.refreshData()
    },
    mounted() {
    },
    watch: {
      province(newVal) {
        this.getProvinceData(newVal);
      }
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
      refreshData() {
        // 刷新数据，重新获取性别数据
        if (this.province) {
          this.getProvinceData(this.province);
        } else {
          this.getData();
        }
      },
      getData() {
        this.pageflag = true;
        axios.get(`${baseUrl}/city/getSexCount`).then(response => {
          if (response.code === "200") {
            const data = response.data[0];
            // 更新男性人数
            this.manNumber = {
              ...this.manNumber,
              number: [data.男]
            };
            // 更新女性人数
            this.womanNumber = {
              ...this.womanNumber,
              number: [data.女]
            };
            // 更新男女比例
            this.proportion = {
              ...this.proportion,
              number: [data.ratio]
            };
          } else {
            this.pageflag = false;
            this.$Message.warning(response.msg);
          }
        }).catch(error => {
          console.error(error);
          this.pageflag = false;
          this.$Message.error('获取数据失败');
        });
      },
      getProvinceData(provinceName) {
        if (!provinceName) return;
        // 根据省份/城市名称获取对应的男性、女性数量和比例数据
        axios.get(`${baseUrl}/city/getSexCountByCity`, {
              params: {
                cityName: provinceName
              }
            })
            .then(response => {
              if (response.code === "200") {
                const data = response.data[0]; // 直接使用 response.data
                // 更新男性人数
                this.manNumber = {
                  ...this.manNumber,
                  number: [data.男]
                };
                // 更新女性人数
                this.womanNumber = {
                  ...this.womanNumber,
                  number: [data.女]
                };
                // 更新男女比例
                this.proportion = {
                  ...this.proportion,
                  number: [data.ratio]
                };
              } else {
                this.pageflag = false;
                this.$Message.warning(response.msg);
              }
            })
            .catch(error => {
              console.error("获取省份数据失败", error);
              this.pageflag = false;
              this.$Message.error("获取省份数据失败");
            });
      },
    },
};
</script>
<style lang='scss' scoped>
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
  margin-top: 104px;
}
</style>
