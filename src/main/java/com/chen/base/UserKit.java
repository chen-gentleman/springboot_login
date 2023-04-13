package com.chen.base;

import com.chen.bean.User;

/**
 * @Author @Chenxc
 * @Date 2023/3/15 17:22
 */
public class UserKit {
    private static final ThreadLocal<User> USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void saveUser(User user){
        USER_THREAD_LOCAL.set(user);
    }

    public static void removeUser(){
        USER_THREAD_LOCAL.remove();
    }

    public static User getUser(){
        return USER_THREAD_LOCAL.get();
    }
}
