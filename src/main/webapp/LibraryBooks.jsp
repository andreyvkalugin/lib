<%@ page import="java.sql.*" %>
<%@ page import="ru.kalugin.elibrary.SqlExecuteHelper" %>
<%--
  Created by IntelliJ IDEA.
  User: Kalugin Andrey
  Date: 25.07.2020
  Time: 11:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
        ResultSet resultFromQuery = SqlExecuteHelper.getResultStateAndClear();
%>

<head>
    <meta charset='UTF-8'>
    <title>Перечень найденных книг</title>
    <style>table{border-spacing: 7px 11px;}</style>
</head>
    <table>
        <tbody>
        <tr>
            <th width="10">id</th>
            <th width="80">author</th>
            <th width="80">title</th>
            <th align="left">quantity</th>
        </tr>
            <% while (resultFromQuery.next()){%>
                <tr>
                    <td width="+10+">
                        <%=resultFromQuery.getInt(1)%>
                    </td>
                    <td>
                        <%=resultFromQuery.getString(2)%>
                    </td>
                    <td>
                        <%=resultFromQuery.getString(3)%>
                    </td>
                    <td>
                        <%=resultFromQuery.getInt(4)%>
                    </td>
                </tr>
          <%}%>
        </tbody>
    </table>
</html>
