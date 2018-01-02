package com.xwtec.tools.core.service.pushtools.impl;

import com.xwtec.tools.core.entity.PushParams;
import com.xwtec.tools.core.entity.ResultMsg;
import com.xwtec.tools.core.entity.ResultStatusCode;
import com.xwtec.tools.core.entity.UserInfoEntity;
import com.xwtec.tools.core.repository.PushRepository;
import com.xwtec.tools.core.service.pushtools.PushService;
import com.xwtec.tools.core.utils.io.IOUtils;
import com.xwtec.tools.core.utils.ziptools.ZipTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    private final Logger logger;
    private final ZipTools zipTools;
    private final String BASE_NAME;
    private final String BASE_PATH;
    private final String CHINA_MOBILE_NUMBER;
    private final PushRepository pushRepository;
    private final String ENTER;
    private final int MAX_NUMBER;

    @SuppressWarnings("all")
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
        logger = LoggerFactory.getLogger(this.getClass());
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

    public void truncate() {
        pushRepository.truncate();
    }

    @Override
    public ResultMsg compressedFiles(MultipartFile sourceData, PushParams pushParams, HttpServletResponse response) {

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
            List<UserInfoEntity> userInfoEntities = new ArrayList<>();
            insertPhoneNumbersBySqlLoader(strSet);
            Map<String,List<UserInfoEntity>> map = selectVersion(pushParams,strSet);
            if (map.get("ios") != null)
                userInfoEntities.addAll(map.get("ios"));
            if (map.get("android") != null)
                userInfoEntities.addAll(map.get("android"));
            ByteArrayOutputStream[] byteOutArr = getByteArrayOutputStreams(pushParams, userInfoEntities);

            String zipPath = BASE_PATH + "_" + UUID.randomUUID().toString().replace("-", "") + ".zip";
            zipTools.zipByteOutArr(zipPath, byteOutArr, originalFilename);
            result = IOUtils.responseWrite(response, zipPath);
        } catch (IOException e) {
            e.printStackTrace();
            result = ResultStatusCode.SYSTEM_ERR;
        } finally {
            truncate();
        }
        return new ResultMsg(result.getErrcode(), result.getErrmsg());
    }

    private Map<String,List<UserInfoEntity>> selectVersion(PushParams pushParams, Set<UserInfoEntity> userInfoEntities) {
        long startTime = System.currentTimeMillis();
        int size = userInfoEntities.size();
        userInfoEntities = null;
        Map<String,List<UserInfoEntity>> map = new HashMap<>(2);
        ForkJoinTask<List<UserInfoEntity>> androidTask = null;
        ForkJoinTask<List<UserInfoEntity>> IOSTask = null;
        if (pushParams.isVersion_android())
            androidTask = new PushParallelTask(pushParams, 0,size,VersionEnum.ANDROID, 100000).fork();
        if (pushParams.isVersion_ios())
            IOSTask = new PushParallelTask(pushParams, 0,size,VersionEnum.IOS, 100000).fork();
        if (androidTask != null)
            map.put("android",androidTask.join());
        if (IOSTask != null)
            map.put("ios",IOSTask.join());
        logger.info("selectVersion耗时====" + ((System.currentTimeMillis()-startTime)/1000.0));
        return map;
    }

    private class PushParallelTask extends RecursiveTask<List<UserInfoEntity>> {
        private final VersionEnum version;
        private PushParams pushParams;
        private int begin;
        private int end;
        private final int max;

        private PushParallelTask(PushParams pushParams, int begin, int end, VersionEnum version,int max) {
            this.pushParams = pushParams;
            this.begin = begin;
            this.end = end;
            this.version = version;
            this.max = max;
        }

        @Override
        protected List<UserInfoEntity> compute() {
            int total = end - begin;
            if (total < max){
                return process();
            }
            int average = total / 2;
            ForkJoinTask<List<UserInfoEntity>> leftTask = new PushParallelTask(pushParams,begin,begin + average,version,max).fork();

            ForkJoinTask<List<UserInfoEntity>> rightTask = new PushParallelTask(pushParams,begin + average, end,version,max).fork();

            List<UserInfoEntity> left = leftTask.join();
            List<UserInfoEntity> right = rightTask.join();
            left.addAll(right);
            return left;
        }

        private List<UserInfoEntity> process(){
            long startTime = System.currentTimeMillis();
            List<UserInfoEntity> list = null;
            if (version.equals(VersionEnum.ANDROID)) {
                list = queryAndroid();
            }else if (version.equals(VersionEnum.IOS)) {
                list = queryIos();
            }
            logger.info(Thread.currentThread().getName() + "...耗时=" + ((System.currentTimeMillis()-startTime)/1000.0));
            return list;
        }

        private List<UserInfoEntity> queryIos() {
            List<UserInfoEntity> list = null;
            if (pushParams.isShow_msgid() && pushParams.isShow_phone())
                list = pushRepository.queryIOSPhoneAndMsgid(begin,end);
            else if (pushParams.isShow_phone())
                list = pushRepository.queryIOSPhone(begin,end);
            else if (pushParams.isShow_msgid())
                list = pushRepository.queryIOSMsgid(begin,end);
            return list;
        }

        private List<UserInfoEntity> queryAndroid(){
            List<UserInfoEntity> list = null;
            if (pushParams.isShow_msgid() && pushParams.isShow_phone())
                list = pushRepository.queryAndroidPhoneAndMsgid(begin,end);
            else if (pushParams.isShow_phone())
                list = pushRepository.queryAndroidPhone(begin,end);
            else if (pushParams.isShow_msgid())
                list = pushRepository.queryAndroidMsgid(begin,end);
            return list;
        }
    }

    @Override
    public void insertPhoneNumbersBySqlLoader(Set<UserInfoEntity> entities) {
        File file = new File("/data/webapp/push_msgid/push_msgid.txt");
        try
                (Writer writer = new BufferedWriter(new FileWriter(file))) {
            String content = entities.stream().map(UserInfoEntity::getPhone).collect(Collectors.joining("\r\n"));
            writer.write(content);
            writer.flush();
            String command="/data/webapp/xw_script/load181.sh";
            long beginTime2 = System.currentTimeMillis();
            Process process = Runtime.getRuntime().exec(command);
            readerProcessAndWait(process);
            long endTime2 = System.currentTimeMillis();
            logger.info("sql loader end : process time = "+(endTime2-beginTime2));
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (file.exists())
                file.delete();
        }
    }

    private int readerProcessAndWait(Process process) throws InterruptedException, IOException {
        try
                (
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(process.getInputStream(),"utf8"));
                BufferedReader brError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "utf8"))
                )
        {
            String line;
            while ((line=bufferedReader.readLine()) != null)
                logger.info(line);

            //读取标准错误流
            String errorLine;
            while ((errorLine = brError.readLine()) != null)
                logger.info(errorLine);

        }
        return process.waitFor();
    }

    private ByteArrayOutputStream[] getByteArrayOutputStreams(PushParams pushParams, List<UserInfoEntity> userInfoEntities) throws IOException {
        int numberSplit = pushParams.getNumberSplit();
        int size = userInfoEntities.size() / numberSplit;
        if (userInfoEntities.size() % numberSplit != 0)
            size += 1;
        ByteArrayOutputStream[] byteOutArr = new ByteArrayOutputStream[size];
        int offset = 0;
        for (int i = 0; i < size; i++) {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            int i1 = offset + numberSplit;
            if (i == size - 1)
                i1 = userInfoEntities.size();
            for (int j = offset; j < i1; j++) {
                String content = "";
                UserInfoEntity userInfoEntity = userInfoEntities.get(j);
                if (pushParams.isShow_phone() && pushParams.isShow_msgid()) {
                    content = (userInfoEntity.getPhone()) + "\t" + (userInfoEntity.getMsgid());
                } else if (pushParams.isShow_phone()) {
                    content = userInfoEntity.getPhone();
                } else if (pushParams.isShow_msgid()) {
                    content = userInfoEntity.getMsgid();
                }
                if (j != i1 - 1)
                    content = content + ENTER;
                byteOut.write(content.getBytes());
            }
            offset += numberSplit;
            byteOutArr[i] = byteOut;
        }
        return byteOutArr;
    }
    private enum VersionEnum {
        ANDROID, IOS
    }
}
