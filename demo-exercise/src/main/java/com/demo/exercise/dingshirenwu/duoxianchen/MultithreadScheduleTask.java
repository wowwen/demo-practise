package com.demo.exercise.dingshirenwu.duoxianchen;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author jiangyw
 * @date 2022/3/30 8:59
 * @description 基于注解设定多线程定时任务
 */
//@Component注解用于对那些比较中立的类进行注释；
//相对与在持久层、业务层和控制层分别采用 @Repository、@Service 和 @Controller 对分层中的类进行注释
@Component
//@EnableScheduling   // 1.开启定时任务
@EnableAsync        // 2.开启多线程
public class MultithreadScheduleTask {

    //注：这里的@Async注解很关键
    @Async
    @Scheduled(fixedDelay = 1000) //间隔1秒执行
    public void first() throws InterruptedException {
        System.out.println("第一个定时任务开始于：" + LocalDateTime.now().toLocalTime() + "\r\n线程号：" + Thread.currentThread().getName());
        System.out.println();
        //睡10秒，模拟业务进行
        Thread.sleep(1000 * 10);
    }

    @Async
    @Scheduled(fixedDelay = 2000)
    public void second(){
        System.out.println("第二个定时任务开始于：" + LocalDateTime.now().toLocalTime() + "\r\n线程号：" + Thread.currentThread().getName());
        System.out.println();
    }

    /**
     * 第一个定时任务开始于：11:15:52.450
     * 线程号：task-1
     *
     * 第二个定时任务开始于：11:15:52.450
     * 线程号：task-2
     *
     * 第一个定时任务开始于：11:15:53.454
     * 线程号：task-3
     *
     * 第二个定时任务开始于：11:15:54.460
     * 线程号：task-4
     *
     * 第一个定时任务开始于：11:15:54.460
     * 线程号：task-5
     *
     * 第一个定时任务开始于：11:15:55.466
     * 线程号：task-6
     *
     * 第二个定时任务开始于：11:15:56.460
     * 线程号：task-7
     *
     * 第一个定时任务开始于：11:15:56.477
     * 线程号：task-8
     *
     * 第一个定时任务开始于：11:15:57.478
     * 线程号：task-2
     *
     * 第二个定时任务开始于：11:15:58.468
     * 线程号：task-4
     *
     * 结论：从截图中可以看出，第一个和第二个定时任务互不影响；并且，由于开启了多线程，第一个任务的执行时间也不受其本身执行时间的限制，所以需要注意可能会出现重复操作导致数据异常
     */

}
