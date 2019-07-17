package com.zilean.queue.domain.response;

import lombok.Data;

import java.io.Serializable;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-17 13:14
 */
@Data
public class BusinessResponse implements Serializable {
    private Integer code;
    private String msg;
}
