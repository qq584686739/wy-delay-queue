package com.zilean.queue.service.impl;

import com.zilean.queue.dao.ZileanJobRepository;
import com.zilean.queue.domain.entity.ZileanJobDO;
import com.zilean.queue.exception.ZileanException;
import com.zilean.queue.service.ZileanJobService;
import com.zilean.queue.service.base.AbstractZileanService;
import com.zilean.queue.util.ZileanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.zilean.queue.constant.RedisConstant.TODAY_DELAYED_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_DELAYED_TOTAL_KEY;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_FOR_NOT_FOUNT_JOB;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_UPDATE_JOB;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-02 20:51
 */
@Service
public class ZileanJobServiceImpl extends AbstractZileanService<ZileanJobRepository, ZileanJobDO> implements ZileanJobService {

    @Resource
    private ZileanJobRepository zileanJobRepository;

    @Override
    public ZileanJobRepository jpaRepository() {
        return zileanJobRepository;
    }

    @Override
    public ZileanJobDO insert(ZileanJobDO zileanJobDO) {
        ZileanUtil.defaultValueForZileanJobDO(zileanJobDO);
        ZileanJobDO insert = super.insert(zileanJobDO);
        delayedQueue.offerAsync(zileanJobDO.getDelayedId(), zileanJobDO.getDelay(), TimeUnit.SECONDS);
        redissonClient.getAtomicLong(TODAY_DELAYED_KEY).incrementAndGetAsync();
        redissonClient.getAtomicLong(TODAY_DELAYED_TOTAL_KEY).incrementAndGetAsync();
        return insert;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Serializable id) {
        if (delayedQueue.contains(id)) {
            Optional<ZileanJobDO> zileanJobDOOptional = super.selectById(id);
            if (zileanJobDOOptional.isPresent()) {
                ZileanJobDO zileanJobDO = zileanJobDOOptional.get();
                zileanJobDO.setStatus(100000);
                super.updateById(zileanJobDO);
            } else {
                // TODO: 2019-07-14 找不到
                throw new ZileanException(ERROR_UPDATE_JOB);
            }
            // TODO: 2019-07-04 update db status
            if (!delayedQueue.remove(id)) {
                throw new ZileanException(ERROR_UPDATE_JOB);
            }
        } else {
            // 延迟队列找不到该任务
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIdList(List<Serializable> idList) {
        super.deleteByIdList(idList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ZileanJobDO updateById(ZileanJobDO zileanJobDO) {
        if (delayedQueue.contains(zileanJobDO.getDelayedId())) {

            // TODO: 2019-07-14 update db

            int delay;
            if (null == zileanJobDO.getDelay() || 0 == (delay = zileanJobDO.getDelay())) {
                // 既然没有更新时间，那么就不更新缓存
                return null;
            }

            // TODO: 2019-07-14 计算延迟时间 ，如果时间快到了，则不允许修改
            if (delayedQueue.remove(zileanJobDO.getDelayedId())) {
                delayedQueue.offer(zileanJobDO.getDelayedId(), delay, TimeUnit.SECONDS);
            } else {
                // 移除失败
                throw new ZileanException(ERROR_UPDATE_JOB);
            }
        } else {
            // 延迟队列找不到该任务
            throw new ZileanException(ERROR_FOR_NOT_FOUNT_JOB);
        }

        return null;
    }

    // TODO: 2019-07-14 JPA和事务的问题
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ZileanJobDO appendById(ZileanJobDO zileanJobDO) {

        // TODO: 2019-07-05 校验append的length和现有的length长度，总长度不得超过指定值，超过则报错

        if (delayedQueue.contains(zileanJobDO.getDelayedId())) {
            // TODO: 2019-07-14 update db
        } else {
            // 延迟队列找不到该任务
            throw new ZileanException(ERROR_FOR_NOT_FOUNT_JOB);
        }

        return null;
    }

    @Override
    public Optional<ZileanJobDO> selectById(Serializable id) {
        return super.selectById(id);
    }


    @Override
    public boolean cancel(Serializable delayedId) {
        if (delayedQueue.contains(delayedId)) {
            // TODO: 2019-07-14 remove delayedId

            // TODO: 2019-07-14 update status,updateTime, ver
            return true;
        } else {
            // 延迟队列找不到该任务
            throw new ZileanException(ERROR_FOR_NOT_FOUNT_JOB);
        }
    }
}
