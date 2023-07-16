package com.demo.practise.common.syscurrenttimemillis;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @FileName: CurrentTimeMillisClockUpgrade
 * @Author: jiangyw8
 * @Date: 2020-11-6 16:48
 * @Description: 针对System.currentTimeMillis()性能问题的解决方案.
 * 如何解决这个问题？最常见的办法是用单个调度线程来按毫秒更新时间戳，相当于维护一个全局缓存。其他线程取时间戳时相当于从内存取，不会再造成时钟资源的争用，
 * 代价就是牺牲了一些精确度。具体代码如下
 * 使用的时候，直接CurrentTimeMillisClock.getInstance().now()就可以了.
 */
public class CurrentTimeMillisClockUpgrade {
    private volatile long now;

    private CurrentTimeMillisClockUpgrade(){
        this.now = System.currentTimeMillis();
        scheduleTick();
    }

    private void scheduleTick(){
        new ScheduledThreadPoolExecutor(1, runnable -> {
            Thread thread = new Thread(runnable, "current-time-millis");
            thread.setDaemon(true);
            return thread;
        }).scheduleAtFixedRate(() -> {
            now = System.currentTimeMillis();
        }, 1, 1, TimeUnit.MILLISECONDS);
    }

    public long now(){
        return now;
    }

    public static CurrentTimeMillisClockUpgrade getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public static class SingletonHolder{
        private static final CurrentTimeMillisClockUpgrade INSTANCE  =  new CurrentTimeMillisClockUpgrade();
    }
}
