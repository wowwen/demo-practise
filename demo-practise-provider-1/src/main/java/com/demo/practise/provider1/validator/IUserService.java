package com.demo.practise.provider1.validator;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: IUserService
 * @Author: jiangyw8
 * @Date: 2020-9-25 15:37
 * @Description: TODO
 */
public interface IUserService {
    Integer save(UserDTO userDTO);

    Integer updateById(UserDTO userDTO);

    Integer create(UserDTO userDTO);
}
