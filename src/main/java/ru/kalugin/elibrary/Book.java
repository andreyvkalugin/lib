package ru.kalugin.elibrary;


public class Book {
	protected int id;
	protected String title;
	protected String author;
	protected float price;

	public Book() {
	}

	public Book(int id, String author) {
		this.id = id;
		this.author = author;
	}

	public Book(int id, String title, String author, float price) {
		this(title, author, price);
		this.id = id;
	}

	public Book(String title, String author, float price) {
		this.title = title;
		this.author = author;
		this.price = price;
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

	public float getPrice() {
		return this.price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Book{" +
				"id=" + id +
				", title='" + title + '\'' +
				", author='" + author + '\'' +
				", price=" + price +
				'}';
	}
}
