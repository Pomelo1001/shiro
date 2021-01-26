package com.example.demo;

import com.example.demo.entity.MyBean;
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
        MyBean myBean1 = (MyBean) context.getBean("myBean");
        System.out.println("myBean1 = " + myBean1.getMessage());
        MyBean myBean2 = (MyBean) context.getBean("&myBean");
        System.out.println("myBean2 = " + myBean2.getMessage());
    }

}
