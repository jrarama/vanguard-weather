package com.vanguard.weatherapi.model;

import lombok.Data;

import java.util.List;

@Data
public class ForecastResponse {

    private Double tempMin;
    private Double tempMax;
    private List<WeatherDetail> list;
}
