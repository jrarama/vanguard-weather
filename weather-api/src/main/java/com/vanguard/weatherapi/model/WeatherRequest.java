package com.vanguard.weatherapi.model;

import lombok.Data;

@Data
public class WeatherRequest {
    private String city;
    private Double lon;
    private Double lat;
    private String units;
}
