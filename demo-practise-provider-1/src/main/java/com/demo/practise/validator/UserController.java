package com.demo.practise.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: UserDTORest
 * @Author: jiangyw8
 * @Date: 2020-9-25 15:24
 * @Description: TODO
 */
@RestController
@RequestMapping(value = "/user")
//@Validated在类上单独使用，校验不会起作用，没有效果
//@Validated
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 使用@Valid只会校验DTO中未添加group属性的（即默认为default组的）校验注解
     * @param userDTO
     * @return
     */
    @PostMapping(value = "/save")
    public RspDTO save(@Valid @RequestBody UserDTO userDTO){
        System.out.println("===开始执行save()===");
        Integer save = iUserService.save(userDTO);
        return RspDTO.success("0");
    }

    /**
     * 走参数校验注解的 groups 组合校验
     * 使用@Validated会校验group属性和default的校验注解
     * @param userDTO
     * @return
     */
    @PostMapping("/update/groups")
    public RspDTO update(@RequestBody @Validated(Update.class) UserDTO userDTO) {
        iUserService.updateById(userDTO);
        return RspDTO.success("0");
    }

    /**
     * 嵌套校验对象中的对象或者对象中的List<Object>,Controller中用@Valid/@Validated都可以，DTO中一定要用@Valid
     * @param userDTO
     * @return
     */
    @PostMapping("/create/groups/nest")
    public RspDTO create(@RequestBody @Valid UserDTO userDTO){
        iUserService.create(userDTO);
        return RspDTO.success("0");
    }
}
