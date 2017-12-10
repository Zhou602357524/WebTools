package com.xwtec.tools.core.service.pushtools.impl;

import com.xwtec.tools.core.entity.PushParams;
import com.xwtec.tools.core.entity.ResultMsg;
import com.xwtec.tools.core.entity.ResultStatusCode;
import com.xwtec.tools.core.entity.UserInfoEntity;
import com.xwtec.tools.core.repository.PushRepository;
import com.xwtec.tools.core.service.pushtools.PushService;
import com.xwtec.tools.core.utils.io.IOUtils;
import com.xwtec.tools.core.utils.ziptools.ZipTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
    private final PushRepository pushRepository;
    private final String ENTER;
    private final int MAX_NUMBER;

    @Autowired
    public PushServiceImpl(PushRepository pushRepository) {
        this.pushRepository = pushRepository;
        BASE_NAME = "push_tool";
        CHINA_MOBILE_NUMBER = "^((13[4-9])|(147)|(15[0-2,7-9])|(17[8])|(18[2-4,7-8]))\\d{8}|(170[5])\\d{7}|(198)\\d{8}$";
        BASE_PATH = getClass().getResource("/").getPath() + "temp/" + BASE_NAME;
        zipTools = new ZipTools(BASE_NAME);
        String os = System.getProperty("os.name");
        MAX_NUMBER = 1000;
        if (os.toLowerCase().startsWith("win"))
            ENTER = "\r\n";
        else
            ENTER = "\n";
    }

    public void insertPhoneNumbers(List<UserInfoEntity> numbers) {
        int size = numbers.size();
        int i = size / MAX_NUMBER;
        if (size % MAX_NUMBER != 0)
            i = i + 1;
        if (i > 0) {
            int offset = 0;
            for (int i1 = 0; i1 < i; i1++) {
                if (i1 == i - 1) {
                    pushRepository.insertPhoneNumbers(numbers.subList(offset, size));
                    break;
                }
                pushRepository.insertPhoneNumbers(numbers.subList(offset, offset + MAX_NUMBER));
                offset += MAX_NUMBER;
            }
        } else {
            pushRepository.insertPhoneNumbers(numbers);
        }
    }

    @Override
    public int getCount() {
        return pushRepository.selectCount();
    }

    private void truncate() {
        pushRepository.truncate();
    }

    @Override
    public ResultMsg compressedFiles(MultipartFile sourceData, PushParams pushParams, HttpServletResponse response) {

        String zipPath = null;
        ResultStatusCode result;
        try {
            String originalFilename = sourceData.getOriginalFilename();
            BufferedReader reader = new BufferedReader(new InputStreamReader(sourceData.getInputStream()));
            Set<UserInfoEntity> strSet = new HashSet<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String phone = line.trim();
                boolean matches = Pattern.matches(CHINA_MOBILE_NUMBER, phone);
                if (matches) {
                    UserInfoEntity userInfoEntity = new UserInfoEntity();
                    userInfoEntity.setPhone(phone);
                    strSet.add(userInfoEntity);
                }
            }
            List<UserInfoEntity> userInfoEntities = new ArrayList<>(strSet);
            pushRepository.insertPhoneNumbers(userInfoEntities);
            userInfoEntities = selectVersion(pushParams);

            ByteArrayOutputStream[] byteOutArr = getByteArrayOutputStreams(pushParams,userInfoEntities);

            zipPath = BASE_PATH + "_" + UUID.randomUUID().toString().replace("-", "") + ".zip";
            zipTools.zipByteOutArr(zipPath, byteOutArr, originalFilename);
            result = IOUtils.responseWrite(response, zipPath);
        } catch (IOException e) {
            e.printStackTrace();
            result = ResultStatusCode.SYSTEM_ERR;
        } finally {
            truncate();
        }
        return new ResultMsg(result.getErrcode(),result.getErrmsg());
    }

    private ByteArrayOutputStream[] getByteArrayOutputStreams(PushParams pushParams, List<UserInfoEntity> userInfoEntities) throws IOException {
        int splitNumber = pushParams.getSplitNumber();
        ByteArrayOutputStream[] byteOutArr = new ByteArrayOutputStream[splitNumber];
        int size = userInfoEntities.size() / splitNumber;
        int offset = 0;
        for (int i = 0; i < splitNumber; i++) {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            int i1 = offset + size;
            if (i == splitNumber - 1)
                i1 = userInfoEntities.size();
            for (int j = offset; j < i1; j++) {
                String content = "";
                UserInfoEntity userInfoEntity = userInfoEntities.get(j);
                if (pushParams.isShow_phone() && pushParams.isShow_msgid()) {
                     content = (userInfoEntity.getPhone()) + "\t" + (userInfoEntity.getMsgid());
                } else if (pushParams.isShow_phone()){
                    content = userInfoEntity.getPhone();
                } else if (pushParams.isShow_msgid()){
                    content = userInfoEntity.getMsgid();
                }
                if (j != i1 - 1)
                    content = content + ENTER;
                byteOut.write(content.getBytes());
            }
            offset += size;
            byteOutArr[i] = byteOut;
        }
        return byteOutArr;
    }

    private List<UserInfoEntity> selectVersion(PushParams pushParams) {

        List<UserInfoEntity> list = new ArrayList<>();

        if (pushParams.isVersion_android()) {
            if (pushParams.isShow_msgid() && pushParams.isShow_phone())
                list.addAll(pushRepository.queryAndroidPhoneAndMsgid());
            else if (pushParams.isShow_phone())
                list.addAll(pushRepository.queryAndroidPhone());
            else if (pushParams.isShow_msgid())
                list.addAll(pushRepository.queryAndroidMsgid());
        }
        if (pushParams.isVersion_ios()) {
            if (pushParams.isShow_msgid() && pushParams.isShow_phone())
                list.addAll(pushRepository.queryIOSPhoneAndMsgid());
            else if (pushParams.isShow_phone())
                list.addAll(pushRepository.queryIOSPhone());
            else if (pushParams.isShow_msgid())
                list.addAll(pushRepository.queryIOSMsgid());
        }

        return list;
    }

}
