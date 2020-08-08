package com.example.travelapplication.cache;

public class CacheManager {
    private volatile static LruCacheWrapper<String, Object> cache;

    private CacheManager() {
    }

    public static LruCacheWrapper<String, Object> getInstance() {
        if (cache == null) {
            synchronized (LruCacheWrapper.class) {
                if (cache == null) {
                    int cacheSize = (int) Runtime.getRuntime().maxMemory() / 8;
                    cache = new LruCacheWrapper<>(cacheSize);
                }
            }
        }
        return cache;
    }
}
