package com.zilean.queue.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zilean.queue.dao.ZileanJobRepository;
import com.zilean.queue.domain.entity.ZileanJobDO;
import com.zilean.queue.enums.StatusEnum;
import com.zilean.queue.exception.ZileanException;
import com.zilean.queue.service.ZileanJobService;
import com.zilean.queue.service.base.AbstractZileanService;
import com.zilean.queue.util.ZileanTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.zilean.queue.constant.RedisConstant.TODAY_DELAYED_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_DELAYED_TOTAL_KEY;
import static com.zilean.queue.constant.ZileanConstant.DEFAULT_MIN_ALLOW_UPDATE_DELAY_RANGE;
import static com.zilean.queue.constant.ZileanConstant.MAX_BODY_LENGTH;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_ADD_JOB_FOR_PARAM_BODY;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_FOR_NOT_FOUNT_JOB;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_UPDATE_JOB;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_UPDATE_JOB_FOR_EXPIRE;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_UPDATE_JOB_FOR_SOON_EXPIRE;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-02 20:51
 */
@Slf4j
@Service
public class ZileanJobServiceImpl extends AbstractZileanService<ZileanJobRepository, ZileanJobDO> implements ZileanJobService {

    @Resource
    private ZileanJobRepository zileanJobRepository;

    @Override
    public ZileanJobRepository jpaRepository() {
        return zileanJobRepository;
    }

    @Override
    @Transactional
    public ZileanJobDO insert(ZileanJobDO zileanJobDO) {
        ZileanJobDO insert = super.insert(zileanJobDO);
        delayedQueue.offerAsync(zileanJobDO.getDelayedId(), zileanJobDO.getDelay(), TimeUnit.SECONDS);
        redissonClient.getAtomicLong(TODAY_DELAYED_KEY).incrementAndGetAsync();
        redissonClient.getAtomicLong(TODAY_DELAYED_TOTAL_KEY).incrementAndGetAsync();
        return insert;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Serializable delayedId) {
        updateStatusByDelayedId(delayedId, StatusEnum.JOB_STATUS_DELETE.getStatus());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIdList(List<Serializable> idList) {
        super.deleteByIdList(idList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ZileanJobDO updateById(ZileanJobDO zileanJobDO) {
        containsDelayedCacheByDelayedId(zileanJobDO.getDelayedId());

        /*
         * 【检查时间】
         * 检查时间的方式：
         * 我们不对延迟队列进行遍历，我们根据数据库的创建时间，和延迟时间，以及当前时间
         * 对其进行计算，得出当前任务剩余多久执行，如果时间太短，我们则不允许更改；如果
         * 时间允许，我们将删除old job，创建一个新的job，延迟时间为计算后的时间。
         */

        ZileanJobDO find = selectByDelayedId(zileanJobDO.getDelayedId());
        String body = zileanJobDO.getBody();
        if (!StringUtils.isEmpty(body)) {
            find.setBody(body);
        }
        String header = zileanJobDO.getHeader();
        if (!StringUtils.isEmpty(header)) {
            find.setHeader(header);
        }
        String callBack = zileanJobDO.getCallBack();
        if (!StringUtils.isEmpty(callBack)) {
            find.setCallBack(callBack);
        }
        find.setName(zileanJobDO.getName());
        find.setTtr(zileanJobDO.getTtr());


        Integer delayTime = find.getDelay();
        if (null == delayTime || 0 == delayTime) {
            // 既然没有更新时间，那么就不更新缓存，只需要直接更新db即可
            return super.updateById(find);
        }

        Long createTime = find.getCreateTime();
        long timeDifference;
        try {
            timeDifference = ZileanTimeUtil.getTimeDifference(createTime);
        } catch (ParseException e) {
            log.error("[zilean-error]ZileanJobServiceImpl.updateById: parse time exception! ", e);
            throw new ZileanException(ERROR_UPDATE_JOB);
        }
        if (delayTime - timeDifference <= 0) {
            // job过期
            throw new ZileanException(ERROR_UPDATE_JOB_FOR_EXPIRE);
        }
        if (delayTime - timeDifference <= DEFAULT_MIN_ALLOW_UPDATE_DELAY_RANGE) {
            // job即将过期，不再允许修改时间
            throw new ZileanException(ERROR_UPDATE_JOB_FOR_SOON_EXPIRE);
        }
        find.setDelay(zileanJobDO.getDelay());
        ZileanJobDO update = super.updateById(find);

        // 更新缓存
        if (delayedQueue.remove(zileanJobDO.getDelayedId())) {
            delayedQueue.offer(zileanJobDO.getDelayedId(), zileanJobDO.getDelay(), TimeUnit.SECONDS);
        } else {
            // 移除失败
            throw new ZileanException(ERROR_UPDATE_JOB);
        }
        return update;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ZileanJobDO appendById(ZileanJobDO zileanJobDO) {
        containsDelayedCacheByDelayedId(zileanJobDO.getDelayedId());

        ZileanJobDO find = selectByDelayedId(zileanJobDO.getDelayedId());
        String body = zileanJobDO.getBody();
        if (!StringUtils.isEmpty(body)) {
            body = find.getBody() + body;
            if (body.length() > MAX_BODY_LENGTH) {
                throw new ZileanException(ERROR_ADD_JOB_FOR_PARAM_BODY);
            }
            find.setBody(body);
        }
        String header = zileanJobDO.getHeader();
        if (!StringUtils.isEmpty(header)) {
            String findHeader = find.getHeader();
            JSONObject findHeaderJsonObject;
            if (!StringUtils.isEmpty(findHeader)) {
                findHeaderJsonObject = JSONObject.parseObject(findHeader);
            } else {
                findHeaderJsonObject = new JSONObject();
            }

            JSONObject headerJsonObject = JSONObject.parseObject(header);
            findHeaderJsonObject.putAll(headerJsonObject);
            findHeader = JSON.toJSONString(findHeaderJsonObject);
            find.setHeader(findHeader);
        }

        return super.updateById(find);
    }

    @Override
    public Optional<ZileanJobDO> selectById(Serializable id) {
        return super.selectById(id);
    }


    @Override
    public void cancel(Serializable delayedId) {
        updateStatusByDelayedId(delayedId, StatusEnum.JOB_STATUS_CANCEL.getStatus());
    }

    private void containsDelayedCacheByDelayedId(String delayedId) {
        if (!delayedQueue.contains(delayedId)) {
            // 延迟队列找不到该任务
            throw new ZileanException(ERROR_FOR_NOT_FOUNT_JOB);
        }
    }

    private ZileanJobDO selectByDelayedId(String delayedId) {
        ZileanJobDO probe = new ZileanJobDO();
        probe.setDelayedId(delayedId);
        Optional<ZileanJobDO> findOpt = zileanJobRepository.findOne(Example.of(probe));
        if (!findOpt.isPresent()) {
            throw new ZileanException(ERROR_FOR_NOT_FOUNT_JOB);
        }
        return findOpt.get();
    }

    private void updateStatusByDelayedId(Serializable delayedId, int status) {
        String delayedIdStr = String.valueOf(delayedId);
        containsDelayedCacheByDelayedId(delayedIdStr);
        ZileanJobDO zileanJobDO = selectByDelayedId(delayedIdStr);
        zileanJobDO.setStatus(status);
        super.updateById(zileanJobDO);
        if (!delayedQueue.remove(delayedIdStr)) {
            throw new ZileanException(ERROR_UPDATE_JOB);
        }
    }
}
