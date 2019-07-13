package com.zilean.queue.domain;

import com.zilean.queue.constant.ZileanConstant;
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
     * 任务id
     */
    protected String delayedId;

    /**
     * 延迟时间
     */
    protected int delay;

    /**
     * token
     */
    protected String token;

    /**
     * 任务名称，如果不传，则赋予默认值
     */
    protected String name = ZileanConstant.DEFAULT_DELAY_NAME;

    /**
     * 校验参数
     * true : 合法
     * false: 不合法
     *
     * @param opt opt
     */
    abstract void check(int opt);
}
