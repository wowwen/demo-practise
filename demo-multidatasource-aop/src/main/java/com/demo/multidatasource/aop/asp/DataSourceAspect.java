//package com.demo.multidatasource.aop.asp;
//
//import com.demo.multidatasource.aop.annotation.DataSource;
//import com.demo.multidatasource.aop.context.DataSourceContextHolder;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//
///**
// * 定义注解的处理类，拦截自定义的@DataSource注解，然后在执行sql前设置所需数据源
// */
////切面
//@Aspect
//@Component
//// 自定义数据源注解与@Transaction注解同一个方法时会先执行@Transaction
//// 即获取数据源在切换数据源之前，所以会导致自定义注解失效。
////@Order(@Order的value越小，就越先执行)，此处设置为-1保证该AOP在@Transactional之前执行
//@Order(-1)
//public class DataSourceAspect {
//    @Pointcut("@annotation(com.demo.multidatasource.aop.annotation.DataSource)")
//    public void dataSourcePointCut() {}
//
//    @Around("dataSourcePointCut()")
//    public Object dataSourceAround(ProceedingJoinPoint proceed) throws Throwable {
//        MethodSignature methodSignature = (MethodSignature) proceed.getSignature();
//        Method method = methodSignature.getMethod();
//        DataSource dataSource = method.getAnnotation(DataSource.class);
//
//        try{
//            return proceed.proceed();
//        } finally {
//            //方法执行完之后，销毁数据源
//            DataSourceContextHolder.clearDataSource();
//        }
//    }
//
//}
