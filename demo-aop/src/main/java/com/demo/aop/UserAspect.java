package com.demo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * AOP的功能1：统一认证
 * 使用原生AOP实现统一拦截
 */
@Aspect //说明此类为一个切面
@Component
public class UserAspect {

    //定义切点，这里使用Aspect表达式语法
    @Pointcut("execution(* com.demo.aop.controller.UserController.*(..))")
    public void pointcut(){}

    //前置通知
    @Before("pointcut()")
    public void beforeAdvice(){
        System.out.println("执行了前置通知");
    }

    //环绕通知
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
}
