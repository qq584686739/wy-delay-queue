package com.zilean.queue.aop;

import com.zilean.queue.redis.RedissonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

import static com.zilean.queue.constant.RedisConstant.VISIT_KEY;
import static com.zilean.queue.constant.RedisConstant.VISIT_TOTAL_KEY;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-02 00:32
 */
@Aspect
@Component
public class ZileanLogAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 定义切入点
     */
    @Pointcut("execution(public * com.zilean.queue.controller.*.*(..))")
    private void logPointCut() {
    }

    /**
     * 前置通知 日志
     *
     * @param joinPoint joinPoint
     */
    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) {

        // 设置今日访问量、访问总量
        RedissonClient redissonClient = RedissonUtil.getRedissonClient();
        redissonClient.getAtomicLong(VISIT_KEY).incrementAndGet();
        redissonClient.getAtomicLong(VISIT_TOTAL_KEY).incrementAndGet();


        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == attributes) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();

        if ("realTimeMonitor".equals(joinPoint.getSignature().getName())) {
            return;
        }

        log.info("invoke before==========[{}][{}][{}][{}][{}]",
            Thread.currentThread().getName(),
            request.getRequestURL(),
            request.getMethod(),
            joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(),
            Arrays.toString(joinPoint.getArgs()));
    }
}
