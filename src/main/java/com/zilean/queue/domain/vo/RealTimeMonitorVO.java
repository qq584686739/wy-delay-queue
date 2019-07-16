package com.zilean.queue.domain.vo;

import com.zilean.queue.enums.DelayEnum;
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
public class RealTimeMonitorVO implements Serializable {
    private static final long serialVersionUID = -4831972263872629610L;

    private DelayEnum type;
    private List<Monitor> monitorList;

    @Data
    public static class Monitor {
        private String time;
        private Long num;
    }
}
