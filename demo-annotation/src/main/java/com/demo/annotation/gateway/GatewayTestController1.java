package com.demo.annotation.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @author owen
 * @date 2025/4/17 0:52
 * @description
 */
@RestController
public class GatewayTestController1 {

    @GetMapping("/headers")
    public String test(HttpServletRequest request, @RequestHeader(required = false) String header1){
        Enumeration<String> headerNames = request.getHeaderNames();
        String s = headerNames.nextElement();
        System.out.println("heard:" + s);
        return "headers";
    }
}
