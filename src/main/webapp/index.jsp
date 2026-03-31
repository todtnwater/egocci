<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dto.SongDto" %>
<%@ page import="dto.ScheduledDto" %>
<%@ page import="dao.ScheduledDao" %>
<%@ page import="dto.VideoDto" %>
<%@ page import="dao.VideoDao" %>

<%
    String sessionId = (String)session.getAttribute("sessionId");
    String userName = (String)session.getAttribute("userName");
    String sessionLevel = (String)session.getAttribute("sessionLevel");
%>
<%
    // 안전한 데이터 가져오기
    SongDto latestSong = null;
    ArrayList<SongDto> popularSongs = null;
    ScheduledDto latestScheduled = null;
    ArrayList<VideoDto> indexVideoList = null;
    VideoDto mainVideo = null;

    try {
        latestSong = (SongDto)request.getAttribute("latestSong");
        popularSongs = (ArrayList<SongDto>)request.getAttribute("popularSongs");
    } catch(Exception e) {
        System.out.println("데이터 가져오기 오류: " + e.getMessage());
    }
    
    try {
        ScheduledDao scheduledDao = new ScheduledDao();
        ArrayList<ScheduledDto> performanceList = scheduledDao.getPerformanceList();
        if (performanceList != null && !performanceList.isEmpty()) {
            latestScheduled = performanceList.get(0);
        }
    } catch(Exception e) {
        System.out.println("스케줄 데이터 가져오기 오류: " + e.getMessage());
    }

    try {
        VideoDao videoDao = new VideoDao();
        indexVideoList = videoDao.getAllVideos();
        if (indexVideoList != null && indexVideoList.size() > 5) {
            indexVideoList = new ArrayList<>(indexVideoList.subList(0, 5));
        }
        mainVideo = videoDao.getMainVideo();
    } catch(Exception e) {
        System.out.println("비디오 데이터 가져오기 오류: " + e.getMessage());
    }

    // 메인 영상 embed URL 결정
    // DB에 is_main=true 인 영상이 있으면 그걸 사용, 없으면 기존 하드코딩 fallback
    String mainEmbedUrl = "https://www.youtube.com/embed/RPitSv41MSw?autoplay=0&mute=0&controls=1";
    if (mainVideo != null && !mainVideo.getEmbedUrl().isEmpty()) {
        mainEmbedUrl = mainVideo.getEmbedUrl() + "?autoplay=0&mute=0&controls=1";
    }
%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.cdnfonts.com/css/varsity-team" rel="stylesheet">
    <title>KOKYOUNGBIN</title>
    
    <link rel="stylesheet" href="css/common.css">
    <link rel="stylesheet" href="css/menu.css"> 
    <link rel="stylesheet" href="css/slider.css">
    <link rel="stylesheet" href="css/index.css">
    
    <script src="js/script.js"></script>
</head>
<body>

<script>
	function goPage(gubun) {
		work.t_gubun.value = gubun;
		work.method = "post";
		work.action = "Member";
		work.submit();
	}
	function goPageManager(gubun){
		work.t_gubun.value = gubun;
		work.method="post";
		work.action="Manager";
		work.submit();
	}
	function goSongView(songId) {
		song.t_gubun.value = "view";
		song.t_no.value = songId;
		song.method = "post";
		song.action = "Song";
		song.submit();
	}
</script>
<form name="work">
	<input type="hidden" name="t_gubun">
</form>
<form name="song">
	<input type="hidden" name="t_gubun">
	<input type="hidden" name="t_no">
</form>

<input type="checkbox" id="email-toggle">

<!-- 헤더 -->
<header class="header">
    <div id="m-title"><a href="Index">KOKYOUNGBIN</a></div>
    <div><a href="https://github.com/todtnwater/egocci" class="lang-area" title="github" target="_blank">
    <img src="<%=request.getContextPath()%>/image/song/githubimage.png" alt="Github" onerror="retryImage(this, 'git')" style="width:50px;height:50px;margin-top:2%;">
