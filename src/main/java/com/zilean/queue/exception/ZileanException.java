package com.zilean.queue.exception;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-01 20:50
 */
public class ZileanException extends RuntimeException {

    private ZileanExceptionEnum bizExceptionEnum;

    ZileanException() {
    }

    public ZileanException(ZileanExceptionEnum bizExceptionEnum) {
        this.bizExceptionEnum = bizExceptionEnum;
    }

    public ZileanExceptionEnum getBizExceptionEnum() {
        return bizExceptionEnum;
    }

    public void setBizExceptionEnum(ZileanExceptionEnum bizExceptionEnum) {
        this.bizExceptionEnum = bizExceptionEnum;
    }

    @Override
    public String toString() {
        return "BizException{" +
            "bizExceptionEnum=" + bizExceptionEnum +
            '}';
    }

}
