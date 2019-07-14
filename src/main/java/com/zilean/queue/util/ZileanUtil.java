package com.zilean.queue.util;

import com.zilean.queue.domain.entity.ZileanJobDO;
import com.zilean.queue.domain.entity.base.BaseDO;
import com.zilean.queue.enums.StatusEnum;
import com.zilean.queue.enums.TypeEnum;

import static com.zilean.queue.constant.ZileanConstant.DEFAULT_RETRY_TIME;
import static com.zilean.queue.constant.ZileanConstant.DEFAULT_VER;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-14 03:50
 */
public class ZileanUtil {

    public static <DO extends BaseDO> void defaultInsertValueForBaseDO(DO d) {
        long curTime = ZileanTimeUtil.getCurTime();
        d.setCreateTime(curTime);
        d.setUpdateTime(curTime);
        d.setStatus(StatusEnum.JOB_STATUS_DELAYED.getStatus());
        d.setVer(DEFAULT_VER);
        if (d instanceof ZileanJobDO) {
            ((ZileanJobDO) d).setRetryTime(DEFAULT_RETRY_TIME);
            ((ZileanJobDO) d).setType(TypeEnum.JOB_TYPE_SIMPLE.getType());
        }
    }

    public static <DO extends BaseDO> void defaultUpdatetValueForBaseDO(DO d) {
        long curTime = ZileanTimeUtil.getCurTime();
        d.setUpdateTime(curTime);
        d.setVer(d.getVer() + 1);
    }
}
