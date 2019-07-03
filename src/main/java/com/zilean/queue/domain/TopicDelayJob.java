package com.zilean.queue.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-01 20:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TopicDelayJob extends ZileanDelayJob {
    private static final long serialVersionUID = -7456117880248254519L;

    @Override
    void check(int opt) {

    }
}