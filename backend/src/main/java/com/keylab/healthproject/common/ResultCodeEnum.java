package com.keylab.healthproject.common;

/**
 * 作者： gjmyguy
 * 功能: 参数枚举类,在此添加返回信息的枚举
 * 日期: 2024/10/25,16:34
 */

public enum ResultCodeEnum {
    SUCCESS("200", "成功"),

    PARAM_ERROR("400", "参数异常"),
    PARAM_LOST_ERROR("4001", "参数缺失"),
    SYSTEM_ERROR("500", "系统异常"),
    CITYNAME_LOST_ERROR("4002","城市名称缺失")
    ;

    public String code;
    public String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}