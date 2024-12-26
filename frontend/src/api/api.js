
/*
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-28 10:25:38
 */
import axios from 'axios';
import UtilVar from "@/config/UtilVar";
import router from '@/router';

let baseUrl = UtilVar.baseUrl
const CancelToken = axios.CancelToken;
export { baseUrl };
// axios.defaults.withCredentials = true;
// 添加请求拦截器
axios.interceptors.request.use(
    (config) => {
        let token = localStorage.getItem("token");

        // 判断是否是 /login 接口
        if (!config.url.includes("/login")) {
            // 如果不是 /login，则设置 Authorization 头
            if (token) {
                config.headers['Authorization'] = `Bearer ${token}`; // 使用标准的 Bearer 格式
            }
        }

        // 设置通用的 Content-Type
        config.headers['Content-Type'] = "application/json;charset=utf-8";

        return config;
    },
    (error) => {
        console.error("请求拦截器错误：", error);
        return Promise.reject(error);
    }
);

/**
 * @响应拦截
 */
axios.interceptors.response.use(response => {

    if (response.status !== 200) {
        return Promise.reject(response)
    }
    // 判断是否是登录接口
    if (response.config.url.includes("/login")) {
        // 登录接口不需要通用的登录过期处理逻辑
        return response.data;
    }
      /**
     * @code 登录过期 token验证失败 根据后端调
     */
    
    if (response.data.code == UtilVar.code) {
        router.push("/login")
    }
    return response.data
}, error => {
    // console.log('axiosError',error);
    let err = {
        success: false,
        msg: "未知异常，请联系管理员！"
    }
    return Promise.reject(err)
})


