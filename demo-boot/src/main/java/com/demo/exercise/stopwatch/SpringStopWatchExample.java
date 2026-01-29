package com.demo.exercise.stopwatch;

import org.springframework.util.StopWatch;

public class SpringStopWatchExample {

    public static void main(String[] args) throws InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //模拟任务执行
        Thread.sleep(1000);
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());

        StopWatch sw1 = new StopWatch();
        sw1.start("任务1");
        Thread.sleep(2000);
        sw1.stop();
        System.out.println(stopWatch.getLastTaskTimeMillis());

        StopWatch sw2 = new StopWatch();
        sw2.start("任务2");
        Thread.sleep(3000);
        sw2.stop();
        sw2.start("任务3");
        Thread.sleep(4000);
        sw2.stop();
        sw2.start("任务4");
        Thread.sleep(5000);
        sw2.stop();

        System.out.println(sw2.prettyPrint());

        /**
         * 更多用法
         * 不同的打印结果
         *
         * getTotalTimeSeconds() 获取总耗时秒，同时也有获取毫秒的方法
         * prettyPrint() 优雅的格式打印结果，表格形式
         * shortSummary() 返回简短的总耗时描述
         * getTaskCount() 返回统计时间任务的数量
         * getLastTaskInfo().getTaskName() 返回最后一个任务TaskInfo对象的名称
         */
    }
}
