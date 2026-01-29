package com.demo.exercise.encrypt.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class User {
    private Integer id;

    private String name;

    private UserTypeEnum userTypeEnum = UserTypeEnum.COMMON;
    /**
     * 注解@JsonFormat主要是后台到前台的时间格式的转换
     * 注解@DataFormat主要是前后到后台的时间格式的转换
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registerTime;
}