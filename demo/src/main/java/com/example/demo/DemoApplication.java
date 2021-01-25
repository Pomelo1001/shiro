package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        /**
         * 以下见：https://www.guitu18.com/post/2019/04/28/33.html
         */
    }

}
