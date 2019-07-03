package com.zilean.queue.service;

import com.zilean.queue.domain.ZileanDelayJob;
import com.zilean.queue.redis.RedissonUtil;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;

import java.util.List;

import static com.zilean.queue.constant.ZileanConstant.BUCKET_NAME;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-02 21:02
 */
public abstract class AbstractZileanService implements ZileanService {
    protected final RedissonClient redissonClient = RedissonUtil.getRedissonClient();
    protected final RBlockingQueue<String> bucketBlockingQueue = redissonClient.getBlockingQueue(BUCKET_NAME);
    protected final RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(bucketBlockingQueue);

    @Override
    public int insert(ZileanDelayJob job) {
        return 0;
    }

    @Override
    public int deleteById(String id) {
        return 0;
    }

    @Override
    public int deleteById(List<String> idList) {
        return 0;
    }

    @Override
    public int updateById(ZileanDelayJob job) {
        return 0;
    }

    @Override
    public ZileanDelayJob selectById(String id) {
        return null;
    }
}
