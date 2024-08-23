package com.demo.redis.lettuce.bloomfilter;

import com.demo.redis.lettuce.helper.RedisHelper;
import com.demo.redis.lettuce.helper.RedisKeyEnum;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author jiangyw
 * @date 2024/8/10 17:41
 * @description RedisHelper测试类
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisHelperTest {

    @Resource
    RedisHelper redisHelper;

    @Test
    public void setBitTest(){
        Boolean aBoolean = redisHelper.setBit(RedisKeyEnum.BIT_TEST, 0, true);
        System.out.println(aBoolean);
    }

    @Test
    public void setBitFieldTest(){
        List<Long> longs = redisHelper.setBitField(RedisKeyEnum.BIT_FIELD_TEST);
        System.out.println(longs);
    }

    @Test
    public void getBitTest(){
        Boolean bit = redisHelper.getBit(RedisKeyEnum.BIT_TEST, 1);
        System.out.println(bit);
    }

    @Test
    public void addHyper(){
        Long aLong = redisHelper.addHyperLogLog(RedisKeyEnum.HYPER_LOG_TEST, Lists.newArrayList(1, 2, 3));
        System.out.println(aLong);
    }

    @Test
    public void countHyper(){
        Long aLong = redisHelper.countHyperLogLog(RedisKeyEnum.HYPER_LOG_TEST);
        System.out.println(aLong);
    }

    @Test
    public void countHyperKeys(){
        Long aLong = redisHelper.countHyperLogLog(Arrays.asList(RedisKeyEnum.HYPER_LOG_TEST));
        System.out.println(aLong);
    }

    @Test
    public void deleteHyperLogLog(){
        redisHelper.deleteHyperLogLog(RedisKeyEnum.HYPER_LOG_TEST);
    }
}
