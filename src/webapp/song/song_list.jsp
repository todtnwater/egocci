<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dto.SongDto" %>
<%
    ArrayList<SongDto> albumSongs = (ArrayList<SongDto>)request.getAttribute("albumSongs");
    ArrayList<SongDto> epSongs = (ArrayList<SongDto>)request.getAttribute("epSongs");
    ArrayList<SongDto> singleSongs = (ArrayList<SongDto>)request.getAttribute("singleSongs");
%>

<%@ include file="../header.jsp" %>
<script>
function goSongView(songId) {
    const form = document.songForm;
    form.t_gubun.value = "view";
    form.t_no.value = songId;
    form.submit();
}
</script>

<div class="content-row">
    <main class="main-content">
        <!-- 검색 폼 -->
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

        <!-- 관리자 메뉴 -->
        <%
        String level = (String)session.getAttribute("level");
        if("top".equals(level)) {
        %>
        <div class="admin-menu">
            <a href="Song?t_gubun=writeForm" class="btn-submit">곡 등록</a>
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
                        </div></div></div></div> <!-- 이전 앨범 종료 -->
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
                            <a href="javascript:goSongView(<%=dto.getSong_id()%>)" class="music-card">
                                <div class="card-image">
                                    <img src="images/song/<%=dto.getSong_cover_image()%>" alt="<%=dto.getSong_title()%>">
                                </div>
                                <div class="card-content">
                                    <h3 class="song-title"><%=dto.getSong_title()%></h3>
                                </div>
                            </a>
        <%
            }
            if(!currentAlbum.equals("")) { %>
                    </div></div></div></div> <!-- 마지막 앨범 종료 -->
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
                            <a href="Song?t_gubun=view&t_no=<%=dto.getSong_id()%>" class="music-card">
                                <div class="card-image">
                                    <img src="images/song/<%=dto.getSong_cover_image()%>" alt="<%=dto.getSong_title()%>">
                                </div>
                                <div class="card-content">
                                    <h3 class="song-title"><%=dto.getSong_title()%></h3>
                                </div>
                            </a>
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
                            <a href="Song?t_gubun=view&t_no=<%=dto.getSong_id()%>" class="music-card">
                                <div class="card-image">
                                    <img src="images/song/<%=dto.getSong_cover_image()%>" alt="<%=dto.getSong_title()%>">
                                </div>
                                <div class="card-content">
                                    <h3 class="song-title"><%=dto.getSong_title()%></h3>
                                </div>
                            </a>
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

<%@ include file="../footer.jsp" %>