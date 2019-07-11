package com.zilean.queue.task;

import com.zilean.queue.redis.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import static com.zilean.queue.constant.RedisConstant.VISIT_KEY;
import static com.zilean.queue.constant.RedisConstant.VISIT_TOTAL_KEY;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-11 16:40
 */
@Slf4j
@Configuration
public class ZileanTask {
    @Scheduled(cron = "0 55 23 * * ?")
    public void clearCache() {
        log.info("ZileanTask.clearCache...");
        RedissonClient redissonClient = RedissonUtil.getRedissonClient();
        redissonClient.getAtomicLong(VISIT_KEY).setAsync(0L);
        long total = redissonClient.getAtomicLong(VISIT_TOTAL_KEY).get();

        // TODO: 2019-07-11 总访问量归档入库
    }
}
