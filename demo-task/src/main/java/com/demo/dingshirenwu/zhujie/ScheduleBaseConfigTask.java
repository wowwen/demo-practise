package com.demo.dingshirenwu.zhujie;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 本类演示如何基于application.yml中的配置信息开启定时任务，
 * 并支持cron表达式可配置，此例中在application.yml中配置如下
 * spring：
 *      task:
 *       #定时处理redis数据cron表达式
 *       cron: 0 0 23 * * ?
 */
@Configuration
@ConditionalOnProperty(value = "spring.task.cron", matchIfMissing = false)
@EnableScheduling
public class ScheduleBaseConfigTask {

    @Scheduled(cron = "${spring.task.cron}")
    public void test(){
        System.out.println("执行定时任务");
    }
}
