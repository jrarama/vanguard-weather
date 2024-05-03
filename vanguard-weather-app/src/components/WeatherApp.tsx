import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import PlaceIcon from '@mui/icons-material/Place';

import { useState, useEffect } from 'react';
import { Alert, Container, Paper } from '@mui/material';
import CityDialog from './dialogs/CityDialog';
import { CityRequest, DailyHourlyForecast } from '../models/Forecast';
import { DailyForecastList } from './DailyForecastList';


export default function WeatherApp() {
    const [cityRequest, setCityRequest] = useState<CityRequest | null>(null);
    const [city, setCity] = useState<string>('');
    const [maxTemp, setMaxTemp] = useState<number|null>(null);
    const defMaxTemp:number = 30;
    const [currentMaxTemp, setCurrentMaxTemp] = useState<number>(30);
    const [open, setOpen] = useState(false);
    const [forecast, setForecast] = useState<DailyHourlyForecast | null>(null);

    const handleOpen = () => {
        setOpen(true);
    };
    const handleClose = () => {
        setOpen(false);
    };

    const handleSubmit = (city:string, temp:number) => {
        setCity(city);
        setMaxTemp(temp)
        console.log('City:', city, 'Max Temp:', temp);
        handleClose();
    };

    const updateWeather = (json: CityRequest) => {
        fetch('http://localhost:8080/forecast', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                city: json.displayName,
                lat: json.lat,
                lon: json.lon,
                units: 'metric'
            })
        })
        .then(res => res.json())
        .then((res:DailyHourlyForecast) => {
            console.log('Forecast Response', res);
            setForecast(res);
        });
    };

    useEffect(() => {
        if (city) {
            console.log('City has been updated to', city);
            fetch('http://localhost:8080/city?q=' + city)
                .then(res => res.json())
                .then((res:any) => {
                    console.log('City Response', res);

                    if (Array.isArray(res) && res.length > 0) {
                        const c = res[0];
                        const r = {
                            name: c.name,
                            displayName: c.name + (c.state ? ', ' + c.state: '') + (c.country ? ', ' + c.country: ''),
                            lat: c.lat,
                            lon: c.lon
                        };
                        setCityRequest(r);
                        updateWeather(r);
                    }
                });
        }
    }, [city]);

    useEffect(() => {
        if (!cityRequest) {
            const loc = localStorage.getItem('cityRequest');
            const json = JSON.parse(loc + '');

            console.log('Getting value from localstorage', json);
            if (json) {
                setCityRequest(json);
                updateWeather(json);
            }
        } else if (cityRequest && cityRequest.name) {
            localStorage.setItem('cityRequest', JSON.stringify(cityRequest));
            console.log('Setting value in localstorage');
        }
    }, [cityRequest]);


    useEffect(() => {
        if (!maxTemp) {
            const loc = localStorage.getItem('maxTemp');

            console.log('Getting value from localstorage for maxTemp', loc);
            if (loc) {
                setMaxTemp(Number(loc));
            } else {
                setMaxTemp(defMaxTemp);
                localStorage.setItem('maxTemp', defMaxTemp + '');
                console.log('Setting value in localstorage for maxTemp');
            }
        } else {
            localStorage.setItem('maxTemp', maxTemp + '');
            console.log('Setting value in localstorage for maxTemp');
        }
    }, [maxTemp]);

    useEffect(() => {
        if (forecast && forecast.daily) {
            const temp = forecast.daily.list[0].main.temp_max;
            console.log('Cuurent day max temp', temp);
            setCurrentMaxTemp(temp);
        }

    }, [forecast])

    return (
        <>
        { currentMaxTemp > maxTemp! ? (
            <Alert severity="error">Current temperature is greater than the preferred max temperature of {maxTemp}&deg;</Alert>
        ):'' }

        <Container maxWidth="xl" sx={{pt: 3}}>
            {cityRequest && cityRequest.displayName ? 
            <Typography variant="h4" component="h2" key="cityName">
            {cityRequest.displayName}
        </Typography>: ''}
            
            <Typography variant="body2" component="h2" sx={{mt: 1}} color='#444'>
                Preferred max temperature is: {maxTemp}&deg;. Set your current 
                <Button variant='outlined' color='inherit' size='small' sx={{ ml: 1}} 
                    onClick={() => handleOpen()}><PlaceIcon fontSize='small'/> city </Button>
                
            </Typography>

            { forecast && forecast.hourly && forecast.hourly.list ? (
                <>
                     <Container>
                        <Typography variant="h2" component="div" sx={{mt: 0}}>
                            {forecast.hourly.list[0].main.temp + ''}&deg;
                            <img alt={forecast.hourly.list[0].weather[0].main} src={'https://openweathermap.org/img/wn/' + (forecast.hourly.list[0].weather[0].icon) + '@2x.png'} style={{height: '60px'}}/>
                        </Typography>
                    </Container>
                    { forecast.daily ? (
                        <Container>
                            <Typography variant="body1" component="div">
                                {forecast.hourly.list[0].weather[0].main + ''}
                            </Typography>
                            <Typography variant="body1" component="span">
                                Low: {forecast.daily.list[0].main.temp_min + ''}&deg;, High: {forecast.daily.list[0].main.temp_max + ''}&deg; 
                            </Typography>
                        </Container>
                    ): ''}
                    
                </>
            ): '' }

            
            
        </Container>
        <CityDialog open={open} handleClose={handleClose} handleSubmit={handleSubmit} maxTemp={maxTemp!} city={cityRequest?.name || ''} />

        { forecast && forecast.daily ? 
        (
            <Container>
            <Paper>
            <DailyForecastList forecast={forecast.daily} />
            </Paper>
            </Container>

            
        ): ''}

        
        </>
    );
}
