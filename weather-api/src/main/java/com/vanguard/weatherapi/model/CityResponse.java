package com.vanguard.weatherapi.model;

import lombok.Data;

import java.util.List;

@Data
public class CityResponse {
    private String query;
    private List<CityDetail> cities;
}
