package com.zilean.queue.constant;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-11 00:18
 */
public class RedisConstant {
    /**
     * project name
     */
    public static final String PROJECT_NAME = "ZILEAN-QUEUE:";

    /**
     * bucket name
     */
    public static final String BUCKET_NAME = PROJECT_NAME + "ZILEAN-BUCKET";


    // task lock start
    /**
     * task lock
     */
    public static final String TASK_LOCK_KEY = PROJECT_NAME + "TASK_LOCK";

    /**
     * task lock ttl, second
     */
    public static final long TASK_LOCK_TTL = 60 * 10L;

    /**
     * task lock wait, second
     */
    public static final long TASK_LOCK_WAIT = 3L;

    /**
     * task lock retry quantity
     */
    public static final int TASK_LOCK_RETRY_QUANTITY = 3;
    // task lock end


    // visit start
    /**
     * 访问量key
     */
    public static final String VISIT_KEY = PROJECT_NAME + "visitToday";
    /**
     * 总访问量key
     */
    public static final String VISIT_TOTAL_KEY = PROJECT_NAME + "visitTotal";
    // visit end


    // delayed start
    /**
     * 今日延迟数key
     */
    public static final String TODAY_DELAYED_KEY = PROJECT_NAME + "delayedToday";

    /**
     * 总延迟数key
     */
    public static final String TODAY_DELAYED_TOTAL_KEY = PROJECT_NAME + "delayedTotal";
    // delayed end


    // ready start
    /**
     * 今日读取数key
     */
    public static final String TODAY_READY_KEY = PROJECT_NAME + "readyToday";

    /**
     * 总读取数key
     */
    public static final String TODAY_READY_TOTAL_KEY = PROJECT_NAME + "readyTotal";
    // ready end


    // failed start
    /**
     * 今日失败数key
     */
    public static final String TODAY_FAILED_KEY = PROJECT_NAME + "failedToday";

    /**
     * 总失败数key
     */
    public static final String TODAY_FAILED_TOTAL_KEY = PROJECT_NAME + "failedTotal";
    // failed end


    // success start
    /**
     * 今日成功数key
     */
    public static final String TODAY_SUCCESS_KEY = PROJECT_NAME + "successToday";

    /**
     * 总成功数key
     */
    public static final String TODAY_SUCCESS_TOTAL_KEY = PROJECT_NAME + "successTotal";
    // success end
}
