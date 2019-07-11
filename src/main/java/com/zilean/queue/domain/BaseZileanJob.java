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
public abstract class BaseZileanJob implements Serializable {

    private static final long serialVersionUID = 6266181599908960090L;
    /**
     * jobId
     */
    protected String id;

    /**
     * 延迟时间
     */
    protected int delay;

    /**
     * token
     */
    protected String token;

    /**
     * 校验参数
     * true : 合法
     * false: 不合法
     *
     * @param opt opt
     */
    abstract void check(int opt);
}
