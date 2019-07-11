package com.zilean.queue.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-11 23:03
 */
@Data
public class AdminDelayedJobListVO implements Serializable {
    private static final long serialVersionUID = 2849824049545656452L;

    private String jobId;
    private Integer delay;
    private String token;
    private String callBack;
    private String header;
    private String body;
    private Long ttr;
}
