package com.zilean.queue.domain.entity.base;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-14 01:55
 */
@Data
@MappedSuperclass
public class BaseDO implements Serializable {
    private static final long serialVersionUID = 6151924242138391341L;
    /**
     * 创建时间:yyyyMMddHHmmss
     * statistics:yyyyMMdd000000
     */
    @Column(name = "create_time", nullable = false)
    protected Long createTime;

    /**
     * 更新时间:yyyyMMddHHmmss
     */
    @Column(name = "update_time", nullable = false)
    protected Long updateTime;

    /**
     * 版本号
     */
    @Column(name = "ver", nullable = false)
    protected Integer ver;

    /**
     * job：1delayed、2ready、3failed、4finish、5delete、6cancel
     * statistics:1正常
     * token：1正常
     */
    @Column(name = "status", nullable = false)
    protected Integer status;
}
