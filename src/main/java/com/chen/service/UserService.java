package com.chen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.bean.Result;
import com.chen.bean.User;

import java.util.List;

/**
 * @Author @Chenxc
 * @Date 2023/3/14 10:01
 */
public interface UserService extends IService<User> {
    List<User> findAll();

    User findByUserName(String userName);

    User findById(Integer id);

    Result add(User user);

    Result delete(Integer id);

    Result update(User user);
}
