package com.example.demo.practise.common.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * ©Copyright ©1968-2020 Midea Group,IT
 *
 * @FileName: OfficialDemo
 * @Author: jiangyw8
 * @Date: 2020-11-8 17:59
 * @Description: TODO
 */
public class OfficialDemo {
    public static void main(String[] args) throws InterruptedException {
        for (int j = 0; j < 10; j++) {
            CountDownLatch startSignal = new CountDownLatch(1);
            CountDownLatch doneSignal = new CountDownLatch(4);
            int threadCount = 0;
            for (int i = 0; i < 4; i++) {
                Thread thread = new Thread(new Worker(startSignal, doneSignal));
                System.out.println("子线程已经创建完毕，start未执行" + Thread.currentThread().getName());
                thread.start();
                System.out.println("子线程开始执行" + Thread.currentThread().getName());
                threadCount++;
                System.out.println("子线程已经创建完毕" + Thread.currentThread().getName());
            }
            System.out.println(threadCount);
            if (threadCount == 4){
                System.out.println("准备，还没有开始跑");  // don't let run yet
                startSignal.countDown();      // let all threads proceed
                System.out.println("主线程已执行countdown()");
            }
            System.out.println("正在跑，等待结束");
            doneSignal.await();           // wait for all to finish
            double v = Math.random() * 10000;
            System.out.println("选手" + Thread.currentThread().getName() + "跑了" + v + "秒");
            Thread.sleep((long) (v));
            System.out.println("结束了");
        }
    }
}

class Worker implements Runnable {
    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;

    Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
        System.out.println("线程" + Thread.currentThread().getName() + "创建完毕");
    }

    @Override
    public void run() {
        try {
            System.out.println("正在等待命令");
            startSignal.await();
            System.out.println("正在跑");
            doneSignal.countDown();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        } // return;

    }
}
