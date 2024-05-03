package com.vanguard.weatherapi.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Slf4j
@Service
public class RestService {

    @Autowired
    private RestTemplate restTemplate;

    public <T> T getRequest(Class<T> clazz, String url) {
        log.info("Fetching request for class {}", clazz);
        ResponseEntity<T> entity = restTemplate.getForEntity(url, clazz, new HashMap<>());
        if (entity.getStatusCode().isError()) {
            log.error("Error fetching response for url: {}, status: {}", url, entity.getStatusCode().value());
            return null;
        } else {
            return entity.getBody();
        }
    }
}
