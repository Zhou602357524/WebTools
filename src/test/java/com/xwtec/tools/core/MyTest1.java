package com.xwtec.tools.core;

import com.csvreader.CsvReader;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class MyTest1 {




    @Test
    public void sbTest() {
        try
                (Writer out = new BufferedWriter(new FileWriter("E:\\test.txt"))) {
            StringBuilder sb1 = new StringBuilder();
            StringBuffer sb2 = new StringBuffer();
            StopWatch timer = new StopWatch();
            long flag = 13000000000L;
            List<Long> list = new LinkedList();
            for (int i = 0; i < 1000000; i++) {
                list.add(++flag);
            }
            timer.start();
            //String s = list.parallelStream().map(String::valueOf).collect(Collectors.joining("\r\n"));
            list.stream().forEachOrdered(a -> sb2.append(a).append("\r\n"));
            out.write(sb2.toString());
            timer.stop();
            System.out.println(timer.getTotalTimeSeconds());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2(){
        try {
            Stream<FileInputStream> stream = Stream.of(new FileInputStream(""));
            List<String> MyList = stream.map(e -> {
                List<String> list = new LinkedList<>();
                try (BufferedReader reader = new BufferedReader(new FileReader(""))) {
                    String line;
                    while ((line = reader.readLine()) != null)
                        list.add(line);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                return list;
            }).reduce(new LinkedList<String>(), (a, b) -> {
                a.addAll(b);
                return a;
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() throws IOException {
        String url = "C:\\Users\\Administrator\\Desktop\\潜在宽带客户且掌厅注册活跃用户12月数据\\潜在宽带客户且掌厅注册活跃用户12月数据\\CI_CUSER_20180125143337936.csv";
        try {
            // 用来保存数据
            ArrayList<String[]> csvFileList = new ArrayList<String[]>();
            // 定义一个CSV路径
            String csvFilePath = url;
            // 创建CSV读对象 例如:CsvReader(文件路径，分隔符，编码格式);
            CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName("UTF-8"));
            // 跳过表头 如果需要表头的话，这句可以忽略
            reader.readHeaders();
            // 逐行读入除表头的数据
            while (reader.readRecord()) {
                System.out.println(reader.getRawRecord());
                csvFileList.add(reader.getValues());
            }
            reader.close();

            // 遍历读取的CSV文件
            for (int row = 0; row < csvFileList.size(); row++) {
                // 取得第row行第0列的数据
                String cell = csvFileList.get(row)[0];
                System.out.println("------------>"+cell);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {


    }

    private class MyString {

        private final char[] value ;

        public MyString() {
            this.value = new char[0];
            MyTest1 myTest1 = new MyTest1();
            myTest1.aa();
        }


    }

    public void aa() {
        System.out.println(this);
    }
}
