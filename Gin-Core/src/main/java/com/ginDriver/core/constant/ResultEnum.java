package com.ginDriver.core.constant;

/**
 * @author DueGin
 */
public enum ResultEnum {

    SUCCESS(200, "请求成功"),
    PARAMETER_ERROR(1001, "请求参数有误!"),
    UNKNOWN_ERROR(9999, "未知的错误!"),
    ERROR(500, "服务器错误！");

    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
