package com.moliceiro.mealbooking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.moliceiro.mealbooking.service.WeatherService;
import com.moliceiro.mealbooking.service.MenuService;
import com.moliceiro.mealbooking.repository.MenuRepository;

class WeatherService_UnitTests {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuService menuService;

    @Mock
    private Logger logger;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        weatherService.setApiKey("dummy-api-key");
    }

    @Test
    void testGetWeatherForecast_onSuccess() {
        // Arrange
        LocalDate date = LocalDate.now();
        AtomicLong totalRequests = new AtomicLong(0);
        AtomicLong hits = new AtomicLong(0);
        AtomicLong misses = new AtomicLong(0);
        Map<String, Object> mockResponse = new HashMap<>();
        Map<String, Object> forecastEntry = new HashMap<>();
        Map<String, Double> main = Map.of("temp", 20.0);
        Map<String, String> weatherDetails = Map.of("description", "Clear sky");
        forecastEntry.put("main", main);
        forecastEntry.put("weather", Arrays.asList(weatherDetails));
        mockResponse.put("list", Arrays.asList(forecastEntry));

        String expectedUrl = "http://api.openweathermap.org/data/2.5/forecast?q=Aveiro,PT&appid=dummy-api-key&units=metric";
        when(restTemplate.getForObject(expectedUrl, Map.class)).thenReturn(mockResponse);

        // Act
        String result = weatherService.getWeatherForecast(date, totalRequests, hits, misses);

        // Assert
        assertEquals("Clear sky, 20.0°C", result);
        assertEquals(1, totalRequests.get());
        verify(restTemplate, times(1)).getForObject(expectedUrl, Map.class);
    }

    @Test
    void testGetWeatherForecast_whenApiReturnsNull() {
        // Arrange
        LocalDate date = LocalDate.now();
        AtomicLong totalRequests = new AtomicLong(0);
        AtomicLong hits = new AtomicLong(0);
        AtomicLong misses = new AtomicLong(0);

        String expectedUrl = "http://api.openweathermap.org/data/2.5/forecast?q=Aveiro,PT&appid=dummy-api-key&units=metric";
        when(restTemplate.getForObject(expectedUrl, Map.class)).thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> weatherService.getWeatherForecast(date, totalRequests, hits, misses));
        assertEquals(1, totalRequests.get());
        verify(restTemplate, times(1)).getForObject(expectedUrl, Map.class);
    }

    @Test
    void testGetWeatherForecast_withMalformedResponse() {
        // Arrange
        LocalDate date = LocalDate.now();
        AtomicLong totalRequests = new AtomicLong(0);
        AtomicLong hits = new AtomicLong(0);
        AtomicLong misses = new AtomicLong(0);
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("list", Arrays.asList(new HashMap<>()));

        String expectedUrl = "http://api.openweathermap.org/data/2.5/forecast?q=Aveiro,PT&appid=dummy-api-key&units=metric";
        when(restTemplate.getForObject(expectedUrl, Map.class)).thenReturn(mockResponse);

        // Act & Assert
        assertThrows(NullPointerException.class, () -> weatherService.getWeatherForecast(date, totalRequests, hits, misses));
        assertEquals(1, totalRequests.get());
        verify(restTemplate, times(1)).getForObject(expectedUrl, Map.class);
    }
}