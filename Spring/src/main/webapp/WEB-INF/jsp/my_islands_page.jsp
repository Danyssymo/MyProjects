<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="utf-8">
    <meta name="keywords" content="Путешествовать, Начни свой путь, Скидка 10-30% Off, почему выбрали нас">
    <meta name="description" content="">
    <title>Главная</title>
    <link rel="stylesheet" href="resources/css/nicepage.css" media="screen">
    <link rel="stylesheet" href="resources/css/main2.css" media="screen">
    <script class="u-script" type="text/javascript" src="resources/js/main.js" defer=""></script>
    <meta name="generator" content="Nicepage 6.15.2, nicepage.com">
    <link id="u-theme-google-font" rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:100,100i,300,300i,400,400i,500,500i,700,700i,900,900i|Open+Sans:300,300i,400,400i,500,500i,600,600i,700,700i,800,800i">
    <link id="u-page-google-font" rel="stylesheet" href="https://fonts.googleapis.com/css?family=Lobster:400">
    <link href="https://fonts.googleapis.com/css2?family=Pacifico&display=swap" rel="stylesheet">
    <script type="application/ld+json">{
        "@context": "http://schema.org",
        "@type": "Organization",
        "name": "",
        "sameAs": []
    }</script>
    <meta name="theme-color" content="#478ac9">
    <meta property="og:title" content="Главная">
    <meta property="og:type" content="website">
    <meta data-intl-tel-input-cdn-path="intlTelInput/">
</head>
<body>
<section class="u-clearfix u-section-3" id="sec-ad85">
    <div class="u-clearfix u-sheet u-sheet-1">
        <h2 class="u-align-center u-text u-text-1" data-animation-name="customAnimationIn"
            data-animation-duration="1500"> Your Islands</h2>
        <a href="${pageContext.request.contextPath}/pagination" class="button-my-island">O</a>
        <div class="u-expanded-width u-list u-list-1">
            <div id="islands-container" class="u-repeater u-repeater-1">
                <c:forEach var="myIsland" items="${myIslands}">
                    <div class="u-container-style u-image u-image-round u-list-item u-radius-25 u-repeater-item u-shading u-image-1" data-image-width="934" data-image-height="921" data-animation-name="customAnimationIn" data-animation-duration="1500" data-animation-delay="500">
                        <div class="u-container-layout u-similar-container u-valign-bottom u-container-layout-1">
                            <div class="u-border-3 u-border-white u-line u-line-horizontal u-opacity u-opacity-45 u-line-1" data-animation-name="customAnimationIn" data-animation-duration="1500" data-animation-delay="500"></div>
                            <h3 class="u-text u-text-default u-text-2" data-animation-name="customAnimationIn" data-animation-duration="1750" data-animation-delay="500">${myIsland.islandName} | ${myIsland.country.countryName}</h3>
                            <p class="u-text u-text-default u-text-3">${myIsland.area} Km²</p>
                            <p class="u-text u-text-default u-text-3">${myIsland.price} $</p>
                            <a href="${pageContext.request.contextPath}/island_card?id=${myIsland.id}" class="button-my-island">O</a>
                        </div>
                        <c:if test="${myIsland.image != null}">
                            <img src="${pageContext.request.contextPath}${myIsland.image.imagePath}" alt="Island Image" style="max-width: 300px;"/>
                        </c:if>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</section>
</body>
</html>
