package com.moliceiro.mealbooking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class WeatherService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    @Autowired
    private RestTemplate restTemplate;
    @Value("${openweathermap.api.key}")
    private String apiKey;

    @Cacheable("weatherForecast")
    public String getWeatherForecast(LocalDate date, AtomicLong totalRequests,
            AtomicLong hits, AtomicLong misses) {
        totalRequests.incrementAndGet();
        String url = String.format(
                "http://api.openweathermap.org/data/2.5/forecast?q=Aveiro,PT&appid=%s&units=metric",
                apiKey);
        
        Map response = restTemplate.getForObject(url, Map.class);
        logger.info("Fetched weather forecast for date: {}", date);

        List<Map> list = (List<Map>) response.get("list");
        Map<String, Object> forecast = list.get(0);
        Map<String, Double> main = (Map<String, Double>) forecast.get("main");
        List<Map> weather = (List<Map>) forecast.get("weather");
        String desc = (String) weather.get(0).get("description");
        Double temp = main.get("temp");
        return String.format("%s, %.1f°C", desc, temp);
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}