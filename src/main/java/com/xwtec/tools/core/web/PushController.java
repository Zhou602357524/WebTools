package com.xwtec.tools.core.web;

import com.xwtec.tools.core.entity.PushParams;
import com.xwtec.tools.core.entity.UserInfoEntity;
import com.xwtec.tools.core.service.pushtools.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zc
 * \* Date: 2017/11/30 0030
 * \* Time: 下午 3:28
 * \* Description:
 * \
 */
@Controller
@RequestMapping("/push")
public class PushController {

    private PushService pushService;

    @Autowired
    public PushController(PushService pushService) {
        this.pushService = pushService;
    }

    @RequestMapping("/index")
    public String index() {
        return "push/push_tools";
    }

    @ResponseBody
    @PostMapping("/process")
    public synchronized Object process(
            @RequestParam(name = "sourceData") MultipartFile sourceData,
            HttpServletResponse response,
            PushParams pushParams) {

        return  pushService.compressedFiles(sourceData, pushParams,response);
    }

    @RequestMapping("/getCount")
    @ResponseBody
    public Object getCount() {
        return pushService.getCount();
    }

    @RequestMapping("print")
    @ResponseBody
    public Object print(@RequestParam(name = "sourceData") MultipartFile sourceData){
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(sourceData.getInputStream()));
            String line;
            while ((line = reader.readLine()) !=null)
                sb.append(line).append("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
    @RequestMapping("/truncate")
    @ResponseBody
    public void truncate(){
        pushService.truncate();
    }

    @RequestMapping("/load")
    @ResponseBody
    public String load(@RequestParam(name = "sourceData") MultipartFile sourceData) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(sourceData.getInputStream()));
        Set<UserInfoEntity> strSet = new HashSet<>();
        String line;
        while ((line = reader.readLine()) != null) {
            String phone = line.trim();
            boolean matches = Pattern.matches("^((13[4-9])|(147)|(15[0-2,7-9])|(17[8])|(18[2-4,7-8]))\\d{8}|(170[5])\\d{7}|(198)\\d{8}$", phone);
            if (matches) {
                UserInfoEntity userInfoEntity = new UserInfoEntity();
                userInfoEntity.setPhone(phone);
                strSet.add(userInfoEntity);
            }
        }
        List<UserInfoEntity> userInfoEntities = new ArrayList<>(strSet);
        pushService.insertPhoneNumbersBySqlLoader(userInfoEntities);
        return null;
    }
    @RequestMapping("hello")
    @ResponseBody
    public Object hello(){
        hello2();
        String hello3 = hello3();
        return "hello";
    }
    @RequestMapping("hello3")
    @ResponseBody
    public String hello3() {

        return "hello3";
    }

    private void hello2() {
        System.out.println("hello2");
    }

}
