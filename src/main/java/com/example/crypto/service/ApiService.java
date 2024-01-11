package com.example.crypto.service;

import com.example.crypto.model.Cryptocurrency;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApiService {
    private final RestTemplate restTemplate;

    @Value("${crypto.property.api-url}")
    private String apiUrl;

    public List<Cryptocurrency> getAll() {
        ResponseEntity<List<Cryptocurrency>> exchange =
                this.restTemplate.exchange(
                        apiUrl, HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
        return exchange.getBody();
    }
}
