package com.vanguard.weatherapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class RestServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private RestService restService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRequestError() {
        ResponseEntity<String> r = mock(ResponseEntity.class);
        doReturn(HttpStatus.INTERNAL_SERVER_ERROR).when(r).getStatusCode();
        doReturn(r).when(restTemplate).getForEntity(anyString(), any(), anyMap());

        assertNull(restService.getRequest(String.class, "url"));
    }

    @Test
    public void testGetRequestNoError() {
        String body = "body";
        ResponseEntity<String> r = mock(ResponseEntity.class);
        doReturn(HttpStatus.OK).when(r).getStatusCode();
        doReturn(body).when(r).getBody();
        doReturn(r).when(restTemplate).getForEntity(anyString(), any(), anyMap());

        assertEquals(restService.getRequest(String.class, "url"), body);
    }
}
