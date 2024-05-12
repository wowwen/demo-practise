package com.demo.annotation.springmvc;

import com.demo.annotation.handler.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Spring MVC的注解
 */
@RestController
public class ControllerAnnotation {

    @RequestMapping(path = "/mvc1")
    public void path() {
        System.out.println("mvc");
    }

    @RequestMapping(path = "/mvc2", params = "query")
    public void params(@RequestParam String query) {
        System.out.println("mvc with params" + query);
    }

    @RequestMapping(value = "/mvc3", headers = "Accept=application/json")
    public void headers() {
        System.out.println("mvc with headers");
    }

    @RequestMapping(value = "/mvc4", consumes = "application/json") //- 指定请求的Content-Type,- 只映射到Content-Type为"application/json"的请求。
    public void consumes() {
        System.out.println("mvc with consumes");
    }

    @RequestMapping(value = "/mvc5", produces = "application/json") //- 指定响应的Content-Type,- 表示该处理器将返回JSON格式的数据
    public void produces() {
        System.out.println("mvc with produces");
    }

    @PatchMapping("/patch")
    public ResponseEntity patch() {
        return new ResponseEntity("patch方法响应", HttpStatus.OK);
    }

    @GetMapping("/user/notFound")
    public ResponseEntity notFound() {
        throw new NotFoundException();
    }

    @GetMapping("/user/notFound1")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String norFound1() {
        return String.valueOf("not Found 1");
    }

    @RequestMapping("/user/notFound2")
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "NOT FOUND 2 REASON")
    public String norFound2() {
        return String.valueOf("not Found 2");
    }

    @GetMapping("/user/{id}/roles/{roleId}")
    public String getUserRole(@PathVariable(name = "id") Integer id, @PathVariable(required = false) Integer roleId) {
        return "id:" + id + "roleId:" + roleId;
    }

    @GetMapping("/user/parameter")
    public void getUserRoleByParam(@RequestParam(name = "id") Integer id, @RequestParam(defaultValue = "0") Integer roleId) {
        System.out.println("id:" + id + "roleId:" + roleId);
    }

    @PostMapping("/user/save")
    public void saveUser(@ModelAttribute("user") User user) {
        System.out.println(user.id);
    }

//    @PostMapping("/user/save")
//    public void saveUser(@ModelAttribute User user) { //与@PathVariable和@RequestParam注解一样，如果参数名与模型具有相同的名字，则不必指定索引名称，简写示例如此例
//        System.out.println(user.id);
//    }

    public class User {
        private Integer id = 1;
        private String name;
    }

    @PostMapping("/user/save1")
    public void saveUser1(@ModelAttribute("zhangsan") User zhangsan) {
        System.out.println(zhangsan.id);
    }

    @ModelAttribute("zhangsan")
    User getUser() {
        User user = new User();
        user.id = 2;
        user.name = "zhangsan";
        return user;
    }

    @ModelAttribute("lisi")
    User getUser1() {
        User user = new User();
        user.id = 3;
        user.name = "lisi";
        return user;
    }

}
