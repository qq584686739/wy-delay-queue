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
    /**
     * 访问量key
     */
    // TODO: 2019-07-11 访问将会异步入库
    // TODO: 2019-07-11 每天有定时任务将访问量入库
    public static final String VISIT_KEY = PROJECT_NAME + "visit";
    /**
     * 总访问量key
     */
    // TODO: 2019-07-11 定时任务需要设置总访问量key
    public static final String VISIT_TOTAL_KEY = PROJECT_NAME + "visitTotal";


    /**
     * 今日延迟数key
     */
    // TODO: 2019-07-11 定时任务需要清理每日的延迟任务数量
    public static final String TODAY_DELAYED_KEY = PROJECT_NAME + "todayDelayed";

    /**
     * 总延迟数key
     */
    public static final String TODAY_DELAYED_TOTAL_KEY = PROJECT_NAME + "todayDelayedTotal";


    /**
     * 今日读取数key
     */
    // TODO: 2019-07-11 定时任务需要清理每日的读取队列数量
    public static final String TODAY_READY_KEY = PROJECT_NAME + "todayReady";

    /**
     * 总读取数key
     */
    public static final String TODAY_READY_TOTAL_KEY = PROJECT_NAME + "todayReadyTotal";


    /**
     * 今日失败数key
     */
    // TODO: 2019-07-11 定时任务需要清理每日的失败队列数量
    public static final String TODAY_FAILED_KEY = PROJECT_NAME + "todayFailed";

    /**
     * 总失败数key
     */
    public static final String TODAY_FAILED_TOTAL_KEY = PROJECT_NAME + "todayFailedTotal";


}
