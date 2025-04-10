package com.moliceiro.mealbooking.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String description;

    private String weather;

    @Column(nullable = false)
    private int reservationLimit; 

    @Column(nullable = false)
    private int bookedMeals; 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public int getReservationLimit() {
        return reservationLimit;
    }

    public void setReservationLimit(int reservationLimit) {
        this.reservationLimit = reservationLimit;
    }

    public int getBookedMeals() {
        return bookedMeals;
    }

    public void setBookedMeals(int bookedMeals) {
        this.bookedMeals = bookedMeals;
    }

    public int getAvailableMeals() {
        return reservationLimit - bookedMeals;
    }
}