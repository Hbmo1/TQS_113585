package com.moliceiro.mealbooking.repository;

import com.moliceiro.mealbooking.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByRestaurantIdAndDateBetween(Long restaurantId, LocalDate start, LocalDate end);
}