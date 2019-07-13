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
 * created on 2019-07-13 19:31
 */
@Data
@Entity
@Table(name = "tbl_zilean_token")
public class ZileanTokenDO implements BaseDO, Serializable {

    private static final long serialVersionUID = 5409702228663768433L;
    /**
     * 自增主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * username
     */
    @Column(name = "username", nullable = false)
    private String username;

    /**
     * password
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * salt
     */
    @Column(name = "salt", nullable = false)
    private String salt;

    /**
     * 昵称
     */
    @Column(name = "nickname", nullable = false)
    private String nickname;

    /**
     * token
     */
    @Column(name = "token", nullable = false)
    private String token;

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
     * 1正常
     */
    @Column(name = "status", nullable = false)
    private Integer status;

}
