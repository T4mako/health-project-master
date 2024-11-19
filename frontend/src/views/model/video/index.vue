<template>
    <div class="video-player-container">
      <video-player class="video-player vjs-custom-skin" ref="videoPlayer" :options="playerOptions" :playsinline="true">
      </video-player>
    </div>
</template>

<script>
export default {
  data() {
    return {
      playerOptions: {
        // 视频设置
        sources: [
          {
            src: 'http://localhost:8979/live/STREAM_NAME/index.m3u8', // 视频文件地址
            type: 'application/x-mpegURL', // 视频类型
          },
        ],
        notSupportedMessage: "此视频暂无法播放，请稍后再试", // 提示信息
        autoplay: true, // 是否自动播放
        controls: true, // 是否显示控制栏
        // poster: 'http://path/to/poster.jpg', // 视频封面
        aspectRatio: '16:9',
        fluid: false, // 关闭自适应
        width: 1600, // 设置宽度
        height: 900, // 设置高度（与宽度保持比例一致）
      },

    };
  },
  mounted() {
    // 通过 ref 获取 video-player 实例
    const videoPlayer = this.$refs.videoPlayer.player;
    if (videoPlayer) {
      videoPlayer.play();
    } else {
      console.error('未能获取到 video-player 实例');
    }
  },
};
</script>

<style scoped>
.video-player {
  width: 1600px !important;
  height: 900px !important;
  margin: 0 auto;
  /* 让视频居中 */
}

.video-player-container {
  max-width: 1600px;
  margin: 0 auto;
  /* 居中 */
}
</style>
