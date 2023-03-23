package com.demo.cache;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

/**
 * CaffeineCache提供三种缓存回收策略：基于大小回收，基于时间回收，基于引用回收
 *                                基于大小回收又分为两种：1、基于缓存大小 2、基于权重
 */
public class CaffeineCacheEvict {

    /**
     * 基于大小的回收方式
     * @return
     */
    //基于缓存大小进行驱逐
    public Object CacheEvictBySize(){
        Cache<Object, Object> build = Caffeine.newBuilder()
                .maximumSize(100)
                .build();
        return build;
    }

    //基于权重进行驱逐
    //maximumWeight()与weigher()配合使用，如果只指定了weigher()，而没有指定maximumWeight(),则会报错java.lang.IllegalStateException: weigher requires maximumWeight
    public Object CacheEvictByWeight(){
        Cache<Object, Object> build = Caffeine.newBuilder()
                .maximumWeight(100)
                //按value的长度设置权重
                .weigher((k, v) -> (String.valueOf(v).length() + 1))
                .build();
        return build;
    }

    /**
     * 基于引用
     * 基于引用的回收策略，核心时利用JVM的GC回收机制来达到清理数据的目的。当一个对象不再被引用的时候，jvm会选择在适当的时候将其回收。
     * Caffeine支持三种不同的基于引用的回收方法:1.weakKeys() 采用 弱引用 的方式存储key值内容，当key对象不再被引用的时候，由GC进行回收
     *                                    2.weakValues() 采用 弱引用 的方式存储value值内容，当value对象不再被引用的时候，由GC进行回收
     *                                    3.softValues() 采用 软引用 的方式存储value值内容，不会被GC回收，但是当内存容量满时，基于LRU（least-recently-used最近最少使用）策略进行回收
     * @param key
     * @return
     */
    public Object CacheEvictWeakKeys(String key){
        LoadingCache<Object, Object> cache = Caffeine.newBuilder()
                .weakKeys()
                //同步加载，传入一个CacheLoader实现类。实现load方法，通过key加载value
                .build(k -> String.valueOf(k).length() + 1);
        return cache.get(key);
    }

    public Object CacheEvictWeakValues(String key){
        LoadingCache<Object, Object> cache = Caffeine.newBuilder()
                .weakValues()
                .build(k -> String.valueOf(k));
        return cache.get(key);
    }

    public Object CacheEvictBoth(String key){
        LoadingCache<Object, String> cache = Caffeine.newBuilder()
                .weakKeys()
                .weakValues()
                .build(k -> String.valueOf(k));
        return cache.get(key);
    }

    public Object CacheEvictSoftValues(String key){
        LoadingCache<Object, String> cache = Caffeine.newBuilder()
                .softValues()
                .build(k -> String.valueOf(k));
        return cache.get(key);
    }

    /**
     * 实际使用时注意，weakValues不支持在AsyncLoadingCache中使用，比如下面代码启动运行时候会报错
     * Exception in thread "main" java.lang.IllegalStateException: Weak or soft values cannot be combined with AsyncLoadingCache
     * 	at com.github.benmanes.caffeine.cache.Caffeine.requireState(Caffeine.java:201)
     * 	at com.github.benmanes.caffeine.cache.Caffeine.buildAsync(Caffeine.java:1192)
     * 	at com.github.benmanes.caffeine.cache.Caffeine.buildAsync(Caffeine.java:1167)
     * 	at com.veezean.skills.cache.caffeine.CaffeineCacheService.main(CaffeineCacheService.java:297)
     * ————————————————
     * 版权声明：本文为CSDN博主「小二上酒8」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
     * 原文链接：https://blog.csdn.net/Huangjiazhen711/article/details/127900732
     * @param args
     */
/*
    public static void main(String[] args) {
        AsyncLoadingCache<Object, String> cache = Caffeine.newBuilder()
                .weakValues()
                .buildAsync(k -> String.valueOf(k));
    }
*/

    /**
     * 与weakValues一样，需要注意softValues也不支持在AsyncLoadingCache中使用。此外，还需要注意softValues与weakValues两者也不可以一起使用,否则启动时也会报错
     * Exception in thread "main" java.lang.IllegalStateException: Value strength was already set to WEAK
     * 	at com.github.benmanes.caffeine.cache.Caffeine.requireState(Caffeine.java:201)
     * 	at com.github.benmanes.caffeine.cache.Caffeine.softValues(Caffeine.java:572)
     * 	at com.veezean.skills.cache.caffeine.CaffeineCacheService.main(CaffeineCacheService.java:297)
     * @param args
     */
/*
    public static void main(String[] args) {
        LoadingCache<String, User> cache = Caffeine.newBuilder()
                .weakKeys()
                .weakValues()
                .softValues()
                .build(key -> userDao.getUser(key));
    }
*/





    public static void main(String[] args) {

    }

}
