package com.xwcet.tools.core.pushtools;

import com.xwcet.tools.core.io.IOUtils;
import com.xwcet.tools.core.service.pushtools.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;

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
    public Object process(@RequestParam(required = true,name = "sourceData") MultipartFile sourceData, @RequestParam(defaultValue="1")int splitNumber, HttpServletResponse response) {
        String zipPath = pushService.compressedFiles(sourceData, splitNumber);

        return IOUtils.responseWrite(response,zipPath) == 1;
    }


}
