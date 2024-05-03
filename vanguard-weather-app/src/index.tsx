import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import { Auth0Provider } from '@auth0/auth0-react';
import reportWebVitals from './reportWebVitals';

import { createTheme, ThemeProvider } from '@mui/material/styles';

const root = ReactDOM.createRoot(
  document.getElementById('root') as HTMLElement
);

const theme = createTheme({
  palette: {
    primary: {
      main: '#c20029',
    },
    secondary: {
      main: '#ff7043',
    },
  },
});

root.render(
  <Auth0Provider
      domain="dev-aiobxajcz4xt4q8h.au.auth0.com"
      clientId="C5k3dggCQ27a1RrRc4m8FKcTbojsgtHh"
      authorizationParams={{
        redirect_uri: window.location.origin
      }}
    >
        <ThemeProvider theme={theme}>
          <App />
        </ThemeProvider>
    </Auth0Provider>,
  );

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

