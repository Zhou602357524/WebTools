package com.xwtec.tools.core;

import org.junit.Test;
import org.springframework.util.StopWatch;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MyTest1 {


    @Test
    public void sbTest() {
        try
                (BufferedWriter out = new BufferedWriter(new FileWriter("E:\\test.txt"))) {
            //StringBuilder sb = new StringBuilder();
            StringBuffer sb = new StringBuffer();
            StopWatch timer = new StopWatch();
            long flag = 13000000000L;
            List<Long> list = new LinkedList();
            for (int i = 0; i < 10000000; i++) {
                list.add(++flag);
            }
            timer.start();
            //list.parallelStream().forEachOrdered(e -> sb.append(e).append("\r\n"));
            /*list.parallelStream().forEachOrdered(e -> {
                try {
                    out.write(e.toString() + "\r\n");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            });*/
            for (Long aLong : list) {
                out.write(aLong.toString() + "\r\n");
            }
            out.write(sb.toString());
            timer.stop();
            System.out.println(timer.getTotalTimeSeconds());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
