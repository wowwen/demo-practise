package com.demo.dingshirenwu.dynamic.update;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.PeriodicTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Date;

@Data
@Slf4j
@Component
@PropertySource("classpath:/task-config.ini") //指定配置文件
public class ScheduleTask implements SchedulingConfigurer {

    @Value("${printTime.cron}")
    private String cron;

    private Long timer = 10000L;

    /**
     * 方式1：使用CronTrigger
     * @param scheduledTaskRegistrar
     */
//    @Override
//    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
//        //动态使用cron表达式设置循环间隔
//        scheduledTaskRegistrar.addTriggerTask(new Runnable() {
//                                                  @Override
//                                                  public void run() {
//                                                      log.info("当前时间:{}", LocalTime.now());
//                                                  }
//                                              },
//                new Trigger() {
//                    @Override
//                    public Date nextExecutionTime(TriggerContext triggerContext) {
//                        //使用CronTrigger触发器，可动态修改cron表达式来操作循环规则
//                        CronTrigger cronTrigger = new CronTrigger(cron);
//                        Date nextExecutionTime = cronTrigger.nextExecutionTime(triggerContext);
//                        return nextExecutionTime;
//                    }
//                });
//    }

    /**
     * 方式2：区别于CronTrigger，该触发器可以随意设置循环间隔，不想Cron表达式只能定义小于等于间隔59秒
     * @param scheduledTaskRegistrar
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        //动态使用cron表达式设置循环间隔
        scheduledTaskRegistrar.addTriggerTask(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      log.info("当前时间:{}", LocalTime.now());
                                                  }
                                              },
                new Trigger() {
                    @Override
                    public Date nextExecutionTime(TriggerContext triggerContext) {
                        //使用不同的触发器，为设置循环时间的关键，区别于CronTrigger触发器，该触发器可随意设置循环间隔时间，单位为毫秒
                        PeriodicTrigger periodicTrigger = new PeriodicTrigger(timer);
                        Date nextExecutionTime = periodicTrigger.nextExecutionTime(triggerContext);
                        return nextExecutionTime;
                    }
                });
    }



}
