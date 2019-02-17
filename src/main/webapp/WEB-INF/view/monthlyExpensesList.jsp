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
<jsp:include page="navbar.jsp"/>

<div class="container">
    <div class="row my-4">
        <div class="col-sm-12 col-md-8 col-lg-9">
            <h2>${message}</h2>
        </div>
        <div class="col-sm-6 col-md-4 col-lg-3">
            <form action="<c:url value="/expense/expensesList" />" class="ml-auto">
                <input type="hidden" name="message" value="${message}">
                <select onchange="this.form.submit()" name="selectedCategory" class="custom-select mb-3">
                    <option hidden>Select category</option>
                    <option value="All"
                            <c:if test="${selectedCategory == 'All'}">selected</c:if>
                    >all
                    </option>
                    <c:forEach items="${categories}" var="category">
                        <option value="${category}"
                                <c:if test="${category.toString() == selectedCategory}">selected</c:if>
                        >${category.categoryName} </option>
                    </c:forEach>
                </select>
            </form>
        </div>
        <br>

    </div>
    <div class="table-responsive">
        <table class="table table-striped table-dark text-center">
            <thead>
            <tr>
                <th scope="col">Value</th>
                <th scope="col">Date</th>
                <th scope="col">Category</th>
            </tr>
            </thead>
            <c:choose>
                <c:when test="${filteredMonthlyExpenses == null}">
                    <c:forEach items="${monthlyExpenses}" var="expense">
                        <tbody>
                        <tr>
                            <td class="align-middle">${expense.value} zl</td>
                            <td class="align-middle"><fmt:formatDate value="${expense.date}"
                                                                     pattern="MM/dd/yyyy"/></td>
                            <td class="align-middle">${expense.category.categoryName}</td>
                            <td>
                                <form action="<c:url value="/expense/showExpense"/>">
                                    <input type="hidden" name="id" value="${expense.id}">
                                    <button type="submit" class="btn btn-success">Show expense</button>
                                </form>
                            </td>
                        </tr>
                        </tbody>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <c:forEach items="${filteredMonthlyExpenses}" var="expense">
                        <tbody>
                        <tr>
                            <td class="align-middle">${expense.value} zl</td>
                            <td class="align-middle"><fmt:formatDate value="${expense.date}"
                                                                     pattern="MM/dd/yyyy"/></td>
                            <td class="align-middle">${expense.category.categoryName}</td>
                            <td>
                                <form action="<c:url value="/expense/showExpense"/>">
                                    <input type="hidden" name="id" value="${expense.id}">
                                    <button type="submit" class="btn btn-success">Show expense</button>
                                </form>
                            </td>
                        </tr>
                        </tbody>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </table>
    </div>
</div>
</body>
</html>
