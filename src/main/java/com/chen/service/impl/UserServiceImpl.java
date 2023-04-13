package com.chen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.bean.Result;
import com.chen.bean.User;
import com.chen.dao.UserDao;
import com.chen.events.UserRegisterEvent;
import com.chen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author @Chenxc
 * @Date 2023/3/14 10:01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao,User> implements UserService , ApplicationEventPublisherAware {
    @Autowired
    private UserDao userDao;

    private ApplicationEventPublisher applicationEventPublisher;


    @Override
    @Cacheable(cacheNames = "user",key = "'user:cache:findAll'")
    public List<User> findAll() {
        return userDao.findAll();
    }


    @Override
    //@Cacheable(cacheNames = "user",key = "'user:cache:'+#root.methodName+#root.args[0]")
    public User findByUserName(String userName){
        return userDao.findByUserName(userName);
    }


    @Override
    //@Cacheable(cacheNames = "user",key = "'user:cache:'+#root.methodName+#root.args[0]")
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    @Override
    @CacheEvict(cacheNames = "user",key = "'user:cache:findAll'")
    public Result add(User user) {
        int num = userDao.add(user);
        if(num > 0){
            applicationEventPublisher.publishEvent(new UserRegisterEvent(this,user.getUserName()));
            return Result.OK("添加成功");
        }else{
            return Result.OK("添加失败");
        }
    }

    @Override
    @CacheEvict(cacheNames = "user",key = "'user:cache:findAll'")
    public Result delete(Integer id) {
        if(null == id){
            return Result.fail("参数异常");
        }
        User user = findById(id);
        if(null == user){
            return Result.fail("数据库记录不存在");
        }
        int delete = userDao.delete(id);
        return delete > 0?Result.OK("删除成功"):Result.fail("删除失败");
    }

    @Override
    @CacheEvict(cacheNames = "user",key = "'user:cache:findAll'")
    public Result update(User user) {
        if(null == user.getId() || null == user){
            return Result.fail("参数异常");
        }
        User dbUser = findById(user.getId());
        if(null == dbUser){
            return Result.fail("数据库记录不存在");
        }
        int update = userDao.update(user);
        return update > 0?Result.OK("更新成功"):Result.fail("更新失败");
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
