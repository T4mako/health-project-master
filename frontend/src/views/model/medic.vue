<template>
  <div class="model-query">
    <!-- 对话内容展示区域 -->
    <div class="dialogue-container">
      <div class="titletext">健康服务AI 模型问答</div>
      <div v-for="(message, index) in dialogue" :key="index" class="message" :class="message.sender">
        <img :src="message.avatar" alt="avatar" class="avatar" />
        <div class="content">
          <p v-if="!message.loading">{{ message.text }}</p>
          <p v-else class="loading">稍等，AI 正在思考...</p>
        </div>
      </div>
    </div>

    <!-- 输入框区域 -->
    <div class="input-container">
      <textarea
          v-model="prompt"
          placeholder="请输入问题..."
          rows="3">
      </textarea>
      <button @click="sendQuery" :disabled="loading">提交问题</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'ModelQuery',
  data() {
    return {
      prompt: '',         // 当前输入的问题
      dialogue: [         // 初始对话
        {
          sender: 'ai',
          text: '欢迎使用城市健康服务大模型，此模型为测试版本，你可以问我一些关于身体健康相关的问题。',
          avatar: 'https://via.placeholder.com/40?text=AI',
          loading: false
        }
      ],
      loading: false      // 加载状态
    };
  },
  methods: {
    async sendQuery() {
      if (!this.prompt.trim()) {
        alert("请输入问题！");
        return;
      }

      this.loading = true; // 开始加载状态

      // 用户消息
      this.dialogue.push({
        sender: 'user',
        text: this.prompt,
        avatar: 'https://via.placeholder.com/40?text=U',
        loading: false
      });

      // AI 消息占位
      const aiMessage = {
        sender: 'ai',
        text: '',
        avatar: 'https://via.placeholder.com/40?text=AI',
        loading: true
      };
      this.dialogue.push(aiMessage);

      try {
        const result = await axios.post('http://127.0.0.1:5000/predict', {
          prompt: this.prompt
        });

        // 替换占位消息为真实内容
        aiMessage.text = result.response || '未能获取模型回答。';
        aiMessage.loading = false;
      } catch (error) {
        console.error('请求失败:', error.message);
        aiMessage.text = '请求失败，请稍后再试。';
        aiMessage.loading = false;
      } finally {
        this.prompt = ''; // 清空输入框
        this.loading = false; // 结束加载状态
      }
    }
  }
};
</script>

<style scoped>
.titletext {
  font-size: 24px;
  font-weight: 900;
  letter-spacing: 4px;
  background: linear-gradient(92deg, #0072ff 0%, #00eaff 48.85%, #01aaff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin-bottom: 16px;
  text-align: center;
}
/* 整体容器 */
.model-query {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: transparent;
  color: white;
  overflow: hidden;
}

/* 对话内容展示区域 */
.dialogue-container {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
  border: 1px solid #00eded;
  border-radius: 8px;
  margin: 10px 20px;
}

h1 {
  text-align: center;
  margin-bottom: 20px;
  color: #007bff;
}

/* 消息样式 */
.message {
  display: flex;
  align-items: flex-start;
  margin-bottom: 10px;
}

.message.user {
  flex-direction: row; /* 用户消息靠左 */
}

.message.ai {
  flex-direction: row-reverse; /* AI 消息靠右 */
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin: 0 10px;
}

.content {
  max-width: 70%;
  background-color: #e9e9e9;
  color: #333;
  border-radius: 10px;
  padding: 10px 15px;
  word-wrap: break-word;
  word-break: break-word;
}
.message .content {
  max-width: 70%;            /* 限制消息气泡的宽度 */
  color: #333;               /* 文字颜色 */
  border-radius: 10px;       /* 圆角边框 */
  padding: 10px 15px;        /* 内边距 */
  word-wrap: break-word;     /* 自动换行 */
  word-break: break-word;    /* 单词换行 */
  font-size: 16px;           /* 统一字体大小 */
  line-height: 1.6;          /* 设置行高，改善可读性 */
  box-sizing: border-box;    /* 包括内边距在宽高计算中 */
}


.message.user .content {
  background-color: #007bff;
  color: white;
}
.message.ai .content {
  background-color: #bfbfbf; /* AI 消息背景颜色 */
  color: #333;               /* AI 消息字体颜色 */
}
.message.ai .content.loading {
  font-style: italic;
  color: gray;
}
/* 输入框固定到底部 */
.input-container {
  display: flex;
  padding: 20px;
  position: sticky;
  bottom: 0;

}
textarea {
  flex: 1;
  padding: 10px;
  font-size: 16px;
  resize: none;
  background-color: #bfbfbf;
  color: #333;
  border: 1px solid #00eded;
  border-radius: 5px;
  margin-right:20px;
}

textarea::placeholder {
  color: #aaa;
}

button {
  padding: 10px 20px;
  font-size: 16px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #0056b3;
}

button:disabled {
  background-color: #555;
  cursor: not-allowed;
}
</style>
