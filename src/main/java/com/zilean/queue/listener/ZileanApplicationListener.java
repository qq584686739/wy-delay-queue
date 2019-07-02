package com.zilean.queue.listener;

import com.zilean.queue.handler.ZileanBucketHandler;
import com.zilean.queue.util.ThreadPoolExecutorUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 描述:
 *
 * @author xjh
 * created on 2019-07-02 18:26
 */
public class ZileanApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ThreadPoolExecutorUtil.getDefaultSingleThreadPool().execute(new ZileanBucketHandler());
    }
}
