<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Kurka
  Date: 2019-01-01
  Time: 21:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value='/static/css/bootstrap.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/static/css/et.css'/>" rel="stylesheet"/>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.7.0/css/all.css"
          integrity="sha384-lZN37f5QGtY3VHgisS14W3ExzMWZxybE1SJSEsQp9S+oqd12jhcu+A56Ebc1zFSJ" crossorigin="anonymous">
</head>
<body>
<jsp:include page="navbar.jsp"/>

<div class="container">
    <div class="row">
        <div class="col-sm-12 col-md-8 col-lg-7 mx-auto">
            <div class="card card-body bg-dark mt-5">
                <div class="row">
                    <div class="col-sm-12 col-md-6 col-lg-6 p-2">
                        <i class="fas fa-calendar-day fa-2x m-2"></i>
                        Date: <fmt:formatDate value="${expense.date}" pattern="MM/dd/yyyy"/>
                    </div>
                    <div class="col-sm-12 col-md-6 col-lg-6 p-2">
                        <i class="${expense.category.categoryIcon}"></i>
                        Category: ${expense.category.categoryName}
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12 col-md-6 col-lg-6 p-2">
                        <i class="fas fa-money-bill fa-2x m-2"></i>
                        Value: ${expense.value} zl
                    </div>
                    <div class="col-sm-12 col-md-6 col-lg-6 p-2">
                        <i class="fas fa-scroll fa-2x m-2"></i>
                        Description: ${expense.description}
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-6 p-2">
                        <form action="<c:url value="/expense/edit"/>">
                            <input type="hidden" name="id" value="${expense.id}">
                            <input type="submit" value="Edit expense" class="btn btn-warning">
                        </form>
                    </div>
                    <div class="col-sm-6 p-2">
                        <form method="post" action="<c:url value="/expense/delete/${expense.id}"/> ">
                            <button type="submit" onclick="return confirm('Do you really want to delete the expense?');"
                                    class="btn btn-danger">Delete expense
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</div>
<br><br>


</body>
</html>
