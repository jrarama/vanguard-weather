package com.vanguard.weatherapi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.function.Supplier;

@Service
public class CacheService {

    @Autowired
    private Jedis jedis;

    public String getValue(String key, Supplier<String> setFunction, long expirySeconds) {
        String value = jedis.get(key);
        if (value == null) {
            value = setFunction.get();
            setValue(key, value, expirySeconds);
        }
        return value;
    }

    public void setValue(String key, String value, long expirySeconds) {
        if (expirySeconds == 0) {
            jedis.set(key, value);
        } else {
            jedis.setex(key, expirySeconds, value);
        }
    }
}
