package com.example.demo;

import com.example.demo.entity.MyBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {

        SpringApplication.run(DemoApplication.class, args);
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);
        /**
         * 以下见：https://www.guitu18.com/post/2019/04/28/33.html
         * factoryBean的作用：它能在需要的时候生产一个对象，且不仅仅限于它自身，它能返回任何Bean的实例。
         *
         */
        MyBean myBean1 = (MyBean) context.getBean("myBean");
        System.out.println("myBean1 = " + myBean1.getMessage());
        MyBean myBean2 = (MyBean) context.getBean("&myBean");
        System.out.println("myBean2 = " + myBean2.getMessage());
        System.out.println(myBean1.equals(myBean2));

    }

}
