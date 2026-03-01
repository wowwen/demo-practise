package com.demo.practise.common.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 *
 * @FileName: CountdownLatchTest3
 * @Author: owen
 * @Date: 2020-11-8 0:34
 * @Description: new Thread()之后选手已经创建出来，thread.start()之后选手（子线程）已经就绪，begin.countDown()则裁判发令可以开跑了，
 * 但是具体什么时候执行子线程中的run方法，是由cpu中竞态条件决定的，没法控制，好比即使发令枪响，有的人也过了会才起跑。
 */
public class CountdownLatchTest3 {

    // 模拟了100米赛跑，10名选手已经准备就绪，只等裁判一声令下。当所有人都到达终点时，比赛结束。
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            System.out.println("第" + i + "轮开始");
            // 开始的倒数锁
            final CountDownLatch begin = new CountDownLatch(1);
            // 结束的倒数锁
            final CountDownLatch end = new CountDownLatch(4);
            // 4名选手
            for (int index = 0; index < 4; index++) {
                Thread thread = new Thread(new Player(begin, end), " "+ index);
                System.out.println("选手编号为" + thread.getName() + "号");
                thread.start();
                System.out.println(thread.getName() + "号选手已经准备就绪");
            }
            System.out.println("裁判" + Thread.currentThread().getName() + "即将发布口令");
            // begin减1，计算变为0，开始游戏
            begin.countDown();
            System.out.println("裁判" + Thread.currentThread().getName() + "已发送口令，正在等待所有选手到达终点");
            // 等待end变为0，即所有选手到达终点
            end.await();
            System.out.println("所有选手都到达终点");
            System.out.println("裁判" + Thread.currentThread().getName() + "汇总成绩排名");
            System.out.println("第" + i + "轮结束");
            System.out.println();
        }
    }
}

class Player implements Runnable {
    // 开始的倒数锁
    private final CountDownLatch begin;
    // 结束的倒数锁
    private final CountDownLatch end;

    Player(CountDownLatch begin, CountDownLatch end) {
        this.begin = begin;
        this.end = end;
        System.out.println("选手已经创建完毕");
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            // 如果当前计数为零，则此方法立即返回。
            // 等待
            System.out.println("选手" + Thread.currentThread().getName() + "正在等待裁判发布口令");
            begin.await();
            System.out.println("选手" + Thread.currentThread().getName() + "已接受裁判口令，开始跑了");
            double v = Math.random() * 10000;
            System.out.println("选手" + Thread.currentThread().getName() + "跑了" + v + "秒");
            Thread.sleep((long) (v));
            System.out.println("选手" + Thread.currentThread().getName() + "到达终点");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 每个选手到达终点时，end就减一,直至所有人都到达终点
            end.countDown();
        }
    }
}
