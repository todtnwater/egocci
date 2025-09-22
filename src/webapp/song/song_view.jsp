<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dto.SongDto" %>
<%
	String t_no = request.getParameter("t_no");
	if (t_no == null || !t_no.matches("\\d+")) {
	    response.sendRedirect("Song?t_gubun=list");
	    return;
	}
	
    ArrayList<SongDto> albumSongs = (ArrayList<SongDto>)request.getAttribute("albumSongs");
    ArrayList<SongDto> epSongs = (ArrayList<SongDto>)request.getAttribute("epSongs");
    ArrayList<SongDto> singleSongs = (ArrayList<SongDto>)request.getAttribute("singleSongs");
%>

<%@ include file="../header.jsp" %>
<style>
.music-card-form {
    display: inline-block;
    margin: 0;
    padding: 0;
}

.music-card-button {
    background: none;
    border: none;
    padding: 0;
    cursor: pointer;
    width: 100%;
    text-align: left;
    color: inherit;
    font: inherit;
    text-decoration: none;
    display: block;
}

.music-card-button:hover {
    transform: translateY(-2px);
    transition: transform 0.2s ease;
}
</style>
<div class="content-row">
    <main class="main-content">
        <!-- 검색 폼 - GET 유지 (검색은 북마크/공유 가능해야 함) -->
        <form name="s" action="Song" method="get">
            <input type="hidden" name="t_gubun" value="list">
            <div class="search-box">
                <select name="songtype" class="search-select">
                    <option value="all">전체</option>
                    <option value="album">앨범</option>
                    <option value="ep">EP</option>
                    <option value="single">싱글</option>
                </select>
                <select name="field" class="search-select">
                    <option value="all">전체</option>
                    <option value="title">제목</option>
                    <option value="lyrics">가사</option>
                </select>
                <input type="text" name="keyword" class="search-input"
                       placeholder="검색어 없이 조회하면 전체 곡 리스트가 나옵니다!" value=""/>
                <button type="submit" class="icon-button search">
                    <span class="material-icons">search</span>
                </button>
            </div>
        </form>

        <!-- 관리자 메뉴 - POST 방식으로 변경 -->
        <%
        String level = (String)session.getAttribute("level");
        if("top".equals(level)) {
        %>
        <div class="admin-menu">
            <form method="post" action="Song" style="display:inline;">
                <input type="hidden" name="t_gubun" value="writeForm">
                <button type="submit" class="btn-submit">곡 등록</button>
            </form>
        </div>
        <%}%>

        <!-- 앨범 섹션 -->
        <div class="content-title">
            <h2>앨범</h2>
        </div>
        <%
        if(albumSongs != null && !albumSongs.isEmpty()) {
            String currentAlbum = "";
            for(SongDto dto : albumSongs) {
                if(!currentAlbum.equals(dto.getAlbum_title())) {
                    if(!currentAlbum.equals("")) { %>
                        </div></div></div></div>
                    <%}
                    currentAlbum = dto.getAlbum_title();
        %>
        <div class="content-list">
            <div class="content-album-title">
                <p class="info"><%=dto.getAlbum_title()%> | <%=dto.getAlbum_number()%> | 앨범</p>
            </div>
            <div class="music-slider-section">
                <div class="slider-container">
                    <div class="cards-wrapper">
                        <div class="cards-track">
        <%
                }
        %>
                            <!-- 곡 상세보기는 POST 방식으로 변경 -->
                            <form method="post" action="Song" class="music-card-form">
                                <input type="hidden" name="t_gubun" value="view">
                                <input type="hidden" name="t_no" value="<%=dto.getSong_id()%>">
                                <button type="submit" class="music-card-button" aria-label="곡 상세보기: <%=dto.getSong_title()%>">
                                    <div class="card-image">
                                        <img src="images/song/<%=dto.getSong_cover_image()%>" alt="<%=dto.getSong_title()%>">
                                    </div>
                                    <div class="card-content">
                                        <h3 class="song-title"><%=dto.getSong_title()%></h3>
                                    </div>
                                </button>
                            </form>
        <%
            }
            if(!currentAlbum.equals("")) { %>
                    </div></div></div></div>
            <%}
        } else { %>
        <div class="content-list">
            <p class="no-content">등록된 앨범이 없습니다.</p>
        </div>
        <%}%>

        <!-- EP 섹션 -->
        <div class="content-title">
            <h2>EP</h2>
        </div>
        <%
        if(epSongs != null && !epSongs.isEmpty()) {
            String currentEP = "";
            for(SongDto dto : epSongs) {
                if(!currentEP.equals(dto.getAlbum_title())) {
                    if(!currentEP.equals("")) { %>
                        </div></div></div></div>
                    <%}
                    currentEP = dto.getAlbum_title();
        %>
        <div class="content-list">
            <div class="content-album-title">
                <p class="info"><%=dto.getAlbum_title()%> | <%=dto.getAlbum_number()%> | EP</p>
            </div>
            <div class="music-slider-section">
                <div class="slider-container">
                    <div class="cards-wrapper">
                        <div class="cards-track">
        <%
                }
        %>
                           <form method="post" action="Song" class="music-card-form" data-song-id="<%=dto.getSong_id()%>">
                                <input type="hidden" name="t_gubun" value="view">
                                <input type="hidden" name="t_no" value="<%=dto.getSong_id()%>">
                                <button type="submit" class="music-card-button">
                                    <div class="card-image">
                                        <img src="images/song/<%=dto.getSong_cover_image()%>" alt="<%=dto.getSong_title()%>">
                                    </div>
                                    <div class="card-content">
                                        <h3 class="song-title"><%=dto.getSong_title()%></h3>
                                    </div>
                                </button>
                            </form>
        <%
            }
            if(!currentEP.equals("")) { %>
                    </div></div></div></div>
            <%}
        } else { %>
        <div class="content-list">
            <p class="no-content">등록된 EP가 없습니다.</p>
        </div>
        <%}%>

        <!-- 싱글 섹션 -->
        <div class="content-title">
            <h2>싱글</h2>
        </div>
        <div class="content-list">
            <div class="content-album-title">
                <p class="info">싱글 | <%=singleSongs != null ? singleSongs.size() : 0%>곡</p>
            </div>
            <div class="music-slider-section">
                <div class="slider-container">
                    <div class="cards-wrapper">
                        <div class="cards-track">
                            <%
                            if(singleSongs != null && !singleSongs.isEmpty()) {
                                for(SongDto dto : singleSongs) {
                            %>
                            <form method="post" action="Song" class="music-card-form">
                                <input type="hidden" name="t_gubun" value="view">
                                <input type="hidden" name="t_no" value="<%=dto.getSong_id()%>">
                                <button type="submit" class="music-card-button">
                                    <div class="card-image">
                                        <img src="images/song/<%=dto.getSong_cover_image()%>" alt="<%=dto.getSong_title()%>">
                                    </div>
                                    <div class="card-content">
                                        <h3 class="song-title"><%=dto.getSong_title()%></h3>
                                    </div>
                                </button>
                            </form>
                            <%
                                }
                            } else {
                            %>
                            <p class="no-content">등록된 싱글이 없습니다.</p>
                            <%}%>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</div>