package com.chen.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @Author @Chenxc
 * @Date 2023/4/3 9:41
 */
@ControllerAdvice
@Slf4j
public class AdviceController {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e){
        log.error(e+"");
        return "forward:/error";
    }

}
