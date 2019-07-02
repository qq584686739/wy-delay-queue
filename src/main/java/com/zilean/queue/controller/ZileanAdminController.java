package com.zilean.queue.controller;

import com.zilean.queue.domain.ZileanJob;
import com.zilean.queue.domain.response.ZileanResponse;
import com.zilean.queue.redis.RedissonUtil;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-01 21:24
 */
@RestController
@RequestMapping("/admin")
public class ZileanAdminController {
    @GetMapping("/job")
    public ZileanResponse getJob(String id) {
        RedissonClient redissonClient = RedissonUtil.getRedissonClient();
        RScoredSortedSet<Object> scoredSortedSet = redissonClient.getScoredSortedSet("hello");
        return ZileanResponse.success((ZileanJob) scoredSortedSet.first());
    }
}
