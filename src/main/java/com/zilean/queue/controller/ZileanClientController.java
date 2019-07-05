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

import static com.zilean.queue.constant.ZileanConstant.OPT_APPEND;
import static com.zilean.queue.constant.ZileanConstant.OPT_DELETE;
import static com.zilean.queue.constant.ZileanConstant.OPT_INSERT;
import static com.zilean.queue.constant.ZileanConstant.OPT_UPDATE;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PUBLISH_JOB_ERROR;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_FOR_NOT_FOUNT_JOB;

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

    // TODO: 2019-07-04 add logback.xml

    // TODO: 2019-07-03 readme文件
    // TODO: 2019-07-03 准备local dev test prod 环境
    // TODO: 2019-07-03 admin 页面：Thymeleaf
    // TODO: 2019-07-03 完善pom文件其他信息


    // TODO: 2019-07-02 设计数据库
    // TODO: 2019-07-03 mybatis generator ？jpa？
    // TODO: 2019-07-03 druid 配置


    // TODO: 2019-07-02 做防重复提交
    // TODO: 2019-07-01 post，最后需要改成post方式，临时写成get测试
    // TODO: 2019-07-04 回调需要设置timeout 重试机制 失败队列

    // TODO: 2019-07-03 topic job 开发


    // TODO: 2019-07-04 用户的概念，token等。

    @GetMapping("/publish")
    public ZileanResponse publish(SimpleDelayJob simpleDelayJob) {
        simpleDelayJob.check(OPT_INSERT);
        if (1 != zileanClientServiceImpl.insert(simpleDelayJob)) {
            return ZileanResponse.error(ERROR_ADD_JOB_FOR_PUBLISH_JOB_ERROR);
        }
        return ZileanResponse.success();
    }

    @GetMapping("/append")
    public ZileanResponse append(SimpleDelayJob simpleDelayJob) {
        simpleDelayJob.check(OPT_APPEND);
        if (1 != zileanClientServiceImpl.appendById(simpleDelayJob)) {
            return ZileanResponse.error(ERROR_FOR_NOT_FOUNT_JOB);
        }
        return ZileanResponse.success();
    }

    @GetMapping("/update")
    public ZileanResponse update(SimpleDelayJob simpleDelayJob) {
        simpleDelayJob.check(OPT_UPDATE);
        if (1 != zileanClientServiceImpl.updateById(simpleDelayJob)) {
            return ZileanResponse.error(ERROR_FOR_NOT_FOUNT_JOB);
        }
        return ZileanResponse.success();
    }

    @GetMapping("/delete")
    public ZileanResponse delete(SimpleDelayJob simpleDelayJob) {
        simpleDelayJob.check(OPT_DELETE);
        if (1 != zileanClientServiceImpl.deleteById(simpleDelayJob.getId())) {
            return ZileanResponse.error(ERROR_FOR_NOT_FOUNT_JOB);
        }
        return ZileanResponse.success();
    }
}
