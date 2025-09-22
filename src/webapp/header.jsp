<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dto.SocialDto" %>
<%
    ArrayList<SocialDto> socialLinks = (ArrayList<SocialDto>)request.getAttribute("socialLinks");
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>KOKYOUNGBIN</title>
    <link href="https://fonts.cdnfonts.com/css/varsity-team" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
	<script src="script.js"></script>
</head>

<header class="header">
    <div id="m-title"><a href="index.jsp">KOKYOUNGBIN</a></div>
</header>

<body>
<input type="checkbox" id="email-toggle">

<!-- 우측 콘택트 메뉴 -->
<div class="right-contact-menu">
    <div class="contact-icons">
        <label for="email-toggle" class="contact-icon email-icon" title="Send Email">
            <svg width="40" height="40" fill="currentColor" viewBox="0 0 16 16">
                <path d="M0 4a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V4Zm2-1a1 1 0 0 0-1 1v.217l7 4.2 7-4.2V4a1 1 0 0 0-1-1H2Zm13 2.383-4.708 2.825L15 11.105V5.383Zm-.034 6.876-5.64-3.471L8 9.583l-1.326-.795-5.64 3.47A1 1 0 0 0 2 13h12a1 1 0 0 0 .966-.741ZM1 11.105l4.708-2.897L1 5.383v5.722Z"/>
            </svg>
        </label>
        
        <!-- 동적 소셜 링크 -->
        <%
        if(socialLinks != null && !socialLinks.isEmpty()) {
            for(SocialDto social : socialLinks) {
        %>
        <a href="<%=social.getPlatform_url()%>" class="contact-icon" title="<%=social.getPlatform_name()%>" target="_blank">
            <img src="images/<%=social.getPlatform_icon()%>" alt="<%=social.getPlatform_name()%>">
        </a>
        <%
            }
        } else {
        %>
        <!-- 기본 소셜 링크들 (DB 연결 실패시) -->
        <a href="https://www.instagram.com/egoccl/" class="contact-icon" title="Instagram" target="_blank">
            <img src="images/Instagram.png">
        </a>
        <a href="https://www.youtube.com/@egoccl/videos" class="contact-icon" title="YouTube" target="_blank">
            <img src="images/Youtube.png">
        </a>
        <a href="https://soundcloud.com/egoccl" class="contact-icon" title="SoundCloud" target="_blank">
            <img src="images/Soundcloud.png">
        </a>
        <a href="https://open.spotify.com/artist/0Tu6vmfKlkQB9kCyYNt79I" class="contact-icon" title="Spotify" target="_blank">
            <img src="images/Spotify.png">
        </a>
        <%}%>
    </div>
    
    <!-- 이메일 패널 -->
    <div class="email-panel">
        <div class="panel-header">
            <label for="email-toggle" class="close-btn">×</label>
            <h3>Send Email</h3>
            <img src="images/email_pic.jpg" class="panel-profile-pic">
        </div>
        <form class="email-form" id="emailForm">
            <input type="text" name="name" placeholder="Your Name" required>
            <input type="email" name="email" placeholder="Your Email" required>
            <input type="text" name="subject" placeholder="Subject" required>
            <textarea name="message" placeholder="Your Message" required></textarea>
            <button type="submit" class="send-btn">Send Message</button>
        </form>
        <!-- 모바일용 이메일 표시 -->
        <div class="email-display-mobile">
            <p>Contact us directly:</p>
            <div class="email-address-box">
                <input type="text" value="korangemgmt@gmail.com" id="email-address" readonly>
                <button type="button" onclick="copyEmail()">복사</button>
            </div>
            <p id="copy-msg">복사완료!</p>
        </div>
    </div>
</div>

<div class="container">
    <!-- 레프트 메뉴 -->
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
                <li><a href="Video">egocci</a></li>
            </ul>
        </div>
    </nav>
</div>