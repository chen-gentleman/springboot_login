package com.chen.base;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.chen.base.ByteFileType.*;

/**
 * @Author @Chenxc
 * @Date 2023/4/10 16:50
 */
public class Render {

    public static void renderByteToTxt(byte[] bytes, HttpServletResponse response) throws IOException {
        common(response);
        response.setContentType(TXT.contentType);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

    public static void renderByteToPDF(byte[] bytes, HttpServletResponse response) throws IOException {
        common(response);
        response.setContentType(PDF.contentType);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

    public static void renderByteToImage(byte[] bytes, HttpServletResponse response,ByteFileType type) throws IOException {
        common(response);
        ServletOutputStream outputStream = response.getOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage read = ImageIO.read(inputStream);
        ImageIO.write(read,type.suffix,outputStream);
    }


    public static void renderByteToAudio(byte[] bytes, HttpServletResponse response) throws IOException {
        common(response);
        response.setContentType(AUDIO.contentType);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }

    private static void common(HttpServletResponse response){
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Accept-Ranges", "bytes");
        response.setDateHeader("Expires", 0);
    }
}
