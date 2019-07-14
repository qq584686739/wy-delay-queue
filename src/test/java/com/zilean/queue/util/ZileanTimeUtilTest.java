package com.zilean.queue.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ZileanTimeUtilTest {

    @Test
    public void getCurTime() {
        Long curTime = ZileanTimeUtil.getCurTime();
        System.out.println(curTime);
    }

    @Test
    public void getCurHourMinuteSecond() {
    }

    @Test
    public void getCurYearMonthDay() {
        Long curYearMonthDay = ZileanTimeUtil.getCurYearMonthDay();
        System.out.println(curYearMonthDay);
    }
}