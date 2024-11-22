<template>
  <div class="city-index">
    <div class="left">
      <ItemWrap class="one" title="人数比例(单位人)"><One :communityName="communityName"/></ItemWrap>
      <ItemWrap class="two" title="男女比例(单位人)"><Two :communityName="communityName"/></ItemWrap>
      <ItemWrap class="three" title="年龄分布(单位人)"><Three :communityName="communityName"/></ItemWrap>
      <ItemWrap class="four" title="健康状况(单位人)"><Four :communityName="communityName"/></ItemWrap>
    </div>
    <div class="right">
      <ItemWrap title="已检测人信息">
        <right-bottom :communityName="communityName"/>
      </ItemWrap>
    </div>
  </div>
</template>

<script>

import One from "@/views/city/One";
import Two from "@/views/city/Two";
import Three from "@/views/city/Three";
import Four from "@/views/city/Four";
import Five from "@/views/city/Five";
import RightBottom from "@/views/indexs/right-bottom";
export default {
  name: "cityIndex",
  components: {RightBottom, Five, Four, Three, Two, One},
  data() {
    return {
      communityName: '',
    };
  },
  created() {
    this.communityName = this.$route.params.cityName;
  },
  methods: {

  },
}
</script>

<style lang="scss" scoped>
  .one, .two, .three, .four{
    width: 400px;
    height: 400px;
  }
  .left {
    height: 86%;
    width: 900px;
    display: flex;
    justify-content: space-around;
    flex-wrap: wrap;
    border: 1px solid deepskyblue;
    align-items: center;
  }
  .right {
    height: 88%;
    width: 900px;
  }
  .city-index {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
</style>
