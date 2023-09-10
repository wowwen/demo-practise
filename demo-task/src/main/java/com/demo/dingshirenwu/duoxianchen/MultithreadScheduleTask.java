package com.demo.dingshirenwu.duoxianchen;

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
//@EnableScheduling   // 1.开启定时任务.情景2注释掉
//@EnableAsync        // 2.开启多线程   情景2注释掉
public class MultithreadScheduleTask {
    //情景2
    //注：这里的@Async注解很关键，通过此注解创建异步线程.如果只注解@Async而不开启上面的@EnableAsync注解，则first()任务与second()任务再同一个线程中执行。另外，即使本类上的@EnableScheduling被注释了
    //但是在方法上有注解@Scheduled，还是会定期执行。
    //由于第一个定时任务first()中线程sleep了10s，超过了second()中的@Scheduled(fixedDelay = 2000)，所以first()执行完之后，会立即执行second（）；而second执行完之后，second没有sleep，所以再执行时
    //先是first上面的@Scheduled(fixedDelay = 1000)要延迟1s，再执行。
    //执行结果:
    //第一个定时任务开始于：12:21:38.455
    //线程号：pool-1-thread-1
    //
    //2023-09-10 12:21:38.460  INFO 110852 --- [           main] com.demo.dingshirenwu.TaskApplication    : Started TaskApplication in 3.984 seconds (JVM running for 5.499)
    //第二个定时任务开始于：12:21:48.458
    //线程号：pool-1-thread-1
    //
    //2023-09-10 12:21:48.459  INFO 110852 --- [pool-1-thread-1] c.d.d.dynamic.update.ScheduleTask        : current time:12:21:48.459
    //第一个定时任务开始于：12:21:49.474
    //线程号：pool-1-thread-1
    //
    //2023-09-10 12:21:59.487  INFO 110852 --- [pool-1-thread-1] c.d.d.dynamic.update.ScheduleTask        : current time:12:21:59.487
    //第二个定时任务开始于：12:21:59.488
    //线程号：pool-1-thread-1
    //
    //2023-09-10 12:22:00.007  INFO 110852 --- [pool-1-thread-1] c.d.d.dynamic.update.ScheduleTask        : current time:12:22:00.007
    //第一个定时任务开始于：12:22:00.502
    //线程号：pool-1-thread-1
    //
    //第二个定时任务开始于：12:22:10.517
    //线程号：pool-1-thread-1
    @Async
//    @Scheduled(fixedDelay = 1000) //间隔1秒执行
    public void first() throws InterruptedException {
        System.out.println("第一个定时任务开始于：" + LocalDateTime.now().toLocalTime() + "\r\n线程号：" + Thread.currentThread().getName());
        System.out.println();
        //睡10秒，模拟业务进行
        Thread.sleep(1000 * 10);
    }

    @Async
//    @Scheduled(fixedDelay = 2000)
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
