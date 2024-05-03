package com.vanguard.weatherapi.model;

import lombok.Data;

import java.util.List;

@Data
public class DailyForecastResponse {

    private ForecastResponse daily;
    private ForecastResponse hourly;
}
