package com.vanguard.weatherapi.model;

import lombok.Data;

@Data
public class GeocoderCity {
    private String name;
    private Double lat;
    private Double lon;
    private String country;
    private String state;
}
