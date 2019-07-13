package com.zilean.queue.service.base;

import com.zilean.queue.domain.entity.base.BaseDO;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-02 20:52
 */
public interface ZileanService<DO extends BaseDO> {
    /**
     * 添加任务
     * 返回影响job的个数
     *
     * @param job job
     * @return int
     */
    DO insert(DO job);

    /**
     * 删除任务
     * 返回影响job的个数
     *
     * @param id delayedId
     * @return int
     */
    void deleteById(Serializable id);

    /**
     * 批量删除任务
     * 返回影响job的个数
     *
     * @param idList idList
     * @return int
     */
    void deleteByIdList(List<Serializable> idList);

    /**
     * 更新任务
     * 返回影响job的个数
     *
     * @param job job
     * @return int
     */
    DO updateById(DO job);

    /**
     * 追加任务
     * 返回影响job的个数
     *
     * @param job job
     * @return int
     */
    DO appendById(DO job);

    /**
     * 查找任务
     *
     * @param id delayedId
     * @return DO
     */
    Optional<DO> selectById(Serializable id);


}
