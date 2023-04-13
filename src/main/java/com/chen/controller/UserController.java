package com.chen.controller;

import com.chen.base.UserKit;
import com.chen.bean.Result;
import com.chen.bean.User;
import com.chen.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author @Chenxc
 * @Date 2023/3/14 10:00
 */
@Controller()
@RequestMapping("user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    StringRedisTemplate template;

    @RequestMapping(value = "/findAll")
    public String findAll(ModelMap map){
        List<User> users = userService.findAll();
        map.put("users",users);
        return "user/index.html";
    }

    @RequestMapping(value = "/findById/{id}")
    @ResponseBody
    public User findById(@PathVariable("id") Integer id){
        return userService.findById(id);
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    public Result add(User user){
        return userService.add(user);
    }

    @RequestMapping(value = "/delete/{id}")
    @ResponseBody
    public Result delete(@PathVariable("id") Integer id){
        return userService.delete(id);
    }


    @GetMapping("/update/{id}")
    public String update(ModelMap map,@PathVariable("id") Integer id){
        User user = userService.findById(id);
        if(null != user){
            map.addAttribute("user",user);
        }
        return "user/edit.html";
    }

    @PostMapping(value = "/update")
    @ResponseBody
    public Result update(User user){
        return userService.update(user);
    }


}
