package com.chen.events;

import org.springframework.context.ApplicationEvent;

/**
 * @Author @Chenxc
 * @Date 2023/3/17 15:38
 */
public class UserRegisterEvent extends ApplicationEvent {
    private String userName;
    public UserRegisterEvent(Object source,String userName) {
        super(source);
        this.userName = userName;
    }

    public String getUserName(){
        return this.userName;
    }
}
