<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
	<title>Books Store Application</title>
</head>
<body>
	<center>
		<h1>Books Management</h1>
        <h2>
        	<a href="list">List All Books</a>
        </h2>
	</center>
    <div align="center">
			<form action="find" method="post">
        <table border="1" cellpadding="5">
            <caption>
            	<h2>
            		Find Book
            	</h2>
            </caption>
            <tr>
                <th>Query: </th>
                <td>
					<input type="text" name="query" size="45" value="key word for find"/>
                </td>
            </tr>
			<tr>
				<td colspan="2" align="center">
					<input type="submit" value="Find" />
				</td>
			</tr>
        </table>
        </form>
    </div>	
</body>
</html>
