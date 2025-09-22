<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dto.SongDto" %>
<%
    // 안전한 데이터 가져오기
    SongDto latestSong = null;
    ArrayList<SongDto> popularSongs = null;
    
    try {
        latestSong = (SongDto)request.getAttribute("latestSong");
        popularSongs = (ArrayList<SongDto>)request.getAttribute("popularSongs");
    } catch(Exception e) {
        System.out.println("데이터 가져오기 오류: " + e.getMessage());
    }
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

<!-- 헤더 -->
<header class="header">
    <div id="m-title"><a href="Index">KOKYOUNGBIN</a></div>
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
        
        <!-- 고정 소셜 링크들 -->
        <a href="https://www.instagram.com/egoccl/" class="contact-icon" title="Instagram" target="_blank">
            <img src="images/Instagram.png" alt="Instagram" onerror="this.outerHTML='<span style=\'font-size:20px;\'>📷</span>'">
        </a>
        <a href="https://www.youtube.com/@egoccl/videos" class="contact-icon" title="YouTube" target="_blank">
            <img src="images/Youtube.png" alt="YouTube" onerror="this.outerHTML='<span style=\'font-size:20px;\'>📺</span>'">
        </a>
        <a href="https://soundcloud.com/egoccl" class="contact-icon" title="SoundCloud" target="_blank">
            <img src="images/Soundcloud.png" alt="SoundCloud" onerror="this.outerHTML='<span style=\'font-size:20px;\'>🎵</span>'">
        </a>
        <a href="https://open.spotify.com/artist/0Tu6vmfKlkQB9kCyYNt79I" class="contact-icon" title="Spotify" target="_blank">
            <img src="images/Spotify.png" alt="Spotify" onerror="this.outerHTML='<span style=\'font-size:20px;\'>🎶</span>'">
        </a>
        <a href="https://music.apple.com/kr/artist/고경빈/1461067097" class="contact-icon" title="Apple Music" target="_blank">
            <img src="images/Applemusic.png" alt="Apple Music" onerror="this.outerHTML='<span style=\'font-size:20px;\'>🍎</span>'">
        </a>
        <a href="https://www.melon.com/artist/timeline.htm?artistId=2857454" class="contact-icon" title="Melon" target="_blank">
            <img src="images/Melon.png" alt="Melon" onerror="this.outerHTML='<span style=\'font-size:20px;\'>🍈</span>'">
        </a>
    </div>
    
    <!-- 이메일 패널 -->
    <div class="email-panel">
        <div class="panel-header">
            <label for="email-toggle" class="close-btn">×</label>
            <h3>Send Email</h3>
            <img src="images/email_pic.jpg" class="panel-profile-pic" 
                 onerror="this.style.display='none'">
        </div>
        <form class="email-form" id="emailForm">
            <input type="text" name="name" placeholder="Your Name" required>
            <input type="email" name="email" placeholder="Your Email" required>
            <input type="text" name="subject" placeholder="Subject" required>
            <textarea name="message" placeholder="Your Message" required></textarea>
            <button type="submit" class="send-btn">Send Message</button>
        </form>
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

    <!-- Main Content -->
    <main class="main-content">
        <!-- 디버깅 정보 (개발시에만 사용) -->
        <%-- 
        <div style="background: rgba(255,255,255,0.1); padding: 10px; margin: 10px; color: white;">
            <p>디버깅 정보:</p>
            <p>latestSong: <%=latestSong != null ? latestSong.getSong_title() : "null"%></p>
            <p>popularSongs: <%=popularSongs != null ? popularSongs.size() + "개" : "null"%></p>
        </div>
        --%>
        
        <!-- 콘텐츠 영역 1번 최신 곡 정보 -->
        <div class="content-top">
            <%if(latestSong != null) {%>
            <div class="video-container">
                <iframe 
                    src="https://www.youtube.com/embed/RPitSv41MSw?autoplay=0&mute=0&controls=1" 
                    frameborder="0" 
                    allow="autoplay; encrypted-media" 
                    allowfullscreen>
                </iframe>
            </div>
            <div class="index-new-info">
                <h2><%=latestSong.getSong_title()%></h2>
                <p class="info"><%=latestSong.getRelease_date() != null ? latestSong.getRelease_date() : "2024"%> | 
                싱글 | <%=latestSong.getDuration() != null ? latestSong.getDuration() : "3:11"%></p>
                
                <%if(latestSong.getLyrics() != null && !latestSong.getLyrics().equals("")) {%>
                <textarea disabled><%=latestSong.getLyrics()%></textarea>
                <%} else {%>
                <textarea disabled>최신 곡 <%=latestSong.getSong_title()%>

아티스트: <%=latestSong.getArtist_name()%>
장르: <%=latestSong.getGenre() != null ? latestSong.getGenre() : "발라드"%>

감상해주셔서 감사합니다.</textarea>
                <%}%>
                
                <div class="index-icon-container">
                    <a href="Song?t_gubun=view&t_no=<%=latestSong.getSong_id()%>" class="index-icon" title="곡 상세보기">
                        <span style="font-size: 24px; color: white;">♪</span>
                    </a>
                    <a href="https://www.youtube.com/@egoccl/videos" class="index-icon" target="_blank">
                        <img src="images/Youtube.png" alt="YouTube" onerror="this.outerHTML='<span style=\'font-size:20px;\'>📺</span>'">
                    </a>
                    <a href="https://open.spotify.com/artist/0Tu6vmfKlkQB9kCyYNt79I" class="index-icon" target="_blank">
                        <img src="images/Spotify.png" alt="Spotify" onerror="this.outerHTML='<span style=\'font-size:20px;\'>🎵</span>'">
                    </a>
                    <a href="https://music.apple.com/kr/artist/고경빈/1461067097" class="index-icon" target="_blank">
                        <img src="images/Applemusic.png" alt="Apple Music" onerror="this.outerHTML='<span style=\'font-size:20px;\'>🍎</span>'">
                    </a>
                </div>
            </div>
            <%} else {%>
            <div class="index-new-info">
                <h2>KOKYOUNGBIN</h2>
                <p class="info">아티스트 포트폴리오 사이트</p>
                <textarea disabled>안녕하세요, 고경빈입니다.

이곳에서 제 음악을 만나보실 수 있습니다.
다양한 감정과 이야기를 담은 곡들을 
여러 플랫폼에서 감상하실 수 있습니다.

음악과 함께해주셔서 감사합니다.</textarea>
                
                <div class="index-icon-container">
                    <a href="Song" class="index-icon" title="전체 곡 보기">
                        <span style="font-size: 24px; color: white;">♪</span>
                    </a>
                    <a href="https://www.youtube.com/@egoccl/videos" class="index-icon" target="_blank">
                        <img src="images/Youtube.png" alt="YouTube" onerror="this.outerHTML='<span style=\'font-size:20px;\'>📺</span>'">
                    </a>
                    <a href="https://open.spotify.com/artist/0Tu6vmfKlkQB9kCyYNt79I" class="index-icon" target="_blank">
                        <img src="images/Spotify.png" alt="Spotify" onerror="this.outerHTML='<span style=\'font-size:20px;\'>🎵</span>'">
                    </a>
                </div>
            </div>
            <%}%>
        </div>

        <div class="content-row">
            <!-- 콘텐츠 영역 2번(왼쪽) -->
            <div class="content-mid1">
                <a href="Scheduled">
                    <img class="sc-img" src="images/scheduled/p1.png" 
                         onerror="this.outerHTML='<div style=\'background:rgba(255,255,255,0.1);height:200px;display:flex;align-items:center;justify-content:center;color:white;font-size:18px;border-radius:8px;\'>Scheduled Performance</div>'">
                </a>
            </div>
            <!-- 콘텐츠 영역 3번(오른쪽) -->
            <div class="content-mid2">
                <div class="thumbnail-container">
                    <a href="Video?id=1">
                        <img src="https://img.youtube.com/vi/jeVN6ybJW-Q/maxresdefault.jpg" alt="유튜브 썸네일">
                    </a>
                </div>
                <div class="thumbnail-container">
                    <a href="Video?id=2">
                        <img src="https://img.youtube.com/vi/KpYlVYK2qxk/maxresdefault.jpg" alt="유튜브 썸네일">
                    </a>
                </div>
                <div class="thumbnail-container">
                    <a href="Video?id=3">
                        <img src="https://img.youtube.com/vi/3nZMIP8hwfQ/maxresdefault.jpg" alt="유튜브 썸네일">
                    </a>
                </div>
                <div class="thumbnail-container">
                    <a href="Video?id=4">
                        <img src="https://img.youtube.com/vi/3YrPvM-Qsws/maxresdefault.jpg" alt="유튜브 썸네일">
                    </a>
                </div>
                <div class="thumbnail-container">
                    <a href="Video?id=5">
                        <img src="https://img.youtube.com/vi/uQqzV6XPbbI/maxresdefault.jpg" alt="유튜브 썸네일">
                    </a>
                </div>
            </div>
        </div>

        <!-- 콘텐츠 영역 4번(아래) 인기 곡 슬라이더 -->
        <div class="content-mid3">
            <div class="music-slider-section">
                <div class="slider-container">
                    <div class="cards-wrapper">
                        <div class="cards-track" id="cardsTrack">
                            <%
                            if(popularSongs != null && !popularSongs.isEmpty()) {
                                for(SongDto song : popularSongs) {
                            %>
                            <a href="Song?t_gubun=view&t_no=<%=song.getSong_id()%>" class="music-card">
                                <div class="card-image">
                                    <div class="song-cover-placeholder">
                                        <div class="cover-info">
                                            <span class="cover-icon">♪</span>
                                            <p class="cover-title"><%=song.getSong_title()%></p>
                                        </div>
                                    </div>
                                </div>
                                <div class="card-content">
                                    <h3 class="song-title"><%=song.getSong_title()%></h3>
                                    <p class="artist-name"><%=song.getArtist_name()%></p>
                                    <%if(song.getIs_title_track()) {%>
                                    <span class="title-track-badge">타이틀</span>
                                    <%}%>
                                </div>
                            </a>
                            <%
                                }
                            } else {
                            %>
                            <!-- 기본 곡들 (DB 연결 실패시) -->
                            <a href="Song" class="music-card">
                                <div class="card-image">
                                    <div class="song-cover-placeholder">
                                        <div class="cover-info">
                                            <span class="cover-icon">♪</span>
                                            <p class="cover-title">일렁임</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="card-content">
                                    <h3 class="song-title">일렁임</h3>
                                    <p class="artist-name">고경빈 (KOKYOUNGBIN)</p>
                                </div>
                            </a>
                            
                            <a href="Song" class="music-card">
                                <div class="card-image">
                                    <div class="song-cover-placeholder">
                                        <div class="cover-info">
                                            <span class="cover-icon">♪</span>
                                            <p class="cover-title">제일 좋아하긴 해</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="card-content">
                                    <h3 class="song-title">제일 좋아하긴 해</h3>
                                    <p class="artist-name">고경빈 (KOKYOUNGBIN)</p>
                                    <span class="title-track-badge">타이틀</span>
                                </div>
                            </a>
                            
                            <a href="Song" class="music-card">
                                <div class="card-image">
                                    <div class="song-cover-placeholder">
                                        <div class="cover-info">
                                            <span class="cover-icon">♪</span>
                                            <p class="cover-title">사랑노래</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="card-content">
                                    <h3 class="song-title">사랑노래</h3>
                                    <p class="artist-name">고경빈 (KOKYOUNGBIN)</p>
                                </div>
                            </a>
                            <%}%>
                        </div>
                    </div>

                    <button class="nav-btn prev" id="prevBtn">
                        <svg width="18" height="18" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m15 18-6-6 6-6"/>
                        </svg>
                    </button>

                    <button class="nav-btn next" id="nextBtn">
                        <svg width="18" height="18" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m9 18 6-6-6-6"/>
                        </svg>
                    </button>
                </div>

                <div class="indicators" id="indicators">
                    <!-- 인디케이터들이 동적으로 생성됩니다 -->
                </div>
            </div>
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