package com.chen.utils;


import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.captcha.LineCaptcha;
import com.chen.base.RedisConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author @Chenxc
 * @Date 2023/3/16 10:09
 */
public class CaptchaUtil  {
    private static final Logger logger = LoggerFactory.getLogger(CaptchaUtil.class);
    public static final String CAPTCHA_KEY = "c_captcha";
    private static final int CAPTCHA_TTL = 180;

    public static void lineCaptcha(HttpServletResponse response, RedisTemplate<String,Object> redisTemplate,int width, int height, int codeCount, int lineCount){
        LineCaptcha lineCaptcha = cn.hutool.captcha.CaptchaUtil.createLineCaptcha(width, height,codeCount,lineCount);
        write(response,redisTemplate,lineCaptcha);
    }

    public static void gifCaptcha(HttpServletResponse response,RedisTemplate<String,Object> redisTemplate,int width, int height,int codeCount){
        GifCaptcha gifCaptcha = cn.hutool.captcha.CaptchaUtil.createGifCaptcha(width, height, codeCount);
        write(response,redisTemplate,gifCaptcha);
    }

    public static boolean verify(Cookie cookie,String input,RedisTemplate<String,Object> redisTemplate){
        String cookieValue = cookie.getValue();
        Object verifyCode = redisTemplate.opsForValue().get(RedisConstant.COOKIE_KEY + cookieValue);
        if(null == verifyCode){
            return false;
        }
        boolean success = input.equalsIgnoreCase(verifyCode.toString());
        if(success){
            //使该验证码过期
            redisTemplate.delete(RedisConstant.COOKIE_KEY + cookieValue);
        }
        return success;
    }


    private static void write(HttpServletResponse response,RedisTemplate<String,Object> redisTemplate, AbstractCaptcha captcha){
        ServletOutputStream sos = null;
        try {
            String cookieValue = UUID.randomUUID().toString().replace("-", "");
            Cookie cookie = new Cookie(CAPTCHA_KEY, cookieValue);
            cookie.setMaxAge(-1);
            cookie.setPath("/");
            response.addCookie(cookie);
            redisTemplate.opsForValue().set(RedisConstant.COOKIE_KEY+cookieValue,captcha.getCode(),CaptchaUtil.CAPTCHA_TTL, TimeUnit.SECONDS);
            sos = response.getOutputStream();
            captcha.write(sos);
        } catch (IOException e) {
            logger.error(e.toString());
        }finally {
            if(null != null){
                try {
                    sos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
