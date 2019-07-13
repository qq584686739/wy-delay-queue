package com.zilean.queue.task;

import com.zilean.queue.dao.ZileanStatisticsRepository;
import com.zilean.queue.domain.entity.ZileanStatisticsDO;
import com.zilean.queue.enums.StatusEnum;
import com.zilean.queue.redis.RedissonUtil;
import com.zilean.queue.util.ZileanTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

import static com.zilean.queue.constant.RedisConstant.TASK_LOCK_KEY;
import static com.zilean.queue.constant.RedisConstant.TASK_LOCK_RETRY_QUANTITY;
import static com.zilean.queue.constant.RedisConstant.TASK_LOCK_TTL;
import static com.zilean.queue.constant.RedisConstant.TASK_LOCK_WAIT;
import static com.zilean.queue.constant.RedisConstant.TODAY_DELAYED_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_DELAYED_TOTAL_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_FAILED_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_FAILED_TOTAL_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_READY_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_READY_TOTAL_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_SUCCESS_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_SUCCESS_TOTAL_KEY;
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

    @Autowired
    private ZileanStatisticsRepository zileanStatisticsRepository;

    /**
     * 我们的定时任务将会定时的将缓存的访问量、延迟数、读取数、成功数、失败数保存入库，
     * 并且会即使清理缓存中的数据。
     * 我们使用分布式锁，防止多个实例之间进行同时执行任务。
     * 我们将每天执行一次。
     */
    @Scheduled(cron = "00 05 00 * * ?")
    public void clearCache() {
        log.info("【TASK】ZileanTask.clearCache ... start ...");
        RedissonClient redissonClient = RedissonUtil.getRedissonClient();
        RLock lock = redissonClient.getLock(TASK_LOCK_KEY);
        int retryQuantity = TASK_LOCK_RETRY_QUANTITY;
        try {
            while (!lock.tryLock(TASK_LOCK_WAIT, TASK_LOCK_TTL, TimeUnit.SECONDS)) {
                if (--retryQuantity > 0) {
                    continue;
                }
                log.info("【TASK】ZileanTask.clearCache ... lock failed and exit! ...");
                return;
            }

            RAtomicLong visitNumForToday = redissonClient.getAtomicLong(VISIT_KEY);
            RAtomicLong delayNumForToday = redissonClient.getAtomicLong(TODAY_DELAYED_KEY);
            RAtomicLong readyNumForToday = redissonClient.getAtomicLong(TODAY_READY_KEY);
            RAtomicLong successNumForToday = redissonClient.getAtomicLong(TODAY_SUCCESS_KEY);
            RAtomicLong failedNumForToday = redissonClient.getAtomicLong(TODAY_FAILED_KEY);


            ZileanStatisticsDO zileanStatisticsDO = new ZileanStatisticsDO();
            zileanStatisticsDO.setCreateTime(ZileanTimeUtil.getCurYearMonthDay());
            zileanStatisticsDO.setVer(1);
            zileanStatisticsDO.setStatus(StatusEnum.STATISTICS_STATUS_NORMAL.getStatus());

            zileanStatisticsDO.setVisitNum((int) visitNumForToday.get());
            zileanStatisticsDO.setVisitTotal((int) redissonClient.getAtomicLong(VISIT_TOTAL_KEY).get());

            zileanStatisticsDO.setDelayedNum((int) delayNumForToday.get());
            zileanStatisticsDO.setDelayedTotal((int) redissonClient.getAtomicLong(TODAY_DELAYED_TOTAL_KEY).get());

            zileanStatisticsDO.setReadyNum((int) readyNumForToday.get());
            zileanStatisticsDO.setReadyTotal((int) redissonClient.getAtomicLong(TODAY_READY_TOTAL_KEY).get());

            zileanStatisticsDO.setSuccessNum((int) successNumForToday.get());
            zileanStatisticsDO.setSuccessTotal((int) redissonClient.getAtomicLong(TODAY_SUCCESS_TOTAL_KEY).get());

            zileanStatisticsDO.setFailedNum((int) failedNumForToday.get());
            zileanStatisticsDO.setFailedTotal((int) redissonClient.getAtomicLong(TODAY_FAILED_TOTAL_KEY).get());

            zileanStatisticsRepository.save(zileanStatisticsDO);


            visitNumForToday.setAsync(0L);
            delayNumForToday.setAsync(0L);
            readyNumForToday.setAsync(0L);
            successNumForToday.setAsync(0L);
            failedNumForToday.setAsync(0L);

            // TODO: 2019-07-13 成功消费后记得记录成功条数

        } catch (InterruptedException e) {
            log.info("【TASK】ZileanTask.clearCache ... InterruptedException ...", e);
            return;
        } catch (Exception e) {
            log.info("【TASK】ZileanTask.clearCache ... error ...", e);
            return;
        } finally {
            // 我们不再主动释放分布式锁，由TTL主动释放
            // 如果主动释放，可能会由于时间差，第二个进程、第三个进程，继续执行定时任务。
//             lock.unlock() ;
        }

        log.info("【TASK】ZileanTask.clearCache ...  end  ...");
    }
}
