package ru.kalugin.elibrary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DTO {
	private String jdbcURL;
	private String jdbcUsername;
	private String jdbcPassword;
	private Connection jdbcConnection;
	private static boolean firstRequest = true;
	
	public DTO(String jdbcURL, String jdbcUsername, String jdbcPassword) {
		this.jdbcURL = jdbcURL;
		this.jdbcUsername = jdbcUsername;
		this.jdbcPassword = jdbcPassword;
	}
	
	protected void connect() throws SQLException {
		if (jdbcConnection == null || jdbcConnection.isClosed()) {
			try {
				Class.forName("org.h2.Driver");
			} catch (ClassNotFoundException e) {
				throw new SQLException(e);
			}
			jdbcConnection = DriverManager.getConnection(
										jdbcURL, jdbcUsername, jdbcPassword);
			if (firstRequest){
				firstRequest = false;
				Statement statement = jdbcConnection.createStatement();
				statement.addBatch("CREATE TABLE IF NOT EXISTS author_table (id INT(11) AUTO_INCREMENT PRIMARY KEY, author VARCHAR(100) not NULL);");
				statement.addBatch("CREATE TABLE IF NOT EXISTS book (book_id INT(11) AUTO_INCREMENT PRIMARY KEY, title VARCHAR(100) not NULL, price FLOAT not NULL, author_id int(11), FOREIGN KEY (author_id) REFERENCES author_table (id));");
				//statement.addBatch("INSERT INTO author_table (author) VALUES ('кирил');");
				//statement.addBatch("INSERT INTO book (title, price, author_id) VALUES ('жуков', 5, 1);");
				statement.executeBatch();
				statement.close();
			}
		}
	}
	
	protected void disconnect() throws SQLException {
		if (jdbcConnection != null && !jdbcConnection.isClosed()) {
			jdbcConnection.close();
		}
	}
	
	public void insertBook(Book book) throws SQLException {
		connect();
		getInsertionInAuthorTable(book);
		getInsertionInBookTable(book, getAuthorId(book.getAuthor()));
		disconnect();
	}

	private void getInsertionInBookTable(Book book, int authorId) throws SQLException {
		String setBook = "INSERT INTO book (title, price, author_id) VALUES (?, ?, ?)";

		PreparedStatement statement = jdbcConnection.prepareStatement(setBook);
		statement.setString(1, book.getTitle());
		statement.setFloat(2, book.getPrice());
		statement.setInt(3, authorId);
		statement.executeUpdate();
		statement.close();
	}

	private void getInsertionInAuthorTable(Book book) throws SQLException {
		String setAuthor = "INSERT INTO author_table (author) SELECT * FROM (SELECT CAST (? as CHAR(11))) AS tmp WHERE NOT EXISTS (SELECT author FROM author_table WHERE author = CAST (? as CHAR(11))) LIMIT 1;";
		PreparedStatement statement = jdbcConnection.prepareStatement(setAuthor);
		statement.setString(1, book.getAuthor());
		statement.setString(2, book.getAuthor());
		statement.executeUpdate();
		statement.close();
	}

	public List<Book> listAllBooks() throws SQLException {
		List<Book> listBook = new ArrayList<>();
		
		String sql = "SELECT b.book_id, b.title, a.author, b.price FROM book AS b INNER JOIN author_table AS a ON b.author_id = a.id";
		
		connect();
		
		Statement statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(sql);
		
		while (resultSet.next()) {
			int id = resultSet.getInt(1);
			String title = resultSet.getString(2);
			String author = resultSet.getString(3);
			float price = resultSet.getFloat(4);
			
			Book book = new Book(id, title, author, price);
			listBook.add(book);
		}
		
		resultSet.close();
		statement.close();
		
		disconnect();
		
		return listBook;
	}
	
	public boolean deleteBook(Book book) throws SQLException {
		String sql = "DELETE FROM book where book_id = ?";
		
		connect();
		
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setInt(1, book.getId());
		
		boolean rowDeleted = statement.executeUpdate() > 0;
		statement.close();
		disconnect();
		return rowDeleted;		
	}
	
	public boolean updateBook(Book book) throws SQLException {
		String sql = "UPDATE book SET title = ?, price = ?, author_id = ?";
		sql += " WHERE book_id = ?";
		connect();
		int authorId = getAuthorId(book.getAuthor());
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setString(1, book.getTitle());
		statement.setFloat(2,  book.getPrice());
		statement.setInt(3, authorId);
		statement.setInt(4, book.getId());
		
		boolean rowUpdated = statement.executeUpdate() > 0;
		statement.close();
		disconnect();
		return rowUpdated;		
	}

	private int getAuthorId(String author) throws SQLException {
		String getAuthorSql = "(SELECT * FROM author_table WHERE author = ?) LIMIT 1";

		PreparedStatement statement = jdbcConnection.prepareStatement(getAuthorSql);
		statement.setString(1, author);
		System.out.println(author);
		ResultSet resultSet = statement.executeQuery();
		resultSet.next();
		int authorId = resultSet.getInt("id");
		System.out.println(authorId);
		statement.close();

		return authorId;
	}

	public Book getBook(int id) throws SQLException {
		Book book = null;
		String sql = "SELECT * FROM book WHERE book_id = ?";
		
		connect();
		
		PreparedStatement statement = jdbcConnection.prepareStatement(sql);
		statement.setInt(1, id);
		
		ResultSet resultSet = statement.executeQuery();
		
		if (resultSet.next()) {
			String title = resultSet.getString("title");
			String author = resultSet.getString("author");
			float price = resultSet.getFloat("price");
			
			book = new Book(id, title, author, price);
		}
		
		resultSet.close();
		statement.close();
		
		return book;
	}

	public List<Author> listAllAuthor() throws SQLException {
		String getAuthorsSql = "SELECT * FROM author_table";
		List<Author> listAuthor = new ArrayList<>();
		connect();

		Statement statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(getAuthorsSql);

		while (resultSet.next()) {
			int id = resultSet.getInt(1);
			String author = resultSet.getString(2);

			Author authors = new Author(id, author);
			listAuthor.add(authors);
		}

		resultSet.close();
		statement.close();

		disconnect();

		return listAuthor;


	}
}
