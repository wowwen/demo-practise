package com.example.demo.practise.provider1.validator;

import org.springframework.stereotype.Service;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: UserServiceImpl
 * @Author: jiangyw8
 * @Date: 2020-9-25 15:37
 * @Description: TODO
 */
@Service
public class UserServiceImpl implements IUserService{

    @Override
    public Integer save(UserDTO userDTO) {
        return 1;
    }

    @Override
    public Integer updateById(UserDTO userDTO) {
        return 1;
    }

    @Override
    public Integer create(UserDTO userDTO) {
        return 1;
    }
}
