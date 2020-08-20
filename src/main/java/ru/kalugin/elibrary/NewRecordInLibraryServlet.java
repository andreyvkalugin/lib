package ru.kalugin.elibrary;

import ru.kalugin.elibrary.SqlExecuteHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

public class NewRecordInLibraryServlet extends HttpServlet {
    //public SqlExecuteHelper executeHelper = new SqlExecuteHelper();
    public String title, author, quantity, query;
    private Connection connect;
    private static SqlExecuteHelper.Operations stateCurrent;
    private Statement connStatement;
    private ResultSet result;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    public void setResultState (ResultSet x) {
        SqlExecuteHelper.result = x;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        title = request.getParameter("title");
        author = request.getParameter("author");
        quantity = request.getParameter("quantity");
        query = request.getParameter("query");

        try {
            Class.forName("org.h2.Driver");
            connect = DriverManager.getConnection("jdbc:h2:~/test", "sa", "");
            connStatement = connect.createStatement();
            connStatement.execute("CREATE TABLE IF NOT EXISTS books (id BIGINT AUTO_INCREMENT PRIMARY KEY, title varchar(100) not NULL, author varchar(100) not NULL, quantity numeric not NULL);");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            connStatement.execute("insert into books (title,author,quantity) values ('" + title + "', '" + author + "'," + quantity + ");");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            result = connStatement.executeQuery("SELECT * FROM books");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        setResultState(result);

        this.getServletContext().getRequestDispatcher("/LibraryBooks.jsp").forward(request, response);
      /* switch ( SqlExecuteHelper.evaluateState (this))
       {
           case GET_ALL_BOOKS_OPERATION: {
               executeHelper.printAllBooks();
               SqlExecuteHelper.redirectTo(this, "/LibraryBooks.jsp", request, response);
               }
           case INSERT_OPERATION:{
               executeHelper.insertInBooks(this);
               SqlExecuteHelper.redirectTo(this, "/LibraryBooks.jsp", request, response);
           }
           case FIND_BOOK_OPERATION:{
               executeHelper.findInBooks(this);
               SqlExecuteHelper.redirectTo(this, "/LibraryBooks.jsp", request, response);
           }
       }*/
    }
}

