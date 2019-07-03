package com.zilean.queue.domain;

import com.zilean.queue.exception.ZileanException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

import static com.zilean.queue.constant.ZileanConstant.MAX_BODY_LENGTH;
import static com.zilean.queue.constant.ZileanConstant.MAX_DELAY_15_DAYS;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_BODY;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_CALLBACK;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_DELAY;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_ID;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_ISAPPEND;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-01 20:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleDelayJob extends ZileanDelayJob {
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
    public void check() {
        if (null == id || StringUtils.isEmpty(id.trim())) {
            throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_ID);
        }
        if (this.delay < 0 || this.delay > MAX_DELAY_15_DAYS) {
            throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_DELAY);
        }

        String httpPrefix = "http://";
        String httpsPrefix = "https://";
        boolean isCallbackValid = StringUtils.isEmpty(this.callBack) ||
            !this.callBack.startsWith(httpPrefix) && !this.callBack.startsWith(httpsPrefix);
        if (isCallbackValid) {
            throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_CALLBACK);
        }

        // TODO: 2019-07-03 header
//        INVALID_SIGIN

        if (this.body == null || this.body.trim().isEmpty() || this.body.length() > MAX_BODY_LENGTH) {
            throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_BODY);
        }

        if (0 != this.isAppend && 1 != this.isAppend) {
            throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_ISAPPEND);
        }
    }
}
