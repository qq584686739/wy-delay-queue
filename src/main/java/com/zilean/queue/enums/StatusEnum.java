package com.zilean.queue.enums;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-13 23:50
 */
public enum StatusEnum {

    // ==========job status start
    // 延迟状态：1delayed、2ready、3failed、4finish、5delete
    JOB_STATUS_DELAYED(1),
    JOB_STATUS_READY(2),
    JOB_STATUS_FAILED(3),
    JOB_STATUS_FINISH(4),
    JOB_STATUS_DELETE(5),

    // ==========job status end

    // ==========token status start
    /**
     * token status normal
     */
    TOKEN_STATUS_NORMAL(1),
    // ==========token status end

    // ==========statistics status start
    /**
     * statistics status normal
     */
    STATISTICS_STATUS_NORMAL(1);
    // ==========statistics status end

    private int status;


    StatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StatusEnum{" +
            "status=" + status +
            '}';
    }}
