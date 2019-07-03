package com.zilean.queue.constant;

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


}
