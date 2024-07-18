package com.demo.websocket.springboot.way1;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 这里是让web页面，用HTTP的方式调用的方式给websocket的客户端发送消息
 */
@Controller("websocket_system")
@RequestMapping("/api/socket")
public class WebSocketController {
    //页面请求
    //这个方法没用
//    @GetMapping("/index/{userId}")
//    public ModelAndView socket(@PathVariable String userId) {
//        ModelAndView modelAndView = new ModelAndView("/socket1");
//        modelAndView.addObject("userId", userId);
//        return modelAndView;
//    }

    //推送数据接口
    @ResponseBody
    @RequestMapping("/socker/push/{sid}")
    public Map pushData2Web(@PathVariable String sid, String message) {
        Map<String, Object> result = new HashMap<>();
        WebSocketServer.sendInfo(message, sid);
        result.put("cid", sid);
        result.put("message", message);
        return result;
    }
}
