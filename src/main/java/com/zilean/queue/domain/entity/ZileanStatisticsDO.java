package com.zilean.queue.domain.entity;

import com.zilean.queue.domain.entity.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-13 19:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tbl_zilean_statistics")
public class ZileanStatisticsDO extends BaseDO {

    private static final long serialVersionUID = 8343060305240563687L;
    /**
     * 自增主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 当日访问量
     */
    @Column(name = "visit_num", nullable = false)
    private Integer visitNum;
    /**
     * 访问总量
     */
    @Column(name = "visit_total", nullable = false)
    private Integer visitTotal;
    /**
     * 当日延迟任务数
     */
    @Column(name = "delayed_num", nullable = false)
    private Integer delayedNum;
    /**
     * 延迟任务总数
     */
    @Column(name = "delayed_total", nullable = false)
    private Integer delayedTotal;
    /**
     * 当日读取任务数
     */
    @Column(name = "ready_num", nullable = false)
    private Integer readyNum;
    /**
     * 读取任务总数
     */
    @Column(name = "ready_total", nullable = false)
    private Integer readyTotal;
    /**
     * 当日成功任务数
     */
    @Column(name = "success_num", nullable = false)
    private Integer successNum;
    /**
     * 成功任务总数
     */
    @Column(name = "success_total", nullable = false)
    private Integer successTotal;
    /**
     * 当日失败任务数
     */
    @Column(name = "failed_num", nullable = false)
    private Integer failedNum;
    /**
     * 失败任务总数
     */
    @Column(name = "failed_total", nullable = false)
    private Integer failedTotal;
}
