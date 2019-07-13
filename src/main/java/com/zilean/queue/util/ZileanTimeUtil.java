package com.zilean.queue.util;

import java.util.Calendar;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-01 20:37
 */
public class ZileanTimeUtil {

    public final static String ZERO_HOUR_MINUTE_SECOND = "000000";

    private ZileanTimeUtil() {
    }

    /**
     * 获取当前时间
     * 返回时间格式：yyyyMMddHHmmss
     *
     * @return long
     */
    public static long getCurTime() {
        Calendar calendar = Calendar.getInstance();
        String result = String.valueOf(calendar.get(Calendar.YEAR)) +
            (calendar.get(Calendar.MONTH) + 1) +
            calendar.get(Calendar.DAY_OF_MONTH) +
            calendar.get(Calendar.HOUR_OF_DAY) +
            calendar.get(Calendar.MINUTE) +
            calendar.get(Calendar.SECOND);
        return Long.parseLong(result);
    }

    /**
     * 获取当前时分秒
     * 格式：HH:mm:ss
     *
     * @return String
     */
    public static String getCurHourMinuteSecond() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" +
            calendar.get(Calendar.MINUTE) + ":" +
            calendar.get(Calendar.SECOND);
    }

    /**
     * 获取当前年月日
     * 格式：yyyyMMdd000000
     *
     * @return Long
     */
    public static Long getCurYearMonthDay() {
        Calendar calendar = Calendar.getInstance();
        String month = getDoubleMonthOrDay(calendar.get(Calendar.MONTH) + 1);
        String day = getDoubleMonthOrDay(calendar.get(Calendar.DAY_OF_MONTH));
        return Long.valueOf(calendar.get(Calendar.YEAR) + month + day + ZERO_HOUR_MINUTE_SECOND);
    }

    private static String getDoubleMonthOrDay(int month) {
        String monthStr = String.valueOf(month);
        if (month < 10) {
            monthStr = "0" + month;
        }
        return monthStr;
    }


}
