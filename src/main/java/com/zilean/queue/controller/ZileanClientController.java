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

    // TODO: 2019-07-03 readme
    // TODO: 2019-07-03 local dev test prod 环境
    // TODO: 2019-07-03 admin 页面 Thymeleaf
    // TODO: 2019-07-03 完善pom文件


    // TODO: 2019-07-02 设计数据库
    // TODO: 2019-07-03 mybatis generator ？jpa？
    // TODO: 2019-07-03 druid 配置


    // TODO: 2019-07-02 做防重复提交
    // TODO: 2019-07-01 post，最后需要改成post方式，临时写成get测试

    // TODO: 2019-07-03 topic job 开发


    @GetMapping("/publish")
    public ZileanResponse publish(SimpleDelayJob simpleDelayJob) {
        simpleDelayJob.check();
        zileanClientServiceImpl.insert(simpleDelayJob);
        return ZileanResponse.success();
    }
}
