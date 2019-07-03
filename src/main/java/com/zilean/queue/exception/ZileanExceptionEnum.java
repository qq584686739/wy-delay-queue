package com.zilean.queue.exception;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-01 20:50
 */
public enum ZileanExceptionEnum {

    // 参数异常!
    ERROR_ADD_JOB_FOR_PARAM_ID(10010001, "id not valid,can not be empty!"),
    ERROR_ADD_JOB_FOR_PARAM_DELAY(10010002, "delay not valid,can not <0 or >15 days!"),
    ERROR_ADD_JOB_FOR_PARAM_CALLBACK(10010003, "callback not valid, it should be a http url!"),
    ERROR_ADD_JOB_FOR_PARAM_BODY(10010004, "body not valid, can not empty or length beyond 2000!"),
    ERROR_ADD_JOB_FOR_PARAM_ISAPPEND(10010005, "isAppend not valid, must be 0 or 1!"),
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
