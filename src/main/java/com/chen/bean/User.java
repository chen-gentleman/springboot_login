package com.chen.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

/**
 * @Author @Chenxc
 * @Date 2023/3/14 10:02
 */
@Data
@ToString
@TableName("sb_user")
public class User {
    @TableId
    private Integer id;
    private String userName;
    private String name;
    private String avatar;
    private String phone;
    private Integer sex;
    private String email;
    private String password;
}
