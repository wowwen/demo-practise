package com.demo.redis.jedis.hyperloglog;

import redis.clients.jedis.Jedis;

public class PfTest {
    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        for (int i = 0; i < 100000; i++) {
            jedis.pfadd("count", "user" + i);
            //非精确统计值，加到100的时候已经不相等了
            long total = jedis.pfcount("count");

            if (total != i + 1){
                System.out.println(total + "||"+ (i + 1));
                break;
            }
        }
        jedis.close();
    }
}
