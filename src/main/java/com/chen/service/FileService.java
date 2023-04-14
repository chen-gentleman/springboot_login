package com.chen.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.bean.File;
import com.chen.bean.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * @Author @Chenxc
 * @Date Hello, 陈杏昌 2023/04/14,014 10:02
 */
public interface FileService extends IService<File> {
    List<File> list(Integer userId);
    boolean saveFile(MultipartFile file, java.io.File fileIo, Integer userId, String uploadPath, String newName);

    Result deleteById(Long id);
}
