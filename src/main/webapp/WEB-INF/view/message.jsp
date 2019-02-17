<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value='/static/css/bootstrap.css'/>" rel="stylesheet"/>
    <link href="<c:url value="/static/css/et.css"/>" rel="stylesheet"/>
</head>
<body>
<jsp:include page="navbar.jsp"/>

<div class="container">
    <div class="row">
        <div class="col-sm-12 col-md-8 col-lg-7 mx-auto">
            <div class="card card-body bg-dark text-danger mt-5 mx-auto">
                <h3 class="mx-auto">${message}</h3>
            </div>
        </div>
    </div>
</div>
</body>
</html>
