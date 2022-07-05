package com.example.demo.practise.common.syscurrenttimemillis;

import java.util.concurrent.CountDownLatch;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: CurrentTimeMillisPerfDemo
 * @Author: jiangyw8
 * @Date: 2020-11-6 11:47
 * @Description: 测试System.currentTimeMillis()性能问题
 */
public class CurrentTimeMillisPerfDemo {
    private static final int COUNT = 100;

    public static void main(String[] args) throws InterruptedException {
        //获取纳秒数 秒--毫秒--微秒--纳秒
        //1s=1000ms=1000000微秒=1000000000纳秒=1000000000000皮秒=10^15飞秒=10^18啊秒=10^21仄秒=10^24幺秒
        //1s=10^3ms(毫秒millisecond)=10^6μs(微秒microseconds)=10^9ns(纳秒nanosecond)=10^12ps(皮秒)=10^15fs(飞秒)=10^18as(阿秒)=10^21zm(仄秒)=10^24ym(幺秒)
        long beginTime = System.nanoTime();

        //串行
        for (int i = 0; i < COUNT; i++) {
            System.currentTimeMillis();
        }

        long elapsedTime = System.nanoTime() - beginTime;

        System.out.println("100 System.currentTimeMillis() serial calls: " + elapsedTime + " ns");

        //并行
        //赛跑模型：多个选手（子线程）在起跑线等待发令枪（主线程）响，然后同时开跑
        //发令枪
        CountDownLatch startLatch = new CountDownLatch(1);
        //选手
        CountDownLatch endLatch = new CountDownLatch(COUNT);
        //选手要干的事（跑）
        for (int i = 0; i < COUNT; i++) {
            new Thread(() -> {
                try {
                    startLatch.await();
                    System.currentTimeMillis();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    endLatch.countDown();
                }
            }).start();
        }

        beginTime = System.nanoTime();
        //枪响
        startLatch.countDown();
        //如果endLatch为0则开跑
        endLatch.await();
        elapsedTime = System.nanoTime() - beginTime;
        System.out.println("100 System.currentTimeMillis() parallel calls: " + elapsedTime + " ns");
    }
}
