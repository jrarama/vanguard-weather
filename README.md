# Vanguard Weather App

This weather app uses Java 17 and Spring Boot for backend, React JS for Frontend, Redis for caching, Auth0 for authentication and authorization, and OpenWeatherMap for fetching weather forecast.

I used below APIs from OpenWeather

1.)	Geocoding – Fetching the lat and lon of a city

    http://api.openweathermap.org/geo/1.0/direct?q={cityName},{stateCode},{countryCode}&limit={limit}&appid={API_key}

2.)	5 Day weather forecast

    http://api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API_key}



[Weather App Screenshot](weather-app.png)

## Set Up
1. Redis

You need a redis installation. If you need one, you can try it here: https://redis.io/try-free/

Update the config in `weather-api/src/main/resources/application.properties`
```
caching.jedis.host=
caching.jedis.port=
caching.jedis.username=
caching.jedis.password=
```

2. Auth0

You can register a free Auth0 tenant at https://auth0.com/signup

Setup your Auth0 and update the config in `weather-api/src/main/resources/application.properties` 
with your App ID 
```
openweather.api.appid=
```

## Run Backend
```bash
cd weather-api
mvn clean install
mvn spring-boot:run
```

## Run Frontend
Open a new terminal/command line

```bash
cd vanguard-weather-app
npm install
npm start
```
