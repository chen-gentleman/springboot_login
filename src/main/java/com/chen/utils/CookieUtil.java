package com.chen.utils;

import cn.hutool.core.bean.BeanUtil;
import com.chen.base.RedisConstant;
import com.chen.base.SystemConstant;
import com.chen.base.UserKit;
import com.chen.bean.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author @Chenxc
 * @Date 2023/3/16 9:28
 */
public class CookieUtil {
    public static Cookie get(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }


}