</a></div>
</header>

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
            <img src="<%=request.getContextPath()%>/image/song/Instagram.png" alt="Instagram" onerror="retryImage(this, '📷')">
        </a>
        <a href="https://www.youtube.com/@egoccl/videos" class="contact-icon" title="YouTube" target="_blank">
            <img src="<%=request.getContextPath()%>/image/song/Youtube.png" alt="YouTube" onerror="retryImage(this, '📺')">
        </a>
        <a href="https://soundcloud.com/egoccl" class="contact-icon" title="SoundCloud" target="_blank">
            <img src="<%=request.getContextPath()%>/image/song/Soundcloud.png" alt="SoundCloud" onerror="retryImage(this, '🎵')">
        </a>
        <a href="https://open.spotify.com/artist/0Tu6vmfKlkQB9kCyYNt79I" class="contact-icon" title="Spotify" target="_blank">
            <img src="<%=request.getContextPath()%>/image/song/Spotify.png" alt="Spotify" onerror="retryImage(this, '🎶')">
        </a>
        <a href="https://music.apple.com/kr/artist/고경빈/1461067097" class="contact-icon" title="Apple Music" target="_blank">
            <img src="<%=request.getContextPath()%>/image/song/Applemusic.png" alt="Apple Music" onerror="retryImage(this, '🍎')">
        </a>
        <a href="https://www.melon.com/artist/timeline.htm?artistId=2857454" class="contact-icon" title="Melon" target="_blank">
            <img src="<%=request.getContextPath()%>/image/song/Melon.png" alt="Melon" onerror="retryImage(this, '🍈')">
        </a>
        <a href="https://github.com/todtnwater/egocci" class="lang-area" title="github" target="_blank">
        <img src="<%=request.getContextPath()%>/image/song/githubimage.png"  class="contact-icon" alt="Github" onerror="retryImage(this, 'git')">
		</a>
        
    </div>
    
    <!-- 이메일 패널 -->
    <div class="email-panel">
        <div class="panel-header">
        	<label for="email-toggle" class="close-btn">×</label>
		    <h3>Send Email</h3>
		    <img src="<%=request.getContextPath()%>/image/song/email_pic.jpg" class="panel-profile-pic" onerror="this.style.display='none'">
		</div>
        <div class="email-address-box">
            <p id="copy-msg">복사완료!</p>
