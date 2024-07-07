package com.demo.redis.jedis.delayqueue;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Type;
import java.util.Set;
import java.util.UUID;

public class RedisDelayingQueue<T> {
    //TODO 类名后加泛型 是啥意思？
    static class TaskItem<T> {
        public String id;

        public T msg;
    }

    // fastjson 序列化对象中存在 generic 类型时，需要使用 TypeReference
    private Type TaskType = new TypeReference<TaskItem<T>>() {
    }.getType();

    private Jedis jedis;
    private String queueKey;

    //构造函数
    public RedisDelayingQueue(Jedis jedis, String queueKey) {
        this.jedis = jedis;
        this.queueKey = queueKey;
    }

    public void delay(T msg) {
        TaskItem<Object> task = new TaskItem<>();
        String id = UUID.randomUUID().toString();
        task.id = id;
        task.msg = msg;
        String value = JSON.toJSONString(task);
        //塞入队列，当前时间加5S为score，意思是5s后会再次处理
        jedis.zadd(queueKey, System.currentTimeMillis() + 5000, value);
    }

    public void loop() {
        while (!Thread.interrupted()) {
            //只取一条,实际上redis中的数据未动，只是时间窗口在不断的往右挪动，并且后面的offset及count限定了从0位开始只取1个，所以取得是第一个
            Set<String> values = jedis.zrangeByScore(queueKey, 0, System.currentTimeMillis(), 0, 1);
            if (values.isEmpty()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
                continue;
            }
            String s = values.iterator().next();
            if (jedis.zrem(queueKey, s) > 0) { //拿到了这个值（锁）
                //将拿到的值利用fastJson反序列化成对象
                TaskItem task = JSON.parseObject(s, TaskType);
                this.handleMsg((T) task.msg);
            }
        }
    }

    public void handleMsg(T msg) {
        System.out.println(msg);
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        RedisDelayingQueue<Object> queue = new RedisDelayingQueue<>(jedis, "delayQueue-demo");
        //生成值往延时队列里面插
        Thread producer = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println(i);
                queue.delay("delayLock-" + i);
            }
        });

        //同一个任务可能会被多个进程取到之后再使用 zrem 进行争抢，那些没抢到
        //的进程都是白取了一次任务，这是浪费，也导致了loop()中的handlMsg方法在打印的时候线程号会出现乱序
        Thread consumer = new Thread(() -> queue.loop());
        //启动线程
        producer.start();
        consumer.start();

        try {
            producer.join();
            Thread.sleep(6000);
            consumer.interrupt();
            consumer.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
