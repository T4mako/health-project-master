<template>
  <div class="bg">
    <el-card class="login-card" shadow="hover">
      <div class="title">数字健康家庭服务城市平台</div>
      <h2 class="login-title">用户登录</h2>
      <el-form :model="loginForm" :rules="rules" ref="loginForm" label-width="70px">
        <!-- 用户名 -->
        <el-form-item label="用户名" prop="username" style="margin-bottom: 25px">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <!-- 密码 -->
        <el-form-item label="密码" prop="password" style="margin-bottom: 25px">
          <el-input
              v-model="loginForm.password"
              placeholder="请输入密码"
              show-password
              type="password"
          ></el-input>
        </el-form-item>
        <!-- 登录按钮 -->
        <el-form-item class="button-container" label-width="0px">
          <el-button type="primary" @click="onLogin" plain style="width: 100px">登录</el-button>
          <el-button @click="onReset" plain style="width: 100px">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import axios from "axios";
import {baseUrl} from "@/api/api";

export default {
  name: "login",
  data(){
    return{
      loginForm: {
        username: "",
        password: ""
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" }
        ]
      }
    }
  },
  methods:{
    async onLogin() {
      this.$refs.loginForm.validate(async (valid) => {
        if (valid) {
          try {
            // 发送登录请求
            const response = await axios.post(`${baseUrl}/login`, {
              username: this.loginForm.username,
              password: this.loginForm.password
            });
            console.log(response)

            // 登录成功
            if (response.code === '200') {
              const token = response.data.token;
              localStorage.setItem("token", token); // 存储 Token 到 localStorage
              // 跳转到首页或其他页面
              this.$router.push("/homeIndex");
            } else {
              this.$message.error(response.message || "登录失败，请检查用户名或密码！");
            }
          } catch (error) {
            this.$message.error("登录请求失败，请稍后再试！");
            console.error("登录错误：", error);
          }
        } else {
          this.$message.error("请填写完整表单！");
        }
      });
    },
    onReset() {
      this.$refs.loginForm.resetFields();
    }
  }
}
</script>



<style scoped lang="scss">
.bg{
  width: 100%;
  height: 100%;
  background-image: url('../assets/img/pageBg.png');
}
.login-card {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 400px;
  padding: 20px;
  background-color: rgba(255, 255, 255, 0.6); /* 更高透明度的白色背景 */
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.login-title {
  text-align: center;
  margin-bottom: 25px;
  font-size: 24px;
  color: #333;
}
.title{
  text-align: center;
  font-size: 24px;
  font-weight: 900;
  letter-spacing: 4px;
  background: linear-gradient(92deg, #0072ff 0%, #00eaff 48.85%, #01aaff 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin-bottom: 20px;
}
.button-container {
  text-align: center;
  .el-form-item__content {
    margin-left: 0 !important;
  }
}

</style>