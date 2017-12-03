package com.xwcet.tools.core.web;

import cn.apiclub.captcha.Captcha;
import cn.apiclub.captcha.backgrounds.GradiatedBackgroundProducer;
import cn.apiclub.captcha.gimpy.FishEyeGimpyRenderer;
import com.xwcet.tools.core.utils.cookie.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private int captchaExpires = 3 * 60; //超时时间3min
    private int captchaW = 200;
    private int captchaH = 60;

    @RequestMapping(value = "/getCaptcha", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] getCaptcha(HttpServletResponse response) {
        //生成验证码
        String uuid = UUID.randomUUID().toString();
        Captcha captcha = new Captcha.Builder(captchaW, captchaH)
                .addText().addBackground(new GradiatedBackgroundProducer())
                .gimp(new FishEyeGimpyRenderer())
                .build();

        //将验证码以<key,value>形式缓存到redis
        redisTemplate.opsForValue().set(uuid, captcha.getAnswer(), captchaExpires, TimeUnit.SECONDS);

        //将验证码key，及验证码的图片返回
        Cookie cookie = new Cookie("CaptchaCode", uuid);
        response.addCookie(cookie);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        try {
            ImageIO.write(captcha.getImage(), "png", bao);
            return bao.toByteArray();
        } catch (IOException e) {
            return null;
        }

    }

    @ResponseBody
    @PostMapping("/checkCaptcha")
    public String checkCaptcha(HttpServletRequest request, String val) {

        try {

            String captchaCode = CookieUtils.getValue(request, "CaptchaCode");

            if (captchaCode == null) {
                throw new Exception();
            }
            String captchaValue = redisTemplate.opsForValue().get(captchaCode);
            if (captchaValue == null) {
                throw new Exception();
            }
            if (!val.equals(captchaValue))
                throw new Exception();
            redisTemplate.delete(captchaCode);

            return "success";
        } catch (Exception e) {

            return "error";
        }
    }

}