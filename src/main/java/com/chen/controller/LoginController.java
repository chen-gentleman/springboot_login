package com.chen.controller;

import cn.hutool.http.HttpUtil;
import com.chen.base.RedisConstant;
import com.chen.base.SystemConstant;
import com.chen.base.UserKit;
import com.chen.bean.Result;
import com.chen.bean.User;
import com.chen.service.UserService;
import com.chen.utils.CaptchaUtil;
import com.chen.utils.CookieUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Author @Chenxc
 * @Date 2023/3/14 11:45
 */
@Controller
@RequestMapping("admin")
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${system.loginPage}")
    private String loginPage;

    @Autowired
    private UserService userService;

    @RequestMapping("/")
    public String index(){
        return "index.html";
    }

    @GetMapping("register")
    public String register() {
        return "login/register.html";
    }

    @PostMapping("register")
    @ResponseBody
    public Result register(String username,String name, String password,String email) {
        User user = userService.findByUserName(username);
        if(null != user){
            return Result.fail("该用户名已经被使用哦！请换一个哈!");
        }
        String encode = passwordEncoder.encode(password);
        User add = new User();
        add.setUserName(username);
        add.setPassword(encode);
        add.setName(name);
        add.setEmail(email);
        Result result = userService.add(add);
        if(!(result.getState()).equals("ok")){
            return Result.fail("注册失败!");
        }
        return Result.OK("恭喜成功注册，欢迎使用!");
    }


    @GetMapping("login")
    public String loginPage(){
        return "login/"+loginPage;
    }

    @PostMapping("/login")
    @ResponseBody
    public Result login(String username, String password,String captcha, HttpServletRequest request,HttpServletResponse response){
        boolean verifySuccess = CaptchaUtil.verify(CookieUtil.get(request, CaptchaUtil.CAPTCHA_KEY), captcha, redisTemplate);
        if(!verifySuccess){
            return Result.verifyFail();
        }
        User user = userService.findByUserName(username);
        if(null == user){
            Result result = Result.loginFail();
            return result;
        }
        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if(!matches){
            Result result = Result.loginFail();
            return result;
        }
        afterLoginSuccess(user,response);
        Result result = Result.OK();
        return result;
    }

    private void afterLoginSuccess(User user ,HttpServletResponse response){
        String token = UUID.randomUUID().toString().replace("-", "");
        Cookie cookie = new Cookie(SystemConstant.SB_SESSION_ID, token);
        cookie.setMaxAge(SystemConstant.LOGIN_COOKIE_TTL);
        cookie.setPath("/");
        response.addCookie(cookie);
        redisTemplate.opsForHash().put(RedisConstant.LOGIN_USER_KEY+token,"id",user.getId());
        redisTemplate.opsForHash().put(RedisConstant.LOGIN_USER_KEY+token,"userName",user.getUserName());
        redisTemplate.expire(RedisConstant.LOGIN_USER_KEY+token,RedisConstant.LOGIN_TOKEN_TTL, TimeUnit.SECONDS);
    }

    /**
     * 验证码
     */
    @GetMapping("/captcha")
    public void captcha(HttpServletResponse response){
        //CaptchaUtil.lineCaptcha(response,redisTemplate,200,100,5,3);
        CaptchaUtil.gifCaptcha(response,redisTemplate,200,100,5);
    }


    @RequestMapping("/download")
    @ResponseBody
    public String fileDownLoad(HttpServletResponse response, @RequestParam("fileName") String fileName){
        File file = new File("C:\\Users\\Administrator\\Desktop\\template\\pic\\"+ fileName);
        if(!file.exists()){
            return "下载文件不存在";
        }
        file.length();
        response.reset();
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("utf-8");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName );

        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
            byte[] buff = new byte[1024];
            OutputStream os  = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            return "fail";
        }
        return "success";
    }
}
