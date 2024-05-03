package com.vanguard.weatherapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class WeatherDetail {

    private List<Description> weather;
    private Temperature main;
    private Long dt;
    @JsonProperty("dt_txt")
    private String date;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Description {
        private String main;
        private String description;
        private String icon;

        public Description(Description desc) {
            this.main = desc.main;
            this.description = desc.description;
            this.icon = desc.icon;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Temperature {
        private Double temp;
        @JsonProperty("temp_min")
        private Double tempMin;
        @JsonProperty("temp_max")
        private Double tempMax;

        public Temperature(Temperature t) {
            this.temp = t.temp;
            this.tempMax = t.tempMax;
            this.tempMin = t.tempMin;
        }
    }
}
