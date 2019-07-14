package com.zilean.queue.exception;

import static com.zilean.queue.constant.ZileanConstant.MAX_BODY_LENGTH;
import static com.zilean.queue.constant.ZileanConstant.MAX_CALLBACK_LENGTH;
import static com.zilean.queue.constant.ZileanConstant.MAX_ID_LENGTH;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-01 20:50
 */
public enum ZileanExceptionEnum {

    // add job error
    ERROR_ADD_JOB_FOR_PARAM_ID(10010001, "delayedId not valid,can not be empty!"),
    ERROR_ADD_JOB_FOR_PARAM_ID_BEYOND_LENGTH(10010002, "delayedId length by yound " + MAX_ID_LENGTH + "!"),
    ERROR_ADD_JOB_FOR_PARAM_DELAY(10010003, "delay not valid,can not <0 or >15 days!"),
    ERROR_ADD_JOB_FOR_PARAM_CALLBACK(10010004, "callback not valid, it should be a http url!"),
    ERROR_ADD_JOB_FOR_PARAM_CALLBACK_BEYOND_LENGTH(10010005, "callback beyond " + MAX_CALLBACK_LENGTH + "!"),
    ERROR_ADD_JOB_FOR_PARAM_BODY(10010006, "body not valid, can not empty or length beyond " + MAX_BODY_LENGTH + "!"),
    ERROR_ADD_JOB_FOR_PUBLISH_JOB_ERROR(10010007, "publish job error!"),

    // append job error
    ERROR_APPEND_JOB(10020001, "append job error!"),


    // update job error
    ERROR_UPDATE_JOB(10030001, "update job error!"),
    ERROR_UPDATE_JOB_FOR_EXPIRE(10030002, "update job error, job expire!"),
    ERROR_UPDATE_JOB_FOR_SOON_EXPIRE(10030003, "update job error, there's not much time left!"),


    // 添加队列失败!
    ERROR_ADD_JOB_FOR_REDIS(10010002, "添加队列失败!"),


    ERROR_FOR_NOT_FOUNT_JOB(20010001, "not found job!"),


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
