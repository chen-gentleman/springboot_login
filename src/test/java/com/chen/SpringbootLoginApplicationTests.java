package com.chen;

import com.alibaba.druid.filter.config.ConfigTools;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


class SpringbootLoginApplicationTests {
    public static void main(String[] args) throws Exception {
        String pass = "123456";
        ConfigTools.main(new String[]{pass});
    }

}
