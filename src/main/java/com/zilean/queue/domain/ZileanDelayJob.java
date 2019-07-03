package com.zilean.queue.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-01 20:30
 */
@Data
public abstract class ZileanDelayJob implements Serializable {

    /**
     * jobId
     */
    protected String id;

    /**
     * 延迟时间
     */
    protected int delay;

    /**
     * 校验参数
     * true : 合法
     * false: 不合法
     */
    abstract void check();
}