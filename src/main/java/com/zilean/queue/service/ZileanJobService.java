package com.zilean.queue.service;

import com.zilean.queue.domain.entity.ZileanJobDO;
import com.zilean.queue.service.base.ZileanService;

import java.io.Serializable;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-14 01:35
 */
public interface ZileanJobService extends ZileanService<ZileanJobDO> {

    /**
     * 取消延迟任务
     * true  取消成功
     * false 取消失败
     *
     * @param delayedId delayedId
     * @return boolean
     */
    boolean cancel(Serializable delayedId);
}
