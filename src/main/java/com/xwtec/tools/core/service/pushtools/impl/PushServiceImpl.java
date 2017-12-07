package com.xwtec.tools.core.service.pushtools.impl;

import com.xwtec.tools.core.repository.PushRepository;
import com.xwtec.tools.core.service.pushtools.PushService;
import com.xwtec.tools.core.utils.ziptools.ZipTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zc
 * \* Date: 2017/12/1 0001
 * \* Time: 上午 10:37
 * \* Description:
 * \ push业务处理实现类
 */
@Service
@Transactional
public class PushServiceImpl implements PushService {

    private final ZipTools zipTools;
    private final String BASE_NAME;
    private final String BASE_PATH;
    private final String CHINA_MOBILE_NUMBER;
    private PushRepository pushRepository;
    private final String ENTER;

    @Override
    public void insertPhoneNumbers(List<String> numbers) {
        pushRepository.insertPhoneNumbers(numbers);

    }

    @Override
    public void truncate() {
        pushRepository.truncate();
    }

    @Autowired
    public PushServiceImpl(PushRepository pushRepository) {
        this.pushRepository = pushRepository;
        BASE_NAME  = "push_tool";
        CHINA_MOBILE_NUMBER  = "^((13[4-9])|(147)|(15[0-2,7-9])|(17[8])|(18[2-4,7-8]))\\d{8}|(170[5])\\d{7}|(198)\\d{8}$";
        BASE_PATH = getClass().getResource("/").getPath() + "temp/" + BASE_NAME;
        zipTools = new ZipTools(BASE_NAME);
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win"))
            ENTER = "\r\n";
        else
            ENTER = "\n";
    }

    @Override
    public String compressedFiles(MultipartFile sourceData, int splitNumber) {

        String zipPath = null;
        try {
            String originalFilename = sourceData.getOriginalFilename();

            BufferedReader reader = new BufferedReader(new InputStreamReader(sourceData.getInputStream()));
            Set<String> strSet = new HashSet<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String content = line.trim();
                boolean matches = Pattern.matches(CHINA_MOBILE_NUMBER,content);
                if (matches)
                    strSet.add(content);
            }

            List<String> strList = new ArrayList<>(strSet);
            int size = strList.size() / splitNumber;
            int offset = 0;
            ByteArrayOutputStream[] byteOutArr = new ByteArrayOutputStream[splitNumber];
            for (int i = 0; i < splitNumber; i++) {
                ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                int i1 = offset + size;
                if (i == splitNumber - 1)
                    i1 = strList.size();
                for (int j = offset; j < i1; j++) {
                    String content = strList.get(j);
                    if (j != i1 - 1)
                        content = content + ENTER;
                    byteOut.write(content.getBytes());
                }
                offset += size;
                byteOutArr[i] = byteOut;
            }
            zipPath = BASE_PATH + "_" + UUID.randomUUID().toString().replace("-","") + ".zip";
            zipTools.zipByteOutArr(zipPath, byteOutArr, originalFilename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zipPath;
    }
    private List<String> selectVersion(List<String> params,boolean ios,boolean android){

        List<String> list = new ArrayList<>();
        insertPhoneNumbers(params);
        if (ios){


        }
        return list;
    }
    private List<String> selectIOS(){

        return null;
    }

    private List<String> selectAndroid(){
        return null;
    }
}
