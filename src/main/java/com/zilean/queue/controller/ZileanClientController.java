package com.zilean.queue.controller;

import com.zilean.queue.domain.SimpleDelayJob;
import com.zilean.queue.domain.response.ZileanResponse;
import com.zilean.queue.service.ZileanService;
import com.zilean.queue.util.ThreadPoolExecutorUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-01 17:06
 */
@RestController
@RequestMapping("/client")
public class ZileanClientController {
    /**
     * add exec
     */
    private final ExecutorService addExec = ThreadPoolExecutorUtil.newCachedThreadPool();

    /**
     * add result exec
     */
    private final ExecutorService addResultExec = ThreadPoolExecutorUtil.newCachedThreadPool();

    @Resource
    private ZileanService zileanClientServiceImpl;

    // TODO: 2019-07-02 设计数据库
    // TODO: 2019-07-02 做放重复提交
    // TODO: 2019-07-01 post

    // TODO: 2019-07-02 做修改的话，修改数据直接修改数据库即可，但是要校验job是未执行的job ，如果修改时间的话，先删除再添加。
    @GetMapping("/publish")
    public ZileanResponse publish(SimpleDelayJob simpleDelayJob) {
        simpleDelayJob.check();
        zileanClientServiceImpl.insert(simpleDelayJob);
        return ZileanResponse.success();
    }
}