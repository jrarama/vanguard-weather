package com.vanguard.weatherapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vanguard.weatherapi.model.DailyForecastResponse;
import com.vanguard.weatherapi.model.GeocoderCity;
import com.vanguard.weatherapi.model.WeatherDetail;
import com.vanguard.weatherapi.model.WeatherRequest;
import com.vanguard.weatherapi.service.WeatherApiService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin
public class WeatherController {

    @Autowired
    private WeatherApiService apiService;

    @GetMapping("/city")
    public ResponseEntity<List<GeocoderCity>> city(@RequestParam(name = "q") String q, HttpServletRequest request) {
        try {
            return ResponseEntity.ok(apiService.getCity(q));
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON response from server", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/weather")
    public ResponseEntity<WeatherDetail> weather(@RequestBody WeatherRequest request) {
        try {
            return ResponseEntity.ok(apiService.getWeather(request));
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON response from server", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/forecast")
    public ResponseEntity<DailyForecastResponse> forecast(@RequestBody WeatherRequest request) {
        try {
            return ResponseEntity.ok(apiService.getDailyHourlyForecast(request, 4));
        } catch (JsonProcessingException e) {
            log.error("Error parsing JSON response from server", e);
            return ResponseEntity.badRequest().build();
        }
    }
}
