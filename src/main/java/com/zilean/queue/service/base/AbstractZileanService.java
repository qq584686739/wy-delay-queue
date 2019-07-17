package com.zilean.queue.service.base;

import com.zilean.queue.domain.entity.base.BaseDO;
import com.zilean.queue.redis.RedissonUtil;
import com.zilean.queue.util.ZileanUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.zilean.queue.constant.RedisConstant.ZILEAN_QUEUE;


/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-02 21:02
 */
@Slf4j
public abstract class AbstractZileanService<T extends JpaRepository, DO extends BaseDO> implements ZileanService<DO> {
    protected final RedissonClient redissonClient = RedissonUtil.getRedissonClient();
    protected final RBlockingQueue<String> bucketBlockingQueue = redissonClient.getBlockingQueue(ZILEAN_QUEUE);
    protected final RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(bucketBlockingQueue);

    /**
     * 子类请覆盖该方法传入jpaRepository
     *
     * @return T
     */
    public abstract T jpaRepository();


    @Override
    public DO insert(DO d) {
        ZileanUtil.defaultInsertValueForBaseDO(d);
        return (DO) jpaRepository().save(d);
    }

    @Override
    public void deleteById(Serializable delayedId) {
        jpaRepository().deleteById(delayedId);
    }

    @Override
    public void deleteByIdList(List<Serializable> idList) {
        jpaRepository().deleteAll(idList);
    }

    @Override
    public DO updateById(DO d) {
        ZileanUtil.defaultUpdateValueForBaseDO(d);
        return (DO) jpaRepository().save(d);
    }

    @Override
    public DO appendById(DO d) {
        return (DO) jpaRepository().save(d);
    }

    @Override
    public Optional<DO> selectById(Serializable delayedId) {
        return jpaRepository().findById(delayedId);
    }

    @Override
    public List<DO> selectRangeByCreateTime(long startTime, long endTime, Sort.Direction direction) {
        String propertiesForCreateTime = "createTime";
        return ((JpaSpecificationExecutor) jpaRepository()).findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.gt(root.get(propertiesForCreateTime), startTime));
            predicates.add(criteriaBuilder.lt(root.get(propertiesForCreateTime), endTime));
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, new Sort(direction, propertiesForCreateTime));
    }

}
