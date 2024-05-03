import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';

import { useAuth0 } from "@auth0/auth0-react";
import LogoutButton from './LogoutButton';
import WeatherApp from './WeatherApp';

export default function ButtonAppBar() {
  const { user, isAuthenticated, isLoading, loginWithRedirect } = useAuth0();

  if (!isLoading && !isAuthenticated) {
    loginWithRedirect();
  }

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <IconButton
            size="large"
            edge="start"
            color="inherit"
            aria-label="menu"
            sx={{ mr: 2 }}
          >
            <MenuIcon />
          </IconButton>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Vanguard Weather App
          </Typography>

          {!isLoading && isAuthenticated && user ? 
          <span>
            <LogoutButton />
            <Button variant='outlined' color='inherit' sx={{ ml: 3}}>{user.nickname}</Button>
          </span>
          
          : ''}
          
        </Toolbar>
      </AppBar>

      <WeatherApp />
    </Box>
  );
}