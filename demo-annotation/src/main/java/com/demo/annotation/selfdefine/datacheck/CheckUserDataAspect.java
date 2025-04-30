package com.demo.annotation.selfdefine.datacheck;

import com.demo.annotation.handler.BusinessException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author owen
 * @date 2024/8/5 18:25
 * @description
 */
@Aspect
public class CheckUserDataAspect {
    @Pointcut("@annotation(com.demo.annotation.selfdefine.datacheck.CheckUserData)")
    public void userAgents() {
        //本方法无方法体，作用是给同类中其他方法使用此切入点
    }

    @Around("userAgents()")
    public Object dataAspect(ProceedingJoinPoint point) throws Throwable {
        //step1:从http header中取userId,然后从数据库或者缓存查用户所有的角色
        List<Integer> roleTypes = new ArrayList<>();
        //这里是模拟获取到的用户角色类型
        roleTypes.add(1);

        //step2:获取方法上的注解
        final MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        CheckUserData checkFlag = method.getAnnotation(CheckUserData.class);
        //step3:检查注解的属性是否允许普通用户
        if (checkFlag.checkUserData()){
            //从roleTypes中检查是否有数据权限,没有就抛异常
            if (!roleTypes.contains(1)){
                throw new BusinessException();
            }
        }
        return point.proceed();
    }
}
