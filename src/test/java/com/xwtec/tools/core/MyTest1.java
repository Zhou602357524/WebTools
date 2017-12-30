package com.xwtec.tools.core;

import org.junit.Test;
import org.springframework.util.StopWatch;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MyTest1 {


    @Test
    public void sbTest() {
        try
                (Writer out = new BufferedWriter(new FileWriter("E:\\test.txt"))) {

            StopWatch timer = new StopWatch();
            long flag = 13000000000L;
            List<Long> list = new LinkedList();
            for (int i = 0; i < 10000000; i++) {
                list.add(++flag);
            }
            timer.start();
            String s = list.stream().map(String::valueOf).collect(Collectors.joining("\r\n"));
            out.write(s);
            timer.stop();
            System.out.println(timer.getTotalTimeSeconds());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
