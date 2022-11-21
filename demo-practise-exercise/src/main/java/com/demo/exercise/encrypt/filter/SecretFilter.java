package com.demo.exercise.encrypt.filter;

import com.demo.exercise.encrypt.controller.advice.SecretConstant;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author jiang
 */
public class SecretFilter implements Filter {
    /**
     * 存储是否加密的开关
     */
    public static ThreadLocal<Boolean> secretThreadLocal = new ThreadLocal<>();
    /**
     * 存储加密私钥
     */
    public static ThreadLocal<String> clientPrivateKeyThreadLocal = new ThreadLocal<>();

    @Value("${secret.privateKey.h5}")
    private String privateKeyH5;
    @Value("${secret.privateKey.web}")
    private String privateKeyWeb;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //根据请求路由决定是否加密,如果路径中不是以/secret开头的则不需要加密
        if (!request.getRequestURI().startsWith(SecretConstant.PREFIX)) {
            secretThreadLocal.set(Boolean.FALSE);
            filterChain.doFilter(request, response);
        } else {
            //加密
            secretThreadLocal.set(Boolean.TRUE);
            //根据传递的header中的值决定取哪个privateKey
            String privateKey = "H5".equalsIgnoreCase(Objects.requireNonNull(request.getHeader("clientType"))) ? privateKeyH5 : privateKeyWeb;
            clientPrivateKeyThreadLocal.set(privateKey);
            filterChain.doFilter(request, response);
        }
    }
}
