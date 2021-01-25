package com.entity;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * @version 1.1.0
 * @author：cp
 * @time：2021-1-25
 * @Description: todo
 */
@Component
public class MyBean implements FactoryBean {
    private String message;

    public MyBean() {
        this.message = "通过构造方法初始化实例";
    }

    @Override
    public Object getObject() throws Exception {
        MyBean myBean = new MyBean();
        myBean.message = "通过FactoryBean.getObject()初始化实例";
        return myBean;
    }

    @Override
    public Class<?> getObjectType() {
        return MyBean.class;
    }

    public String getMessage() {
        return message;
    }
}
