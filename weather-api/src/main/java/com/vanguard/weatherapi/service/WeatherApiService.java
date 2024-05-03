package com.vanguard.weatherapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vanguard.weatherapi.config.WeatherApiConfig;
import com.vanguard.weatherapi.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@Service
public class WeatherApiService {

    @Autowired
    private WeatherApiConfig config;

    @Autowired
    private RestService restService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private ObjectMapper mapper;

    public List<GeocoderCity> getCity(String city) throws JsonProcessingException {
        String lower = String.valueOf(city).toLowerCase().replace(" ", "_");;
        String url = String.format(config.getGeocoderUrl(), city, 3, config.getAppid());
        String jsonResponse = cacheService.getValue("city/" + lower, () -> restService.getRequest(String.class, url), 0);
        List<GeocoderCity> cities = mapper.readValue(jsonResponse, new TypeReference<>() {});
        log.info("Response for city: {}, size: {}", city, cities.size());
        return cities;
    }

    public WeatherDetail getWeather(WeatherRequest request) throws JsonProcessingException {
        String key = String.join(";", request.getCity().toLowerCase(), String.valueOf(request.getLat()),
                String.valueOf(request.getLon()), request.getUnits()).replace(" ", "_");;

        String url = String.format(config.getWeatherUrl(), request.getLat(), request.getLon(), request.getUnits(),
                config.getAppid());

        String jsonResponse = cacheService.getValue("weather/" + key, () -> restService.getRequest(String.class, url),
                config.getIntervalSeconds());
        WeatherDetail detail = mapper.readValue(jsonResponse, WeatherDetail.class);
        log.info("Response for weather: {}", CollectionUtils.isEmpty(detail.getWeather()) ? "NA":
                detail.getWeather().get(0).getDescription());
        return detail;
    }

    public ForecastResponse getForecast(WeatherRequest request) throws JsonProcessingException {
        String key = String.join(";", request.getCity().toLowerCase(), String.valueOf(request.getLat()),
                String.valueOf(request.getLon()), request.getUnits()).replace(" ", "_");

        String url = String.format(config.getForecastUrl(), request.getLat(), request.getLon(), request.getUnits(),
                config.getAppid());

        String jsonResponse = cacheService.getValue("forecast/" + key, () -> restService.getRequest(String.class, url),
                3600);
        ForecastResponse detail = mapper.readValue(jsonResponse, ForecastResponse.class);
        log.info("Response for forecast with size: {}", CollectionUtils.isEmpty(detail.getList()) ? "NA":
                detail.getList().size());
        return detail;
    }

    public DailyForecastResponse getDailyHourlyForecast(WeatherRequest request, int days) throws JsonProcessingException {
        DailyForecastResponse forecast = new DailyForecastResponse();
        ForecastResponse response = getForecast(request);
        forecast.setDaily(getDailyForecast(response, days));
        forecast.setHourly(getHourlyForecast(response));

        return forecast;
    }

    private ForecastResponse getHourlyForecast(ForecastResponse response) {
        ForecastResponse result = new ForecastResponse();
        List<WeatherDetail> list = new ArrayList<>();

        for (WeatherDetail item: response.getList()) {
            if (result.getTempMin() == null) {
                result.setTempMin(item.getMain().getTempMin());
            }
            if (result.getTempMax() == null) {
                result.setTempMax(item.getMain().getTempMax());
            }

            result.setTempMin(Math.min(result.getTempMin(), item.getMain().getTempMin()));
            result.setTempMax(Math.max(result.getTempMax(), item.getMain().getTempMax()));

            list.add(item);
        }

        result.setList(list);
        return result;
    }

    private ForecastResponse getDailyForecast(ForecastResponse response, int days) {
        ForecastResponse daily = new ForecastResponse();
        List<WeatherDetail> list = new ArrayList<>();
        daily.setList(list);

        Map<String, WeatherDetail> map = new TreeMap<>();
        for (WeatherDetail item: response.getList()) {
            String key = item.getDate().split(" ", -1)[0];
            WeatherDetail detail;
            if (!map.containsKey(key)) {
                if (map.size() >= days) break;
                detail = new WeatherDetail();
                detail.setDate(key + " 00:00:00");
                detail.setMain(new WeatherDetail.Temperature(item.getMain()));
                detail.setWeather(item.getWeather() == null ? null:
                        item.getWeather().stream().map(WeatherDetail.Description::new).toList());
                map.put(key, detail);

                if (daily.getTempMin() == null) {
                    daily.setTempMin(item.getMain().getTempMin());
                }
                if (daily.getTempMax() == null) {
                    daily.setTempMax(item.getMain().getTempMax());
                }
            } else {
                detail = map.get(key);

                // set min and max temp
                if (item.getMain().getTempMin() < detail.getMain().getTempMin()) {
                    detail.getMain().setTempMin(item.getMain().getTempMin());
                }
                if (item.getMain().getTempMax() > detail.getMain().getTempMax()) {
                    detail.getMain().setTempMax(item.getMain().getTempMax());
                }
            }

            daily.setTempMin(Math.min(daily.getTempMin(), detail.getMain().getTempMin()));
            daily.setTempMax(Math.max(daily.getTempMax(), detail.getMain().getTempMax()));
        }

        list.addAll(map.values());
        return daily;
    }
}
