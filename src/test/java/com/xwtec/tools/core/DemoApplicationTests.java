package com.xwtec.tools.core;

import com.xwtec.tools.core.repository.PushRepository;
import com.xwtec.tools.core.service.pushtools.PushService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
