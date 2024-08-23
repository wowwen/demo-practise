package com.demo.redis.lettuce.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.reactivex.internal.util.LinkedArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * opsForZSet()：对应redis中的zset （有序集合）
 * opsForValue()：对应redis中的String (字符串类型)
 * opsForHash()：对应redis中的Hash （哈希）
 * opsForList()：对应redis中的List（链表）
 * opsForSet()：对应redis中的Set（集合）
 * ————————————————
 * 版权声明：本文为CSDN博主「MrYuShiwen」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
 * 原文链接：https://blog.csdn.net/MrYushiwen/article/details/122493709
 */
@Component
@Slf4j
public class RedisHelper {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

//    @Resource
//    RedisTemplate<String, Serializable> lettuceRedisTemplate;


    public Set keys(RedisKeyEnum keyEnum) {
        try {
            return redisTemplate.keys(keyEnum.getKey());
        } catch (Exception e) {
            log.error("redis keys error:", e);
        }
        return Collections.EMPTY_SET;
    }

    /**
     * 指定缓存失效时间
     *
     * @param keyEnum 键
     * @param time    时间(秒)
     * @return boolean true:成功 false：失败 {@literal null} when used in pipeline / transaction.
     */
    public boolean setKeyExpire(RedisKeyEnum keyEnum, long time) {
        try {
            if (time > 0) {
                return redisTemplate.expire(keyEnum.getKey(), time, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("redis set expire error:", e);
        }
        return false;
    }

    /**
     * 根据key 获取过期时间
     *
     * @param keyEnum 键 不能为null
     * @return 时间(秒) 返回-1代表为永久有效 返回-2代表key不存在；{@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/ttl">Redis Documentation: TTL</a>
     */
    public long getKeyExpire(RedisKeyEnum keyEnum) {
        return redisTemplate.getExpire(keyEnum.getKey());
    }

    //###########################String操作######################################

    /**
     * 判断key是否存在
     *
     * @param keyEnum 键
     * @return true 存在 false不存在
     * @see <a href="https://redis.io/commands/exists">Redis Documentation: EXISTS</a>
     */
    public boolean existKey(RedisKeyEnum keyEnum) {
        try {
            return redisTemplate.hasKey(keyEnum.getKey());
        } catch (Exception e) {
            log.error("redis exists key error:", e);
        }
        return false;

    }

    /**
     * 设置值，采用boundValueOps绑定key后执行, 也可以采用opsForValue()，区别在于boundValueOps绑定key后，返回一个接口，可以在这个接口上连续调用多个方法来执行不同的操作，而无需每次都指定键
     *
     * @param keyEnum 键
     * @param value   值
     */
    public boolean set(RedisKeyEnum keyEnum, Object value) {
        try {
            redisTemplate.boundValueOps(keyEnum.getKey()).set(value);
            return true;
        } catch (Exception e) {
            log.error("redis set error : ", e);
            return false;
        }
    }

    public boolean set(RedisKeyEnum keyEnum, Object value, Long seconds) {
        try {
            redisTemplate.boundValueOps(keyEnum.getKey()).set(value, Duration.ofSeconds(seconds));
            return true;
        } catch (Exception e) {
            log.error("redis set with TTL error:", e);
        }
        return false;
    }

    public boolean set(RedisKeyEnum keyEnum, Object value, Long seconds, TimeUnit unit) {
        try {
            redisTemplate.boundValueOps(keyEnum.getKey()).set(value, seconds, unit);
            return true;
        } catch (Exception e) {
            log.error("redis set with TTL unit error:", e);
        }
        return false;
    }

    /**
     * 如果键不存在，则设置键值对
     *
     * @param keyEnum 键
     * @param value   值
     * @return 设置成功 false：失败 {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/setnx">Redis Documentation: SETNX</a>
     */
    public boolean setIfAbsent(RedisKeyEnum keyEnum, Object value) {
        try {
            Boolean success = redisTemplate.boundValueOps(keyEnum.getKey()).setIfAbsent(value);
            return success != null && success;
        } catch (Exception e) {
            log.error("redis set if absent error:", e);
        }
        return false;
    }

    public boolean setIfAbsent(RedisKeyEnum keyEnum, Object value, Long seconds) {
        try {
            Boolean success = redisTemplate.boundValueOps(keyEnum.getKey()).setIfAbsent(value,
                    Duration.ofSeconds(seconds));
            return success != null && success;
        } catch (Exception e) {
            log.error("redis set if absent with TTL error", e);
        }
        return false;
    }

    public boolean setIfAbsent(RedisKeyEnum keyEnum, Object value, Long seconds, TimeUnit unit) {
        try {
            Boolean success = redisTemplate.boundValueOps(keyEnum.getKey()).setIfAbsent(value, seconds, unit);
            return success != null && success;
        } catch (Exception e) {
            log.error("redis set if absent with TTL unit error", e);
        }
        return false;
    }

    /**
     * 设置多个键值对，此方法会覆盖旧值，如果不想覆盖旧值，请使用MSETNX
     *
     * @param map 包含键值对的集合
     */
    public void multiSet(Map<RedisKeyEnum, Object> map) {
        TreeMap<String, Object> treeMap = new TreeMap<>();
        map.forEach((k, v) -> {
            String key = k.getKey();
            treeMap.put(key, v);
        });
        redisTemplate.opsForValue().multiSet(treeMap);
    }

    /**
     * map中的键值对要么所有设置成功，只要有一个已存在，则都不成功。是原子性的操作
     *
     * @param map must not be {@literal null}.
     * @param {@literal null} when used in pipeline / transaction.
     * @return true：全部成功 false：全部失败
     * @see <a href="https://redis.io/commands/msetnx">Redis Documentation: MSETNX</a>
     */
    public boolean multiSetIfAbsent(Map<RedisKeyEnum, Object> map) {
        try {
            HashMap<String, Object> hashMap = new HashMap<>();
            map.forEach((k, v) -> {
                String key = k.getKey();
                hashMap.put(key, v);
            });
            return redisTemplate.opsForValue().multiSetIfAbsent(hashMap);
        } catch (Exception e) {
            log.error("redis multi if absent set error", e);
        }
        return false;
    }

    public Object get(RedisKeyEnum keyEnum) {
        return redisTemplate.boundValueOps(keyEnum.getKey()).get();
    }

    /**
     * 获取key的旧值，设置新值
     *
     * @return 旧值，如果旧值没有，则返回null {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/getset">Redis Documentation: GETSET</a>
     */
    public Object getAndSet(RedisKeyEnum keyEnum, Object value) {
        return redisTemplate.boundValueOps(keyEnum.getKey()).getAndSet(value);
    }

    /**
     * 根据传入的keys的顺序返回values
     *
     * @param keys 键
     * @return List<Object> 根据keys的顺序返回的结果集合。 {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/mget">Redis Documentation: MGET</a>
     */
    public List<Object> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 在指定key上加1
     *
     * @param keyEnum 键
     * @return 加1之后的值
     */
    public Long incr(RedisKeyEnum keyEnum) {
        try {
            return redisTemplate.boundValueOps(keyEnum.getKey()).increment();
        } catch (Exception e) {
            log.error("redis incr error:", e);
        }
        return null;
    }

    /**
     * 在指定的key上加上delta
     *
     * @param keyEnum 键
     * @param delta   增量
     * @return 返回value加上delta之后的值 {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/incrby">Redis Documentation: INCRBY</a>
     */
    public Long incrBy(RedisKeyEnum keyEnum, long delta) {
        try {
            return redisTemplate.boundValueOps(keyEnum.getKey()).increment(delta);
        } catch (Exception e) {
            log.error("redis incr by delta error", e);
        }
        return null;
    }

    /**
     * 指定key的value上加上浮点数delta，正数则加，负数则减
     *
     * @param keyEnum 键
     * @param delta   变量
     * @return 加上delta之后的值。{@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/incrbyfloat">Redis Documentation: INCRBYFLOAT</a>
     */
    public Double incrByFloat(RedisKeyEnum keyEnum, double delta) {
        try {
            return redisTemplate.boundValueOps(keyEnum.getKey()).increment(delta);
        } catch (Exception e) {
            log.error("redis incr by double delta error:", e);
        }
        return null;
    }

    /**
     * key的value减1
     * @param keyEnum 键
     * @return 减1过后的值.{@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/decr">Redis Documentation: DECR</a>
     */
    public Long decr(RedisKeyEnum keyEnum) {
        try {
            redisTemplate.boundValueOps(keyEnum.getKey()).decrement();
        } catch (Exception e) {
            log.error("redis decrement error:", e);
        }
        return null;
    }


    //todo

    //============BitMap=================

    /**
     * 将offset位上的值设置为value
     * @param keyEnum 键
     * @param offset 偏移量
     * @param value 待设置的值
     * @return
     */
    public Boolean setBit(RedisKeyEnum keyEnum, long offset, boolean value){
        try{
            return redisTemplate.opsForValue().setBit(keyEnum.getKey(), offset, value);
        }catch (Exception e){
            log.error("redis set bit error:", e);
        }
        return false;
    }

    /**
     * todo 待完善方法
     * 批量设置offset位置的值
     * @param keyEnum
     * @return List<Long> 对应offset位的旧值
     */
    public List<Long> setBitField(RedisKeyEnum keyEnum){
        try{
            BitFieldSubCommands commands = BitFieldSubCommands.create();
            BitFieldSubCommands to = commands.set(BitFieldSubCommands.BitFieldType.unsigned(1)).valueAt(1).to(2);
            return redisTemplate.opsForValue().bitField(keyEnum.getKey(), to);
        }catch (Exception e){
            log.error("redis setBitField error:", e);
        }
        return Collections.emptyList();
    }

    /**
     * 返回offset位的值，如果不存在，则返回0
     * @param keyEnum 键
     * @param offset 位置
     * @return boolean {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/getbit">Redis Documentation: GETBIT</a>
     */
    public Boolean getBit(RedisKeyEnum keyEnum, long offset){
        try{
            return redisTemplate.opsForValue().getBit(keyEnum.getKey(), offset);
        }catch (Exception e){
            log.error("redis get bit error:", e);
        }
        return false;
    }

    //todo 待继续添加相关命令

    //==========================HyperLogLog==================================

    /**
     * 添加HyperLogLog元素
     * @param keyEnum 键
     * @param values 值集合
     * @return 1 of at least one of the values was added to the key; 0 otherwise. {@literal null} when used in pipeline /
     *         transaction.
     */
    public Long addHyperLogLog(RedisKeyEnum keyEnum, List<Object> values){
        Object[] valuesArray = values.toArray();
        return redisTemplate.opsForHyperLogLog().add(keyEnum.getKey(), valuesArray);
    }

    public Long countHyperLogLog(RedisKeyEnum keyEnum){
        return redisTemplate.opsForHyperLogLog().size(keyEnum.getKey());
    }

    public Long countHyperLogLog(List<RedisKeyEnum> keyEnums){
        List<String> keyList = new ArrayList<>(keyEnums.size());
        keyEnums.forEach(keyEnum -> keyList.add(keyEnum.getKey()));
        String[] keys = keyList.toArray(new String[0]);
        return redisTemplate.opsForHyperLogLog().size(keys);
    }

    public void deleteHyperLogLog(RedisKeyEnum keyEnum){
        redisTemplate.opsForHyperLogLog().delete(keyEnum.getKey());
    }

    /**
     * 合并源keys的values到destination的key
     * @param destination 目的key
     * @param sourceKeys 源keys的集合
     * @return 融合后的整个目标key的基数大小
     */
    public Long mergeHyperLogLog(RedisKeyEnum destination, List<RedisKeyEnum> sourceKeys){
        List<String> keyList = new ArrayList<>(sourceKeys.size());
        sourceKeys.forEach(keyEnum -> keyList.add(keyEnum.getKey()));
        String[] keys = keyList.toArray(new String[0]);
        return redisTemplate.opsForHyperLogLog().union(destination.getKey(), keys);
    }

    //==========================================




    /**
     * 获取值并转换为对应对象
     *
     * @param key
     * @param clazz
     * @return
     */
    public Object get(String key, Class clazz) {
        try {
            return Optional.ofNullable(redisTemplate.boundValueOps(key).get())
                    .map(obj -> JSON.toJavaObject((JSONObject) obj, clazz)).orElse(null);
        } catch (Exception e) {
            log.error("redis get error : ", e);
            return null;
        }
    }


}
