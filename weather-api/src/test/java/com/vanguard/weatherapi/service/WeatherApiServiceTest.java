package com.vanguard.weatherapi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vanguard.weatherapi.config.WeatherApiConfig;
import com.vanguard.weatherapi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

public class WeatherApiServiceTest {

    @Mock
    private WeatherApiConfig config;

    @Mock
    private RestService restService;

    @Mock
    private CacheService cacheService;

    @Spy
    private ObjectMapper mapper = new ObjectMapper();

    @Spy
    @InjectMocks
    private WeatherApiService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        doReturn("abc123").when(config).getAppid();
    }

    @Test
    public void testGetCity() throws JsonProcessingException {
        doReturn("url").when(config).getGeocoderUrl();
        GeocoderCity c1 = new GeocoderCity();
        c1.setName("city1,au");
        List<GeocoderCity> list = new ArrayList<>();
        list.add(c1);

        String json = mapper.writeValueAsString(list);
        doReturn(json).when(cacheService).getValue(startsWith("city/"), any(), anyLong());

        List<GeocoderCity> actual = service.getCity("My City");
        assertEquals(1, actual.size());
        assertEquals(c1.getName(), actual.get(0).getName());
    }


    @Test
    public void testGetWeather() throws JsonProcessingException {
        doReturn("url").when(config).getWeatherUrl();
        WeatherDetail detail = new WeatherDetail();
        detail.setDate("2024-05-01");

        String json = mapper.writeValueAsString(detail);
        doReturn(json).when(cacheService).getValue(startsWith("weather/"), any(), anyLong());

        WeatherRequest request = new WeatherRequest();
        request.setCity("city");
        WeatherDetail actual = service.getWeather(request);

        assertEquals(detail.getDt(), actual.getDt());
    }

    @Test
    public void testGetForecast() throws JsonProcessingException {
        doReturn("url").when(config).getForecastUrl();
        ForecastResponse response = new ForecastResponse();
        response.setTempMax(30d);

        String json = mapper.writeValueAsString(response);
        doReturn(json).when(cacheService).getValue(startsWith("forecast/"), any(), anyLong());

        WeatherRequest request = new WeatherRequest();
        request.setCity("city");
        ForecastResponse actual = service.getForecast(request);

        assertEquals(response.getTempMax(), actual.getTempMax());
    }

    private WeatherDetail createWeatherDetail(String date, double minTemp, double maxTemp) {
        WeatherDetail detail = new WeatherDetail();
        detail.setDate(date);
        WeatherDetail.Temperature temp = new WeatherDetail.Temperature();
        detail.setMain(temp);
        temp.setTempMin(minTemp);
        temp.setTempMax(maxTemp);
        return detail;
    }

    @Test
    public void testGetDailyHourlyForecast() throws JsonProcessingException {
        List<WeatherDetail> list = new ArrayList<>(Arrays.asList(
                createWeatherDetail("2024-05-01 00:00:00", 1d, 2d),
                createWeatherDetail("2024-05-01 03:00:00", 3d, 4d),
                createWeatherDetail("2024-05-01 06:00:00", 5d, 6d),
                createWeatherDetail("2024-05-02 00:00:00", 7d, 8d),
                createWeatherDetail("2024-05-02 03:00:00", 9d, 10d),
                createWeatherDetail("2024-05-02 06:00:00", 11d, 12d),
                createWeatherDetail("2024-05-03 00:00:00", 13d, 14d),
                createWeatherDetail("2024-05-03 03:00:00", 15d, 16d),
                createWeatherDetail("2024-05-03 06:00:00", 17d, 18d)
        ));

        ForecastResponse response = new ForecastResponse();
        response.setList(list);

        WeatherRequest request = new WeatherRequest();
        request.setCity("city");

        doReturn(response).when(service).getForecast(request);

        // Check expectations
        DailyForecastResponse actual = service.getDailyHourlyForecast(request, 2);

        // Check daily
        assertEquals(2, actual.getDaily().getList().size());
        assertEquals(1d, actual.getDaily().getList().get(0).getMain().getTempMin());
        assertEquals(6d, actual.getDaily().getList().get(0).getMain().getTempMax());
        assertEquals(7d, actual.getDaily().getList().get(1).getMain().getTempMin());
        assertEquals(12d, actual.getDaily().getList().get(1).getMain().getTempMax());

        // Check hourly
        assertEquals(9, actual.getHourly().getList().size());
        assertEquals(1d, actual.getHourly().getTempMin());
        assertEquals(18d, actual.getHourly().getTempMax());
    }


}
