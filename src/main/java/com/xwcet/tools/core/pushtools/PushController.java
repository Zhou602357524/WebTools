package com.xwcet.tools.core.pushtools;

import org.hibernate.engine.jdbc.ReaderInputStream;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

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

    @RequestMapping("/index")
    public String index(MultipartFile sourceData, String name) {
        try {
        if (sourceData != null) {
            System.out.println(sourceData.getOriginalFilename());
            BufferedReader reader = new BufferedReader(new InputStreamReader(sourceData.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null){
                System.out.println(line);
            }

        }

        }catch (IOException e) {
            e.printStackTrace();
        }
        return "push/push_tools";
    }




}
