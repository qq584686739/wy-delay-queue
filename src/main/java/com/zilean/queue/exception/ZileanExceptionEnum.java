package com.zilean.queue.exception;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-01 20:50
 */
public enum ZileanExceptionEnum {

    // 参数异常!
    ERROR_ADD_JOB_FOR_PARAM(10010001, "参数异常!"),
    // 添加队列失败!
    ERROR_ADD_JOB_FOR_REDIS(10010002, "添加队列失败!"),

    //系统异常
    ERROR_SYSTEM_BUSY(99999999, "BUSY SYSTEM, PLEASE TRY AGAIN LATER!");

    private Integer code;
    private String msg;

    ZileanExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }}
