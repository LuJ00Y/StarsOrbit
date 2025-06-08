package org.example.todoserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@RefreshScope
@EnableDiscoveryClient//用于开启服务发现功能。
@MapperScan("org.example.todoserver.mapper")
public class TodoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoServerApplication.class, args);
    }

}
