package com.xwtec.tools.core.web;

import com.xwtec.tools.core.entity.PushParm;
import com.xwtec.tools.core.utils.io.IOUtils;
import com.xwtec.tools.core.service.pushtools.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    //private RedisTemplate<Object,Object> redisTemplate;

    @Autowired(required = false)
    public PushController(PushService pushService) {
        this.pushService = pushService;
        //this.redisTemplate = redisTemplate1;
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
            PushParm pushParm,
            HttpServletRequest request)
    {
        String zipPath = pushService.compressedFiles(sourceData,pushParm);

        return IOUtils.responseWrite(response,zipPath);
    }


    @RequestMapping("/getCount")
    @ResponseBody
    public Object getCount(){
        return pushService.getCount();
    }

}
