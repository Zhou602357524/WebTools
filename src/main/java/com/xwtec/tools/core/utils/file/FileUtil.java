package com.xwtec.tools.core.utils.file;

import com.csvreader.CsvReader;
import com.xwtec.tools.core.entity.PushParams;
import com.xwtec.tools.core.entity.UserInfoEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class FileUtil {

    private final static String CHINA_MOBILE_NUMBER = "^((13[4-9])|(147)|(15[0-2,7-9])|(17[8])|(18[2-4,7-8]))\\d{8}|(170[5])\\d{7}|(198)\\d{8}$";

    public static Set<UserInfoEntity> readCSV(InputStream inputStream) {
        Set<UserInfoEntity> csvFileList = new HashSet<>();
        CsvReader reader = null;
        try {
           /* // 定义一个CSV路径
            String csvFilePath = url;*/
            // 创建CSV读对象 例如:CsvReader(文件路径，分隔符，编码格式);
            reader = new CsvReader(inputStream, ',', Charset.forName("UTF-8"));
            // 跳过表头 如果需要表头的话，这句可以忽略
            reader.readHeaders();
            // 逐行读入除表头的数据
            while (reader.readRecord()) {
                String phone = reader.getValues()[0].trim();
                add(csvFileList,phone);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                reader.close();
        }
        return csvFileList;
    }

    public static Set<UserInfoEntity> readTxt(InputStream inputStream, PushParams params) {
        Set<UserInfoEntity> strSet = new HashSet<>();
        try
                (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (params.getSeparator().equals("0")) {
                    String phone = line.trim();
                    add(strSet, phone);
                }else {
                    String[] phones = line.split(",");
                    for (String phone : phones) {
                        add(strSet,phone.trim());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strSet;
    }

    private static void add(Set<UserInfoEntity> set,String phone){
        boolean matches = Pattern.matches(CHINA_MOBILE_NUMBER, phone);
        if (matches) {
            UserInfoEntity userInfoEntity = new UserInfoEntity();
            userInfoEntity.setPhone(phone);
            set.add(userInfoEntity);
        }
    }
}
