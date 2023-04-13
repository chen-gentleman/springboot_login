package com.chen.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author @Chenxc
 * @Date 2023/3/14 10:02
 */

public interface UserDao extends BaseMapper<User> {
    List<User> findAll();

    User findByUserName(@Param("userName") String userName);

    User findById(Integer id);

    int add(User user);

    int delete(Integer id);

    int update(User user);
}
