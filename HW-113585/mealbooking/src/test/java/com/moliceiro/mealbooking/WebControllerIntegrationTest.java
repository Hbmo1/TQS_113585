package com.moliceiro.mealbooking;

import com.moliceiro.mealbooking.model.Menu;
import com.moliceiro.mealbooking.model.Reservation;
import com.moliceiro.mealbooking.model.ReservationStatus;
import com.moliceiro.mealbooking.model.Restaurant;
import com.moliceiro.mealbooking.repository.MenuRepository;
import com.moliceiro.mealbooking.repository.ReservationRepository;
import com.moliceiro.mealbooking.repository.RestaurantRepository;
import com.moliceiro.mealbooking.service.MenuService;
import com.moliceiro.mealbooking.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integrationTest")
public class WebControllerIntegrationTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        public WeatherService weatherService() {
            WeatherService weatherService = Mockito.mock(WeatherService.class);
            Mockito.when(weatherService.getWeatherForecast(Mockito.any(LocalDate.class), Mockito.any(AtomicLong.class), Mockito.any(AtomicLong.class), Mockito.any(AtomicLong.class)))
                    .thenReturn("Sunny, 25.0°C");
            return weatherService;
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MenuService menuService;

    private Restaurant restaurant;
    private Menu menu;
    private Reservation reservation;

    @BeforeEach
    public void setUp() {
        reservationRepository.deleteAll();
        menuRepository.deleteAll();
        restaurantRepository.deleteAll();

        restaurant = new Restaurant();
        restaurant.setName("Test Restaurant");
        restaurant = restaurantRepository.save(restaurant);

        menu = menuService.createMenu(
                restaurant.getId(),
                LocalDate.now().plusDays(1),
                "Test Menu",
                10
        );

        reservation = new Reservation();
        reservation.setCode("TEST123");
        reservation.setMenu(menu);
        reservation.setDate(menu.getDate());
        reservation.setStatus(ReservationStatus.ACTIVE);
        reservation = reservationRepository.save(reservation);
    }

    @Test
    public void testHomePage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("restaurants"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Test Restaurant")));
    }

    @Test
    public void testMenusPage() throws Exception {
        mockMvc.perform(get("/menus")
                .param("restaurantId", restaurant.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("menus"))
                .andExpect(model().attributeExists("menus"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Test Menu")));
    }

    @Test
    public void testBookMeal_success() throws Exception {
        mockMvc.perform(post("/book")
                .param("menuId", menu.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("reservation"))
                .andExpect(model().attributeExists("reservation"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Code:")));
    }

    @Test
    public void testBookMeal_noAvailableMeals() throws Exception {
        menu.setBookedMeals(10);
        menuRepository.save(menu);

        mockMvc.perform(post("/book")
                .param("menuId", menu.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("menus"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("No available meals for this menu.")));
    }

    @Test
    public void testBookMeal_pastDate() throws Exception {
        Menu pastMenu = menuService.createMenu(
                restaurant.getId(),
                LocalDate.now().minusDays(1),
                "Past Menu",
                10
        );

        mockMvc.perform(post("/book")
                .param("menuId", pastMenu.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("menus"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Cannot book a meal for a past date.")));
    }

    @Test
    public void testCheckForm() throws Exception {
        mockMvc.perform(get("/check"))
                .andExpect(status().isOk())
                .andExpect(view().name("check"));
    }

    @Test
    public void testCheckReservation_success() throws Exception {
        mockMvc.perform(post("/check")
                .param("code", "TEST123"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservation"))
                .andExpect(model().attributeExists("reservation"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("TEST123")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Cancel")))
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("disabled"))));
    }

    @Test
    public void testCheckReservation_cancelled() throws Exception {
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        mockMvc.perform(post("/check")
                .param("code", "TEST123"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservation"))
                .andExpect(model().attributeExists("reservation"))
                .andExpect(model().attribute("reservation", org.hamcrest.Matchers.hasProperty("status", org.hamcrest.Matchers.equalTo(ReservationStatus.CANCELLED))))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("TEST123")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Cancel")));
    }

    @Test
    public void testCheckReservation_used() throws Exception {
        reservation.setStatus(ReservationStatus.USED);
        reservationRepository.save(reservation);

        mockMvc.perform(post("/check")
                .param("code", "TEST123"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservation"))
                .andExpect(model().attributeExists("reservation"))
                .andExpect(model().attribute("reservation", org.hamcrest.Matchers.hasProperty("status", org.hamcrest.Matchers.equalTo(ReservationStatus.USED))))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("TEST123")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Cancel")));
    }

    @Test
    public void testCheckReservation_notFound() throws Exception {
        mockMvc.perform(post("/check")
                .param("code", "INVALID"))
                .andExpect(status().isOk())
                .andExpect(view().name("check"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Reservation not found")));
    }

    @Test
    public void testCancelReservation_success() throws Exception {
        mockMvc.perform(post("/cancel")
                .param("code", "TEST123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void testCancelReservation_alreadyCancelled() throws Exception {
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        mockMvc.perform(post("/cancel")
                .param("code", "TEST123"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservation"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Reservation has already been cancelled")));
    }

    @Test
    public void testCancelReservation_alreadyUsed() throws Exception {
        reservation.setStatus(ReservationStatus.USED);
        reservationRepository.save(reservation);

        mockMvc.perform(post("/cancel")
                .param("code", "TEST123"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservation"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Reservation has already been used")));
    }

    @Test
    public void testVerifyForm() throws Exception {
        mockMvc.perform(get("/verify"))
                .andExpect(status().isOk())
                .andExpect(view().name("verify"));
    }

    @Test
    public void testVerifyReservation_success() throws Exception {
        mockMvc.perform(post("/verify")
                .param("code", "TEST123"))
                .andExpect(status().isOk())
                .andExpect(view().name("verify"))
                .andExpect(model().attributeExists("message"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Reservation verified and marked as used.")));
    }

    @Test
    public void testVerifyReservation_alreadyUsed() throws Exception {
        reservation.setStatus(ReservationStatus.USED);
        reservationRepository.save(reservation);

        mockMvc.perform(post("/verify")
                .param("code", "TEST123"))
                .andExpect(status().isOk())
                .andExpect(view().name("verify"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Reservation has already been verified")));
    }

    @Test
    public void testVerifyReservation_cancelled() throws Exception {
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        mockMvc.perform(post("/verify")
                .param("code", "TEST123"))
                .andExpect(status().isOk())
                .andExpect(view().name("verify"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Reservation has been cancelled")));
    }
}