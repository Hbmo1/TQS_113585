package com.moliceiro.mealbooking.repository;

import com.moliceiro.mealbooking.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<Reservation> findByCode(String code);
}