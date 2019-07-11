package com.zilean.queue.controller;

import com.zilean.queue.domain.response.ZileanPageResponse;
import com.zilean.queue.domain.response.ZileanResponse;
import com.zilean.queue.domain.vo.AdminDelayedJobListVO;
import com.zilean.queue.domain.vo.AdminIndexVO;
import com.zilean.queue.domain.vo.RealTimeMonitorVO;
import com.zilean.queue.enums.ZileanEnum;
import com.zilean.queue.redis.RedissonUtil;
import com.zilean.queue.util.DelayUtil;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.zilean.queue.constant.RedisConstant.TODAY_DELAYED_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_DELAYED_TOTAL_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_FAILED_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_FAILED_TOTAL_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_READY_KEY;
import static com.zilean.queue.constant.RedisConstant.TODAY_READY_TOTAL_KEY;
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
    // TODO: 2019-07-03 管理端接口,增删改查

    /**
     * 首页监控数据
     *
     * @return ZileanResponse
     */
    @GetMapping("/index")
    public ZileanResponse index() {
        // TODO: 2019-07-11 访问该接口新增访问量


        AdminIndexVO result = new AdminIndexVO();
        RedissonClient redissonClient = RedissonUtil.getRedissonClient();

        // 设置今日访问量、访问总量
        // TODO: 2019-07-11 init 的时候需要判断访问量的值，如果不存在需要给访问量设置值
        result.setCurVisitNumber(redissonClient.getAtomicLong(VISIT_KEY).get());
        result.setVisitNumberTotal(redissonClient.getAtomicLong(VISIT_TOTAL_KEY).get());

        // 设置今日延迟数、延迟总数
        // TODO: 2019-07-11 任务进入延迟队列需要记录当日延迟数，且记录到总数
        result.setCurDelayed(redissonClient.getAtomicLong(TODAY_DELAYED_KEY).get());
        result.setCurDelayedTotal(redissonClient.getAtomicLong(TODAY_DELAYED_TOTAL_KEY).get());

        // 设置今日读取数、读取总数
        // TODO: 2019-07-11 这种情况可能会重复，从失败队列绕了一圈回来，进入读取队列
        // TODO: 2019-07-11 将任务放到读取队列的时候，需要记录到读取数，以及读取总数
        result.setCurReady(redissonClient.getAtomicLong(TODAY_READY_KEY).get());
        result.setCurReadyTotal(redissonClient.getAtomicLong(TODAY_READY_TOTAL_KEY).get());


        // 设置今日失败数、失败总数
        // TODO: 2019-07-11 失败队列是否需要考虑三级失败队列？
        // TODO: 2019-07-11 可能会有重复进入
        // TODO: 2019-07-11 进入失败队列记录失败数、失败总数
        result.setCurFailed(redissonClient.getAtomicLong(TODAY_FAILED_KEY).get());
        result.setCurFailedTotal(redissonClient.getAtomicLong(TODAY_FAILED_TOTAL_KEY).get());


        // 设置历史数据
        // TODO: 2019-07-11 查询数据库
        // TODO: 2019-07-11 定时任务将每日数据记录到数据库


//        result.setCurVisitNumber(112233L);
//        result.setVisitNumberTotal(112233L);
        result.setCurDelayed(112233L);
        result.setCurDelayedTotal(112233L);
        result.setCurReady(112233L);
        result.setCurReadyTotal(112233L);
        result.setCurFailed(112233L);
        result.setCurFailedTotal(112233L);
        result.setHistoryData(new ArrayList<AdminIndexVO.DailyReport>() {{
            add(new AdminIndexVO.DailyReport() {{
                setVisit(100L);
                setDelayed(200L);
                setReady(300L);
                setFailed(400L);
                setTime("2019/01/01");
            }});
            add(new AdminIndexVO.DailyReport() {{
                setVisit(200L);
                setDelayed(300L);
                setReady(400L);
                setFailed(500L);
                setTime("2019/01/02");
            }});
            add(new AdminIndexVO.DailyReport() {{
                setVisit(300L);
                setDelayed(400L);
                setReady(500L);
                setFailed(600L);
                setTime("2019/01/03");
            }});
        }});
        return ZileanResponse.success(result);
    }

    /**
     * 首页监控历史数据
     *
     * @return ZileanResponse
     */
    @GetMapping("/realTimeMonitorHistory")
    public ZileanResponse realTimeMonitorHistory() {

        // TODO: 2019-07-11 给出1个月的数据即可

        List<RealTimeMonitorVO> result = new ArrayList<>();
        result.add(new RealTimeMonitorVO() {{
            setType(ZileanEnum.DELAYED);
            setMonitorList(new ArrayList<Monitor>() {{
                for (int i = 0; i < 100; i++) {
                    int finalI = i;

                    add(new Monitor() {{
                        setTime("10:20:50" + finalI);
                        setNum(900L);
                    }});
                    add(new Monitor() {{
                        setTime("10:20:55" + finalI);
                        setNum(200L);
                    }});
                    add(new Monitor() {{
                        setTime("10:20:50" + finalI);
                        setNum(10000L);
                    }});
                }
            }});
        }});

        result.add(new RealTimeMonitorVO() {{
            setType(ZileanEnum.READY);
            setMonitorList(new ArrayList<Monitor>() {{
                for (int i = 0; i < 100; i++) {

                    int finalI = i;
                    add(new Monitor() {{
                        setTime("10:20:30" + finalI);
                        setNum(100L);
                    }});
                    add(new Monitor() {{
                        setTime("10:20:35" + finalI);
                        setNum(200L);
                    }});
                    add(new Monitor() {{
                        setTime("10:20:40" + finalI);
                        setNum(1000L);
                    }});
                }
            }});
        }});

        result.add(new RealTimeMonitorVO() {{
            setType(ZileanEnum.SUCCESS);
            setMonitorList(new ArrayList<Monitor>() {{

                add(new Monitor() {{
                    setTime("10:20:30");
                    setNum(100L);
                }});
                add(new Monitor() {{
                    setTime("10:20:35");
                    setNum(200L);
                }});
                add(new Monitor() {{
                    setTime("10:20:40");
                    setNum(1000L);
                }});
            }});
        }});

        result.add(new RealTimeMonitorVO() {{
            setType(ZileanEnum.FAILED);
            setMonitorList(new ArrayList<Monitor>() {{

                add(new Monitor() {{
                    setTime("10:20:30");
                    setNum(100L);
                }});
                add(new Monitor() {{
                    setTime("10:20:35");
                    setNum(200L);
                }});
                add(new Monitor() {{
                    setTime("10:20:40");
                    setNum(1000L);
                }});
            }});
        }});
        return ZileanResponse.success(result);
    }


    /**
     * 首页监控实时数据
     *
     * @return ZileanResponse
     */
    @GetMapping("/realTimeMonitor")
    public ZileanResponse realTimeMonitor() {
        String curTime = DelayUtil.getCurHourMinuteSecond();
        List<RealTimeMonitorVO> result = new ArrayList<>();
        result.add(new RealTimeMonitorVO() {{
            setType(ZileanEnum.DELAYED);
            setMonitorList(new ArrayList<Monitor>() {{
                add(new Monitor() {{
                    setTime(curTime);
                    setNum(100L);
                }});
            }});
        }});
        result.add(new RealTimeMonitorVO() {{
            setType(ZileanEnum.READY);
            setMonitorList(new ArrayList<Monitor>() {{
                add(new Monitor() {{
                    setTime(curTime);
                    setNum(200L);
                }});
            }});
        }});

        result.add(new RealTimeMonitorVO() {{
            setType(ZileanEnum.SUCCESS);
            setMonitorList(new ArrayList<Monitor>() {{
                add(new Monitor() {{
                    setTime(curTime);
                    setNum(300L);
                }});
            }});
        }});

        result.add(new RealTimeMonitorVO() {{
            setType(ZileanEnum.FAILED);
            setMonitorList(new ArrayList<Monitor>() {{
                add(new Monitor() {{
                    setTime(curTime);
                    setNum(400L);
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
