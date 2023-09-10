package com.demo.dingshirenwu.dynamic.update;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/task")
public class ScheduleController {
    private final ScheduleTask scheduleTask;

    public ScheduleController(ScheduleTask scheduleTask) {
        this.scheduleTask = scheduleTask;
    }

    @GetMapping("/updateCron")
    public String updateCron(String cron){
        log.info("新的 cron:{}", cron);
        scheduleTask.setCron(cron);
        return "成功";
    }

    @GetMapping("/updateTimer")
    public String updateTimer(Long timer){
        log.info("新的 timer:{}", timer);
        scheduleTask.setTimer(timer);
        return "成功";
    }


}
