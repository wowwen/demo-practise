package com.demo.practise.common.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @FileName: CountdownLatchTest2
 * @Author: jiangyw8
 * @Date: 2020-11-6 19:25
 * @Description: 测试CountDownLatch赛跑模式
 */
public class CountdownLatchTest2 {
    public static void main(String[] args) {
        for (int j = 0; j < 100; j++) {
            ExecutorService service = Executors.newCachedThreadPool();
            final CountDownLatch cdOrder = new CountDownLatch(1);
            final CountDownLatch cdAnswer = new CountDownLatch(4);

            for (int i = 0; i < 4; i++) {
                Runnable runnable = () -> {
                    try {
                        System.out.println("选手" + Thread.currentThread().getName() + "正在等待裁判发布口令");
                        cdOrder.await();
                        System.out.println("选手" + Thread.currentThread().getName() + "已接受裁判口令");
                        Thread.sleep((long) (Math.random() * 10000));
                        System.out.println("选手" + Thread.currentThread().getName() + "到达终点");
                        cdAnswer.countDown();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                };
                service.execute(runnable);
            }
            try {
                Thread.sleep((long) (Math.random() * 10000));
                System.out.println("裁判" + Thread.currentThread().getName() + "即将发布口令");
                cdOrder.countDown();
                System.out.println("裁判" + Thread.currentThread().getName() + "已发送口令，正在等待所有选手到达终点");
                cdAnswer.await();
                System.out.println("所有选手都到达终点");
                System.out.println("裁判" + Thread.currentThread().getName() + "汇总成绩排名");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            service.shutdown();
            System.out.println("第" + j + "轮结束");
        }
    }

}
