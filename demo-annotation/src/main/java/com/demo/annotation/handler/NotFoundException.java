package com.demo.annotation.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(HttpStatus.BAD_REQUEST) //方式1，加上此注解同时需要注意在异常处理类ControllerAdvice中将异常NotFoundException的处理方法注释，
                                          //或者将处理方法中响应体中不设置HttpStatus的状态码（返回ResponseEntity不行，ResponseEntity构造函数必须提供HttpStatus状态码）
public class NotFoundException  extends RuntimeException{
}
