package com.zilean.queue.domain.vo;

import com.zilean.queue.domain.response.ResponseAble;
import com.zilean.queue.enums.ZileanEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-11 12:54
 */
@Data
public class RealTimeMonitorVO implements Serializable, ResponseAble {
    private static final long serialVersionUID = -4831972263872629610L;

    private List<RealTimeMonitor> realTimeMonitorList;

    @Data
    public static class RealTimeMonitor {
        private ZileanEnum type;
        private List<Monitor> monitorList;

        @Data
        public class Monitor {
            private String time;
            private Long num;
        }
    }
}
