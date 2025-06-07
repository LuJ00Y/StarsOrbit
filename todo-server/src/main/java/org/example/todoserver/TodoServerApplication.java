package org.example.todoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@SpringBootApplication
@RefreshScope
public class TodoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoServerApplication.class, args);
    }

}
