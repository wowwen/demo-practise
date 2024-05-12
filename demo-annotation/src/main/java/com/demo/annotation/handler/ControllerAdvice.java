//package com.demo.annotation.handler;
//
//
//import org.apache.coyote.Response;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import org.springframework.ui.Model;
//import org.springframework.web.bind.WebDataBinder;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.InitBinder;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//import java.text.SimpleDateFormat;
//import java.util.HashMap;
//
//@org.springframework.web.bind.annotation.ControllerAdvice(basePackages = {"com.demo.annotation.springmvc"})//切springmvc这个包
//public class ControllerAdvice {
//
//    /**
//     * 采用@InitBinder在类中进行全局的配置
//     * @param binder
//     */
//    @InitBinder
//    public void binder(WebDataBinder binder){
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        format.setLenient(false); //设置是否严格按照预定的个数来解析输入
//    }
//
////    @ModelAttribute
////    public void modelAttributeChange(Model model){
////        HashMap<String, String> map = new HashMap<>();
////        map.put("msg", "user not found exception");
////        model.addAllAttributes(map);
////    }
//
//    @ExceptionHandler(BusinessException.class)
//    public ResponseEntity businessExceptionHandler(BusinessException ex){
//        return new ResponseEntity(ex.toString(), HttpStatus.UNAUTHORIZED);
//    }
//
//////    @ResponseStatus(HttpStatus.BAD_REQUEST) //使用此注解，可以指定响应所需要的HTTP STATUS--实测结论：bull shit
////    @ExceptionHandler(NotFoundException.class)
////    public ResponseEntity notFoundExceptionHandler(NotFoundException ex){
////        return new ResponseEntity("not found", HttpStatus.NOT_FOUND);
////    }
//
//}
