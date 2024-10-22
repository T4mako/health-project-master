
/*
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2022-04-28 10:25:38
 */
import axios from 'axios';
import UtilVar from "@/config/UtilVar";
import router from '@/router'

let baseUrl = UtilVar.baseUrl
const CancelToken = axios.CancelToken;
export { baseUrl };
// axios.defaults.withCredentials = true;
// 添加请求拦截器
axios.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么 传token
    let token = localStorage.getItem("token");
    config.headers.common['Content-Type'] = "application/json;charset=utf-8";
    config.headers.common['token'] = token;  //Authorization
    return config;
}, function (error) {
    // 对请求错误做些什么
    console.log(error)
    return Promise.reject(error);
});
/**
 * @响应拦截
 */
axios.interceptors.response.use(response => {

    if (response.status !== 200) {
        return Promise.reject(response)
    }
    /**
   * @code 登录过期 token验证失败 根据后端调 
   */
    if (response.data.code == UtilVar.code) {
        // router.push("/login")
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

let configs_ENC = {
    headers: { 'enc': UtilVar.ENC }
}
//处理是否加密数据
let isEncryptionParam = (params) => {
    return params

}
export const GET = async (url, params) => {
    try {
        params = isEncryptionParam(params)
        const data = await axios.get(`${baseUrl}${url}`, {
            params: params,
            headers: configs_ENC.headers
        }, configs_ENC);
        return data;
    } catch (error) {
        return error;
    }
}
//没有基地址 访问根目录下文件

export const GETNOBASE = async (url, params) => {
    try {
        const data = await axios.get(url, {
            params: params,
        });
        return data;
    } catch (error) {
        return error;
    }
}
export const POST = async (url, params) => {
    try {
        params = isEncryptionParam(params)
        const data = await axios.post(`${baseUrl}${url}`, params, configs_ENC);
        return data;
    } catch (error) {
        return error;
    }
}
export const PUT = async (url, params) => {
    try {
        params = isEncryptionParam(params)
        const data = await axios.put(`${baseUrl}${url}`, params, configs_ENC);
        return data;
    } catch (error) {
        return error;
    }
}
export const DELETE = async (url, params) => {
    // console.log(params)
    try {
        params = isEncryptionParam(params)
        const data = await axios.delete(`${baseUrl}${url}`, { data: params, headers: configs_ENC.headers }, configs_ENC);
        return data;
    } catch (error) {
        return error;
    }
}


/**
 * @文件类型提交方法
 */
let configs = {
    headers: { 'Content-Type': 'multipart/form-data' },

}
export const FILESubmit = async (url, params, config) => {
    try {
        const data = await axios.post(`${baseUrl}${url}`, params, {
            ...configs,
            cancelToken: new CancelToken(function executor(c) {
                config.setCancel && config.setCancel(c)
            }),
            onUploadProgress: (e) => {
                if (e.total > 0) {
                    e.percent = e.loaded / e.total * 100;
                }
                // console.log(config)
                config.onProgress && config.onProgress(e)
            },

        });
        return data;
    } catch (err) {
        return err;
    }
}

/**
 * 下载文档流
 * @param {config.responseType} 下载文件流根据后端 配置   arraybuffer || blod
 */
export const FILE = async (config = {}, body, params) => {
    try {
        const data = await axios({
            method: config.method || 'get',
            url: `${baseUrl}${config.url}`,
            data: body,
            params: params,
            responseType: config.responseType || 'blob',
            onDownloadProgress: (e) => {
                // console.log(e,e.currentTarget)
                // if (e.currentTarget.response.size > 0) {
                //     e.percent = e.loaded / e.currentTarget.response.size * 100;
                // }
                // event.srcElement.getResponseHeader('content-length')
                config.onProgress && config.onProgress(e)
            },
        });
        return data;
    } catch (err) {
        return err;
    }
}



const apiUrl = 'http://www.pahealthsys.cn/device/deviceData/getDataByType';

const requestData = {
    current: 1, // 第几页
    size: 10, // 每页多少条
    startDate: '', // 开始日期，可以根据需要填写
    endDate: '', // 结束日期，可以根据需要填写
    type: 'temperature' // 数据类型
};

const requestOptions = {
    method: 'POST',
    headers: {
        'Authorization': 'Bearer 83e60402-b255-4f7d-87ac-139c6564f250',
        'Content-Type': 'application/json'
    },
    body: JSON.stringify(requestData)
};

fetch(apiUrl, requestOptions)
    .then(response => response.json())
    .then(data => {
        if (data.code === 0) {
            const healthData = data.data.records;
            console.log(healthData); // 在这里处理健康数据
        } else {
            console.error('请求失败:', data.msg);
        }
    })
    .catch(error => {
        console.error('请求失败:', error);
    });
