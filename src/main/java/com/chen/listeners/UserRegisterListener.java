package com.chen.listeners;

import com.chen.events.UserRegisterEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * @Author @Chenxc
 * @Date 2023/3/17 15:43
 */
@Component
public class UserRegisterListener  {
    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender mailSender;

    @EventListener
    public void sendEmail(UserRegisterEvent event){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(from);
        mailMessage.setTo(from);
        mailMessage.setSubject("用户注册");
        mailMessage.setText("用户："+event.getUserName()+"注册成功");
        mailSender.send(mailMessage);
    }
}
