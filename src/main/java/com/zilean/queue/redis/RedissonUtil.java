package com.zilean.queue.redis;

import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RQueue;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RSet;
import org.redisson.api.RSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-01 16:58
 */
public class RedissonUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedissonUtil.class);

    private static RedissonClient redissonClient;

    private RedissonUtil() {

    }

    static {
        try {
            Config config = Config.fromYAML(RedissonUtil.class.getClassLoader().getResource("redis.yaml"));
            long startTime = System.currentTimeMillis();
            redissonClient = Redisson.create(config);
            long endTime = System.currentTimeMillis();
            logger.info(" initialization RedissosnClient use {}ms", endTime - startTime);
            logger.info(" redisconfig:{} ", config.toYAML());
        } catch (Exception e) {
            logger.error(" initialization RedissosnClient error :", e);
        }
    }

    /**
     * 获取 redissonClient
     *
     * @return redissonclient
     */
    public static RedissonClient getRedissonClient() {
        return redissonClient;
    }


    /**
     * 关闭 redissonClient
     *
     * @return
     */
    public static void close() {
        redissonClient.shutdown();
    }

    /**
     * 通用对象桶
     * Redisson的分布式RBucketJava对象是一种通用对象桶可以用来存放任类型的对象*
     *
     * @param <V>        泛型
     * @param objectName 对象名
     * @return RBucket
     */
    public static <V> RBucket<V> getRBucket(String objectName) {
        return redissonClient.getBucket(objectName);
    }

    /**
     * 获取map对象
     *
     * @param <K>        the type parameter
     * @param <V>        the type parameter
     * @param objectName the object name
     * @return the r map
     */
    public static <K, V> RMap<K, V> getMap(String objectName) {
        return redissonClient.getMap(objectName);
    }

    /**
     * 获取支持单个元素过期的map对象
     *
     * @param <K>        the type parameter
     * @param <V>        the type parameter
     * @param objectName the object name
     * @return the r map cache
     */
    public static <K, V> RMapCache<K, V> getMapCache(String objectName) {
        return redissonClient.getMapCache(objectName);
    }

    /**
     * 获取set对象
     *
     * @param <V>        the type parameter
     * @param objectName the object name
     * @return the r set
     */
    public static <V> RSet<V> getSet(String objectName) {
        return redissonClient.getSet(objectName);
    }

    /**
     * 获取SortedSet对象
     *
     * @param <V>        the type parameter
     * @param objectName the object name
     * @return the r sorted set
     */
    public static <V> RSortedSet<V> getSorteSet(String objectName) {
        return redissonClient.getSortedSet(objectName);
    }

    /**
     * 获取ScoredSortedSett对象
     *
     * @param objectName
     * @param <V>
     * @return
     */
    public static <V> RScoredSortedSet<V> getScoredSorteSet(String objectName) {
        return redissonClient.getScoredSortedSet(objectName);
    }

    /**
     * 获取list对象
     *
     * @param <V>        the type parameter
     * @param objectName the object name
     * @return the r listener
     */
    public static <V> RList<V> getList(String objectName) {
        return redissonClient.getList(objectName);
    }

    /**
     * 获取queue对象
     *
     * @param <V>        the type parameter
     * @param objectName the object name
     * @return the r queue
     */
    public static <V> RQueue<V> getQueue(String objectName) {
        return redissonClient.getQueue(objectName);
    }


    /**
     * Get blocking queue r blocking queue.
     *
     * @param <V>        the type parameter
     * @param objectName the object name
     * @return the r blocking queue
     */
    public static <V> RBlockingQueue<V> getBlockingQueue(String objectName) {
        return redissonClient.getBlockingQueue(objectName);
    }

    /**
     * Get atomic long r atomic long.
     *
     * @param objectName the object name
     * @return the r atomic long
     */
    public static RAtomicLong getAtomicLong(String objectName) {
        return redissonClient.getAtomicLong(objectName);
    }

    /**
     * Get lock r lock.
     *
     * @param objectName the object name
     * @return the r lock
     */
    public static RLock getLock(String objectName) {
        return redissonClient.getLock(objectName);
    }
}
