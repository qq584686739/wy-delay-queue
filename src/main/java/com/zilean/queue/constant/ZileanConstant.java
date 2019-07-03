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

    /**
     * project name
     */
    public static final String PROJECT_NAME = "ZILEAN-QUEUE:";

    /**
     * bucket name
     */
    public static final String BUCKET_NAME = PROJECT_NAME + "ZILEAN-BUCKET";

    public static final int MAX_DELAY_15_DAYS = 1296000;
    public static final int MAX_BODY_LENGTH = 2000;
    public static final String[] INVALID_SIGIN = new String[]{"@", "#", "$", "%", "^", "&", "*"};

    public static final int OPT_INSERT = 0;
    public static final int OPT_APPEND = 1;
    public static final int OPT_UPDATE = 2;
    public static final int OPT_DELETE = 3;
    public static final List<Integer> OPT_LIST = Arrays.asList(OPT_INSERT, OPT_APPEND, OPT_UPDATE, OPT_DELETE);


    public static final String HTTP_PREFIX = "http://";
    public static final String HTTPS_PREFIX = "https://";
}
