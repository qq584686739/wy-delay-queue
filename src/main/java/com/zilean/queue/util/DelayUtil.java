package com.zilean.queue.util;

import java.util.Calendar;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-01 20:37
 */
public class DelayUtil {

    private DelayUtil() {
    }

    /**
     * 最大的延迟时间，单位：秒，最大为72小时（3天）
     */
    public static final long MAX_DELAY_TIME = 60 * 60 * 24 * 3L;

    /**
     * 根据delay获取绝对时间分数
     * 返回时间格式为:yyyyMMddHHmmss.0
     *
     * @param delay 延迟的时间，单位:s
     * @return double 返回的绝对时间分数,格式为:yyyyMMddHHmmss.0
     */
    public static double transformationScoreWithDelay(String delay) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, Integer.parseInt(delay));
        String result = String.valueOf(calendar.get(Calendar.YEAR)) +
            (calendar.get(Calendar.MONTH) + 1) +
            calendar.get(Calendar.DAY_OF_MONTH) +
            calendar.get(Calendar.HOUR_OF_DAY) +
            calendar.get(Calendar.MINUTE) +
            calendar.get(Calendar.SECOND);
        return Double.parseDouble(result);
    }
}
