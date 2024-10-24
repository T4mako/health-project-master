package com.keylab.healthproject.common;

import lombok.Data;
import java.io.Serializable;

/**
 * @author T4mako
 * @date 2024/10/24 13:24
 * @desc 通用返回结果类
 */
@Data
public class R<T> implements Serializable {

    private Integer code; //响应码

    private String msg; //错误信息

    private T data; //数据

    public static <T> R<T> error(Integer code,String msg) {
        R<T> r = new R<T>();
        r.msg = msg;
        r.code = code;
        return r;
    }

    public static <T> R<T> success(T object,String msg) {
        R<T> r = new R<T>();
        r.data = object;
        r.msg = msg;
        r.code = 200;
        return r;
    }
}

