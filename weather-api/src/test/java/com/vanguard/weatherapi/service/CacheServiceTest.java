package com.vanguard.weatherapi.service;

import com.vanguard.weatherapi.config.CacheConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import redis.clients.jedis.Jedis;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CacheServiceTest {

    @Mock
    private Jedis jedis;

    @Mock
    private CacheConfig config;

    @InjectMocks
    private CacheService cacheService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetValueNotInCache() {
        Supplier<String> func = () -> "value";
        assertEquals("value", cacheService.getValue("key", func, 0));
    }

    @Test
    public void testGetValueHasCache() {
        Supplier<String> func = () -> "value";
        doReturn("other").when(jedis).get(eq("key"));
        assertEquals("other", cacheService.getValue("key", func, 0));
    }

    @Test
    public void testSetValueZeroExpiry() {
        cacheService.setValue("key", "value", 0);
        verify(jedis, atMostOnce()).set(anyString(), anyString());
    }

    @Test
    public void testSetValueNonZeroExpiry() {
        cacheService.setValue("key", "value", 100);
        verify(jedis, atMostOnce()).setex(anyString(), anyLong(), anyString());
    }
}
