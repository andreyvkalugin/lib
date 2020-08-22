<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>Author list</title>
</head>
<body>
	<center>
		<h1>Books Management</h1>
        <h2>
        	<a href="list">List All Books</a>
        </h2>
	</center>
    <div align="center">
        <table border="1" cellpadding="5">
            <caption><h2>List of Authors</h2></caption>
            <tr>
                <th>ID</th>
                <th>Author</th>
            </tr>
            <c:forEach var="author" items="${listAuthor}">
                <tr>
                    <td><c:out value="${author.id}" /></td>
                    <td><c:out value="${author.author}" /></td>
                </tr>
            </c:forEach>
        </table>
    </div>	
</body>
</html>
