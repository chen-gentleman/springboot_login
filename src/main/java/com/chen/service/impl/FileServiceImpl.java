package com.chen.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.bean.File;
import com.chen.bean.Result;
import com.chen.dao.FileDao;
import com.chen.service.FileService;
import com.chen.utils.Md5Util;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Author @Chenxc
 * @Date Hello, 陈杏昌 2023/04/14,014 10:03
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileDao, File> implements FileService {
    @Override
    public List<File> list(Integer userId) {
        QueryWrapper<File> wrapper = new QueryWrapper<>();
        wrapper.eq("up_user",userId);
        List<File> list = list(wrapper);
        return list;
    }

    @Override
    public boolean saveFile(MultipartFile file, java.io.File fileIo,Integer userId, String uploadPath, String newName){
        String filename = file.getOriginalFilename();
        long fileSize = file.getSize();
        File file1 = new File();
        file1.setUpUser((long)userId);
        file1.setName(filename);
        file1.setFileSize(fileSize);
        file1.setCreateTime(new Date());
        try {
            file1.setFileMd5(DigestUtils.md5DigestAsHex(new FileInputStream(fileIo)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        file1.setLocalPath(uploadPath+ java.io.File.separator+newName);
        return save(file1);
    }

    @Override
    public Result deleteById(Long id) {
        File file = getById(id);
        if(null == file){
            return Result.fail("数据记录不存在");
        }
        boolean b = removeById(file);
        if(b){
            java.io.File file1 = new java.io.File(file.getLocalPath());
            if (file1.exists()){
                file1.delete();
            }
        }
        return b?Result.OK("删除成功"):Result.fail("删除失败");
    }
}
