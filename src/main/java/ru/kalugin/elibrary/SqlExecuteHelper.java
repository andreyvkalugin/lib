package ru.kalugin.elibrary;

import ru.kalugin.elibrary.NewRecordInLibraryServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

public class SqlExecuteHelper {
    private Connection connect;
    private static Operations stateCurrent;
    private Statement connStatement;
    public static ResultSet result;

    public SqlExecuteHelper() {
        try {
            Class.forName("org.h2.Driver");
            connect = DriverManager.getConnection("jdbc:h2:~/file:test", "sa", "");
            connStatement = connect.createStatement();
            connStatement.execute("CREATE TABLE IF NOT EXISTS books (id BIGINT AUTO_INCREMENT PRIMARY KEY, title varchar(100) not NULL, author varchar(100) not NULL, quantity numeric not NULL);");
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void destroyConnection() {
        try {connStatement.close();}
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public static ResultSet getResultStateAndClear(){
        ResultSet dummy = result;
        result = null;
        return dummy;
    }

    public static Operations evaluateState (NewRecordInLibraryServlet servaletInfo) {
        if ((servaletInfo.title == null || servaletInfo.author == null || servaletInfo.quantity == null) && (servaletInfo.query == null))
            stateCurrent = Operations.GET_ALL_BOOKS_OPERATION;
        else if (servaletInfo.title != null && servaletInfo.author != null && servaletInfo.quantity != null)
            stateCurrent = Operations.INSERT_OPERATION;
        else if (servaletInfo.query != null)
            stateCurrent = Operations.FIND_BOOK_OPERATION;
        return stateCurrent;
    }

    public static void redirectTo (NewRecordInLibraryServlet servlet, String to, HttpServletRequest request, HttpServletResponse response) {
        try {
            servlet.getServletContext().getRequestDispatcher(to).forward(request, response);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertInBooks (NewRecordInLibraryServlet servaletInfo) {
        try {
            connStatement.execute("insert into books (title,author,quantity) values ('" + servaletInfo.title + "', '" + servaletInfo.author + "'," + servaletInfo.quantity + ");");
            result = connStatement.executeQuery("SELECT * FROM books");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void findInBooks (NewRecordInLibraryServlet servaletInfo) {
        try {
            result = connStatement.executeQuery("select * from books where title like '%"+ servaletInfo.query +"%' or author like '%"+ servaletInfo.query +"%'");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void printAllBooks () {
        try {
            result = connStatement.executeQuery("SELECT * FROM books");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public enum Operations
    {
        INSERT_OPERATION,
        GET_ALL_BOOKS_OPERATION,
        FIND_BOOK_OPERATION
    }
}


