package com.zilean.queue.handler;

import com.zilean.queue.redis.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RFuture;

import java.util.concurrent.TimeUnit;

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
        // TODO: 2019-07-02 这里加上host port
        log.info("zilean bucket handler running...");

        RBlockingQueue<String> queue = RedissonUtil.getRedissonClient().getBlockingQueue(BUCKET_NAME);

        RFuture<String> take;
        for (; ; ) {
            take = queue.takeAsync();
            if (!take.isSuccess()) {
                try {
                    take.await(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            }
            String now = take.getNow();

            // TODO: 2019-07-02 业务处理
            log.info("take = {}.", now);
        }
    }
}
