package com.xwtec.tools.core;

import com.xwtec.tools.core.repository.PushRepository;
import com.xwtec.tools.core.service.pushtools.PushService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
    @Autowired
    private PushRepository pushRepository;
    @Autowired
    private PushService pushService;
    @Test
    //@Transactional
    public void contextLoads() {

    }

    @Test
    public void insert() throws Exception {
        ArrayList<String> arr = new ArrayList<>();
        for (int i = 1; i < 100000; i++) {

            arr.add(String.valueOf(i));
        }

        pushService.insertPhoneNumbers(arr);
    }

    @Test
    public void truncate() {
        System.out.println("===========操作系统是:"+System.getProperties().getProperty("os.name"));

        pushService.truncate();
    }

}
