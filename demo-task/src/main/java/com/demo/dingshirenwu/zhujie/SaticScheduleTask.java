package com.demo.dingshirenwu.zhujie;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * @author owen
 * @date 2022/3/29 19:38
 * @description 基于SpringBoot注解创建定时任务---配置类
 */
@Configuration //1.主要用于标记配置类，兼具Component的效果
//@EnableScheduling  //2.开启定时任务
public class SaticScheduleTask {
    //3.添加定时任务
//    @Scheduled(cron = "0/5 * * * * ?")
    //3-1.或者直接指定时间间隔，例如5秒
//    @Scheduled(fixedRate = 5000)
    private void configureTasks(){
        System.out.println("执行静态定时任务时间: " + LocalDateTime.now());
    }

    /**
     * Cron表达式参数分别表示：
     *
     * 秒（0~59） 例如0/5表示每5秒
     * 分（0~59）
     * 时（0~23）
     * 日（0~31）的某天，需计算
     * 月（0~11）
     * 周几（ 可填1-7 或 SUN/MON/TUE/WED/THU/FRI/SAT）
     * @Scheduled：除了支持灵活的参数表达式cron之外，还支持简单的延时操作，例如 fixedDelay ，fixedRate 填写相应的毫秒数即可。
     *
     * @Scheduled 定时任务的核心
     * - cron： cron表达式，根据表达式循环执行，与fixedRate属性不同的是它是将时间进行了切割。（@Scheduled(cron = "0/5 * * * * *")任务将在5、10、15、20..
     * .这种情况下进行工作）
     * - fixedRate： 每隔多久执行一次，无视工作时间（@Scheduled(fixedRate = 1000) 假设第一次工作时间为2021-05-29
     * 16:58:28，工作时长为3秒，那么下次任务的时候就是2021-05-29 16:58:31）
     * - fixedDelay： 当前任务执行完毕后等待多久继续下次任务（@Scheduled(fixedDelay = 3000) 假设第一次任务工作时间为2021-05-29
     * 16:54:33，工作时长为5秒，那么下次任务的时间就是2021-05-29 16:54:41）
     * - initialDelay： 第一次执行延迟时间，只是做延迟的设定，与fixedDelay关系密切，配合使用，相辅相成。
     */
}

