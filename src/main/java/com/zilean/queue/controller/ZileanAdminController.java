package com.zilean.queue.controller;

import com.zilean.queue.domain.entity.ZileanJobDO;
import com.zilean.queue.domain.entity.ZileanStatisticsDO;
import com.zilean.queue.domain.response.ZileanPageResponse;
import com.zilean.queue.domain.response.ZileanResponse;
import com.zilean.queue.domain.vo.AdminDelayedJobListVO;
import com.zilean.queue.domain.vo.AdminIndexVO;
import com.zilean.queue.domain.vo.RealTimeMonitorVO;
import com.zilean.queue.enums.DelayEnum;
import com.zilean.queue.enums.StatusEnum;
import com.zilean.queue.redis.RedissonUtil;
import com.zilean.queue.service.impl.ZileanJobServiceImpl;
import com.zilean.queue.service.impl.ZileanStatisticsServiceImpl;
import com.zilean.queue.util.ZileanTimeUtil;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import static com.zilean.queue.constant.RedisConstant.TODAY_DELAYED_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_DELAYED_TOTAL_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_FAILED_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_FAILED_TOTAL_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_READY_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_READY_TOTAL_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_SUCCESS_KEY;
import static com.zilean.queue.constant.RedisConstant.VISIT_KEY;
import static com.zilean.queue.constant.RedisConstant.VISIT_TOTAL_KEY;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-01 21:24
 */
@RestController
@RequestMapping("/admin")
public class ZileanAdminController {

    @Autowired
    private ZileanStatisticsServiceImpl zileanStatisticsServiceImpl;

    @Autowired
    private ZileanJobServiceImpl zileanJobServiceImpl;
    // TODO: 2019-07-03 管理端接口,增删改查

    /**
     * 首页监控数据
     *
     * @return ZileanResponse
     */
    @GetMapping("/index")
    public ZileanResponse index() {

        AdminIndexVO result = new AdminIndexVO();
        RedissonClient redissonClient = RedissonUtil.getRedissonClient();

        // 设置今日访问量、访问总量
        result.setCurVisitNumber(redissonClient.getAtomicLong(VISIT_KEY).get());
        result.setVisitNumberTotal(redissonClient.getAtomicLong(VISIT_TOTAL_KEY).get());

        // 设置今日延迟数、延迟总数
        result.setCurDelayed(redissonClient.getAtomicLong(TODAY_DELAYED_KEY).get());
        result.setCurDelayedTotal(redissonClient.getAtomicLong(TODAY_DELAYED_TOTAL_KEY).get());

        // 设置今日读取数、读取总数
        result.setCurReady(redissonClient.getAtomicLong(TODAY_READY_KEY).get());
        result.setCurReadyTotal(redissonClient.getAtomicLong(TODAY_READY_TOTAL_KEY).get());

        // 设置今日失败数、失败总数
        // TODO: 2019-07-11 失败队列是否需要考虑三级失败队列？
        result.setCurFailed(redissonClient.getAtomicLong(TODAY_FAILED_KEY).get());
        result.setCurFailedTotal(redissonClient.getAtomicLong(TODAY_FAILED_TOTAL_KEY).get());

        List<ZileanStatisticsDO> list = zileanStatisticsServiceImpl.selectRangeByCreateTime(
            ZileanTimeUtil.getTimeBefore30Day(), ZileanTimeUtil.getCurTime(), Sort.Direction.ASC);
        if (CollectionUtils.isEmpty(list)) {
            result.setHistoryData(Collections.emptyList());
        } else {
            List historyData = new ArrayList();
            list.forEach(it -> historyData.add(new AdminIndexVO.DailyReport() {{
                setVisit(Long.valueOf(it.getVisitNum()));
                setDelayed(Long.valueOf(it.getDelayedNum()));
                setReady(Long.valueOf(it.getReadyNum()));
                setSuccess(Long.valueOf(it.getSuccessNum()));
                setFailed(Long.valueOf(it.getFailedNum()));
                setTime(ZileanTimeUtil.formatTime(it.getCreateTime()));
                // TODO: 2019-07-15 下次清空一下数据把这里弄一下
            }}));
            result.setHistoryData(historyData);
        }
        return ZileanResponse.success(result);
    }

