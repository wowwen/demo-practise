package com.demo.cache;

import com.github.benmanes.caffeine.cache.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Caffeine Cache提供了三种缓存填充策略：手动，同步加载，异步加载
 *
 */
public class CaffeineCache {

    /**
     *  手动填充
     *  在每次get key的时候指定一个同步函数，如果key不存在，则调用这个函数生成一个值
     * @param key
     * @return
     */
    public Object manualOperator(String key){
        Cache<String, Object> cache = Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .expireAfterAccess(1, TimeUnit.SECONDS)
                //1.基于缓存的计数进行驱逐
                .maximumSize(10)
                .build();

        //如果一个key不存在，那么会进入指定的函数生成value, cache的get方法：能获取到key所指定的value则获取，获取不到则用function函数生成，设进去，再返回
        Object value = cache.get(key, t -> setValue(key).apply(key));
        //设置另一对k:v,会覆盖旧v
        cache.put("hello", value);

        //判断是否存在，如果不存在则返回null
        Object ifPresent = cache.getIfPresent(key);

        //移除一个key
        cache.invalidate(key);
        return value;
    }

    /**
     * 同步加载
     * 构造Cache时候， build方法传入一个CacheLoader实现类，实现load方法，通过key加载value
     * @param key
     * @return
     */
    public Object syncOperator(String key){
        LoadingCache<Object, Object> cache = Caffeine.newBuilder()
                .maximumWeight(10)
                //根据v的长度来计算权重，然后根据总权重大小来限制容量
                .weigher((k, v) -> (String.valueOf(v).length() + 1))
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .build(k -> setValue(key).apply(key));
        return cache.get(key);
    }

    public Function<String, Object> setValue(String key){
        return t -> key + "value";
    }

    public Weigher<Integer, Integer> setWeight(String key){
        return (t, v) -> key.length() + 1;
    }

    /**
     * 异步加载
     * @param key
     * @return
     */
    public Object asynOperator(String key){
        AsyncLoadingCache<Object, Object> asyncLoadingCache = Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .buildAsync(t -> setAsyncValue(key).get());
        return asyncLoadingCache.get(key);
    }

    public CompletableFuture setAsyncValue(String key){
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            return key + "value";
        });
        return future;
    }

}
