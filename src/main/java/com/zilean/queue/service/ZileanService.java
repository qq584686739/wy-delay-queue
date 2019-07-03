package com.zilean.queue.service;

import com.zilean.queue.domain.ZileanDelayJob;

import java.util.List;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-02 20:52
 */
public interface ZileanService {
    /**
     * 添加任务
     * 返回影响job的个数
     *
     * @param job job
     * @return int
     */
    int insert(ZileanDelayJob job);

    /**
     * 删除任务
     * 返回影响job的个数
     *
     * @param id id
     * @return int
     */
    int deleteById(String id);

    /**
     * 批量删除任务
     * 返回影响job的个数
     *
     * @param idList idList
     * @return int
     */
    int deleteById(List<String> idList);

    /**
     * 更新任务
     * 返回影响job的个数
     *
     * @param job job
     * @return int
     */
    int updateById(ZileanDelayJob job);

    /**
     * 查找任务
     *
     * @param id id
     * @return ZileanDelayJob
     */
    ZileanDelayJob selectById(String id);
}