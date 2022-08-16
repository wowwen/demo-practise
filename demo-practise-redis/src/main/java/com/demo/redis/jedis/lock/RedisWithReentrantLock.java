package com.demo.redis.jedis.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.HashMap;
import java.util.Map;

/**
 * 可重入锁
 */
public class RedisWithReentrantLock {
    private ThreadLocal<Map> lockers = new ThreadLocal<>();
    private Jedis jedis;

    public RedisWithReentrantLock(Jedis jedis) {
        this.jedis = jedis;
    }

    private boolean _lock(String key){
        //ex():设置过期时间，秒
        //px():设置过期时间，毫秒
        //nx():如果key不存在，才设置
        //xx():如果key存在，则设置
        SetParams setParams = SetParams.setParams();
        setParams.ex(60);
        setParams.nx();
        return jedis.set(key, "可重入锁value", setParams) != null;
    }

    private void _unlock(String key){
        jedis.del(key);
    }

    private Map<String, Integer> currentLockers(){
        Map<String, Integer> refs = lockers.get();
        if (refs != null){
            return refs;
        }
        lockers.set(new HashMap());
        return lockers.get();
    }

    public boolean lock(String key){
        Map<String, Integer> refs = currentLockers();
        Integer refCnt = refs.get(key);
        if (refCnt != null){
            refs.put(key, refCnt + 1);
            return true;
        }
        boolean ok = _lock(key);
        if (!ok){
            return false;
        }
        refs.put(key, 1);
        return true;
    }

    public boolean unlock(String key){
        Map<String, Integer> refs = currentLockers();
        Integer refCnt = refs.get(key);
        if (refCnt == null){
            return false;
        }
        refCnt -= 1;
        if (refCnt > 0){
            refs.put(key, refCnt);
        }else{
            refs.remove(key);
            _unlock(key);
        }
        return true;
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis();
        RedisWithReentrantLock redis = new RedisWithReentrantLock(jedis);
        //两次加锁
        System.out.println(redis.lock("codehole"));
        System.out.println(redis.lock("codehole"));
        //两次解锁
        System.out.println(redis.unlock("codehole"));
        System.out.println(redis.unlock("codehole"));
    }


}
