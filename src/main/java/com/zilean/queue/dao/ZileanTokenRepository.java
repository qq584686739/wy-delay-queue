package com.zilean.queue.dao;

import com.zilean.queue.domain.entity.ZileanTokenDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-13 17:35
 */
@Component
public interface ZileanTokenRepository extends JpaRepository<ZileanTokenDO, Long>, JpaSpecificationExecutor<ZileanTokenDO> {
}
