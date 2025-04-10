package com.moliceiro.mealbooking.controller;

import com.moliceiro.mealbooking.model.Menu;
import com.moliceiro.mealbooking.model.Reservation;
import com.moliceiro.mealbooking.repository.RestaurantRepository;
import com.moliceiro.mealbooking.service.MenuService;
import com.moliceiro.mealbooking.service.ReservationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.HashMap;

@Controller
public class WebController {
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MenuService menuService;
    @Autowired
    private ReservationService reservationService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("restaurants", restaurantRepository.findAll());
        return "home";
    }

    @Operation(summary = "Get menus for a restaurant", description = "Retrieves a list of available menus for a specified restaurant within a date range.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved menus"),
        @ApiResponse(responseCode = "400", description = "Invalid restaurant ID")
    })
    @GetMapping("/menus")
    public String menus(@RequestParam Long restaurantId, Model model) {
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(1);
        LocalDate endDate = today.plusDays(6);
        model.addAttribute("menus", menuService.getMenusForRestaurant(restaurantId, startDate, endDate));
        model.addAttribute("restaurantId", restaurantId);
        return "menus";
    }

    @Operation(summary = "Book a meal", description = "Books a meal for a specified menu and creates a reservation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully booked a meal"),
            @ApiResponse(responseCode = "400", description = "Invalid menu ID or no available meals")
    })
    @PostMapping("/book")
    public String book(@RequestParam Long menuId, Model model) {
        try {
            Reservation reservation = reservationService.createReservation(menuId);
            model.addAttribute("reservation", reservation);
            return "reservation";
        } catch (IllegalStateException e) {
            Menu menu = menuService.getMenuById(menuId).orElse(null);
            if (menu == null) {
                model.addAttribute("errorMessage", "Menu not found");
                return "error";
            }
            Long restaurantId = menu.getRestaurant().getId();
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusDays(6);
            model.addAttribute("menus", menuService.getMenusForRestaurant(restaurantId, startDate, endDate));
            model.addAttribute("restaurantId", restaurantId);
            model.addAttribute("errorMessage", e.getMessage());
            return "menus";
        }
    }

    @GetMapping("/check")
    public String checkForm() {
        return "check";
    }

    @Operation(summary = "Check a reservation", description = "Retrieves the details of a reservation using its unique code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved reservation details"),
            @ApiResponse(responseCode = "400", description = "Reservation not found")
    })
    @PostMapping("/check")
    public String checkReservation(@RequestParam String code, Model model) {
        try  {
            Reservation reservation = reservationService.getReservationByCode(code);
            model.addAttribute("reservation", reservation);
            return "reservation";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "check";
        }

    }

    @Operation(summary = "Cancel a reservation", description = "Cancels a reservation using its unique code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "Successfully cancelled reservation, redirected to home"),
            @ApiResponse(responseCode = "400", description = "Reservation cannot be cancelled (already cancelled or used)")
    })
    @PostMapping("/cancel")
    public String cancel(@RequestParam String code, Model model, RedirectAttributes redirectAttributes) {
        try {
            reservationService.cancelReservation(code);
            redirectAttributes.addFlashAttribute("message", "Reservation cancelled successfully.");
            model.addAttribute("message", "Reservation cancelled successfully.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            Reservation reservation = reservationService.getReservationByCode(code);
            model.addAttribute("reservation", reservation);
            return "reservation";
        }

        return "redirect:/";
    }

    @GetMapping("/verify")
    public String verifyForm() {
        return "verify";
    }

    @Operation(summary = "Verify a reservation", description = "Verifies a reservation and marks it as used.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully verified reservation"),
            @ApiResponse(responseCode = "400", description = "Reservation cannot be verified (already used or cancelled)")
    })
    @PostMapping("/verify")
    public String verify(@RequestParam String code, Model model) {
        try {
            reservationService.verifyReservation(code);
            model.addAttribute("message", "Reservation verified and marked as used.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "verify";
    }
}