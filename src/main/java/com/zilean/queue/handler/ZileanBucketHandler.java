package com.zilean.queue.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zilean.queue.dao.ZileanJobRepository;
import com.zilean.queue.domain.entity.ZileanJobDO;
import com.zilean.queue.enums.StatusEnum;
import com.zilean.queue.redis.RedissonUtil;
import com.zilean.queue.service.impl.ZileanJobServiceImpl;
import com.zilean.queue.util.HttpUtil;
import com.zilean.queue.util.ThreadPoolExecutorUtil;
import com.zilean.queue.util.ZileanUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static com.zilean.queue.constant.RedisConstant.READY_QUEUE;
import static com.zilean.queue.constant.RedisConstant.TODAY_DELAYED_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_READY_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_READY_TOTAL_KEY;
import static com.zilean.queue.constant.RedisConstant.ZILEAN_QUEUE;


/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-02 18:36
 */
@Slf4j
@Component
public class ZileanBucketHandler implements Runnable {
    private RedissonClient redissonClient = RedissonUtil.getRedissonClient();
    private RBlockingQueue<String> bucket = redissonClient.getBlockingQueue(ZILEAN_QUEUE);
    private RBlockingQueue<String> readyQueue = redissonClient.getBlockingQueue(READY_QUEUE);

    RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(bucket);

    private ExecutorService execDelay = ThreadPoolExecutorUtil.newFixedThreadPool(5);
    private ExecutorService execReady = ThreadPoolExecutorUtil.newFixedThreadPool(5);
    private ExecutorService execFailed = ThreadPoolExecutorUtil.newFixedThreadPool(5);


    @Autowired
    private ZileanJobServiceImpl zileanJobServiceImpl;
    @Autowired
    private ZileanJobRepository zileanJobRepository;

    @Override
    public void run() {
        // TODO: 2019-07-02 在考虑在理的日志是否需要加上 host、hostName、port
        // 延迟队列监听
        ThreadPoolExecutorUtil.newFixedThreadPool(1).execute(() -> {
            log.info("[handle]zilean delay handler running...");
            for (; ; ) {
                listenDelay();
            }
        });

        // 读取队列监听
        ThreadPoolExecutorUtil.newFixedThreadPool(1).execute(() -> {
            log.info("[handle]zilean ready handler running...");
            for (; ; ) {
                listenReady();
            }
        });

        // 失败队列监听
        ThreadPoolExecutorUtil.newFixedThreadPool(1).execute(() -> {
            log.info("[handle]zilean failed handler running...");
            for (; ; ) {
                listenFailed();
            }
        });
    }

    private void listenFailed() {
    }

    private void listenReady() {
        String take;
        try {
            take = readyQueue.take();
        } catch (InterruptedException e) {
            log.error("[handle]ZileanBucketHandler.listenReady take error!", e);
            return;
        }
        execReady.execute(() -> doReadyJob(take));
    }

    private void doReadyJob(String take) {
        ZileanJobDO zileanJobDO;
        try {
            zileanJobDO = zileanJobServiceImpl.selectByDelayedId(take);
        } catch (Exception e) {
            // 延迟队列的delayedId在数据库找不到数据，直接丢弃。
            log.error("[handle]消费队列读取到的数据无法在数据库找到数据,跳过。take:{}.", take);
            return;
        }
        HttpUtil httpUtil;

        String url = zileanJobDO.getCallBack();
        String headerStr = zileanJobDO.getHeader();
        String bodyStr = zileanJobDO.getBody();
        Map<String, String> body = new HashMap<>(1);
        body.put("body", bodyStr);


        if (StringUtils.isEmpty(headerStr)) {
            httpUtil = new HttpUtil(url, body);
        } else {
            JSONObject header = JSONObject.parseObject(headerStr);
            httpUtil = new HttpUtil(url, (Map) header, body);
        }

        httpUtil.setTimeout(zileanJobDO.getTtr());
        HttpUtil.HttpResult result = httpUtil.request();
        // TODO: 2019-07-16  已经消费，当前读取队列数量-1
        if (null == result) {
            // TODO: 2019-07-16 记录请求日志
            // TODO: 2019-07-16 记录返回日志
            intoFailedQueue(take);
            return;
        }
        String response = result.getResponseBodyString("UTF-8");

        // TODO: 2019-07-16 记录请求日志
        // TODO: 2019-07-16 记录返回日志

        if (StringUtils.isEmpty(response)) {
            intoFailedQueue(take);
            return;
        }

        // TODO: 2019-07-16 修改状态
        // TODO: 2019-07-16 成功队列+1

    }

    private void intoFailedQueue(String delayedId) {
        // 进入失败队列
        // TODO: 2019-07-16 修改状态，更新时间，版本
        log.error("");
    }

    private void listenDelay() {
        String take;
        try {
            // TODO: 2019-07-03 还在犹豫是否使用多个bucket
            take = bucket.take();
        } catch (InterruptedException e) {
            log.error("[handle]ZileanBucketHandler.listenDelay take error!", e);
            return;
        }
        execDelay.execute(() -> doDelayJob(take));
    }

    private void doDelayJob(String take) {
        log.info("[handle]延迟队列进入消费队列：{}.", take);
        // TODO: 2019-07-16 突然想到，如果初始化数据时，数据库与缓存数据不一致，要以数据库的数据量为准

        ZileanJobDO zileanJobDO;
        RAtomicLong curDelayedNum = redissonClient.getAtomicLong(TODAY_DELAYED_KEY);
        if (curDelayedNum.get() > 0) {
            curDelayedNum.decrementAndGet();
        }
        try {
            zileanJobDO = zileanJobServiceImpl.selectByDelayedId(take);
        } catch (Exception e) {
            // 延迟队列的delayedId在数据库找不到数据，直接丢弃。
            log.error("[handle]延迟队列进入读取消费队列失败！查找数据库报错！delayedId:{}.", take, e);
            return;
        }
        if (StatusEnum.JOB_STATUS_DELAYED.getStatus() != zileanJobDO.getStatus()) {
            // 状态已经不是延迟状态了，数据不一致，本次执行中断
            log.warn("[handle]延迟队列进入读取消费队列失败！数据库状态已经不是延迟状态:{}.", JSON.toJSONString(zileanJobDO));
            return;
        }
        zileanJobDO.setStatus(StatusEnum.JOB_STATUS_READY.getStatus());
        ZileanUtil.defaultUpdatetValueForBaseDO(zileanJobDO);
        zileanJobRepository.save(zileanJobDO);

        if (!readyQueue.add(take)) {
            readyQueue.addAsync(take);
        }

        redissonClient.getAtomicLong(TODAY_READY_KEY).incrementAndGet();
        redissonClient.getAtomicLong(TODAY_READY_TOTAL_KEY).incrementAndGet();
    }
}
