<%--suppress ALL --%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    response.setCharacterEncoding("UTF-8");
    request.setCharacterEncoding("UTF-8");
%>
<html>
<head>
    <title>Edit Expense</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value='/static/css/bootstrap.css'/>" rel="stylesheet"/>
    <link href="<c:url value='/static/css/et.css'/>" rel="stylesheet"/>
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/demos/style.css">

</head>
<body>
<jsp:include page="navbar.jsp"/>

<div class="container">
    <div class="row">
        <div class="col-sm-12 col-md-8 col-lg-6 mx-auto">
            <div class="card card-body bg-dark mt-5 mx-auto">
                <div class="card-title text-center text-white">
                    <h3 class="text-capitalize">edit expense</h3>
                </div>
                <form:form modelAttribute="expense" method="post">

                    <div class="col-sm-12 col-md-8 col-lg-7  mx-auto">
                        <div class="input-group mt-3 mx-aut">
                            <div class="input-group-prepend">
                                <span class="input-group-text">Date</span>
                            </div>
                            <form:input id="datepicker" path="date" readonly="true" class="form-control"/>
                        </div>
                    </div>


                    <div class="input-group w-50 mx-auto">
                        <form:errors path="date" cssClass="error text-danger"/>
                    </div>


                    <div class="col-sm-12 col-md-8 col-lg-7  mx-auto">
                        <div class="input-group mt-3">
                            <div class="input-group-prepend">
                                <span class="input-group-text">Value</span>
                            </div>
                            <form:input path="value" class="form-control"/>
                        </div>
                    </div>


                    <div class="input-group w-50 mx-auto">
                        <form:errors path="value" cssClass="error text-danger"/>
                    </div>


                    <div class="col-sm-12 col-md-8 col-lg-7  mx-auto">
                        <div class="input-group mt-3 mx-auto">
                            <div class="input-group-prepend">
                                <span class="input-group-text">Category</span>
                            </div>
                            <form:select class="form-control" path="category">
                                <c:forEach items="${categories}" var="category">
                                    <form:option value="${category}">${category.categoryName}</form:option>
                                </c:forEach>
                            </form:select>
                        </div>
                    </div>

                    <div class="col-sm-12 col-md-8 col-lg-7  mx-auto">
                        <div class="input-group mt-4 mx-auto">
                            <div class="input-group-prepend">
                                <span class="input-group-text">Description</span>
                            </div>
                            <form:input class="form-control" path="description"/>
                        </div>
                    </div>

                    <div class="input-group w-50 mx-auto">
                        <form:errors path="description" cssClass="error text-danger"/>
                    </div>

                    <div class="input-group mt-3 w-50 mx-auto">
                        <input type="submit" class="btn btn-success pt-1 px-4" value="Edit">
                    </div>

                </form:form>
            </div>
        </div>
    </div>
</div>
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script>
    $(function () {
        $("#datepicker").datepicker();
    });
</script>
</body>
</html>
