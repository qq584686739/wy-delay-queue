package com.zilean.queue.service.base;

import com.zilean.queue.domain.entity.base.BaseDO;
import com.zilean.queue.redis.RedissonUtil;
import com.zilean.queue.util.ZileanUtil;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import static com.zilean.queue.constant.RedisConstant.BUCKET_NAME;


/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-02 21:02
 */
public abstract class AbstractZileanService<T extends JpaRepository, DO extends BaseDO> implements ZileanService<DO> {
    protected final RedissonClient redissonClient = RedissonUtil.getRedissonClient();
    protected final RBlockingQueue<String> bucketBlockingQueue = redissonClient.getBlockingQueue(BUCKET_NAME);
    protected final RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(bucketBlockingQueue);

    /**
     * 子类请覆盖该方法传入jpaRepository
     *
     * @return T
     */
    public abstract T jpaRepository();


    @Override
    public DO insert(DO d) {
        ZileanUtil.defaultInsertValueForBaseDO(d);
        return (DO) jpaRepository().save(d);
    }

    @Override
    public void deleteById(Serializable delayedId) {
        jpaRepository().deleteById(delayedId);
    }

    @Override
    public void deleteByIdList(List<Serializable> idList) {
        jpaRepository().deleteAll(idList);
    }

    @Override
    public DO updateById(DO d) {
        ZileanUtil.defaultUpdatetValueForBaseDO(d);
        return (DO) jpaRepository().save(d);
    }

    @Override
    public DO appendById(DO d) {
        return (DO) jpaRepository().save(d);
    }

    @Override
    public Optional<DO> selectById(Serializable delayedId) {
        return jpaRepository().findById(delayedId);
    }

}
