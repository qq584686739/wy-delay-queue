package com.zilean.queue.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

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
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == attributes) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();

        log.info("invoke before==========[{}][{}][{}][{}][{}]",
            Thread.currentThread().getName(),
            request.getRequestURL(),
            request.getMethod(),
            joinPoint.getSignature().getDeclaringTypeName(),
            Arrays.toString(joinPoint.getArgs()));
    }
}
