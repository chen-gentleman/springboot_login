package com.chen.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author @Chenxc
 * @Date Hello, 陈杏昌 2023/04/14,014 9:57
 */
@Data
@TableName("sb_file")
public class File {
    @TableId
    private Long id;
    private String name;
    private String localPath;
    private String localUrl;
    private Date createTime;
    private Long upUser;
    private Long fileSize;
    private Integer sortRank;
    private Boolean status;
    private String fileMd5;
}
