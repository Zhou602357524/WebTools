package com.xwcet.tools.core.pushtools;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
    public String index() {

        return "push/push_tools";
    }

    @ResponseBody
    @RequestMapping("/process")
    public String process(MultipartFile sourceData, String name) {

        try {

            System.out.println(sourceData.getOriginalFilename());
            BufferedReader reader = new BufferedReader(new InputStreamReader(sourceData.getInputStream()));
            List<String> strList = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                strList.add(line);
            }
            int size = strList.size() / 4;
            int offset = 0;

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(""))));
            for (int i = 0; i < 4; i++) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
