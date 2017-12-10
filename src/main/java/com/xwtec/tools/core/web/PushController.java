package com.xwtec.tools.core.web;

import com.xwtec.tools.core.entity.PushParams;
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
    public Object process(
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

}
