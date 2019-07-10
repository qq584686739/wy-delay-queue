package com.zilean.queue.domain.vo;

import com.zilean.queue.domain.response.ResponseAble;
import lombok.Data;

import java.util.List;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-11 00:00
 */
@Data
public class AdminIndexVO implements ResponseAble {
    /**
     * 今日访问量
     */
    private Long curVisitNumber;
    /**
     * 总访问量
     */
    private Long visitNumberTotal;
    /**
     * 今日延迟数
     */
    private Long curDelayed;
    /**
     * 延迟总数
     */
    private Long curDelayedTotal;
    /**
     * 今日读取数
     */
    private Long curReady;
    /**
     * 总读取数
     */
    private Long curReadyTotal;
    /**
     * 当前失败数
     */
    private Long curFailed;
    /**
     * 总失败数
     */
    private Long curFailedTotal;

    /**
     * 历史数据
     */
    private List<DailyReport> historyData;

    @Data
    public static class DailyReport {
        /**
         * 访问量
         */
        private Long visit;
        /**
         * 延迟数
         */
        private Long delayed;
        /**
         * 读取数
         */
        private Long ready;
        /**
         * 失败数
         */
        private Long failed;
        /**
         * 日期,格式：yyyy/MM/dd
         */
        private String time;
    }
}
