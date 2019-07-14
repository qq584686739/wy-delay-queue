package com.zilean.queue.domain.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-01 21:01
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ZileanPageResponse<T> extends ZileanResponse<T> {
    private Long count;

    public ZileanPageResponse(Long count, T data) {
        super(data);
        this.count = count;
    }

    public static <T> ZileanPageResponse success(long count, T data) {
        return new ZileanPageResponse<>(count, data);
    }
}
