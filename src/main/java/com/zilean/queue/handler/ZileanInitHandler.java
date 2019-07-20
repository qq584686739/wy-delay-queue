package com.zilean.queue.handler;

import com.zilean.queue.dao.ZileanJobRepository;
import com.zilean.queue.dao.ZileanStatisticsRepository;
import com.zilean.queue.domain.entity.ZileanStatisticsDO;
import com.zilean.queue.util.RedissonUtil;
import com.zilean.queue.util.ThreadPoolExecutorUtil;
import com.zilean.queue.util.ZileanTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.zilean.queue.constant.RedisConstant.TODAY_DELAYED_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_DELAYED_TOTAL_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_FAILED_TOTAL_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_READY_TOTAL_KEY;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-11 00:47
 */
@Slf4j
@Component
public class ZileanInitHandler {

    @Resource
    private ZileanStatisticsRepository zileanStatisticsRepository;

    @Resource
    private ZileanJobRepository zileanJobRepository;

    private static RedissonClient redissonClient = RedissonUtil.getRedissonClient();

    public void run() {
        ThreadPoolExecutorUtil.getDefaultThreadPool().execute(() -> {

            ZileanStatisticsDO probe = new ZileanStatisticsDO();
            // TODO: 2019-07-13 切记：20190202000000得到的统计数据是0201日的数据 ！！！
            probe.setCreateTime(ZileanTimeUtil.getCurYearMonthDay());
            Optional<ZileanStatisticsDO> zileanStatisticsDOOptional = zileanStatisticsRepository.findOne(Example.of(probe));
            ZileanStatisticsDO zileanStatisticsDO = null;
            if (zileanStatisticsDOOptional.isPresent()) {
                zileanStatisticsDO = zileanStatisticsDOOptional.get();
            }

            // 初始化访问总量
            initVisitTotal();

            // 初始化今日延迟数、延迟总数
            initDelayed(zileanStatisticsDO);

            // 初始化读取数
            initReady(zileanStatisticsDO);


            // 初始化失败数
            initFailed(zileanStatisticsDO);

            log.info("init completed ... ");
        });
    }

    private void initFailed(ZileanStatisticsDO zileanStatisticsDO) {
        if (null == zileanStatisticsDO) {
            return;
        }
        RAtomicLong atomicLong = redissonClient.getAtomicLong(TODAY_FAILED_TOTAL_KEY);
        long failedNum = atomicLong.get();
        if (0 == failedNum) {
            atomicLong.set(zileanStatisticsDO.getFailedTotal());
        }
    }

    private void initReady(ZileanStatisticsDO zileanStatisticsDO) {
        if (null == zileanStatisticsDO) {
            return;
        }
        RAtomicLong atomicLong = redissonClient.getAtomicLong(TODAY_READY_TOTAL_KEY);
        long readyNum = atomicLong.get();
        if (0 == readyNum) {
            atomicLong.set(zileanStatisticsDO.getReadyTotal());
        }
    }

    private void initDelayed(ZileanStatisticsDO zileanStatisticsDO) {
        // init delayed today
        RAtomicLong atomicLongForDelay = redissonClient.getAtomicLong(TODAY_DELAYED_KEY);
        long todayDelayedNum = atomicLongForDelay.get();
        if (0L == todayDelayedNum) {
            long count = zileanJobRepository.count((root, criteriaQuery, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.gt(root.get("createTime"), ZileanTimeUtil.getCurYearMonthDay()));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            });
            atomicLongForDelay.set(count);
        }

        // init delayed total
        if (null == zileanStatisticsDO) {
            return;
        }
        RAtomicLong atomicLongForDelayTotal = redissonClient.getAtomicLong(TODAY_DELAYED_TOTAL_KEY);
        long delayedTotalNum = atomicLongForDelayTotal.get();
        if (0L == delayedTotalNum) {
            atomicLongForDelayTotal.set(zileanStatisticsDO.getDelayedTotal());
        }
    }

    private void initVisitTotal() {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(TODAY_DELAYED_TOTAL_KEY);
        long visitTotal = atomicLong.get();
        if (0 == visitTotal) {
            atomicLong.set(zileanJobRepository.count());
        }
    }
}
