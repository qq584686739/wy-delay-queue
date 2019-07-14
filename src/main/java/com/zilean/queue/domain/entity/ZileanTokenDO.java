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
@Table(name = "tbl_zilean_token")
public class ZileanTokenDO extends BaseDO {

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
}
