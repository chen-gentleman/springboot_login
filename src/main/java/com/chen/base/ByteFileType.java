package com.chen.base;

import lombok.Data;

/**
 * @Author @Chenxc
 * @Date 2023/4/10 16:51
 */

public enum ByteFileType {
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx"),
    XLS("application/vnd.ms-excel", "xls"),
    DOC("application/msword", "doc"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx"),
    TXT("text/plain", "txt"),
    PDF("application/pdf", "pdf"),
    GIF("image/gif", "gif"),
    VEDIO("video/x-msvideo", "mp4"),
    AUDIO("audio/x-wav","mp3"),
    JEPG("image/jepg", "jepg"),
    PNG("image/png", "png"),
    JPG("image/jpg", "jpg"),
    BMP("image/bmp", "bmp"),
    WEBP("image/webp", "webp");



    public String contentType;
    public String suffix;

    ByteFileType(String contentType, String suffix) {
        this.contentType = contentType;
        this.suffix = suffix;
    }}
