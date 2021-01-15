package com.hjb.ioc;

import com.hjb.annotation.HuAutowired;
import com.hjb.annotation.HuService;
import com.hjb.bean.BeanDefinition;
import com.hjb.service.UserService;
import com.hjb.util.ClassUtil;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HuAnnotationContext {

    private ConcurrentHashMap<String, BeanDefinition> beanMap = null;

    private String PackageName = "com.hjb.service";

    public HuAnnotationContext(){
        //扫描注册bean
        loadBean(PackageName);

        //刷新上下文，设置属性值
        refresh();
    }
    
    public void refresh(){
        if(beanMap == null){
            return;
        }
        for (Map.Entry<String, BeanDefinition> entry : beanMap.entrySet()) {

            BeanDefinition beanDefinition = entry.getValue();
            getFields(beanDefinition);
        }
    }

    /**
     * 从容器中获取bean
     * @param beanName
     * @return
     */
    public BeanDefinition getBean(String beanName){
        if(!beanMap.contains(beanName)){
            throw new RuntimeException("找不到对应的bean");
        }
        return beanMap.get(beanName);
    }

    /**
     * 初始化bean
     * @param aClass
     * @return
     */
    public Object initBean(Class<?> aClass){
        try {
           return aClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建BeanDefinition
     * @param aClass
     * @return
     */
    public BeanDefinition createBeanDefinition(Class<?> aClass,String beanName){
        BeanDefinition beanDefinition = new BeanDefinition();
        
        Object bean = initBean(aClass);

        beanDefinition.setObject(bean);
        beanDefinition.setBeanClass(aClass);
        beanDefinition.setClassName(beanName);

        return beanDefinition;

    }
    /**
     * 扫描指定包下bean,加入到ioc容器中
     * @param packageName
     */
    public void loadBean(String packageName){
        if(StringUtils.isEmpty(packageName)){
            return;
        }
        //查找class
        List<Class<?>> classList = ClassUtil.getClasses(packageName);
        if(beanMap == null){
            beanMap = new ConcurrentHashMap<>();
        }

        for (Class<?> aClass : classList) {
            HuService annotation = aClass.getAnnotation(HuService.class);

            if(annotation != null){
                if(!aClass.isInterface()){
                    String beanName = !annotation.name().equals("") ? annotation.name() : aClass.getSimpleName();
                    String lowerBeanName = ClassUtil.toLowerCaseFirstOne(beanName);
                    if(!beanMap.contains(lowerBeanName)){

                        BeanDefinition beanDefinition = createBeanDefinition(aClass, lowerBeanName);
                        beanMap.put(lowerBeanName,beanDefinition);
                    }
                }
            }
        }

    }

    /**
     * 查找所有属性，公共和私有
     * @param beanDefinition
     */
    public void getFields(BeanDefinition beanDefinition){
        Class<?> aClass = beanDefinition.getBeanClass();
        //公共属性(public)
        Field[] fields = aClass.getFields();
        setAttribute(beanDefinition,fields);
        //私有属性(private)
        Field[] declaredFields = aClass.getDeclaredFields();
        setAttribute(beanDefinition,declaredFields);
    }

    /**
     * 给加注解的属性赋值
     * @param beanDefinition
     * @param fields
     */
    public void setAttribute(BeanDefinition beanDefinition, Field[] fields){
        for (Field field : fields) {

            HuAutowired huAutowired = field.getAnnotation(HuAutowired.class);
            if(huAutowired != null){

                String name =field.getName();
                BeanDefinition definition = beanMap.get(name);
                if(definition != null){
                    //beanDefinition.setFields(definition);
                    try {
                        field.setAccessible(true);
                        field.set(beanDefinition.getObject(),definition.getObject());
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    public static void main(String[] args) {
        HuAnnotationContext context = new HuAnnotationContext();
        BeanDefinition beanDefinition = context.getBean("userServiceImpl");
        System.out.println(beanDefinition);
        UserService userService = (UserService) beanDefinition.getObject();
        System.out.println(userService.hello());

    }
}