<input type="button" onclick="copyEmail()" value="korangemgmt@gmail.com" id="email-address" readonly>
        </div>
       <% if(sessionId != null && userName != null) { %>
		<form class="email-form" id="emailForm" method="post" action="<%=request.getContextPath()%>/Mail">
		    <input type="hidden" name="t_gubun" value="send">
		
		    <input type="text" name="t_sender_name" value="<%=userName%>" placeholder="Your Name" readonly>
		    <input type="email" name="t_sender_email" value="<%=sessionId%>" placeholder="Your Email" readonly>
		    <input type="text" name="t_subject" placeholder="Subject" required>
		    <textarea name="t_content" placeholder="Your Message" required></textarea>
		    <button type="submit" class="send-btn">Send Message</button>
		</form>
		<% } else { %>
		<div class="email-form login-guide-box">
		    <input type="text" value="로그인 후 메일 전송이 가능합니다." readonly>
		    <button type="button" class="send-btn" onclick="goPage('login')">Login</button>
		</div>
		<% } %>
        <!-- 모바일 -->
        <div class="email-display-mobile">
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
                <li><a href="Gallery">Gallery</a></li>
                <li><a href="Video">Video</a></li>
                <li><a href="Projects">Projects</a></li>
            </ul>
            <ul id="menu-down">
			    <li><a>egocci</a></li>
			    <li><a></a></li>
			   <%
				if(userName != null) {
				%>
				<li><a href="javascript:void(0)">👤 <%=userName%>님</a></li>
				<% if("top".equals(sessionLevel)) { %>
				<li><a href="javascript:goPageManager('list')">회원 관리</a></li>
				<li><a href="<%=request.getContextPath()%>/Mail?t_gubun=adminList">메일 관리</a></li>
				<% } %>
				<li><a href="javascript:goPage('logout')">Logout</a></li>
				<%
				} else {
				%>
				<li><a href="javascript:goPage('login')">Login</a></li>
				<% } %>
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
            <div class="video-container" style="position:relative;">
                <%-- 관리자: 메인 영상 변경 버튼 --%>
                <% if("top".equals(sessionLevel)) { %>
                <div style="position:absolute;top:8px;right:8px;z-index:10;">
                    <a href="Video?t_gubun=list" style="background:rgba(0,0,0,0.65);color:#fff;padding:5px 12px;border-radius:4px;font-size:12px;text-decoration:none;">
                        🎬 메인 영상 변경
                    </a>
                </div>
                <% } %>
                <iframe 
                    src="<%=mainEmbedUrl%>" 
                    frameborder="0" 
                    allow="autoplay; encrypted-media" 
                    allowfullscreen>
                </iframe>
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
                        <img src="<%=request.getContextPath()%>/image/song/Youtube.png" alt="YouTube" onerror="retryImage(this, '📺')">
                    </a>
                    <a href="https://open.spotify.com/artist/0Tu6vmfKlkQB9kCyYNt79I" class="index-icon" target="_blank">
                        <img src="<%=request.getContextPath()%>/image/song/Spotify.png" alt="Spotify" onerror="retryImage(this, '🎵')">
                    </a>
                </div>
            </div>
            <%}%>
        </div>

        <div class="content-row">
            <!-- 콘텐츠 영역 2번(왼쪽) -->
            <div class="content-mid1">
                <% if (latestScheduled != null) { %>
                    <%
                        String scImgPath = request.getContextPath() + "/image/scheduled/p1.png";
                        if (latestScheduled.getPoster_image() != null && !latestScheduled.getPoster_image().isEmpty()) {
                            scImgPath = request.getContextPath() + "/image/scheduled/" + latestScheduled.getPoster_image();
                        }
                    %>
                    <a href="Scheduled">
                        <img class="sc-img" src="<%=scImgPath%>"
                             onerror="this.outerHTML='<div style=\'background:rgba(255,255,255,0.1);height:200px;display:flex;align-items:center;justify-content:center;color:white;font-size:18px;border-radius:8px;\'>Scheduled Performance</div>'">
                    </a>
                <% } else { %>
                    <a href="Scheduled" style="text-decoration:none;">
                        <div style="
                            background: rgba(255,255,255,0.07);
                            border: 1px dashed rgba(255,255,255,0.25);
                            border-radius: 8px;
                            height: 100%;
                            min-height: 200px;
                            display: flex;
                            flex-direction: column;
                            align-items: center;
                            justify-content: center;
                            gap: 12px;
                            color: rgba(255,255,255,0.75);
                            text-align: center;
                            padding: 20px;
                        ">
                            <p style="font-size: 16px; font-weight: bold; margin: 0;">준비중입니다!</p>
                            <p style="font-size: 12px; margin: 0; color: rgba(255,255,255,0.45);">새로운 공연 정보를 준비하고 있어요</p>
                        </div>
                    </a>
                <% } %>
            </div>
            <!-- 콘텐츠 영역 3번(오른쪽) -->
            <div class="content-mid2">
                <%
                if (indexVideoList != null && !indexVideoList.isEmpty()) {
                    for (VideoDto vid : indexVideoList) {
                        String thumb = (vid.getThumbnail_url() != null && !vid.getThumbnail_url().isEmpty())
                                       ? vid.getThumbnail_url() : vid.getAutoThumbnail();
                %>
                <div class="thumbnail-container">
                    <a href="Video?t_gubun=list" title="<%=vid.getTitle()%>">
                        <img src="<%=thumb%>" alt="<%=vid.getTitle()%>"
                             onerror="this.src='<%=request.getContextPath()%>/image/song/default-song.jpg'">
                    </a>
                </div>
                <%
                    }
                } else {
                %>
                <div class="thumbnail-container" style="display:flex;align-items:center;justify-content:center;height:100%;color:rgba(255,255,255,0.5);font-size:13px;">
                    <a href="Video?t_gubun=list">영상 보러가기 ▶</a>
                </div>
                <%}%>
            </div>
        </div>

        <!-- 콘텐츠 영역 4번(아래) 인기 곡 슬라이더 -->
        <div class="content-mid3">
            <div class="music-slider-section">
                <button class="nav-btn prev">
                    <svg width="18" height="18" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m15 18-6-6 6-6"/>
                    </svg>
                </button>
                <div class="slider-container">
                    <div class="cards-wrapper">
                        <div class="cards-track">
                            <%
                            if(popularSongs != null && !popularSongs.isEmpty()) {
                                for(SongDto song : popularSongs) {
                            %>
                           <a href="javascript:goSongView(<%=song.getSong_id()%>)" class="music-card">
                                <div class="card-image">
                                    <%
                                    String songImgPath = "";
                                    if(song.getSong_cover_image() != null && !song.getSong_cover_image().trim().isEmpty()) {
                                        songImgPath = request.getContextPath() + "/image/song/" + song.getSong_cover_image();
                                    } else {
                                        songImgPath = request.getContextPath() + "/image/song/default-song.jpg";
                                    }
                                    %>
                                    <img src="<%=songImgPath%>" alt="<%=song.getSong_title()%>" onerror="this.src='<%=request.getContextPath()%>/image/song/default-song.jpg'">
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
                </div>
                <button class="nav-btn next">
                    <svg width="18" height="18" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m9 18 6-6-6-6"/>
                    </svg>
                </button>
            </div>
            <div class="indicators"></div>
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
