package com.demo.multidatasource.aop.asp;

import com.demo.multidatasource.aop.annotation.DataSource;
import com.demo.multidatasource.aop.context.DataSourceContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
// 自定义数据源注解与@Transaction注解同一个方法时会先执行@Transaction
// 即获取数据源在切换数据源之前，所以会导致自定义注解失效。由于@After再切面之后会清除
//@Order(@Order的value越小，就越先执行)，此处设置为-1保证该AOP在@Transactional之前执行
@Order(-1)
public class DynamicDataSourceAspect {

    @Before("@annotation(com.demo.multidatasource.aop.annotation.DataSource)")
    public void beforeSwitchDS(JoinPoint point){
        //获得当前访问的class
        Class<?> className = point.getTarget().getClass();
        //获得访问的方法名
        String methodName = point.getSignature().getName();
        //得到方法的参数的类型
        Class[] argClass = ((MethodSignature)point.getSignature()).getParameterTypes();
        String dataSource = DataSourceContextHolder.getDataSource();
        try {
            // 得到访问的方法对象
            Method method = className.getMethod(methodName, argClass);
            // 判断是否存在@DataSource注解
            if (method.isAnnotationPresent(DataSource.class)) {
                DataSource annotation = method.getAnnotation(DataSource.class);
                // 取出注解中的数据源名
                dataSource = annotation.value().name();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 切换数据源
        DataSourceContextHolder.setDataSource(dataSource);
    }

    @After("@annotation(com.demo.multidatasource.aop.annotation.DataSource)")
    public void afterSwitchDS(JoinPoint point){
        DataSourceContextHolder.clearDataSource();
    }
}
