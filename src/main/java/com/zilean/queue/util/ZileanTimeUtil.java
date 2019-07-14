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
    public final static String TIME_SEPARATOR = ":";

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
        String result = calendar.get(Calendar.YEAR) +
            getDoubleData(calendar.get(Calendar.MONTH) + 1) +
            getDoubleData(calendar.get(Calendar.DAY_OF_MONTH)) +
            getDoubleData(calendar.get(Calendar.HOUR_OF_DAY)) +
            getDoubleData(calendar.get(Calendar.MINUTE)) +
            getDoubleData(calendar.get(Calendar.SECOND));
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
        return getDoubleData(calendar.get(Calendar.HOUR_OF_DAY)) + TIME_SEPARATOR +
            getDoubleData(calendar.get(Calendar.MINUTE)) + TIME_SEPARATOR +
            getDoubleData(calendar.get(Calendar.SECOND));
    }

    /**
     * 获取当前年月日
     * 时分秒用000000代替
     * 格式：yyyyMMdd000000
     *
     * @return Long
     */
    public static Long getCurYearMonthDay() {
        Calendar calendar = Calendar.getInstance();
        return Long.valueOf(calendar.get(Calendar.YEAR) +
            getDoubleData(calendar.get(Calendar.MONTH) + 1) +
            getDoubleData(calendar.get(Calendar.DAY_OF_MONTH)) +
            ZERO_HOUR_MINUTE_SECOND
        );
    }

    /**
     * 月、日、时、分、秒
     * 不足两位数则在前面补0
     *
     * @param data data
     * @return String
     */
    private static String getDoubleData(int data) {
        String dataStr = String.valueOf(data);
        if (data < 10) {
            dataStr = "0" + data;
        }
        return dataStr;
    }
}
