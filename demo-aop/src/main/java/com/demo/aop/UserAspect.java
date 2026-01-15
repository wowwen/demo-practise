package com.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * AOP的功能1：统一认证
 * 使用原生AOP实现统一拦截
 */

/**
 * 说明此类为一个切面
 */
@Aspect
@Component
public class UserAspect {
    /**
     * 定义切点，这里使用Aspect表达式语法
     */
    @Pointcut("execution(* com.demo.aop.controller.UserController.*(..))")
    public void pointcut(){}

    /**
     * 前置通知
     */
    @Before("pointcut()")
    public void beforeAdvice(){
        System.out.println("执行了前置通知");
    }

    /**
     *  环绕通知
     */
    @Around("pointcut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint){
        System.out.println("进入环绕通知");
        Object obj = null;
        //执行目标方法
        try{
            obj = joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("退出环绕通知");
        return obj;
    }
    //从上面代码可以看出，使用原生的spring aop实现统一拦截的难点主要有几个方面
    //1.实现拦截规则非常困难。如注册方法和登录方法是不拦截的，这样的话排除方法的规则很难定义，甚至没有办法定义
    //2.在切面类中拿到HttpSession比较难
    //为了解决Spring AOP的这些问题，spring提供了拦截器，如LoginInterceptor类


    @After("pointcut()")
    public void doAfter(){
        System.out.println("执行了后置通知");
    }


    //
    @AfterReturning(returning = "obj", pointcut = "pointcut()")
    public void doAfterReturning(Object obj){
        System.out.println("处理完请求，返回内容" + obj);
    }


    /** 测试结果：
     * 进入环绕通知
     * 执行了前置通知
     * 处理完请求，返回内容User AOP 测试
     * 执行了后置通知
     * 退出环绕通知
     */
}
