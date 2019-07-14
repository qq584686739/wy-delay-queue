package com.zilean.queue.enums;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-14 03:33
 */
public enum TypeEnum {
    JOB_TYPE_SIMPLE(1);

    private int type;

    TypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
