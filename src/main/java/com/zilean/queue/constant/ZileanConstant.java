package com.zilean.queue.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-02 10:54
 */
public class ZileanConstant {

    public static final int MAX_DELAY_15_DAYS = 1296000;
    public static final int MAX_BODY_LENGTH = 2000;
    public static final int MAX_ID_LENGTH = 20;
    public static final int MAX_CALLBACK_LENGTH = 200;

    public static final String[] INVALID_SIGIN = new String[]{"@", "#", "$", "%", "^", "&", "*"};

    /**
     * 对job的几种常见操作
     */
    public static final int OPT_INSERT = 0;
    public static final int OPT_APPEND = 1;
    public static final int OPT_UPDATE = 2;
    public static final int OPT_DELETE = 3;
    public static final List<Integer> OPT_LIST = Arrays.asList(OPT_INSERT, OPT_APPEND, OPT_UPDATE, OPT_DELETE);


    public static final String HTTP_PREFIX = "http://";
    public static final String HTTPS_PREFIX = "https://";

    /**
     * 客户端的请求需要对token进行校验，校验token的路径拦截器需要的path
     */
    public static final String CLIENT_PATH_PATTERN = "/client/**";

    /**
     * 默认的任务名称
     */
    public static final String DEFAULT_DELAY_NAME = "unnamed";


    /**
     * 默认的任务执行时间
     */
    public static final int DEFAULT_DELAY_TTR = 5000;

}
