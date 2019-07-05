package com.zilean.queue.dao;

import com.zilean.queue.domain.entity.ZileanJobDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-04 17:35
 */
@Component
public interface ZileanJobDAO extends JpaRepository<ZileanJobDO, Long> {
}
