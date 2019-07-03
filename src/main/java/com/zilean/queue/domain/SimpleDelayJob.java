package com.zilean.queue.domain;

import com.zilean.queue.exception.ZileanException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

import static com.zilean.queue.constant.ZileanConstant.HTTPS_PREFIX;
import static com.zilean.queue.constant.ZileanConstant.HTTP_PREFIX;
import static com.zilean.queue.constant.ZileanConstant.MAX_BODY_LENGTH;
import static com.zilean.queue.constant.ZileanConstant.MAX_DELAY_15_DAYS;
import static com.zilean.queue.constant.ZileanConstant.OPT_APPEND;
import static com.zilean.queue.constant.ZileanConstant.OPT_DELETE;
import static com.zilean.queue.constant.ZileanConstant.OPT_INSERT;
import static com.zilean.queue.constant.ZileanConstant.OPT_UPDATE;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_BODY;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_CALLBACK;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_DELAY;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_ID;

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

    @Override
    public void check(int opt) {
        switch (opt) {
            case OPT_INSERT:
                checkId();
                checkDelay();
                checkBody(false);
                checkCallBack(false);
                checkHeader(true);
                break;

            case OPT_APPEND:
                checkId();
                checkBody(false);
                checkHeader(true);
                break;

            case OPT_UPDATE:
                checkId();
                checkDelay();
                checkBody(true);
                checkCallBack(true);
                checkHeader(true);
                break;

            case OPT_DELETE:
                checkId();
                break;
            default:
        }
    }

    private void checkHeader(boolean canEmpty) {
        // TODO: 2019-07-03 check header，校验header的格式，还有特殊字符的校验
//        INVALID_SIGIN
    }

    private void checkCallBack(boolean canEmpty) {
        if (canEmpty && StringUtils.isEmpty(this.callBack)) {
            return;
        }
        boolean isCallbackValid = StringUtils.isEmpty(this.callBack) ||
            !this.callBack.startsWith(HTTP_PREFIX) && !this.callBack.startsWith(HTTPS_PREFIX);
        if (isCallbackValid) {
            throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_CALLBACK);
        }
    }

    private void checkBody(boolean canEmpty) {
        if (canEmpty) {
            if (this.body == null || this.body.trim().isEmpty()) {
                return;
            }
            if (this.body.length() > MAX_BODY_LENGTH) {
                throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_BODY);
            }
        }

        if (this.body == null || this.body.trim().isEmpty() || this.body.length() > MAX_BODY_LENGTH) {
            throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_BODY);
        }
    }

    private void checkDelay() {
        if (this.delay < 0 || this.delay > MAX_DELAY_15_DAYS) {
            throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_DELAY);
        }
    }

    private void checkId() {
        if (null == id || StringUtils.isEmpty(id.trim())) {
            throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_ID);
        }
    }
}