    /**
     * 首页监控历史数据
     *
     * @return ZileanResponse
     */
    @GetMapping("/realTimeMonitorHistory")
    public ZileanResponse realTimeMonitorHistory() {

        // TODO: 2019-07-15 这种形式数据不准确

        // TODO: 2019-07-15 list倒置

        List<ZileanJobDO> dataList = zileanJobServiceImpl.selectRangeByCreateTime(
            1L, ZileanTimeUtil.getCurTime(), Sort.Direction.DESC);
        if (CollectionUtils.isEmpty(dataList)) {
            return ZileanResponse.success(Collections.emptyList());
        }
        ListIterator<ZileanJobDO> dataListIterator = dataList.listIterator();


        List<RealTimeMonitorVO> result = new ArrayList<>();

        RealTimeMonitorVO delayedVO = new RealTimeMonitorVO();
        delayedVO.setType(DelayEnum.DELAYED);
        delayedVO.setMonitorList(new ArrayList<>());
        result.add(delayedVO);


        RealTimeMonitorVO readyVO = new RealTimeMonitorVO();
        readyVO.setType(DelayEnum.READY);
        readyVO.setMonitorList(new ArrayList<>());
        result.add(readyVO);


        RealTimeMonitorVO successVO = new RealTimeMonitorVO();
        successVO.setType(DelayEnum.SUCCESS);
        successVO.setMonitorList(new ArrayList<>());
        result.add(successVO);


        RealTimeMonitorVO failedVO = new RealTimeMonitorVO();
        failedVO.setType(DelayEnum.FAILED);
        failedVO.setMonitorList(new ArrayList<>());
        result.add(failedVO);


        List<Long> timeList = ZileanTimeUtil.getTimeBefore5Second();
        ListIterator<Long> timeListIterator = timeList.listIterator();
        long start;
        long end = 0;

        while (dataListIterator.hasNext()) {
            ZileanJobDO dataNext = dataListIterator.next();

            while (timeListIterator.hasNext()) {
                if (!timeListIterator.hasPrevious()) {
                    // 是第一次
                    end = timeListIterator.next();
                    continue;
                } else {
                    // 不是第一次
                    start = timeListIterator.next();
                }

                if (dataNext.getCreateTime() >= start && dataNext.getCreateTime() < end) {
                    RealTimeMonitorVO.Monitor monitor = new RealTimeMonitorVO.Monitor();
                    monitor.setNum(1L);
                    monitor.setTime(ZileanTimeUtil.getHourMinuteSecondByCreateTime(start));

                    boolean addDelayed = false;
                    boolean addReady = false;
                    boolean addSuccess = false;
                    boolean addFailed = false;
                    if (dataNext.getStatus() == StatusEnum.JOB_STATUS_DELAYED.getStatus()) {
                        delayedVO.getMonitorList().add(monitor);
                        addDelayed = true;
                    } else if (dataNext.getStatus() == StatusEnum.JOB_STATUS_READY.getStatus()) {
                        readyVO.getMonitorList().add(monitor);
                        addReady = true;
                    } else if (dataNext.getStatus() == StatusEnum.JOB_STATUS_FINISH.getStatus()) {
                        successVO.getMonitorList().add(monitor);
                        addSuccess = true;
                    } else if (dataNext.getStatus() == StatusEnum.JOB_STATUS_FAILED.getStatus()) {
                        failedVO.getMonitorList().add(monitor);
                        addFailed = true;
                    } else {
                        // TODO: 2019-07-15 error
                    }


                    while (dataListIterator.hasNext()) {
                        ZileanJobDO next = dataListIterator.next();
                        if (next.getCreateTime() >= start && next.getCreateTime() < end) {
                            // 下一个依然在，但是两个数据可能是状态不一致的，所以需要进行判断
                            if (dataNext.getStatus() == StatusEnum.JOB_STATUS_DELAYED.getStatus()) {
                                addDelayed = isAddDelayed(delayedVO, start, monitor, addDelayed);
                            } else if (dataNext.getStatus() == StatusEnum.JOB_STATUS_READY.getStatus()) {
                                addReady = isAddDelayed(readyVO, start, monitor, addReady);
                            } else if (dataNext.getStatus() == StatusEnum.JOB_STATUS_FINISH.getStatus()) {
                                addSuccess = isAddDelayed(successVO, start, monitor, addSuccess);
                            } else if (dataNext.getStatus() == StatusEnum.JOB_STATUS_FAILED.getStatus()) {
                                addFailed = isAddDelayed(failedVO, start, monitor, addFailed);
                            } else {
                                // TODO: 2019-07-15 error
                            }
                        } else {
                            // 下一个不在了
                            dataNext = next;
                            break;
                        }
                    }
                } else {
                    // 不在时间范围，下一个时间
                    RealTimeMonitorVO.Monitor monitor = new RealTimeMonitorVO.Monitor();
                    monitor.setNum(0L);
                    monitor.setTime(ZileanTimeUtil.getHourMinuteSecondByCreateTime(start));

                    delayedVO.getMonitorList().add(monitor);
                    readyVO.getMonitorList().add(monitor);
                    successVO.getMonitorList().add(monitor);
                    failedVO.getMonitorList().add(monitor);
                }

                end = start;
            }
        }

//        result.add(new RealTimeMonitorVO() {{
//            setType(DelayEnum.DELAYED);
//            setMonitorList(new ArrayList<Monitor>() {{
//                for (int i = 0; i < 100; i++) {
//
//                    add(new Monitor() {{
//                        setTime("123");
//                        setNum(redissonClient.getAtomicLong(VISIT_KEY).get());
//                    }});
//                }
//            }});
//        }});
//
//        result.add(new RealTimeMonitorVO() {{
//            setType(DelayEnum.READY);
//            setMonitorList(new ArrayList<Monitor>() {{
//                for (int i = 0; i < 100; i++) {
//
//                    int finalI = i;
//                    add(new Monitor() {{
//                        setTime("10:20:30" + finalI);
//                        setNum(100L);
//                    }});
//                    add(new Monitor() {{
//                        setTime("10:20:35" + finalI);
//                        setNum(200L);
//                    }});
//                    add(new Monitor() {{
//                        setTime("10:20:40" + finalI);
//                        setNum(1000L);
//                    }});
//                }
//            }});
//        }});
//
//        result.add(new RealTimeMonitorVO() {{
//            setType(DelayEnum.SUCCESS);
//            setMonitorList(new ArrayList<Monitor>() {{
//
//                add(new Monitor() {{
//                    setTime("10:20:30");
//                    setNum(100L);
//                }});
//                add(new Monitor() {{
//                    setTime("10:20:35");
//                    setNum(200L);
//                }});
//                add(new Monitor() {{
//                    setTime("10:20:40");
//                    setNum(1000L);
//                }});
//            }});
//        }});
//
//        result.add(new RealTimeMonitorVO() {{
//            setType(DelayEnum.FAILED);
//            setMonitorList(new ArrayList<Monitor>() {{
//
//                add(new Monitor() {{
//                    setTime("10:20:30");
//                    setNum(100L);
//                }});
//                add(new Monitor() {{
//                    setTime("10:20:35");
//                    setNum(200L);
//                }});
//                add(new Monitor() {{
//                    setTime("10:20:40");
//                    setNum(1000L);
//                }});
//            }});
//        }});

        // 反转list
        result.forEach(it -> Collections.reverse(it.getMonitorList()));
        return ZileanResponse.success(result);
    }

