<%--
  Created by IntelliJ IDEA.
  User: Msi-G6547
  Date: 13.08.2024
  Time: 3:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:forEach var="island" items="${islands}">
    <div class="u-container-style u-image u-image-round u-list-item u-radius-25 u-repeater-item u-shading u-image-1" data-image-width="934" data-image-height="921" data-animation-name="customAnimationIn" data-animation-duration="1500" data-animation-delay="500">
        <div class="u-container-layout u-similar-container u-valign-bottom u-container-layout-1">
            <div class="u-border-3 u-border-white u-line u-line-horizontal u-opacity u-opacity-45 u-line-1" data-animation-name="customAnimationIn" data-animation-duration="1500" data-animation-delay="500"></div>
            <h3 class="u-text u-text-default u-text-2" data-animation-name="customAnimationIn" data-animation-duration="1750" data-animation-delay="500"> South Africa</h3>
            <p class="u-text u-text-default u-text-3"> Ut enim ad minim veniam</p>
        </div>
    </div>
</c:forEach>
