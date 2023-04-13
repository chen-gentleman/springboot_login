package com.chen.interceptors;

import cn.hutool.core.bean.BeanUtil;
import com.chen.base.RedisConstant;
import com.chen.base.SystemConstant;
import com.chen.base.UserKit;
import com.chen.bean.User;
import com.chen.utils.CookieUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author @Chenxc
 * @Date 2023/3/16 9:26
 */
public class RefreshTokenInterceptor implements HandlerInterceptor {
    private RedisTemplate<String,Object> redisTemplate;
    public RefreshTokenInterceptor(RedisTemplate<String,Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie cookie = CookieUtil.get(request, SystemConstant.SB_SESSION_ID);
        if(null != cookie) {
            String token = cookie.getValue();
            Map<Object, Object> entries = redisTemplate.opsForHash().entries(RedisConstant.LOGIN_USER_KEY + token);
            if (null != entries) {
                User user = BeanUtil.fillBeanWithMap(entries, new User(), false);
                if (user.getId() != null) {
                    UserKit.saveUser(user);
                    redisTemplate.expire(RedisConstant.LOGIN_USER_KEY + token,RedisConstant.LOGIN_TOKEN_TTL, TimeUnit.SECONDS);
                }
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserKit.removeUser();
    }
}
