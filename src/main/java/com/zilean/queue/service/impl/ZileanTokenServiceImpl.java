package com.zilean.queue.service.impl;

import com.zilean.queue.dao.ZileanTokenRepository;
import com.zilean.queue.domain.entity.ZileanTokenDO;
import com.zilean.queue.service.ZileanTokenService;
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
public class ZileanTokenServiceImpl extends AbstractZileanService<ZileanTokenRepository, ZileanTokenDO> implements ZileanTokenService {

    @Autowired
    private ZileanTokenRepository zileanTokenRepository;

    @Override
    public ZileanTokenRepository jpaRepository() {
        return zileanTokenRepository;
    }
}
