package com.zilean.queue.service;

import com.zilean.queue.domain.BaseZileanJob;
import com.zilean.queue.exception.ZileanException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_APPEND_JOB;
import static com.zilean.queue.exception.ZileanExceptionEnum.ERROR_UPDATE_JOB;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-02 20:51
 */
@Service
public class ZileanClientServiceImpl extends AbstractZileanService implements ZileanService {
    @Override
    public int insert(BaseZileanJob job) {
        // TODO: 2019-07-02 元数据入库
        delayedQueue.offerAsync(job.getId(), job.getDelay(), TimeUnit.SECONDS);
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteById(String id) {
        if (delayedQueue.contains(id)) {
            // TODO: 2019-07-04 update db status
            if (!delayedQueue.remove(id)) {
                throw new ZileanException(ERROR_UPDATE_JOB);
            }
        } else {
            // 延迟队列找不到该任务
            return 0;
        }
        return 1;
    }

    @Override
    public int deleteByIdList(List<String> idList) {
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateById(BaseZileanJob job) {
        if (delayedQueue.contains(job.getId())) {
            int delay;
            if (0 != job.getDelay()) {
                delay = job.getDelay();
            } else {
                // TODO: 2019-07-03 查询db,获取delay
                delay = 1000;
            }
            // TODO: 2019-07-04 update db 选择性更新
            if (delayedQueue.remove(job.getId())) {
                delayedQueue.offer(job.getId(), delay, TimeUnit.SECONDS);
            } else {
                throw new ZileanException(ERROR_UPDATE_JOB);
            }
        } else {
            // 延迟队列找不到该任务
            return 0;
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int appendById(BaseZileanJob job) {

        // TODO: 2019-07-05 校验append的length和现有的length长度，总长度不得超过指定值，超过则报错

        if (delayedQueue.contains(job.getId())) {
            // TODO: 2019-07-03 查询db，计算delay，update db
            if (delayedQueue.remove(job.getId())) {
                int delay = 1000;
                delayedQueue.offer(job.getId(), delay, TimeUnit.SECONDS);
            } else {
                throw new ZileanException(ERROR_APPEND_JOB);
            }
        } else {
            // 延迟队列找不到该任务
            return 0;
        }
        return 1;
    }

    @Override
    public BaseZileanJob selectById(String id) {
        return null;
    }
}
