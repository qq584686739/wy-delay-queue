package com.zilean.queue;

import com.zilean.queue.handler.ZileanInitHandler;
import com.zilean.queue.listener.ZileanApplicationListener;
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

    @Autowired
    public ZileanQueueApplication(ZileanInitHandler zileanInitHandler) {
        this.zileanInitHandler = zileanInitHandler;
    }

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ZileanQueueApplication.class);
        application.addListeners(new ZileanApplicationListener());
        application.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        zileanInitHandler.run();
    }
}
