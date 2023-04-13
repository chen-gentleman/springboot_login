package com.chen;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication()
@MapperScan("com.chen.dao")
//@EnableDiscoveryClient
public class SpringbootLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootLoginApplication.class, args);
    }

}
