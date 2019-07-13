package com.zilean.queue.controller;

import com.zilean.queue.domain.SimpleDelayJob;
import com.zilean.queue.domain.entity.ZileanJobDO;
import com.zilean.queue.domain.response.ZileanResponse;
import com.zilean.queue.service.impl.ZileanJobServiceImpl;
import com.zilean.queue.util.ThreadPoolExecutorUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;

import static com.zilean.queue.constant.ZileanConstant.OPT_APPEND;
import static com.zilean.queue.constant.ZileanConstant.OPT_DELETE;
import static com.zilean.queue.constant.ZileanConstant.OPT_INSERT;
import static com.zilean.queue.constant.ZileanConstant.OPT_UPDATE;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-01 17:06
 */
@RestController
@RequestMapping("/client")
public class ZileanClientController {

    // TODO: 2019-07-14 这俩线程池给干掉
    /**
     * add exec
     */
    private final ExecutorService addExec = ThreadPoolExecutorUtil.newCachedThreadPool();

    /**
     * add result exec
     */
    private final ExecutorService addResultExec = ThreadPoolExecutorUtil.newCachedThreadPool();

    // TODO: 2019-07-14 面对接口开发
    @Autowired
    private ZileanJobServiceImpl zileanJobServiceImpl;

    // TODO: 2019-07-04 add logback.xml

    // TODO: 2019-07-03 readme文件
    // TODO: 2019-07-03 准备local dev test prod 环境
    // TODO: 2019-07-03 完善pom文件其他信息


    // TODO: 2019-07-02 做防重复提交
    // TODO: 2019-07-01 post，最后需要改成post方式，临时写成get测试
    // TODO: 2019-07-04 回调需要设置timeout 重试机制 失败队列

    // TODO: 2019-07-03 topic job 开发


    // TODO: 2019-07-04 用户的概念，token等。

    @GetMapping("/publish")
    public ZileanResponse publish(SimpleDelayJob simpleDelayJob) {
        simpleDelayJob.check(OPT_INSERT);
        ZileanJobDO zileanJobDO = new ZileanJobDO();
        BeanUtils.copyProperties(simpleDelayJob, zileanJobDO);
        token(zileanJobDO);
        zileanJobServiceImpl.insert(zileanJobDO);
        return ZileanResponse.success();
    }


    @GetMapping("/append")
    public ZileanResponse append(SimpleDelayJob simpleDelayJob) {
        simpleDelayJob.check(OPT_APPEND);
        ZileanJobDO zileanJobDO = new ZileanJobDO();
        BeanUtils.copyProperties(simpleDelayJob, zileanJobDO);
        token(zileanJobDO);
        zileanJobServiceImpl.appendById(zileanJobDO);
        return ZileanResponse.success();
    }

    @GetMapping("/update")
    public ZileanResponse update(SimpleDelayJob simpleDelayJob) {
        simpleDelayJob.check(OPT_UPDATE);
        ZileanJobDO zileanJobDO = new ZileanJobDO();
        BeanUtils.copyProperties(simpleDelayJob, zileanJobDO);
        token(zileanJobDO);
        zileanJobServiceImpl.updateById(zileanJobDO);
        return ZileanResponse.success();
    }

    @GetMapping("/delete")
    public ZileanResponse delete(SimpleDelayJob simpleDelayJob) {
        simpleDelayJob.check(OPT_DELETE);
        // TODO: 2019-07-14 check token
        zileanJobServiceImpl.deleteById(simpleDelayJob.getDelayedId());
        return ZileanResponse.success();
    }

    @GetMapping("/cancel")
    public ZileanResponse cancel(SimpleDelayJob simpleDelayJob) {
        simpleDelayJob.check(OPT_DELETE);
        // TODO: 2019-07-14 check token
        zileanJobServiceImpl.cancel(simpleDelayJob.getDelayedId());
        return ZileanResponse.success();
    }

    private void token(ZileanJobDO zileanJobDO) {
        // TODO: 2019-07-14 token问题
        zileanJobDO.setTokenId(123L);
    }
}
