package ru.kalugin.elibrary;

public class Book {
	protected int id;
	protected String title;
	protected String author;
	protected int quantity;

	public Book() {
	}

	public Book(int id, String author) {
		this.id = id;
		this.author = author;
	}

	public Book(int id, String title, String author, int quantity) {
		this(title, author, quantity);
		this.id = id;
	}

	public Book(String title, String author, int quantity) {
		this.title = title;
		this.author = author;
		this.quantity = quantity;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Book{" +
				"id=" + id +
				", title='" + title + '\'' +
				", author='" + author + '\'' +
				", quantity=" + quantity +
				'}';
	}
}
