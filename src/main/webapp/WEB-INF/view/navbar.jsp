<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value='/static/css/bootstrap.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/static/css/et.css'/>" rel="stylesheet"/>
</head>
<body>
<nav class="navbar navbar-dark bg-dark navbar-expand-lg sticky-top">
    <a class="navbar-brand" href="<c:url value="/home"/>">ExpensesTracker</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent">
        <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link" href="<c:url value="/add"/>">Add new</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="<c:url value="/showLast30Days"/>">Last 30 days</a>
            </li>
        </ul>
        <form class="form-inline ml-auto" action="<c:url value="/showSpecificMoth"/>">
            <input type="hidden" value="all" name="selectedCategory">
            <select class="form-control mr-sm-2" name="month">
                <option value="0">Whole year</option>
                <c:forEach items="${month}" var="month">
                    <option value="${month}">${month.monthName} </option>
                </c:forEach>
            </select>
            <input class="form-control mr-sm-2" name="year" type="text" placeholder="rok" required
                   oninvalid="this.setCustomValidity('nieprawidłowy rok')"
                   oninput="this.setCustomValidity('')">
            <button class="btn btn-success" type="submit">Search</button>
        </form>
    </div>
</nav>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>

<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.2.1/js/bootstrap.min.js"
        integrity="sha384-B0UglyR+jN6CkvvICOB2joaf5I4l3gm9GU6Hc1og6Ls7i6U/mkkaduKaBhlAXv9k"
        crossorigin="anonymous"></script>

</body>
</html>