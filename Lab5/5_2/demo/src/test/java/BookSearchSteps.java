import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.DataTableType;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import tqs.cucumber_5_2.Book;
import tqs.cucumber_5_2.Library;
import tqs.cucumber_5_2.Utils;


public class BookSearchSteps {
	Library library = new Library();
	List<Book> result = new ArrayList<>();

	@ParameterType("[0-9]{1,2} [A-Za-z]+ [0-9]{4}")
	public LocalDate localDate(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);
		return LocalDate.parse(date, formatter);
	}


	@DataTableType
	public Book bookEntry(Map<String, String> tableEntry){
		return new Book(
				tableEntry.get("title"),
				tableEntry.get("author"),
				Utils.isoTextToLocalDate( tableEntry.get("published") ) );
	}

	@Given("a book with the title {string}, written by {string}, published in {localDate}")
	public void addBook(final String title, final String author, final LocalDate published) {
		Book book = new Book(title, author, published);
		library.addBook(book);
	}
	
	@Given("another book with the title {string}, written by {string}, published in {localDate}")
	public void addAnotherBook(final String title, final String author, final LocalDate published) {
		Book book = new Book(title, author, published);
		library.addBook(book);
	}
	

	@When("the customer searches for books published between {int} and {int}")
	public void setSearchParameters(int fromYear, int toYear) {
		LocalDate from = LocalDate.of(fromYear, 1, 1);
		LocalDate to = LocalDate.of(toYear, 12, 31);
		result = library.findBooks(from, to);
	}


	@Then("{int} books should have been found")
	public void verifyAmountOfBooksFound(final int booksFound) {
        assertEquals(booksFound, result.size());
	}

	@Then("Book {int} should have the title {string}")
	public void verifyBookAtPosition(final int position, final String title) {
		assertEquals(result.get(position - 1).getTitle(), title);
	}
}