import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.en.*;
import org.openqa.selenium.*;

import java.util.List;

//TODO: Use correct css and xpath selectors

public class BookstoreSteps {

    private final WebDriver driver = DriverManager.driver;

    // @BeforeAll
    // public void setup() {
    //     WebDriverManager.chromedriver().setup();
    // }

    // @AfterAll
    // public void tearDown() {
    //     if (driver != null) {
    //         driver.quit();
    //     }
    // }

    @When("I navigate to {string}")
    public void i_navigate_to(String url) {
        driver.get(url);
    }

    @Then("I should see a list of books in the genre {string}")
    public void i_should_see_books_in_genre(String genre) {
        WebElement genreSpan = driver.findElement(By.cssSelector("[data-testid='genre-section-header']"));
        assertTrue(genreSpan.getText().toLowerCase().contains(genre.toLowerCase()));
        List<WebElement> books = driver.findElements(By.cssSelector("[data-testid='book-listing-item']"));
        assertFalse(books.isEmpty(), "No books found for the genre");
    }

    @And("I click on the book titled {string}")
    public void i_click_on_book(String title) {
        WebElement book = driver.findElements(By.cssSelector("[data-testid='book-listing-item']"))
            .stream()
            .filter(el -> el.getText().trim().equalsIgnoreCase(title))
            .findFirst()
            .orElseThrow(() -> new AssertionError("Book not found: " + title));
        book.click();
    }

    @Then("I should be redirected to the book detail page for {string} with the URL {string}")
    public void i_should_be_redirected_to_detail_page(String title, String url) {
        assertEquals(url, driver.getCurrentUrl());
        WebElement titleElement = driver.findElement(By.cssSelector("[data-testid='book-details']"));
        assertEquals(title, titleElement.getText().trim());
    }

    @And("I search for {string}")
    public void i_search_for(String query) {
        WebElement searchInput = driver.findElement(By.name("search"));
        searchInput.sendKeys(query);
        searchInput.sendKeys(Keys.ENTER);
    }

    @Then("{int} books should have been found")
    public void books_should_have_been_found(int count) {
        List<WebElement> books = driver.findElements(By.cssSelector(".book"));
        assertEquals(count, books.size());
    }

    @And("Book {int} should have the title {string}")
    public void book_should_have_title(int index, String title) {
        List<WebElement> titles = driver.findElements(By.cssSelector(".book .book-title"));
        assertEquals(title, titles.get(index - 1).getText().trim());
    }

    @And("both books should have been written by {string}")
    public void both_books_should_have_author(String author) {
        List<WebElement> authors = driver.findElements(By.cssSelector(".book .book-author"));
        assertEquals(2, authors.size());
        for (WebElement el : authors) {
            assertEquals(author, el.getText().trim());
        }
    }

    @And("I click on {string} for the book titled {string}")
    public void i_click_add_to_cart(String buttonText, String bookTitle) {
        WebElement book = driver.findElements(By.cssSelector(".book"))
            .stream()
            .filter(b -> b.findElement(By.className("book-title")).getText().trim().equalsIgnoreCase(bookTitle))
            .findFirst()
            .orElseThrow(() -> new AssertionError("Book not found: " + bookTitle));
        book.findElement(By.xpath(".//button[contains(text(), '" + buttonText + "')]")).click();
    }

    @Then("the cart should show {int} items")
    public void cart_should_show_items(int count) {
        WebElement cartIcon = driver.findElement(By.id("cart-count"));
        assertEquals(String.valueOf(count), cartIcon.getText());
    }

    @And("I click on {string}")
    public void i_click_on(String buttonText) {
        WebElement button = driver.findElement(By.xpath("//button[contains(text(), '" + buttonText + "')]"));
        button.click();
    }

    @And("I fill in the credit card information with an invalid card number")
    public void i_fill_invalid_card() {
        WebElement cardInput = driver.findElement(By.name("cardNumber"));
        cardInput.sendKeys("123");
    }

    @Then("I should see an error message {string}")
    public void i_should_see_error_message(String msg) {
        WebElement error = driver.findElement(By.className("StripeElement--invalid"));
        assertTrue(error.getText().contains(msg));
    }
}
