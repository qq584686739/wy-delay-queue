package com.zilean.queue.enums;

import java.io.Serializable;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-11 13:04
 */
public enum DelayEnum implements Serializable {
    /**
     * 延迟
     */
    DELAYED,
    /**
     * 读取
     */
    READY,
    /**
     * 成功
     */
    SUCCESS,
    /**
     * 失败
     */
    FAILED
}
