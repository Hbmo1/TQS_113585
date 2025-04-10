package com.moliceiro.mealbooking.repository;

import com.moliceiro.mealbooking.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}