package com.zilean.queue.domain.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-04 17:33
 */
@Data
@Entity(name = "zilean_job")
public class ZileanJobDO {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

}
