package com.zilean.queue.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

import java.util.Objects;

import static com.zilean.queue.util.DelayUtil.MAX_DELAY_TIME;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-01 20:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleJob extends ZileanJob {
    private static final long serialVersionUID = 8806335947637844682L;

    /**
     * 回调URL
     */
    private String callBack;

    /**
     * 回调请求头
     */
    private String header = "";

    /**
     * 回调请求体
     */
    private String body;

    /**
     * 是否追加操作。0false；1true
     * 默认0
     */
    private Integer isAppend = 0;

    @Override
    public boolean checkParam() {
        if (StringUtils.isEmpty(this.getId()) ||
            StringUtils.isEmpty(this.getDelay()) ||
            StringUtils.isEmpty(this.getCallBack()) ||
            StringUtils.isEmpty(this.getBody())) {
            return false;
        }

        try {
            long delayTime = Long.parseLong(this.getDelay());
            if (delayTime < 0 || MAX_DELAY_TIME < delayTime) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
