package com.demo.redis.jedis.watch;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * 乐观锁
 * watch监测到key变动，就会放弃执行操作
 */
public class TransactionDemo {
    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        String userId = "user_1";
        String key = keyFor(userId);
        //setnx做初始化
        jedis.setnx(key, String.valueOf(5));
        System.out.println(doubleAccountValue(jedis, userId));
        jedis.close();
    }

    public static final int doubleAccountValue(Jedis jedis, String userId){
        String key = keyFor(userId);
        while (true){
            jedis.watch(key);
            int value = Integer.parseInt(jedis.get(key));
            value *= 2; //翻倍
            //开启事务
            Transaction tx = jedis.multi();
            tx.set(key, String.valueOf(value));
            //执行
            List<Object> result = tx.exec();
            if (result != null){
                //成功了
                break;
            }
        }
        return Integer.parseInt(jedis.get(key));
    }

    public static String keyFor(String userId){
        //String.format得这么用
        String format = String.format("account_{%s}", userId);
        System.out.println("format:"+  format);
        return format;
    }
}
