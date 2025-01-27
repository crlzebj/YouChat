package com.zjx.youchat.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.zjx.youchat.api")
public class WSApplication {
    public static void main(String[] args) {
        SpringApplication.run(WSApplication.class, args);
    }
}
