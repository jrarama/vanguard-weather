package com.vanguard.weatherapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Connection;
import redis.clients.jedis.Jedis;

@Data
@Component
@ConfigurationProperties(prefix = "caching.jedis")
public class CacheConfig {
    private String scheme = "redis";
    private String host;
    private Integer port;
    private String username;
    private String password;

    @Bean
    public Jedis jedis() {
        final String url = String.format("%s://%s:%s@%s:%d", scheme, username, password, host, port);
        Jedis jedis = new Jedis(url);
        Connection connection = jedis.getConnection();
        connection.connect();
        return jedis;
    }
}