    private boolean isAddDelayed(RealTimeMonitorVO delayedVO, long start, RealTimeMonitorVO.Monitor monitor, boolean addDelayed) {
        RealTimeMonitorVO.Monitor tmpMonitor;
        if (addDelayed) {
            tmpMonitor = delayedVO.getMonitorList().get(delayedVO.getMonitorList().size() - 1);
            tmpMonitor.setNum(tmpMonitor.getNum() + 1);
        } else {
            tmpMonitor = new RealTimeMonitorVO.Monitor();
            tmpMonitor.setNum(1L);
            tmpMonitor.setTime(ZileanTimeUtil.getHourMinuteSecondByCreateTime(start));
            delayedVO.getMonitorList().add(monitor);
            addDelayed = true;
        }
        return addDelayed;
    }


    /**
     * 首页监控实时数据
     *
     * @return ZileanResponse
     */
    @GetMapping("/realTimeMonitor")
    public ZileanResponse realTimeMonitor() {
        String curTime = ZileanTimeUtil.getCurHourMinuteSecond();
        List<RealTimeMonitorVO> result = new ArrayList<>();

        RedissonClient redissonClient = RedissonUtil.getRedissonClient();

        result.add(new RealTimeMonitorVO() {{
            setType(DelayEnum.DELAYED);
            setMonitorList(new ArrayList<Monitor>() {{
                add(new Monitor() {{
                    setTime(curTime);
                    setNum(redissonClient.getAtomicLong(TODAY_DELAYED_KEY).get());
                }});
            }});
        }});
        result.add(new RealTimeMonitorVO() {{
            setType(DelayEnum.READY);
            setMonitorList(new ArrayList<Monitor>() {{
                add(new Monitor() {{
                    setTime(curTime);
                    setNum(redissonClient.getAtomicLong(TODAY_READY_KEY).get());
                }});
            }});
        }});

        result.add(new RealTimeMonitorVO() {{
            setType(DelayEnum.SUCCESS);
            setMonitorList(new ArrayList<Monitor>() {{
                add(new Monitor() {{
                    setTime(curTime);
                    setNum(redissonClient.getAtomicLong(TODAY_SUCCESS_KEY).get());
                }});
            }});
        }});

        result.add(new RealTimeMonitorVO() {{
            setType(DelayEnum.FAILED);
            setMonitorList(new ArrayList<Monitor>() {{
                add(new Monitor() {{
                    setTime(curTime);
                    setNum(redissonClient.getAtomicLong(TODAY_FAILED_KEY).get());
                }});
            }});
        }});
        return ZileanResponse.success(result);
    }

