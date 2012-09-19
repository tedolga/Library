<%@ taglib prefix="sql_rt" uri="http://java.sun.com/jstl/sql_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Hello</title>
</head>
<body>
<table border="0" width="700">
    <tr>
        <td width="150"> &nbsp; </td>
        <td width="550">
            <h1>What is your name?</h1>
        </td>
    </tr>
    <tr>
        <td width="150"> &nbsp; </td>
        <td width="550">
            <form method="post">
                <input type="text" name="personName" size="25">
                <br>
                <input type="submit" value="Submit">
                <input type="reset" value="Reset">
            </form>
        </td>
    </tr>
    <tr>
        <td width="150">&nbsp;</td>
        <td width="550">
            <c:out value="${greeting}">...</c:out>
        </td>
    </tr>

</table>

</body>
</html>