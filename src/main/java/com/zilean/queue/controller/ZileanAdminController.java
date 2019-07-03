package com.zilean.queue.controller;

import com.zilean.queue.domain.ZileanDelayJob;
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
    // TODO: 2019-07-03 管理端接口
    // TODO: 2019-07-02 做修改的话，修改数据直接修改数据库即可，但是要校验job是未执行的job ，如果修改时间的话，先删除再添加。

    @GetMapping("/job")
    public ZileanResponse getJob(String id) {
        RedissonClient redissonClient = RedissonUtil.getRedissonClient();
        RScoredSortedSet<Object> scoredSortedSet = redissonClient.getScoredSortedSet("hello");
        return ZileanResponse.success((ZileanDelayJob) scoredSortedSet.first());
    }
}
