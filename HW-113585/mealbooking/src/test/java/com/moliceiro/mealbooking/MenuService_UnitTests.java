package com.moliceiro.mealbooking;

import com.moliceiro.mealbooking.model.Menu;
import com.moliceiro.mealbooking.model.Restaurant;
import com.moliceiro.mealbooking.repository.MenuRepository;
import com.moliceiro.mealbooking.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

import com.moliceiro.mealbooking.service.WeatherService;
import com.moliceiro.mealbooking.service.MenuService;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private MenuService menuService;

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setName("Test Restaurant");
    }

    @Test
    void testGetMenusForRestaurant_onSuccess() {
        // Arrange
        Long restaurantId = 1L;
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(6);
        Menu menu1 = new Menu();
        menu1.setId(1L);
        menu1.setDate(start);
        menu1.setReservationLimit(10);
        menu1.setBookedMeals(5);
        Menu menu2 = new Menu();
        menu2.setId(2L);
        menu2.setDate(start.plusDays(1));
        menu2.setReservationLimit(10);
        menu2.setBookedMeals(3);
        List<Menu> menus = Arrays.asList(menu1, menu2);

        when(menuRepository.findByRestaurantIdAndDateBetween(restaurantId, start, end)).thenReturn(menus);
        when(weatherService.getWeatherForecast(any(LocalDate.class), any(AtomicLong.class), any(AtomicLong.class), any(AtomicLong.class)))
                .thenReturn("Sunny, 25.0°C");

        // Act
        List<Menu> result = menuService.getMenusForRestaurant(restaurantId, start, end);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Sunny, 25.0°C", result.get(0).getWeather());
        assertEquals("Sunny, 25.0°C", result.get(1).getWeather());
        assertEquals(5, result.get(0).getAvailableMeals()); // 10 - 5
        assertEquals(7, result.get(1).getAvailableMeals()); // 10 - 3
        verify(menuRepository, times(1)).findByRestaurantIdAndDateBetween(restaurantId, start, end);
        verify(weatherService, times(2)).getWeatherForecast(any(LocalDate.class), any(AtomicLong.class), any(AtomicLong.class), any(AtomicLong.class));
    }

    @Test
    void testGetMenusForRestaurant_noMenusFound() {
        // Arrange
        Long restaurantId = 1L;
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(6);
        when(menuRepository.findByRestaurantIdAndDateBetween(restaurantId, start, end)).thenReturn(Arrays.asList());

        // Act
        List<Menu> result = menuService.getMenusForRestaurant(restaurantId, start, end);

        // Assert
        assertTrue(result.isEmpty());
        verify(menuRepository, times(1)).findByRestaurantIdAndDateBetween(restaurantId, start, end);
        verify(weatherService, never()).getWeatherForecast(any(LocalDate.class), any(AtomicLong.class), any(AtomicLong.class), any(AtomicLong.class));
    }

    @Test
    public void testCreateMenu_success() {
        // Arrange
        Long restaurantId = 1L;
        LocalDate date = LocalDate.now().plusDays(1);
        String description = "Test Menu";
        int maxMeals = 10;

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(menuRepository.save(any(Menu.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Menu createdMenu = menuService.createMenu(restaurantId, date, description, maxMeals);

        // Assert
        assertNotNull(createdMenu);
        assertEquals(restaurant, createdMenu.getRestaurant());
        assertEquals(date, createdMenu.getDate());
        assertEquals(description, createdMenu.getDescription());
        assertEquals(maxMeals, createdMenu.getReservationLimit());
        assertEquals(0, createdMenu.getBookedMeals());
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(menuRepository, times(1)).save(any(Menu.class));
    }

    @Test
    public void testCreateMenu_restaurantNotFound() {
        // Arrange
        Long restaurantId = 1L;
        LocalDate date = LocalDate.now().plusDays(1);
        String description = "Test Menu";
        int maxMeals = 10;

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            menuService.createMenu(restaurantId, date, description, maxMeals);
        });
        assertEquals("Restaurant not found with ID: " + restaurantId, exception.getMessage());
        verify(restaurantRepository, times(1)).findById(restaurantId);
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    void testGetCacheStats() {
        // Arrange
        Long restaurantId = 1L;
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(6);
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setDate(start);
        menu.setReservationLimit(10);
        menu.setBookedMeals(5);
        List<Menu> menus = Arrays.asList(menu);

        when(menuRepository.findByRestaurantIdAndDateBetween(restaurantId, start, end)).thenReturn(menus);
        when(weatherService.getWeatherForecast(any(LocalDate.class), any(AtomicLong.class), any(AtomicLong.class), any(AtomicLong.class)))
                .thenAnswer(invocation -> {
                    AtomicLong totalRequests = invocation.getArgument(1);
                    AtomicLong hits = invocation.getArgument(2);
                    totalRequests.incrementAndGet();
                    hits.incrementAndGet();
                    return "Cloudy, 20.0°C";
                });

        // Act
        menuService.getMenusForRestaurant(restaurantId, start, end); // Simulate a call to update stats
        Map<String, Long> stats = menuService.getCacheStats();

        // Assert
        assertEquals(3, stats.size());
        assertEquals(1L, stats.get("totalRequests"));
        assertEquals(1L, stats.get("hits"));
        assertEquals(0L, stats.get("misses"));
    }
}


