package com.demo.multidatasource.aop.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceContextHolder {
    private static final Logger log = LoggerFactory.getLogger(DataSourceContextHolder.class);

    //使用ThreadLocal线程安全的使用变量副本
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置数据源
     */
    public static void setDataSource(String dataSource){
        log.info("设置到数据源：{}", dataSource);
        CONTEXT_HOLDER.set(dataSource);
    }

    /**
     * 获取数据源
     * */
    public static String getDataSource() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清空数据源
     * */
    public static void clearDataSource() {
        CONTEXT_HOLDER.remove();
    }
}
