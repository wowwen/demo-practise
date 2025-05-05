package com.demo.aop;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * AOP的功能1：统一认证
 * 使用spring拦截器实现统一用户登录认证
 *
 * Spring拦截器是Spring框架提供的一个功能强大的组件，用于在请求到达控制器之前或之后进行拦截和处理。拦截器可以用于实现各种功能，如身份验证、日志记录、性能监测等。
 *
 * 要使用Spring拦截器，需要创建一个实现了HandlerInterceptor接口的拦截器类。该接口定义了三个方法：preHandle、postHandle和afterCompletion。
 *
 * preHandle方法在请求到达控制器之前执行，可以用于进行身份验证、参数校验等；
 * postHandle方法在控制器处理完请求后执行，可以对模型和视图进行操作；
 * afterCompletion方法在视图渲染完成后执行，用于清理资源或记录日志。
 *
 * 拦截器的实现可以分为以下两个步骤：
 *
 * step1.创建自定义拦截器，实现 HandlerInterceptor 接口的 preHandle（执行具体方法之前的预处理）方法。
 * step2.将自定义拦截器加入 WebMvcConfigurer 的 addInterceptors 方法中，并且设置拦截规则。
 */

public class LoginInterceptor implements HandlerInterceptor {

    /**
     * step1：创建自定义拦截器，自定义拦截器是一个普通类
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //用户登录业务判断
        HttpSession session = request.getSession(false);
//        if (session != null && session.getAttribute("userInfo") != null){
            return true; //验证成功，继续controller的流程
//        }
        //可以跳转登录界面或者返回401/403等没有权限码
//        response.sendRedirect("/login.html");//跳转到登录页面
//
//        return false;
    }

}

