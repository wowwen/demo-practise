package com.demo.netty.proxy.service;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.demo.netty.proxy.mapper.AccountMapper;
import com.demo.netty.proxy.model.ServiceAccount;
import com.demo.redis.lettuce.helper.RedisHelper;
import com.demo.redis.lettuce.helper.RedisKeyEnum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author owen
 * @date 2024/8/29 14:09
 * @description 客户端账号服务类
 */
@Service
public class AccountService {

    @Resource
    private AccountMapper accountMapper;
    @Resource
    private RedisHelper redisHelper;

    public ServiceAccount selectByName(String userName) {
        Object obj = redisHelper.hGet(RedisKeyEnum.HASH_USER_ACCOUNT_PROXY, userName);
        if (ObjectUtil.isNotNull(obj)) {
            return JSON.parseObject(obj.toString(), ServiceAccount.class);
        }
        Optional<ServiceAccount> byName = Optional.ofNullable(accountMapper.selectByName(userName));
        if (byName.isPresent()) {
            redisHelper.hSetWithTTLUseMulti(RedisKeyEnum.HASH_USER_ACCOUNT_PROXY, userName,
                    JSON.toJSONString(byName.get()), 3600);
            return byName.get();
        }

        return null;
    }
}
