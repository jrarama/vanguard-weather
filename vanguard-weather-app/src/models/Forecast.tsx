export type CityRequest = {
    name: string,
    displayName: string,
    lat: number,
    lon: number
}
export type WeatherDescription = {
    main: string,
    description: string,
    icon: string
}

export type WeatherTemperature = {
    temp: number,
    temp_min: number,
    temp_max: number
}

export type WeatherDetail = {
    dt: number,
    dt_txt: string,
    weather: [WeatherDescription],
    main: WeatherTemperature
}

export type WeatherForecast = {
    tempMin: number,
    tempMax: number,
    list: [WeatherDetail]
}
export type DailyHourlyForecast = {
    daily: WeatherForecast,
    hourly: WeatherForecast
}

export type DailyForecastListProps = {
    forecast: WeatherForecast
}
