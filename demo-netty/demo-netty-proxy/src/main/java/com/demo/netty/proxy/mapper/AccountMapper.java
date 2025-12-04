package com.demo.netty.proxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.demo.netty.proxy.model.ServiceAccount;
import org.apache.ibatis.annotations.Param;

/**
 * @author owen
 * @date 2024/9/1 2:07
 * @description
 */
public interface AccountMapper extends BaseMapper<ServiceAccount> {

    ServiceAccount selectByName(@Param("username")String username);
}
