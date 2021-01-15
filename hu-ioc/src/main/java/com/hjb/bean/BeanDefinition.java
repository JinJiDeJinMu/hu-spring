package com.hjb.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanDefinition {

    private Object object;

    private Class<?> beanClass;

    private String className;

    private Map<String,BeanDefinition> fields = null;

    public Map<String, BeanDefinition> getFields() {
        return fields;
    }

    public void setFields(BeanDefinition beanDefinition) {
        if(fields == null){
            fields = new ConcurrentHashMap<>();
        }
        fields.put(beanDefinition.getClassName(),beanDefinition);
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<?> beanClass) {

        this.beanClass = beanClass;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
       this.className = className;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "object=" + object +
                ", beanClass=" + beanClass +
                ", className='" + className + '\'' +
                ", fields=" + fields +
                '}';
    }
}
