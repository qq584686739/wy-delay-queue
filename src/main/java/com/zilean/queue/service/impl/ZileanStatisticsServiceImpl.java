package com.zilean.queue.service.impl;


import com.zilean.queue.dao.ZileanStatisticsRepository;
import com.zilean.queue.domain.entity.ZileanStatisticsDO;
import com.zilean.queue.service.ZileanStatisticsService;
import com.zilean.queue.service.base.AbstractZileanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-14 01:25
 */
@Slf4j
@Service
public class ZileanStatisticsServiceImpl
    extends AbstractZileanService<ZileanStatisticsRepository, ZileanStatisticsDO>
    implements ZileanStatisticsService {

    @Autowired
    private ZileanStatisticsRepository zileanStatisticsRepository;

    @Override
    public ZileanStatisticsRepository jpaRepository() {
        return zileanStatisticsRepository;
    }
}
