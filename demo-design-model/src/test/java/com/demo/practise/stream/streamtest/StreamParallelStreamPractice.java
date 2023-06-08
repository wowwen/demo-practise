package com.demo.practise.stream.streamtest;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Slf4j
public class StreamParallelStreamPractice {

    /**
     * 测试并行流
     */
    @Test
    public void testParallelStream(){
        //并行流时，并非按照1， 2， 3。。。500的顺序输出
        IntStream.range(1, 500).parallel().forEach(System.out::print);
    }

    /**
     * 测试ParallelStream阻塞
     * @throws InterruptedException
     */
    @Test
    public void testParallelStreamBlock() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Set<Thread> threadSet = new CopyOnWriteArraySet<>();
        Thread threadA = new Thread(() -> {
            long n = 321_0000_6587L;
            LongStream.range(1, n)
                    .parallel()
                    .peek(d -> {
                        Thread currentThread = Thread.currentThread();
                        if (!threadSet.contains(currentThread)) {
                            log.info("线程A工作线程：{}", currentThread);
                            threadSet.add(currentThread);
                        }
                    }).sum();
            countDownLatch.countDown();
        });
        Thread threadB = new Thread(() -> {
            long n = 321_0000_6587L;
            long sumB = LongStream.range(1, n)
                    .parallel()
                    .peek(d -> {
                        Thread currentThread = Thread.currentThread();
                        if (!threadSet.contains(currentThread)) {
                            log.info("线程B工作线程：{}", currentThread);
                            threadSet.add(currentThread);
                        }
                    }).sum();
            countDownLatch.countDown();
        });

        threadA.start();
        threadB.start();
        countDownLatch.await();
    }
}
