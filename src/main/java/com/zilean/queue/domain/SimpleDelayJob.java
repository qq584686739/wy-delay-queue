package com.zilean.queue.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zilean.queue.constant.ZileanConstant;
import com.zilean.queue.exception.ZileanException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static com.zilean.queue.constant.ZileanConstant.HTTPS_PREFIX;
import static com.zilean.queue.constant.ZileanConstant.HTTP_PREFIX;
import static com.zilean.queue.constant.ZileanConstant.INVALID_SIGIN;
import static com.zilean.queue.constant.ZileanConstant.MAX_BODY_LENGTH;
import static com.zilean.queue.constant.ZileanConstant.MAX_CALLBACK_LENGTH;
import static com.zilean.queue.constant.ZileanConstant.MAX_DELAY_15_DAYS;
import static com.zilean.queue.constant.ZileanConstant.MAX_HEADER_LENGTH;
import static com.zilean.queue.constant.ZileanConstant.MAX_ID_LENGTH;
import static com.zilean.queue.constant.ZileanConstant.MAX_TTR_TIMEOUT;
import static com.zilean.queue.constant.ZileanConstant.MIN_TTR_TIMEOUT;
import static com.zilean.queue.constant.ZileanConstant.OPT_APPEND;
import static com.zilean.queue.constant.ZileanConstant.OPT_DELETE;
import static com.zilean.queue.constant.ZileanConstant.OPT_INSERT;
import static com.zilean.queue.constant.ZileanConstant.OPT_UPDATE;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_BODY;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_CALLBACK;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_CALLBACK_BEYOND_LENGTH;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_DELAY;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_HEADER_BEYOND_LENGTH;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_HEADER_DECODE_ERROR;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_HEADER_EMPTY;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_HEADER_FORMAT_ERROR;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_HEADER_SPECIAL_CHARACTERS;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_ID;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_ID_BEYOND_LENGTH;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PUBLISH_TTR_TIMEOUT_BEYOND;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-01 20:23
 */
@Slf4j
@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleDelayJob extends BaseZileanJob {
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
     * 回调超时时间，由业务方指控，当然，我们也有默认值
     */
    private Integer ttr = ZileanConstant.DEFAULT_DELAY_TTR;

    @Override
    public void check(int opt) {
        checkId();
        checkTtr();
        switch (opt) {
            case OPT_INSERT:
                checkDelay(false);
                checkBody(false);
                checkCallBack(false);
                checkHeader(true);
                break;

            case OPT_APPEND:
                checkBody(false);
                checkHeader(true);
                break;

            case OPT_UPDATE:
                checkDelay(true);
                checkBody(true);
                checkCallBack(true);
                checkHeader(true);
                break;

            case OPT_DELETE:
                break;
            default:
        }
    }

    private void checkTtr() {
        if (ttr > MIN_TTR_TIMEOUT && ttr < MAX_TTR_TIMEOUT) {
            return;
        }
        throw new ZileanException(ERROR_ADD_JOB_FOR_PUBLISH_TTR_TIMEOUT_BEYOND);
    }

    private void checkHeader(boolean canEmpty) {
        if (StringUtils.isEmpty(header)) {
            if (canEmpty) {
                return;
            }
            throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_HEADER_EMPTY);
        }

        try {
            header = URLDecoder.decode(header, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("header url decode error! {}.", JSON.toJSONString(this));
            throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_HEADER_DECODE_ERROR);
        }

        if (header.length() > MAX_HEADER_LENGTH) {
            throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_HEADER_BEYOND_LENGTH);
        }

        String[] invalidSigin = INVALID_SIGIN;
        int length = invalidSigin.length;
        for (int i = 0; i < length; i++) {
            if (header.contains(invalidSigin[i])) {
                throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_HEADER_SPECIAL_CHARACTERS);
            }
        }

        try {
            JSONObject.parseObject(header);
        } catch (JSONException e) {
            throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_HEADER_FORMAT_ERROR);
        }
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
        if (MAX_CALLBACK_LENGTH < this.callBack.length()) {
            throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_CALLBACK_BEYOND_LENGTH);
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

    private void checkDelay(boolean canEmpty) {
        if (canEmpty) {
            if (this.delay < 0 || this.delay > MAX_DELAY_15_DAYS) {
                throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_DELAY);
            }
        } else {
            if (this.delay <= 0 || this.delay > MAX_DELAY_15_DAYS) {
                throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_DELAY);
            }
        }
    }

    private void checkId() {
        if (null == delayedId || StringUtils.isEmpty(delayedId.trim())) {
            throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_ID);
        }
        if (MAX_ID_LENGTH < delayedId.length()) {
            throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_ID_BEYOND_LENGTH);
        }
    }
}
