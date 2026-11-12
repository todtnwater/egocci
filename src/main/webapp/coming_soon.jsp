<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String pageTitle = (String)request.getAttribute("pageTitle");
    if(pageTitle == null) pageTitle = "Coming Soon";
    String userName = (String)session.getAttribute("userName");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%=pageTitle%> - KOKYOUNGBIN</title>
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/menu.css">
</head>
<script>
    function goPage(gubun) {
        work.t_gubun.value = gubun;
        work.method = "post";
        work.action = "Member";
        work.submit();
    }
</script>
<form name="work">
    <input type="hidden" name="t_gubun">
</form>

<header class="header">
    <div id="m-title"><a href="Index">KOKYOUNGBIN</a></div>
</header>

<body>
<div class="container">
    <nav class="left-menu">
        <div class="menu-toggle">
            <div class="menu-icon">≡</div>
        </div>
        <div class="menu-content">
            <ul>
                <li><a href="Song">Song</a></li>
                <li><a href="Scheduled">Scheduled Performance</a></li>
                <li><a href="Stage">Live Stage</a></li>
                <li><a href="Gallery">Gallery</a></li>
                <li><a href="Video">Video</a></li>
                <li><a href="Projects">Projects</a></li>
            </ul>
            <ul id="menu-down">
                <li><a href="egocci">egocci</a></li>
                <li><a></a></li>
                <%
                if(userName != null) {
                %>
                <li><a href="javascript:void(0)">👤 <%=userName%>님</a></li>
                <li><a href="javascript:goPage('logout')">Logout</a></li>
                <%
                } else {
                %>
                <li><a href="javascript:goPage('login')">Login</a></li>
                <%}%>
            </ul>
        </div>
    </nav>

    <main class="main-content">
        <div style="display:flex; flex-direction:column; align-items:center; justify-content:center; height:60vh; color:white; text-align:center;">
            <h1 style="font-size:2.5rem; margin-bottom:20px; letter-spacing:0.1em;"><%=pageTitle%></h1>
            <p style="font-size:1.1rem; opacity:0.7; margin-bottom:40px;">준비 중입니다. Coming Soon.</p>
            <a href="Index" style="color:white; opacity:0.5; font-size:0.9rem; text-decoration:none; border-bottom:1px solid rgba(255,255,255,0.3); padding-bottom:2px;">← Back to Home</a>
        </div>
    </main>
</div>

<footer class="footer">
    <div class="footer-container">
        <address class="address">
            <p class="titlefooter">KOKYOUNGBIN</p>
            <p>04039 서울특별시 마포구 홍익로3길 20 | 20, Hongik-ro 3-gil, Mapo-gu, Seoul, Republic of Korea</p>
            <p>korangemgmt@gmail.com</p>
        </address>
        <p class="copyright">Copyright &copy; egocci All rights reserved.</p>
    </div>
</footer>
</body>
</html>
