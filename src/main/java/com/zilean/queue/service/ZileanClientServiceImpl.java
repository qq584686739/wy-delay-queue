package com.zilean.queue.service;

import com.zilean.queue.domain.ZileanDelayJob;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-02 20:51
 */
@Service
public class ZileanClientServiceImpl extends AbstractZileanService implements ZileanService {
    @Override
    public int insert(ZileanDelayJob job) {
        // TODO: 2019-07-02 元数据入库
        delayedQueue.offerAsync(job.getId(), job.getDelay(), TimeUnit.SECONDS);
        return 1;
    }
}
