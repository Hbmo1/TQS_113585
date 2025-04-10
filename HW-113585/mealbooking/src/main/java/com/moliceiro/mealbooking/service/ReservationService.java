package com.moliceiro.mealbooking.service;

import com.moliceiro.mealbooking.model.Menu;
import com.moliceiro.mealbooking.model.Reservation;
import com.moliceiro.mealbooking.model.ReservationStatus;
import com.moliceiro.mealbooking.repository.MenuRepository;
import com.moliceiro.mealbooking.repository.ReservationRepository;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MenuRepository menuRepository;
    
@Transactional
public Reservation createReservation(Long menuId) {
    Menu menu = menuRepository.findById(menuId)
            .orElseThrow(() -> new IllegalArgumentException("Menu not found"));

    if (menu.getDate().isBefore(LocalDate.now())) {
        throw new IllegalStateException("Cannot book a meal for a past date. Hiding past menus...");
    }

    if (menu.getAvailableMeals() <= 0) {
        throw new IllegalStateException("No available meals for this menu.");
    }

    Reservation reservation = new Reservation();
    reservation.setMenu(menu);
    reservation.setDate(menu.getDate());
    reservation.setStatus(ReservationStatus.ACTIVE);

    menu.setBookedMeals(menu.getBookedMeals() + 1);
    menuRepository.save(menu);

    return reservationRepository.save(reservation);
}

    public Reservation getReservationByCode(String code) {
        return reservationRepository.findByCode(code).orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
    }

    public void cancelReservation(String code) {
        Reservation reservation = getReservationByCode(code);
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Reservation has already been cancelled");
        }
        if (reservation.getStatus() == ReservationStatus.USED) {
            throw new IllegalStateException("Reservation has already been used");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        Menu menu = reservation.getMenu();
        menu.setBookedMeals(menu.getBookedMeals() - 1);
        menuRepository.save(menu);
    }

    public void verifyReservation(String code) {
        Reservation reservation = getReservationByCode(code);
        if (reservation.getStatus() == ReservationStatus.USED) {
            throw new IllegalStateException("Reservation has already been verified");
        }
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new IllegalStateException("Reservation has been cancelled");
        }
        reservation.setStatus(ReservationStatus.USED);
        reservationRepository.save(reservation);
    }
}