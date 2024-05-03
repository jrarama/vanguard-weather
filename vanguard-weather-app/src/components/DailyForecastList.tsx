import { List, ListSubheader, ListItemButton, ListItemIcon, ListItemText, Slider } from "@mui/material";
import { DailyForecastListProps } from "../models/Forecast";
import { format } from "date-fns";

export const DailyForecastList = ({forecast}: DailyForecastListProps) => {
    let tempMin:number = forecast.tempMin;
    let tempMax:number = forecast.tempMax;
    let total:number = tempMax - tempMin;

    const list = forecast.list.map((cur, index) => {
        let min = ((cur.main.temp_min - tempMin) / total) * 100;
        let max = ((cur.main.temp_max - tempMin) / total) * 100;
        let color: 'primary' | 'secondary' | 'error' | 'info' | 'success' | 'warning' = 'success';
        if (cur.main.temp_max > 20) color = 'warning';
        if (cur.main.temp_max > 25) color = 'error';


        return (<ListItemButton divider={true}>
            <ListItemText primary={ index === 0 ? 'Today' : format(cur.dt_txt, 'eee') } sx={{width: '120px'}} />
            <ListItemIcon>
                <img alt={cur.weather[0].main} src={'https://openweathermap.org/img/wn/' + (cur.weather[0].icon) + '@2x.png'} style={{height: '60px'}}/>
            </ListItemIcon>
            <ListItemText primary={<>{cur.main.temp_min}&deg;</>} sx={{px: 2, pr: 4}} />
            <Slider 
                color={color}
                getAriaLabel={() => 'Temperature range'}
                value={[min, max]}
                
            />
            <ListItemText primary={<>{cur.main.temp_max}&deg;</>} sx={{px: 2}} />
          </ListItemButton>);
    });

    return (
        <List
          sx={{ width: '100%', bgcolor: 'background.paper', mt: 2 }}
          component="nav"
          aria-labelledby="nested-list-subheader"
          subheader={
            <ListSubheader component="div" id="nested-list-subheader" sx={{color: 'black'}}>
              <h3>4 Day Forecast</h3>
            </ListSubheader>
          }
        >

        <>{list}</>
          
        </List>
      );
};