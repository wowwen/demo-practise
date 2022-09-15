package com.demo.redis.jedis.ratelimiter;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

/**
 * 简单限流
 * 这种方案也有缺点，因为它要记录时间窗口内所有的行为记录，如果这
 * 个量很大，比如限定 60s 内操作不得超过 100w 次这样的参数，它是不适合做这样的限流
 * 的，因为会消耗大量的存储空间
 */
public class SimpleRateLimiter {
    private Jedis jedis;

    public SimpleRateLimiter(Jedis jedis) {
        this.jedis = jedis;
    }

    public boolean isActionAllowed(String userId, String actionKey, int period, int maxCount){
        //组装zset的key
        String key = String.format("hist:%s:%s", userId, actionKey);
        long nowTs = System.currentTimeMillis();
        //使用管道提升性能
        Pipeline pipelined = jedis.pipelined();
        //开启事务
        //pipeline是一种批量操作,如何保证其事务?在这里需要声明redis的事务是不支持回滚的。它能保证的是这次的命令都不执行。pipeline期间是独占链接的,在pipeline开启,并未关闭 的情况下,执行其他非pipeline命令将会报异常,这一点也许要注意
        pipelined.multi();
        pipelined.zadd(key, nowTs, "" + nowTs);
        //移除时间窗口之前的行为记录，剩下的都是时间窗口内的
        pipelined.zremrangeByScore(key,0,nowTs - period * 1000);
        //获取窗口内的行为数量
        Response<Long> count = pipelined.zcard(key);
        //设置 zset 过期时间，避免冷用户持续占用内存
        //过期时间应该等于时间窗口的长度，再多宽限 1s
        pipelined.expire(key, period + 1);
        //提交事务
        pipelined.exec();
        //关闭pipelined， 还有一个关闭方法pipelined.sync()
//        pipelined.sync();
        pipelined.close();
        return count.get() <= maxCount;
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        SimpleRateLimiter simpleRateLimiter = new SimpleRateLimiter(jedis);
        for (int i = 0; i < 20; i++) {
            System.out.println(simpleRateLimiter.isActionAllowed("laoqian", "reply", 60, 5));
        }
    }
}
