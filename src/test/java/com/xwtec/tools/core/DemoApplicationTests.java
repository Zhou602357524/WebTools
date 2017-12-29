package com.xwtec.tools.core;

import com.xwtec.tools.core.repository.PushRepository;
import com.xwtec.tools.core.service.pushtools.PushService;
import com.xwtec.tools.core.web.PushController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    @Autowired
    private PushRepository pushRepository;
    @Autowired
    private PushService pushService;
    @Autowired
    private PushController pushController;
    @Test
    //@Transactional
    public void contextLoads() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Thread.currentThread().sleep(1000);
        stopWatch.stop();
        stopWatch.getTotalTimeSeconds();
    }

    @Test
    public void insert() throws Exception {
        List<String> arr = new ArrayList<>();
        for (int i = 0; i < 100000; i++) {

            arr.add(String.valueOf(i));
        }
        long start = System.currentTimeMillis();
        //pushService.insertPhoneNumbers(arr);
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void truncate() {
        System.out.println("===========操作系统是:"+System.getProperties().getProperty("os.name"));

        //pushService.truncate();
    }


}
