package com.chen.bean;

import lombok.Data;
import lombok.ToString;

/**
 * @Author @Chenxc
 * @Date 2023/3/14 10:02
 */
@Data
@ToString
public class User {
    private Integer id;
    private String userName;
    private String name;
    private String avatar;
    private String phone;
    private Integer sex;
    private String email;
    private String password;
}
