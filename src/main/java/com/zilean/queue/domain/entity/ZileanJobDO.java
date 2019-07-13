package com.zilean.queue.domain.entity;

import com.zilean.queue.domain.entity.base.BaseDO;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-04 17:33
 */
@Data
@Entity
@Table(name = "tbl_zilean_queue")
public class ZileanJobDO implements BaseDO, Serializable {

    private static final long serialVersionUID = -4605211241815058608L;
    /**
     * 自增主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 延迟任务名称
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 延迟id
     */
    @Column(name = "delayed_id", nullable = false)
    private String delayedId;

    /**
     * 延迟时间
     */
    @Column(name = "delay", nullable = false)
    private Integer delay;

    /**
     * 回调地址，http://,https://
     */
    @Column(name = "call_back", nullable = false)
    private String callBack;

    /**
     * 回调请求头，json格式
     */
    @Column(name = "header", nullable = false)
    private String header;

    /**
     * 回调请求体，任意格式
     */
    @Column(name = "body", nullable = false)
    private String body;

    /**
     * 延迟状态：1delayed、2ready、3failed、4finish、5delete
     */
    @Column(name = "status", nullable = false)
    private Integer status;

    /**
     * 创建时间:yyyyMMddHHmmss
     */
    @Column(name = "create_time", nullable = false)
    private Long createTime;

    /**
     * 更新时间:yyyyMMddHHmmss
     */
    @Column(name = "update_time", nullable = false)
    private Long updateTime;

    /**
     * 版本号
     */
    @Column(name = "ver", nullable = false)
    private Integer ver;

    /**
     * token_id
     */
    @Column(name = "token_id", nullable = false)
    private Long tokenId;

    /**
     * 回调超时时间
     */
    @Column(name = "ttr", nullable = false)
    private Integer ttr;

    /**
     * 重试次数
     */
    @Column(name = "retry_time", nullable = false)
    private Integer retryTime;

    /**
     * 任务类型：1simple
     */
    @Column(name = "type", nullable = false)
    private Integer type;

}
