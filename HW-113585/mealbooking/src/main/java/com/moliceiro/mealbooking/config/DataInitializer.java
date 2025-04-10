package com.moliceiro.mealbooking.config;

import com.moliceiro.mealbooking.model.Menu;
import com.moliceiro.mealbooking.model.Restaurant;
import com.moliceiro.mealbooking.repository.MenuRepository;
import com.moliceiro.mealbooking.repository.RestaurantRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initDatabase(RestaurantRepository restaurantRepository, MenuRepository menuRepository) {
        return args -> {

            // Insert Restaurants
            Restaurant restaurant1 = new Restaurant();
            restaurant1.setName("Campus Canteen");
            restaurant1 = restaurantRepository.save(restaurant1);

            Restaurant restaurant2 = new Restaurant();
            restaurant2.setName("Engeneering Facility Canteen");
            restaurant2 = restaurantRepository.save(restaurant2);

            Restaurant restaurant3 = new Restaurant();
            restaurant3.setName("University Restaurant");
            restaurant3 = restaurantRepository.save(restaurant3);

            Restaurant restaurant4 = new Restaurant();
            restaurant4.setName("ThreeDims Restaurant");
            restaurant4 = restaurantRepository.save(restaurant4);

            Restaurant restaurant5 = new Restaurant();
            restaurant5.setName("Language Facility Restaurant");
            restaurant5 = restaurantRepository.save(restaurant5);

            // Insert Menus for Restaurant 1 (Campus Canteen)
            Menu menu1 = new Menu();
            menu1.setRestaurant(restaurant1);
            menu1.setDate(LocalDate.of(2025, 4, 10));
            menu1.setDescription("Pizza and Salad");
            menu1.setReservationLimit(10);
            menu1.setBookedMeals(0);
            menuRepository.save(menu1);

            Menu menu2 = new Menu();
            menu2.setRestaurant(restaurant1);
            menu2.setDate(LocalDate.of(2025, 4, 11));
            menu2.setDescription("Pasta and Soup");
            menu2.setReservationLimit(10);
            menu2.setBookedMeals(0);
            menuRepository.save(menu2);

            Menu menu5 = new Menu();
            menu5.setRestaurant(restaurant1);
            menu5.setDate(LocalDate.of(2025, 4, 12));
            menu5.setDescription("Sushi and Miso Soup");
            menu5.setReservationLimit(10);
            menu5.setBookedMeals(0);
            menuRepository.save(menu5);

            Menu menu7 = new Menu();
            menu7.setRestaurant(restaurant1);
            menu7.setDate(LocalDate.of(2025, 4, 13));
            menu7.setDescription("Steak and Potatoes");
            menu7.setReservationLimit(10);
            menu7.setBookedMeals(0);
            menuRepository.save(menu7);

            // Insert Menus for Restaurant 2 (Engineering Facility Canteen)
            Menu menu3 = new Menu();
            menu3.setRestaurant(restaurant2);
            menu3.setDate(LocalDate.of(2025, 4, 8));
            menu3.setDescription("Burger and Fries");
            menu3.setReservationLimit(5);
            menu3.setBookedMeals(0);
            menuRepository.save(menu3);

            Menu menu4 = new Menu();
            menu4.setRestaurant(restaurant2);
            menu4.setDate(LocalDate.of(2025, 4, 10));
            menu4.setDescription("Salad and Sandwich");
            menu4.setReservationLimit(1);
            menu4.setBookedMeals(0);
            menuRepository.save(menu4);

            Menu menu6 = new Menu();
            menu6.setRestaurant(restaurant2);
            menu6.setDate(LocalDate.of(2025, 4, 10));
            menu6.setDescription("Tacos and Guacamole");
            menu6.setReservationLimit(5);
            menu6.setBookedMeals(0);
            menuRepository.save(menu6);

            Menu menu8 = new Menu();
            menu8.setRestaurant(restaurant2);
            menu8.setDate(LocalDate.of(2025, 4, 11));
            menu8.setDescription("Pasta Primavera");
            menu8.setReservationLimit(5);
            menu8.setBookedMeals(0);
            menuRepository.save(menu8);

            // Insert Menus for Restaurant 3 (University Restaurant)
            Menu menu9 = new Menu();
            menu9.setRestaurant(restaurant3);
            menu9.setDate(LocalDate.of(2025, 4, 8));
            menu9.setDescription("Chicken Curry");
            menu9.setReservationLimit(15);
            menu9.setBookedMeals(0);
            menuRepository.save(menu9);

            Menu menu10 = new Menu();
            menu10.setRestaurant(restaurant3);
            menu10.setDate(LocalDate.of(2025, 4, 9));
            menu10.setDescription("Vegetable Stir Fry");
            menu10.setReservationLimit(15);
            menu10.setBookedMeals(0);
            menuRepository.save(menu10);

            Menu menu11 = new Menu();
            menu11.setRestaurant(restaurant3);
            menu11.setDate(LocalDate.of(2025, 4, 10));
            menu11.setDescription("Fish and Chips");
            menu11.setReservationLimit(15);
            menu11.setBookedMeals(0);
            menuRepository.save(menu11);

            Menu menu12 = new Menu();
            menu12.setRestaurant(restaurant3);
            menu12.setDate(LocalDate.of(2025, 4, 11));
            menu12.setDescription("BBQ Ribs");
            menu12.setReservationLimit(15);
            menu12.setBookedMeals(0);
            menuRepository.save(menu12);

            // Insert Menus for Restaurant 4 (ThreeDims Restaurant)
            Menu menu13 = new Menu();
            menu13.setRestaurant(restaurant4);
            menu13.setDate(LocalDate.of(2025, 4, 8));
            menu13.setDescription("Grilled Chicken Salad");
            menu13.setReservationLimit(20);
            menu13.setBookedMeals(0);
            menuRepository.save(menu13);

            Menu menu14 = new Menu();
            menu14.setRestaurant(restaurant4);
            menu14.setDate(LocalDate.of(2025, 4, 9));
            menu14.setDescription("Vegetable Lasagna");
            menu14.setReservationLimit(20);
            menu14.setBookedMeals(0);
            menuRepository.save(menu14);

            Menu menu15 = new Menu();
            menu15.setRestaurant(restaurant4);
            menu15.setDate(LocalDate.of(2025, 4, 10));
            menu15.setDescription("Beef Stroganoff");
            menu15.setReservationLimit(20);
            menu15.setBookedMeals(0);
            menuRepository.save(menu15);

            Menu menu16 = new Menu();
            menu16.setRestaurant(restaurant4);
            menu16.setDate(LocalDate.of(2025, 4, 11));
            menu16.setDescription("Vegetable Stir Fry");
            menu16.setReservationLimit(20);
            menu16.setBookedMeals(0);
            menuRepository.save(menu16);

            // Insert Menus for Restaurant 5 (Language Facility Restaurant)
            Menu menu17 = new Menu();
            menu17.setRestaurant(restaurant5);
            menu17.setDate(LocalDate.of(2025, 4, 8));
            menu17.setDescription("Chicken Caesar Salad");
            menu17.setReservationLimit(25);
            menu17.setBookedMeals(0);
            menuRepository.save(menu17);

            Menu menu18 = new Menu();
            menu18.setRestaurant(restaurant5);
            menu18.setDate(LocalDate.of(2025, 4, 9));
            menu18.setDescription("Vegetable Stir Fry");
            menu18.setReservationLimit(25);
            menu18.setBookedMeals(0);
            menuRepository.save(menu18);

            Menu menu19 = new Menu();
            menu19.setRestaurant(restaurant5);
            menu19.setDate(LocalDate.of(2025, 4, 10));
            menu19.setDescription("Pasta Primavera");
            menu19.setReservationLimit(25);
            menu19.setBookedMeals(0);
            menuRepository.save(menu19);

            Menu menu20 = new Menu();
            menu20.setRestaurant(restaurant5);
            menu20.setDate(LocalDate.of(2025, 4, 11));
            menu20.setDescription("BBQ Chicken");
            menu20.setReservationLimit(25);
            menu20.setBookedMeals(0);
            menuRepository.save(menu20);

            // Insert Menus for Restaurant 5 (Language Facility Restaurant)
            Menu menu21 = new Menu();
            menu21.setRestaurant(restaurant5);
            menu21.setDate(LocalDate.of(2025, 4, 8));
            menu21.setDescription("Chicken Caesar Salad");
            menu21.setReservationLimit(30);
            menu21.setBookedMeals(0);
            menuRepository.save(menu21);

            Menu menu22 = new Menu();
            menu22.setRestaurant(restaurant5);
            menu22.setDate(LocalDate.of(2025, 4, 9));
            menu22.setDescription("Vegetable Stir Fry");
            menu22.setReservationLimit(30);
            menu22.setBookedMeals(0);
            menuRepository.save(menu22);

            Menu menu23 = new Menu();
            menu23.setRestaurant(restaurant5);
            menu23.setDate(LocalDate.of(2025, 4, 10));
            menu23.setDescription("Pasta Primavera");
            menu23.setReservationLimit(30);
            menu23.setBookedMeals(0);
            menuRepository.save(menu23);

            Menu menu24 = new Menu();
            menu24.setRestaurant(restaurant5);
            menu24.setDate(LocalDate.of(2025, 4, 11));
            menu24.setDescription("BBQ Chicken");
            menu24.setReservationLimit(30);
            menu24.setBookedMeals(0);
            menuRepository.save(menu24);
        };
    }
}