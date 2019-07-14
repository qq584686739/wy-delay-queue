package com.zilean.queue.domain.response;

import com.zilean.queue.exception.ZileanExceptionEnum;

import java.util.Objects;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-01 21:01
 */
public class ZileanResponse<T> {
    protected int code;
    protected String msg;
    protected T data;

    public ZileanResponse() {
        this(0, "", null);
    }

    public ZileanResponse(int code) {
        this(code, "", null);
    }

    public ZileanResponse(T data) {
        this(0, "", data);
    }

    public ZileanResponse(int code, String msg) {
        this(code, msg, null);
    }

    public ZileanResponse(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ZileanResponse(ZileanExceptionEnum zileanExceptionEnum) {
        this(zileanExceptionEnum.getCode(), zileanExceptionEnum.getMsg(), null);
    }

    public static ZileanResponse success() {
        return new ZileanResponse<>();
    }

    public static <T> ZileanResponse success(T data) {
        return new ZileanResponse<>(0, "", data);
    }

    public static ZileanResponse error(int code, String msg) {
        return new ZileanResponse<>(code, msg);
    }

    public static ZileanResponse error() {
        return new ZileanResponse<>(ZileanExceptionEnum.ERROR_SYSTEM_BUSY);
    }

    public static ZileanResponse error(ZileanExceptionEnum zileanExceptionEnum) {
        return new ZileanResponse<>(zileanExceptionEnum);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ZileanResponse)) {
            return false;
        }
        ZileanResponse<?> that = (ZileanResponse<?>) o;
        return code == that.code &&
            Objects.equals(msg, that.msg) &&
            Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, msg, data);
    }

    @Override
    public String toString() {
        return "ZileanResponse{" +
            "code=" + code +
            ", msg='" + msg + '\'' +
            ", data=" + data +
            '}';
    }
}
