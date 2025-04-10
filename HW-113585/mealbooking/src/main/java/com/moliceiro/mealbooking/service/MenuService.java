package com.moliceiro.mealbooking.service;

import com.moliceiro.mealbooking.model.Menu;
import com.moliceiro.mealbooking.model.Restaurant;
import com.moliceiro.mealbooking.repository.MenuRepository;
import com.moliceiro.mealbooking.repository.RestaurantRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

@Service
public class MenuService {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private WeatherService weatherService;

    // Cache statistics
    private AtomicLong totalRequests = new AtomicLong(0);
    private AtomicLong hits = new AtomicLong(0);
    private AtomicLong misses = new AtomicLong(0);

        public MenuService(MenuRepository menuRepository, RestaurantRepository restaurantRepository, WeatherService weatherService) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
        this.weatherService = weatherService;
    }

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }


    public Optional<Menu> getMenuById(Long menuId) {
        return menuRepository.findById(menuId);
    }

    public List<Menu> getMenusForRestaurant(Long restaurantId, LocalDate start, LocalDate end) {
        List<Menu> menus = menuRepository.findByRestaurantIdAndDateBetween(restaurantId, start, end);
        menus.forEach(menu -> {
            try {
                String weather = weatherService.getWeatherForecast(menu.getDate(),
                totalRequests, hits, misses);
                menu.setWeather(weather);
            } catch (Exception e) {
                menu.setWeather("Weather data unavailable");
            }

        });
        return menus;
    }

    public Menu createMenu(Long restaurantId, LocalDate date, String description, int maxMeals) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found with ID: " + restaurantId));

        Menu menu = new Menu();
        menu.setRestaurant(restaurant);
        menu.setDate(date);
        menu.setDescription(description);
        menu.setReservationLimit(maxMeals);
        menu.setBookedMeals(0);

        return menuRepository.save(menu);
    }

    public Map<String, Long> getCacheStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalRequests", totalRequests.get());
        stats.put("hits", hits.get());
        stats.put("misses", misses.get());
        return stats;
    }
}