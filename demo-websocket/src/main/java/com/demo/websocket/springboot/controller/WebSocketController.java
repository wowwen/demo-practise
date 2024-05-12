package com.demo.websocket.springboot.controller;


import com.demo.websocket.springboot.server.WebSocketServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller("websocket_system")
@RequestMapping("/api/socket")
public class WebSocketController {
    //页面请求
    @GetMapping("/index/{userId}")
    public ModelAndView socket(@PathVariable String userId) {
        ModelAndView modelAndView = new ModelAndView("/socket1");
        modelAndView.addObject("userId", userId);
        return modelAndView;
    }

    //推送数据接口
    @ResponseBody
    @RequestMapping("/socker/push/{cid}")
    public Map pushData2Web(@PathVariable String cid, String message) {
        Map<String, Object> result = new HashMap<>();
        WebSocketServer.sendInfo(message, cid);
        result.put("cid", cid);
        result.put("message", message);
        return result;
    }

    public static void main(String[] args) {
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(3);
        integers.add(42);

        integers.remove(1);

        for (Integer integer : integers) {
            System.out.println(integer);
        }
    }

}