    @GetMapping("/job")
    public ZileanResponse getJob(String id) {
        RedissonClient redissonClient = RedissonUtil.getRedissonClient();
        RScoredSortedSet<Object> scoredSortedSet = redissonClient.getScoredSortedSet("hello");
        return ZileanResponse.success(scoredSortedSet.first());
    }

    @GetMapping("/jobList")
    public ZileanResponse jobList(int page, int limit) {
        List<AdminDelayedJobListVO> data = new ArrayList<>();
        data.add(new AdminDelayedJobListVO() {{
            setJobId("1");
            setDelay(200);
            setToken("token");
            setCallBack("https://www.27wy.cn");
            setHeader("");
            setBody("body");
            setTtr(10000L);
        }});
        data.add(new AdminDelayedJobListVO() {{
            setJobId("1");
            setDelay(200);
            setToken("token");
            setCallBack("https://www.27wy.cn");
            setHeader("");
            setBody("body");
            setTtr(10000L);
        }});
        data.add(new AdminDelayedJobListVO() {{
            setJobId("1");
            setDelay(200);
            setToken("token");
            setCallBack("https://www.27wy.cn");
            setHeader("");
            setBody("body");
            setTtr(10000L);
        }});
        data.add(new AdminDelayedJobListVO() {{
            setJobId("1");
            setDelay(200);
            setToken("token");
            setCallBack("https://www.27wy.cn");
            setHeader("");
            setBody("body");
            setTtr(10000L);
        }});
        data.add(new AdminDelayedJobListVO() {{
            setJobId("1");
            setDelay(200);
            setToken("token");
            setCallBack("https://www.27wy.cn");
            setHeader("");
            setBody("body");
            setTtr(10000L);
        }});
        data.add(new AdminDelayedJobListVO() {{
            setJobId("1");
            setDelay(200);
            setToken("token");
            setCallBack("https://www.27wy.cn");
            setHeader("");
            setBody("body");
            setTtr(10000L);
        }});
        data.add(new AdminDelayedJobListVO() {{
            setJobId("1");
            setDelay(200);
            setToken("token");
            setCallBack("https://www.27wy.cn");
            setHeader("");
            setBody("body");
            setTtr(10000L);
        }});
        data.add(new AdminDelayedJobListVO() {{
            setJobId("1");
            setDelay(200);
            setToken("token");
            setCallBack("https://www.27wy.cn");
            setHeader("");
            setBody("body");
            setTtr(10000L);
        }});
        data.add(new AdminDelayedJobListVO() {{
            setJobId("1");
            setDelay(200);
            setToken("token");
            setCallBack("https://www.27wy.cn");
            setHeader("");
            setBody("body");
            setTtr(10000L);
        }});
        data.add(new AdminDelayedJobListVO() {{
            setJobId("1");
            setDelay(200);
            setToken("token");
            setCallBack("https://www.27wy.cn");
            setHeader("");
            setBody("body");
            setTtr(10000L);
        }});
        data.add(new AdminDelayedJobListVO() {{
            setJobId("1");
            setDelay(200);
            setToken("token");
            setCallBack("https://www.27wy.cn");
            setHeader("");
            setBody("body");
            setTtr(10000L);
        }});
        long count = 1000;
        ZileanPageResponse success = ZileanPageResponse.success(count, data);
        return success;
    }


//    @GetMapping("/jobList")
//    public String jobList(int page, int limit) {
//        Map result = new HashMap<>();
//
//        List data = new ArrayList<>();
//
//        for (int i = 0; i < limit; i++) {
//            int finalI = i;
//            data.add(new HashMap<String, Object>() {{
//                put("city", "城市-10");
//                put("classify", "诗人");
//                put("experience", finalI);
//                put("id", finalI);
//                put("logins", finalI);
//                put("score", finalI);
//                put("sex222", "女");
//                put("sign", "签名-10");
//                put("username111", "user-10");
//                put("wealth", 71294671);
//            }});
//        }
//
//
//        result.put("code", 0);
//        result.put("count", 1000);
//        result.put("data", data);
//        result.put("msg", "");
//        return JSON.toJSONString(result);
//    }
}
