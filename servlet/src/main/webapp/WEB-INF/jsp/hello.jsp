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