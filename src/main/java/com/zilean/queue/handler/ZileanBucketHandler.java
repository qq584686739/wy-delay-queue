package com.zilean.queue.handler;

import com.zilean.queue.redis.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;

import static com.zilean.queue.constant.ZileanConstant.BUCKET_NAME;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-02 18:36
 */
@Slf4j
public class ZileanBucketHandler implements Runnable {
    @Override
    public void run() {
        // TODO: 2019-07-02 这里加上host hostName port
        log.info("zilean bucket handler running...");

        // TODO: 2019-07-03 还在犹豫是否使用多个bucket
        RBlockingQueue<String> bucket = RedissonUtil.getRedissonClient().getBlockingQueue(BUCKET_NAME);
        RDelayedQueue<String> delayedQueue = RedissonUtil.getRedissonClient().getDelayedQueue(bucket);

        String take = null;
        for (; ; ) {
            try {
                take = bucket.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // TODO: 2019-07-02 业务处理，准备消费，进入消费队列
            log.info("result = {}.", take);
        }
    }
}
