package com.demo.aop.exception.handler;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

/**
 * aop的功能2：统一异常处理
 * 统一异常处理是指 在应用程序中定义一个公共的异常处理机制，用来处理所有的异常情况。 这样可以避免在应用程序中分散地处理异常，降低代码的复杂度和重复度，提高代码的可维护性和可扩展性。
 *
 * 需要考虑以下几点：
 *
 * 异常处理的层次结构：定义异常处理的层次结构，确定哪些异常需要统一处理，哪些异常需要交给上层处理。
 * 异常处理的方式：确定如何处理异常，比如打印日志、返回错误码等。
 * 异常处理的细节：处理异常时需要注意的一些细节，比如是否需要事务回滚、是否需要释放资源等
 * 本文讲述的统一异常处理使用的是 @ControllerAdvice + @ExceptionHandler 来实现的：
 *
 * @ControllerAdvice 表示控制器通知类。
 * @ExceptionHandler 异常处理器。
 * 以上两个注解组合使用，表示当出现异常的时候执行某个通知，即执行某个方法事件，具体实现代码如下：
 */
@ControllerAdvice
public class MyExceptionHandler {
    //拦截所有的空指针异常，进行统一的数据返回
    @ExceptionHandler(NullPointerException.class) //统一处理控制针异常
    /**
     *  返回数据
     */
    @ResponseBody
    public HashMap<String, Object> nullExceptionHandler(NullPointerException e){
        HashMap<String, Object> result = new HashMap<>();
        //与前端定义的异常状态码
        result.put("code", "-1");
        result.put("msg", "空指针异常：" + e.getMessage());
        //返回的数据
        result.put("data", null);
        return result;
    }

}
