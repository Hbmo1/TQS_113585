package tqs.cucumber_5_2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Library {
	private final List<Book> store = new ArrayList<>();

	public void addBook(final Book book) {
		store.add(book);
	}

	public List<Book> findBooks(final LocalDate from, final LocalDate to) {
		return store.stream().filter(book -> {
			final LocalDate published = book.getPublished();
			return !published.isBefore(from) && !published.isAfter(to);
		}).sorted(Comparator.comparing(Book::getPublished)).collect(Collectors.toList());
	}
}