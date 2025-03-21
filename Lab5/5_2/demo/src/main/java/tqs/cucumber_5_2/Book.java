package tqs.cucumber_5_2;

import java.time.LocalDate;

public class Book {
	private final String title;
	private final String author;
	private final LocalDate published;
	
	public Book(String title, String author, LocalDate published) {
		this.title = title;
		this.author = author;
		this.published = published;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public LocalDate getPublished() {
		return published;
	}

	@Override
	public String toString() {
		return "Book [title=" + title + ", author=" + author + ", published=" + published + "]";
	}

	// constructors, getter, setter ommitted
	
}