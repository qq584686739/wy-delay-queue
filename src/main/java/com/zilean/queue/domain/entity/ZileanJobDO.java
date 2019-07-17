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
 * created on 2019-07-04 17:33
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tbl_zilean_queue")
public class ZileanJobDO extends BaseDO {

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

    /**
     * response，json格式
     */
    @Column(name = "response", nullable = true)
    private String response;

}
