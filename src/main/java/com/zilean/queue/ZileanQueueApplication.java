package com.zilean.queue;

import com.zilean.queue.handler.ZileanBucketHandler;
import com.zilean.queue.handler.ZileanInitHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 描述:启动类
 *
 * @author xjh
 * created on 2019-06-28 15:34
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
public class ZileanQueueApplication implements CommandLineRunner {

    private final ZileanInitHandler zileanInitHandler;
    private final ZileanBucketHandler zileanBucketHandler;

    @Autowired
    public ZileanQueueApplication(ZileanInitHandler zileanInitHandler, ZileanBucketHandler zileanBucketHandler) {
        this.zileanInitHandler = zileanInitHandler;
        this.zileanBucketHandler = zileanBucketHandler;
    }

    public static void main(String[] args) {
        new SpringApplication(ZileanQueueApplication.class).run(args);
    }

    @Override
    public void run(String... args) {
        // 同步初始化
        zileanInitHandler.run();
        // 异步监听
        zileanBucketHandler.run();
    }
}
