package com.chen.base;

/**
 * @Author @Chenxc
 * @Date 2023/3/15 17:23
 */
public class RedisConstant {
    public static final String LOGIN_USER_KEY = "user:login:";
    public static final String COOKIE_KEY = "user:captcha:";
    public static final int LOGIN_TOKEN_TTL = 10*60;//登录过期时间 10分钟
}
