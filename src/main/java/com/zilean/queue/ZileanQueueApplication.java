package com.zilean.queue;

import com.zilean.queue.listener.ZileanApplicationListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 描述:启动类
 *
 * @author xjh
 * created on 2019-06-28 15:34
 */
@SpringBootApplication
@EnableTransactionManagement
public class ZileanQueueApplication {

    // TODO: 2019-07-05 init data

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ZileanQueueApplication.class);
        application.addListeners(new ZileanApplicationListener());
        application.run(args);
    }
}
