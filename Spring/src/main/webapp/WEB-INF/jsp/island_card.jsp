<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="resources/css/add_island.css">
</head>
<body>
<div class="form-container">
    <h2>Island Information Form</h2>
    <form:form action="${pageContext.request.contextPath}/update_island" modelAttribute="island" method="post">

        <label for="islandName">Island Name:</label>
        <form:input path="islandName" id="islandName" required="true" />

        <label for="islandArea">Island Area (sq km):</label>
        <form:input path="area" id="islandArea" type="number" required="true" />

        <label for="islandPrice">Island Price ($):</label>
        <form:input path="price" id="islandPrice" type="number" required="true" />

        <label for="pricePerDay">Price Per Day ($):</label>
        <form:input path="pricePerDay" id="pricePerDay" type="number" required="true" />

        <label for="country">Country:</label>
        <form:select path="country.id" id="country" required="true">
            <form:options items="${countries}" itemValue="id" itemLabel="countryName" />
        </form:select>
        <button type="submit">Update</button>
    </form:form>
    <form action="${pageContext.request.contextPath}/uploadImage" method="post" enctype="multipart/form-data">

        <label for="islandImage">Upload Island Image:</label>
        <input type="file" name="image" id="islandImage" />

        <input type="hidden" name="islandId" value="${island.id}" />

        <button type="submit">Upload Image</button>

    </form>
</div>
</body>
</html>
