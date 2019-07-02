package com.zilean.queue.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-06-18 18:02
 */
public class ThreadPoolExecutorUtil {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolExecutorUtil.class);

    private static final int DEFAULT_LIMIT = 10;

    private static final MyThreadFactory DEFAULT_THREAD_FACTORY = new MyThreadFactory();

    private static final ThreadPoolExecutor DEFAULT_THREAD_POOL = new ThreadPoolExecutor(
        0,
        Integer.MAX_VALUE,
        60L,
        TimeUnit.SECONDS,
        new SynchronousQueue<>(),
        DEFAULT_THREAD_FACTORY
    );

    private static final ThreadPoolExecutor DEFAULT_LIMIT_THREAD_POOL = new ThreadPoolExecutor(
        DEFAULT_LIMIT,
        DEFAULT_LIMIT,
        0L,
        TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(),
        DEFAULT_THREAD_FACTORY
    );

    private static final ThreadPoolExecutor DEFAULT_SINGLE_THREAD_POOL = new ThreadPoolExecutor(
        1,
        1,
        0L,
        TimeUnit.MILLISECONDS,
        new LinkedBlockingQueue<>(),
        DEFAULT_THREAD_FACTORY
    );

    /**
     * 获取默认的线程池
     *
     * @return ExecutorService
     */
    public static ExecutorService getDefaultThreadPool() {
        return DEFAULT_THREAD_POOL;
    }

    public static ExecutorService getDefaultSingleThreadPool() {
        return DEFAULT_SINGLE_THREAD_POOL;
    }

    public static ExecutorService getDefaultLimitThreadPool() {
        return DEFAULT_LIMIT_THREAD_POOL;
    }

    public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(
            0,
            Integer.MAX_VALUE,
            60L,
            TimeUnit.SECONDS,
            new SynchronousQueue<>(),
            new MyThreadFactory()
        );
    }

    public static ExecutorService newFixedThreadPool() {
        return newFixedThreadPool(DEFAULT_LIMIT);
    }

    public static ExecutorService newFixedThreadPool(int size) {
        return new ThreadPoolExecutor(
            size,
            size,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            new MyThreadFactory()
        );
    }

    static class MyThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
            return t;
        }
    }

    static class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            logger.error("[error]【线程异常】[t]:{}, [e]:{}.", t, e);
        }
    }

    public static List<Runnable> shutdown(ExecutorService exec) {
        return exec.shutdownNow();
    }
}
