package com.vanguard.weatherapi.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CityDetail implements Serializable {

    private String displayName;
    private Double lon;
    private Double lat;
}
