package com.demo.redis.lettuce.bloomfilter;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * 当布隆过滤器说某个值存在时，这个值可能不存在；当它说不存在时，那就肯定不存
 *  在。
 *
 *  原因就在于布隆过滤器对于已经见过的元素肯定不会误判，它只会误判那些没见过的元
 * 素。
 *
 * 用布隆过滤器时， redis需要添加布隆过滤器插件
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedissonBloomFilter {

    @Resource
    private RedissonClient redissonClient;

    @Test
    public void bloomFilterTest(){
        RBloomFilter<Object> bloomFilter = redissonClient.getBloomFilter("bloom-filter");
        //初始化，容器10000.容错率千分之一
        bloomFilter.tryInit(10000, 0.001);
        //添加10000个
        for (int i = 0; i < 100; i++) {
            bloomFilter.add("YuShiwen" + i);
        }
        //用来统计误判的个数
        int count = 0;
        //查询不存在的数据一千次
        for (int i = 0; i < 10; i++) {
            if (bloomFilter.contains("xiaocheng" + i)) {
                count++;
            }
        }
        System.out.println("判断错误的个数："+count);
        System.out.println("YuShiwen9是否在过滤器中存在："+bloomFilter.contains("YuShiwen9"));
        System.out.println("YuShiwen1是否在过滤器中存在："+bloomFilter.contains("YuShiwen1"));
        System.out.println("预计插入数量：" + bloomFilter.getExpectedInsertions());
        System.out.println("容错率：" + bloomFilter.getFalseProbability());
        System.out.println("hash函数的个数：" + bloomFilter.getHashIterations());
        System.out.println("插入对象的个数：" + bloomFilter.count());
    }

}
