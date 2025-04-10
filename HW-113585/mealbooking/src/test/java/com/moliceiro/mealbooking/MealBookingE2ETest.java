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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MealBookingE2ETest {

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

    @LocalServerPort
    private int port;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MenuService menuService;

    private WebDriver driver;
    private WebDriverWait wait;
    private Restaurant restaurant;
    private Menu menu;
    private Menu pastMenu;
    private Menu noAvailabilityMenu;
    private Reservation activeReservation;
    private Reservation cancelledReservation;
    private Reservation usedReservation;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.firefox.driver", "/path/to/firefoxdriver");
        driver = new FirefoxDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

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

        noAvailabilityMenu = menuService.createMenu(
                restaurant.getId(),
                LocalDate.now().plusDays(2),
                "No Availability Menu",
                1
        );
        noAvailabilityMenu.setBookedMeals(1);
        menuRepository.save(noAvailabilityMenu);

        pastMenu = menuService.createMenu(
                restaurant.getId(),
                LocalDate.now().minusDays(1),
                "Past Menu",
                10
        );

        activeReservation = new Reservation();
        activeReservation.setCode("ACTIVE123");
        activeReservation.setMenu(menu);
        activeReservation.setDate(menu.getDate());
        activeReservation.setStatus(ReservationStatus.ACTIVE);
        activeReservation = reservationRepository.save(activeReservation);

        cancelledReservation = new Reservation();
        cancelledReservation.setCode("CANCELLED123");
        cancelledReservation.setMenu(menu);
        cancelledReservation.setDate(menu.getDate());
        cancelledReservation.setStatus(ReservationStatus.CANCELLED);
        cancelledReservation = reservationRepository.save(cancelledReservation);

        usedReservation = new Reservation();
        usedReservation.setCode("USED123");
        usedReservation.setMenu(menu);
        usedReservation.setDate(menu.getDate());
        usedReservation.setStatus(ReservationStatus.USED);
        usedReservation = reservationRepository.save(usedReservation);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testFullFlow_bookCheckCancelVerify() {
        // Navigate to home page
        driver.get("http://localhost:" + port + "/");
        assertTrue(driver.getTitle().contains("Meal Booking"), "Should be on the home page");

        // Select restaurant from dropdown and view menus
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("restaurantId")));
        dropdown.click();
        WebElement restaurantOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//option[text()='Test Restaurant']")));
        restaurantOption.click();
        WebElement submitButton = driver.findElement(By.xpath("//button[text()='View Menus']"));
        submitButton.click();
        assertTrue(driver.getCurrentUrl().contains("/menus"), "Should be on the menus page");

        // Book a meal
        WebElement bookButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[contains(text(), 'Test Menu')]/following-sibling::td//button[text()='Book']")));
        bookButton.click();

        // Verify we're on the book page and get the reservation code
        assertTrue(driver.getCurrentUrl().contains("/book"), "Should be on the reservation page after booking");
        WebElement codeElement = driver.findElement(By.ByXPath.xpath("//p[strong[normalize-space(text())='Code:']]/span"));
        String reservationCode = codeElement.getText();
        assertFalse(reservationCode.isEmpty(), "Reservation code should be present");

        // Navigate to check page
        WebElement homeLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Home']")));
        homeLink.click();
        WebElement checkLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Check Reservation']")));
        checkLink.click();
        assertTrue(driver.getCurrentUrl().contains("/check"), "Should be on the check page");

        // Check the reservation
        WebElement codeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("code")));
        codeInput.sendKeys(reservationCode);
        WebElement checkSubmitButton = driver.findElement(By.xpath("//button[text()='Check']"));
        checkSubmitButton.click();

        // Verify reservation status
        WebElement statusElement =  driver.findElement(By.ByXPath.xpath("//p[strong[normalize-space(text())='Status:']]/span"));
        assertEquals("ACTIVE", statusElement.getText(), "Reservation status should be ACTIVE");

        // Cancel the reservation
        WebElement cancelButton = driver.findElement(By.xpath("//button[text()='Cancel']"));
        cancelButton.click();

        // Wait for the redirect to the home page and verify the success message
        wait.until(ExpectedConditions.urlContains("/"));
        System.out.println("URL after cancellation: " + driver.getCurrentUrl());
        System.out.println("Cookies after cancellation redirect: " + driver.manage().getCookies());

        WebElement homeHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Select a Restaurant']")));
        assertTrue(homeHeader.isDisplayed(), "Should be redirected to the home page after cancellation");

        WebElement successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'alert-success')]//span")));
        assertEquals("Reservation cancelled successfully.", successMessage.getText(), "Should show success message after cancellation");
        
       // Check the reservation again
        checkLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Check Reservation']")));
        checkLink.click();
        codeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("code")));
        codeInput.sendKeys(reservationCode);
        checkSubmitButton = driver.findElement(By.xpath("//button[text()='Check']"));
        checkSubmitButton.click();

        // Verify updated status
        statusElement = driver.findElement(By.ByXPath.xpath("//p[strong[normalize-space(text())='Status:']]/span"));
        assertEquals("CANCELLED", statusElement.getText(), "Reservation status should be CANCELLED");

        // Navigate to verify page
        homeLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Home']")));
        homeLink.click();
        WebElement verifyLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Verify Reservation']")));
        verifyLink.click();
        assertTrue(driver.getCurrentUrl().contains("/verify"), "Should be on the verify page");

        // Attempt to verify the cancelled reservation
        codeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("code")));
        codeInput.sendKeys(reservationCode);
        WebElement verifySubmitButton = driver.findElement(By.xpath("//button[text()='Verify']"));
        verifySubmitButton.click();

        // Verify error message
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'alert-danger') and @id='errorMessage']")));
        assertEquals("Reservation has been cancelled", errorMessage.getText(), "Should show error for verifying a cancelled reservation");
    }

    @Test
    public void testBookMeal_noAvailability() {
        // Navigate to home page
        driver.get("http://localhost:" + port + "/");

        // Select restaurant from dropdown and view menus
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("restaurantId")));
        dropdown.click();
        WebElement restaurantOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//option[text()='Test Restaurant']")));
        restaurantOption.click();
        WebElement submitButton = driver.findElement(By.xpath("//button[text()='View Menus']"));
        submitButton.click();

        // Verify we're on the menus page
        assertTrue(driver.getCurrentUrl().contains("/menus"), "Should be on the menus page");

        // Check the book button for the no-availability menu
        WebElement bookButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(text(), 'No Availability Menu')]/following-sibling::td//button[text()='Book']")));
        assertTrue(bookButton.getAttribute("disabled") != null, "Book button should be disabled for a menu with no availability");

        // Verify we remain on the menus page
        assertTrue(driver.getCurrentUrl().contains("/menus"), "Should remain on the menus page");
    }

    @Test
    public void testBookMeal_pastDate() {
        // Navigate to home page
        driver.get("http://localhost:" + port + "/");

        // Select restaurant from dropdown and view menus
        WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("restaurantId")));
        dropdown.click();
        WebElement restaurantOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//option[text()='Test Restaurant']")));
        restaurantOption.click();
        WebElement submitButton = driver.findElement(By.xpath("//button[text()='View Menus']"));
        submitButton.click();

        // Verify we're on the menus page
        assertTrue(driver.getCurrentUrl().contains("/menus"), "Should be on the menus page");

        // Attempt to book a past menu
        WebElement bookButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//td[contains(text(), 'Past Menu')]/following-sibling::td//button[text()='Book']")));
        bookButton.click();

        // Verify error message in modal
        WebElement errorModalBody = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("errorModalBody")));
        assertEquals("Cannot book a meal for a past date. Hiding past menus...", errorModalBody.getText(), "Should show error for booking a past date");
    }

    @Test
    public void testCheckReservation_notFound() {
        // Navigate to check page
        driver.get("http://localhost:" + port + "/check");

        // Enter an invalid reservation code
        WebElement codeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("code")));
        codeInput.sendKeys("INVALID_CODE");
        WebElement checkSubmitButton = driver.findElement(By.xpath("//button[text()='Check']"));
        checkSubmitButton.click();

        // Verify error message
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'alert-danger')]//span")));
        assertEquals("Reservation not found", errorMessage.getText(), "Should show 'Reservation not found' message");
    }

    @Test
    public void testVerifyReservation_alreadyUsed() {
        // Navigate to verify page
        driver.get("http://localhost:" + port + "/verify");

        // Attempt to verify a used reservation
        WebElement codeInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("code")));
        codeInput.sendKeys("USED123");
        WebElement verifySubmitButton = driver.findElement(By.xpath("//button[text()='Verify']"));
        verifySubmitButton.click();

        // Verify error message
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'alert-danger') and @id='errorMessage']")));
        assertEquals("Reservation has already been verified", errorMessage.getText(), "Should show error for verifying a used reservation");
    }
}