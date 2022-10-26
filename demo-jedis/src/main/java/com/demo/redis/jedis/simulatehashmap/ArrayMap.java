package com.demo.redis.jedis.simulatehashmap;

import java.util.ArrayList;
import java.util.List;

public class ArrayMap<K, V> {
    private List<K> keys = new ArrayList<>();
    private List<V> values = new ArrayList<>();

    public V put(K k, V v){
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).equals(k)){
                V oldV = values.get(i);
                values.set(i, v);
                return oldV;
            }
        }
        keys.add(k);
        values.add(v);
        return null;
    }

    public V get(K k){
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).equals(k)){
                return values.get(i);
            }
        }
        return null;
    }

    public V delete(K k){
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).equals(k)){
                keys.remove(i);
                return values.remove(i);
            }
        }
        return null;
    }
}
