package com.moliceiro.mealbooking;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.moliceiro.mealbooking.model.Menu;
import com.moliceiro.mealbooking.model.Reservation;
import com.moliceiro.mealbooking.model.ReservationStatus;
import com.moliceiro.mealbooking.repository.MenuRepository;
import com.moliceiro.mealbooking.repository.ReservationRepository;
import com.moliceiro.mealbooking.service.ReservationService;

public class ReservationService_UnitTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateReservation_onSuccess() {
        // Arrange
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setDate(LocalDate.now().plusDays(1));
        menu.setReservationLimit(10);
        menu.setBookedMeals(5);
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArguments()[0]);
        when(menuRepository.save(any(Menu.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Reservation reservation = reservationService.createReservation(1L);

        // Assert
        assertNotNull(reservation);
        assertEquals(ReservationStatus.ACTIVE, reservation.getStatus());
        assertEquals(menu.getDate(), reservation.getDate());
        assertEquals(6, menu.getBookedMeals());
        verify(reservationRepository, times(1)).save(any(Reservation.class));
        verify(menuRepository, times(1)).save(menu);
    }

    @Test
    public void testCreateReservation_whenMenuNotFound() {
        // Arrange
        when(menuRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(1L));
        verify(reservationRepository, never()).save(any(Reservation.class));
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    public void testCreateReservation_whenLimitReached() {
        // Arrange
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setDate(LocalDate.now().plusDays(1));
        menu.setReservationLimit(10);
        menu.setBookedMeals(10); // No meals available
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> reservationService.createReservation(1L));
        verify(reservationRepository, never()).save(any(Reservation.class));
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    public void testGetReservation_onSuccess() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setCode("12345");
        when(reservationRepository.findByCode("12345")).thenReturn(Optional.of(reservation));

        // Act
        Reservation foundReservation = reservationService.getReservationByCode("12345");

        // Assert
        assertNotNull(foundReservation);
        assertEquals("12345", foundReservation.getCode());
        verify(reservationRepository, times(1)).findByCode("12345");
    }

    @Test
    public void testGetReservation_whenNotFound() {
        // Arrange
        when(reservationRepository.findByCode("12345")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reservationService.getReservationByCode("12345"));
        verify(reservationRepository, times(1)).findByCode("12345");
    }

    @Test
    public void testCancelReservation_onSuccess() {
        // Arrange
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setBookedMeals(5);
        Reservation reservation = new Reservation();
        reservation.setCode("12345");
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservation.setMenu(menu);
        when(reservationRepository.findByCode("12345")).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);
        when(menuRepository.save(menu)).thenReturn(menu);

        // Act
        reservationService.cancelReservation("12345");

        // Assert
        assertEquals(ReservationStatus.CANCELLED, reservation.getStatus());
        assertEquals(4, menu.getBookedMeals());
        verify(reservationRepository, times(1)).save(reservation);
        verify(menuRepository, times(1)).save(menu);
    }

    @Test
    public void testCancelReservation_whenNotFound() {
        // Arrange
        when(reservationRepository.findByCode("12345")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reservationService.cancelReservation("12345"));
        verify(reservationRepository, never()).save(any(Reservation.class));
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    public void testCancelReservation_whenNotActive() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setCode("12345");
        reservation.setStatus(ReservationStatus.CANCELLED);
        when(reservationRepository.findByCode("12345")).thenReturn(Optional.of(reservation));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> reservationService.cancelReservation("12345"));
        verify(reservationRepository, never()).save(any(Reservation.class));
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    public void testVerifyReservation_onSuccess() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setCode("12345");
        reservation.setStatus(ReservationStatus.ACTIVE);
        when(reservationRepository.findByCode("12345")).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        // Act
        reservationService.verifyReservation("12345");

        // Assert
        assertEquals(ReservationStatus.USED, reservation.getStatus());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    public void testVerifyReservation_whenNotFound() {
        // Arrange
        when(reservationRepository.findByCode("12345")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reservationService.verifyReservation("12345"));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    public void testVerifyReservation_whenAlreadyUsed() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setCode("12345");
        reservation.setStatus(ReservationStatus.USED);
        when(reservationRepository.findByCode("12345")).thenReturn(Optional.of(reservation));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> reservationService.verifyReservation("12345"));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    public void testVerifyReservation_whenAlreadyCancelled() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setCode("12345");
        reservation.setStatus(ReservationStatus.CANCELLED);
        when(reservationRepository.findByCode("12345")).thenReturn(Optional.of(reservation));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> reservationService.verifyReservation("12345"));
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    public void testCreateReservation_whenDateIsInPast() {
        // Arrange
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setDate(LocalDate.now().minusDays(1));
        menu.setReservationLimit(10);
        menu.setBookedMeals(5);
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> reservationService.createReservation(1L));
        verify(reservationRepository, never()).save(any(Reservation.class));
        verify(menuRepository, never()).save(any(Menu.class));
    }

    @Test
    public void testBookMeal_pastDate() throws Exception {
        // Arrange
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setDate(LocalDate.now().minusDays(1));
        menu.setReservationLimit(10);
        menu.setBookedMeals(5);
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> reservationService.createReservation(1L));
        verify(reservationRepository, never()).save(any(Reservation.class));
        verify(menuRepository, never()).save(any(Menu.class));
    }
}