package com.vanguard.weatherapi.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "openweather.api")
public class WeatherApiConfig {
    private String appid;
    private String geocoderUrl;
    private String weatherUrl;
    private String forecastUrl;
    private int intervalSeconds;
}
