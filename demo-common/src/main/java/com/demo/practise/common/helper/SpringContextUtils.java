package com.demo.practise.common.helper;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

/**
 * @author jiangyw
 * @date 2024/7/17 13:59
 * @description Spring上下文操作类
 */
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        System.out.println("开始设置上下文环境");
        SpringContextUtils.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return applicationContext;
    }

    /**
     * @param beanName bean注册名
     * @return Object
     */
    public static Object getBean(String beanName){
        return applicationContext.getBean(beanName);
    }

    /**
     *
     * @param clazz 对象类型
     * @param <T>
     * @return T 泛型
     */
    public static <T> T getBean(Class<T> clazz){
        return applicationContext.getBean(clazz);
    }

    /**
     *
     * @param beanName
     * @param clazz
     * @param <T>
     * @return T
     */
    public static <T> T getBean(String beanName, Class<T> clazz){
        return applicationContext.getBean(beanName, clazz);
    }

    /**
     * 该方法返回指定beanName的实例，实例可能是共享的（例如：单例模式的bean），也可能是独立的（原型模式的bean）
     * 在实际开发中，这个方法通常用于从Spring容器中获取bean的实例，并在需要时通过提供显式的参数来创建新的bean实例。这对于需要动态配置或注入不同参数的bean特别有用。
     * @param beanName 要检索的bean名称
     * @param args 指明的构造函数的参数/工厂方法的参数，以覆盖bean中定义的默认参数（如果有的话），这些参数仅在创建新实例时应用，而不是检索已存在的实例时应用
     * @return Object bean的一个实例
     * @throws NoSuchBeanDefinitionException 如果Spring容器中不存在此名称的bean定义，则抛出此异常
     * @throws BeanDefinitionStoreException 如果已给出参数（即args不为空），但受影响的bean不是原型（prototype）类型的bean（原型类型的bean在每次请求时都会创建一个新实例），则抛出此异常。注意，在Spring中，只有原型类型的bean才会接受构造函数参数来创建新实例。
     * @throws BeansException 如果由于某种原因（如配置错误、依赖问题等）bean无法被创建，则抛出此异常
     */
    public static Object getBean(String beanName, Object... args){
        return applicationContext.getBean(beanName, args);
    }

    /**
     * 该方法返回指定类型的bean的一个实例。返回的实例可能是共享的（例如，单例模式的bean），也可能是独立的（例如，原型模式的bean）。
     * 在实际开发中，这个方法通常用于通过类型从Spring容器中获取bean的实例，并在需要时通过提供显式的参数来创建新的bean实例。这在某些场景下（如当你不确定bean的确切名称，但知道它的类型时）非常有用。同时，对于需要基于类型进行更复杂的bean检索操作的情况，可以使用ListableBeanFactory和BeanFactoryUtils提供的额外功能。
     * @param clazz bean必须匹配的类型；可以是接口或超类。
     * @param args 指明的构造函数的参数/工厂方法的参数，以覆盖bean中定义的默认参数（如果有的话），这些参数仅在创建新实例时应用，而不是检索已存在的实例时应用
     * @param <T> 返回的实例类型
     * @return T 泛型
     * @throws NoSuchBeanDefinitionException 如果Spring容器中不存在与指定类型匹配的bean定义，则抛出此异常。
     * @throws BeanDefinitionStoreException 如果已给出参数（即args不为空），但受影响的bean不是原型（prototype）类型的bean（原型类型的bean在每次请求时都会创建一个新实例），则抛出此异常。注意，在Spring中，只有原型类型的bean才会接受构造函数参数来创建新实例。
     * @throws BeansException 如果由于某种原因（如配置错误、依赖问题等）bean无法被创建，则抛出此异常
     */
    public static <T> T getBean(Class<T> clazz, Object... args){
        return applicationContext.getBean(clazz, args);
    }

    public static boolean containsBean(String beanName){
        return applicationContext.containsBean(beanName);
    }

    /**
     * 重加载Spring配置文件
     * @param servletContext
     */
    public static void reload(ServletContext servletContext){
        WebApplicationContext webApplicationContext =
                WebApplicationContextUtils.getWebApplicationContext(servletContext);

        if (webApplicationContext.getParent() != null){
            ((AbstractRefreshableApplicationContext)webApplicationContext.getParent()).refresh();
        }
        ((AbstractRefreshableApplicationContext) webApplicationContext).refresh();
    }

}
