package com.demo.practise.common.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author owen
 * @date 2024/10/22 1:22
 * @description 时间日期处理工具类
 */
public class DateHelper {

    static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 校验时间格式是否为 yyyy-MM-dd HH:mm:ss
     *
     * @param date 字符串yyyy-MM-dd HH:mm:ss
     * @return Boolean
     */
    public static Boolean isDateVail(String date) {
        boolean flag = true;
        try {
            LocalDateTime.parse(date, dtf);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
