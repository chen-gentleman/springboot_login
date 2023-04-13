package com.chen.controller;

import cn.hutool.core.io.IoUtil;
import com.chen.base.ByteFileType;
import com.chen.base.Render;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;

/**
 * @Author @Chenxc
 * @Date 2023/3/31 16:20
 */
@Controller
@RequestMapping("/file")
public class FileController {


    @RequestMapping("/checkFileOnline")
    public void checkFileOnline(HttpServletResponse response) throws IOException {
        byte[] bytes = IoUtil.readBytes(new FileInputStream("C:\\Users\\Administrator\\Desktop\\会晤系统测试文件\\视频\\food.mp4"));
        Render.renderByteToAudio(bytes,response);
    }


    @RequestMapping("/download")
    public void download(HttpServletResponse response) throws IOException {
        String path = FileController.class.getResource("/").getPath()+File.separator+"upload"+File.separator+"5bb282c7df4ef413e3831b4b3ea9f072.gif";
        System.out.println(path);
        // 读到流中
        InputStream inputStream = new FileInputStream(path);
        response.reset();
        response.setContentType("application/octet-stream");
        String filename = new File(path).getName();
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
        ServletOutputStream outputStream = response.getOutputStream();
        byte[] b = new byte[1024];
        int len;
        //从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
        while ((len = inputStream.read(b)) > 0) {
            outputStream.write(b, 0, len);
        }
        inputStream.close();
    }
}
