package com.example.travelapplication.cache;

import android.util.LruCache;

import com.example.travelapplication.model.BaseModel;
import com.example.travelapplication.model.DeepClone;

import java.util.ArrayList;
import java.util.List;

public class LruCacheWrapper<K, V>{
    private LruCache<K, V> cache;

    LruCacheWrapper(int cacheSize) {
        cache = new LruCache<>(cacheSize);
    }

    public V put(K key, V value) {
        return cache.put(key, value);
    }

    public V get(K key) {
        return deepClone(cache.get(key));
    }

    public V remove(K key) {
        return cache.remove(key);
    }

    private V deepClone(V v) {
        if (v == null) {
            return null;
        }
        V value;
        if (v instanceof List) {
            List<V> list = new ArrayList<>();
            for (V item : (List<V>) v) {
                if (item instanceof DeepClone) {
                    list.add((V) ((BaseModel) item).deepClone());
                } else {
                    throw new ClassCastException("未实现DeepClone接口：" + item.getClass());
                }
            }
            value = (V) list;
        } else if (v instanceof DeepClone) {
            value = (V) ((BaseModel) v).deepClone();
        } else {
            throw new ClassCastException("未实现DeepClone接口：" + v.getClass());
        }
        return value;
    }
}
