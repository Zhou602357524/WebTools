package com.xwtec.tools.core.web;

import com.xwtec.tools.core.entity.PushParams;
import com.xwtec.tools.core.service.pushtools.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

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
            @Validated PushParams pushParams) {

        return  pushService.compressedFiles(sourceData, pushParams,response);
    }

    @RequestMapping("/getCount")
    @ResponseBody
    public Object getCount() {
        return pushService.getCount();
    }

    @RequestMapping("/truncate")
    @ResponseBody
    public void truncate(){
        pushService.truncate();
    }

}
