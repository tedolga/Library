<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <title>Hello Page</title>
</head>
<body>
<h2>Enter Your Name!</h2>
<form:form method="post" action="home.html">

    <table>
        <tr>
            <td><form:label path="person">Name</form:label></td>
            <td><form:input path="person"/></td>
        </tr>
        <tr>
            <td colspan="1">
                <input type="submit" value="Submit"/>
            </td>
        </tr>
    </table>

</form:form>
</body>
</html>