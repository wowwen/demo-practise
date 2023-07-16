package com.demo.practise.validator;

import org.springframework.stereotype.Service;

/**
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
