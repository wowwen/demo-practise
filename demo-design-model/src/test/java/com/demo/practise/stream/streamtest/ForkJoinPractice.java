package com.demo.practise.stream.streamtest;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

@Slf4j
public class ForkJoinPractice {

    /**
     * 1至1000006587的单线程累加
     */
    @Test
    public void testAddSingleThread(){
        long begin = System.currentTimeMillis();
        int n = 10_0000_6587;
        long sum = 0;
        for (int i = 0; i <= n; i++) {
            sum += i;
        }
        log.info("1到{}的和为：{}", n, sum);
        long end = System.currentTimeMillis();
        log.info("执行花费的时间为：{}", end - begin);
    }

    /**
     * 1至1000006587的Fork/Join方式累加
     */
    @Test
    public void testAddForkJoin(){
        long begin = System.currentTimeMillis();
        int n = 10_0000_6587;
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        log.info("1到{}的和为：{}", n, forkJoinPool.invoke(new NumberAddTask(1, n)));
        long end = System.currentTimeMillis();
        log.info("ForkJoin方式的执行时间为：{}", end - begin);
    }

    public class NumberAddTask extends RecursiveTask<Long>{
        private static final int THRESHOLD = 10_0000;
        private final int begin;
        private final int end;

        public NumberAddTask(int begin, int end) {
            super();
            this.begin = begin;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - begin <= THRESHOLD){
                long sum = 0;
                for (int i = begin; i <= end ; i++) {
                    sum += i;
                }
                return sum;
            }

            int mid = (begin + end)/2;
            NumberAddTask t1 = new NumberAddTask(begin, mid);
            NumberAddTask t2 = new NumberAddTask(mid + 1, end);
            ForkJoinTask.invokeAll(t1, t2);
            return t1.join() + t2.join();
        }
    }

    /**
     * Fork/Join并非万能的
     * 本方法根据官方API蹄冻的Fork/Join方式测试斐波那契数列，效果不佳，甚至不如单线程的递归方式，说明Fork/Join不是万能的
     */
    @Test
    public void testFibonacciForkJoin(){
        //执行f(40) = 102334155使用3411ms
        long begin = System.currentTimeMillis();
        int n = 20;
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        log.info("ForkJoinPool初始化时间：{}ms", System.currentTimeMillis() - begin);
        log.info("斐波那契数列f({}) = {}",n, forkJoinPool.invoke(new FibonacciTask(n)));
        long end = System.currentTimeMillis();
        log.info("ForkJoin方式执行时间：{}", end - begin);
    }

    /**
     * 不用递归计算斐波那契数列反而更快
     */
    @Test
    public void testFibonacci(){
        long begin = System.currentTimeMillis();
        int n = 50000;
        log.info("斐波那契数列f({}) = {}", n, FibonacciUtil.fibonacci(n));
        long end = System.currentTimeMillis();
        log.info("函数方式执行时间：{}ms", end - begin);
    }

    protected class FibonacciTask extends RecursiveTask<Long>{
        private final Integer n;

        public FibonacciTask(Integer n) {
            super();
            this.n = n;
        }

        @Override
        protected Long compute() {
            if (this.n <= 1){
                return this.n.longValue();
            }
            System.out.println("n_0 = " + n);
            FibonacciTask t1 = new FibonacciTask(this.n - 1); //这里时间上形成了一个循环，每次将减后的值又传进FibonacciTask(n)的构造函数
            System.out.println("n_1 = " + n);
            FibonacciTask t2 = new FibonacciTask(this.n - 2);
            System.out.println("n_2 = " + n);
            ForkJoinTask.invokeAll(t1, t2);
            return t1.join() + t2.join();
        }
    }

    /**
     * 斐波那契数列工具类
     */
    public static class FibonacciUtil{

        /**
         * 计算第n位上的斐波那契值
         * @param n
         * @return 返回第n位上的斐波那契值
         */
        public static long calculate(int n){
            if (n < 2){
                return Math.max(n ,0);
            }else {
                return calculate(n -1) + calculate(n -2);
            }
        }

        public static BigInteger fibonacci(int n){
            if (n < 2){
                return BigInteger.valueOf(n);
            }else {
                BigInteger[] ans = new BigInteger[n];
                ans[0] = BigInteger.valueOf(1);
                ans[1] = BigInteger.valueOf(2);
                for (int i = 2; i < n ; i++) {
                    ans[i] = ans[i - 1].add(ans[i - 2]);
                }
                return ans[n - 1];
            }
        }
    }
}
