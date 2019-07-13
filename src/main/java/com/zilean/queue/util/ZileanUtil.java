package com.zilean.queue.util;

import com.zilean.queue.domain.entity.ZileanJobDO;
import com.zilean.queue.enums.StatusEnum;
import com.zilean.queue.enums.TypeEnum;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-14 03:50
 */
public class ZileanUtil {

    /**
     * 为zileanJobDO赋值默认值
     *
     * @param zileanJobDO
     */
    public static void defaultValueForZileanJobDO(ZileanJobDO zileanJobDO) {
        long curTime = ZileanTimeUtil.getCurTime();
        zileanJobDO.setCreateTime(curTime);
        zileanJobDO.setUpdateTime(curTime);
        zileanJobDO.setStatus(StatusEnum.JOB_STATUS_DELAYED.getStatus());
        zileanJobDO.setVer(1);
        zileanJobDO.setRetryTime(0);
        zileanJobDO.setType(TypeEnum.JOB_TYPE_NORMAL.getType());
    }
}
