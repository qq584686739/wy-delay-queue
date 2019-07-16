package com.zilean.queue.controller;

import com.zilean.queue.domain.response.ZileanResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZileanAdminControllerTest {

    @Autowired
    private ZileanAdminController zileanAdminController;

    @Test
    public void index() {
        ZileanResponse index = zileanAdminController.index();
    }

    @Test
    public void realTimeMonitorHistory() {
    }

    @Test
    public void realTimeMonitor() {
    }

    @Test
    public void getJob() {
    }

    @Test
    public void jobList() {
    }
}