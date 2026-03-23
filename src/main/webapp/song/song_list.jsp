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
	song.t_gubun.value="view";
	song.t_no.value = songId;
	song.method="post";
	song.action="Song";
	song.submit();
}
function goWrite(){
	song.t_gubun.value="writeForm";
	song.method="post";
	song.action="Song";
	song.submit();
}
</script>

<div class="content-row">
    <main class="main-content">
        <!-- 검색 폼 -->
        <form name="s" action="Song" method="post">
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
                <input type="text" name="keyword" class="search-input" placeholder="검색어 없이 조회하면 전체 곡 리스트가 나옵니다!" value=""/>
                <button type="submit" class="icon-button search">
                    <span class="material-icons">search</span>
                </button>
            </div>
        </form>

        <!-- 항상 존재하는 네비게이션 폼 (뷰 이동용) -->
        <form name="song">
            <input type="hidden" name="t_gubun">
            <input type="hidden" name="t_no">
        </form>

        <!-- 관리자 메뉴 -->
        <div class="content-title2">
		<%
		String level = (String)session.getAttribute("sessionLevel");
		if("top".equals(level)) {
		%>
		        <div class="admin-menu">
		            <a href="javascript:goWrite()" class="btn-submit">곡 등록</a>
		        </div>
		<%}%>
		</div>

        <!-- 앨범 섹션: 데이터 있을 때만 표시 -->
        <%
        if(albumSongs != null && !albumSongs.isEmpty()) {
        %>
        <div class="content-title">
            <h2>앨범</h2>
        </div>
        <%
            String currentAlbum = "";
            for(SongDto dto : albumSongs) {
                if(!currentAlbum.equals(dto.getAlbum_title())) {
                    if(!currentAlbum.equals("")) { %>
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
            </div>
                    <%}
                    currentAlbum = dto.getAlbum_title();
        %>
        <div class="content-list">
            <div class="content-album-title">
                <p class="info"><%=dto.getAlbum_title()%> | <%=dto.getAlbum_number()%> | 앨범</p>
            </div>
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
                }
                String imagePath = "";
                if(dto.getSong_cover_image() != null && !dto.getSong_cover_image().trim().isEmpty()) {
                    imagePath = request.getContextPath() + "/image/song/" + dto.getSong_cover_image();
                } else {
                    imagePath = request.getContextPath() + "/image/song/default-song.jpg";
                }
        %>
                            <a href="javascript:goSongView(<%=dto.getSong_id()%>)" class="music-card">
                                <div class="card-image">
                                    <img src="<%=imagePath%>" alt="<%=dto.getSong_title()%>" onerror="this.src='<%=request.getContextPath()%>/image/song/default-song.jpg'">
                                </div>
                                <div class="card-content">
                                    <h3 class="song-title"><%=dto.getSong_title()%></h3>
                                </div>
                            </a>
        <%
            }
            if(!currentAlbum.equals("")) { %>
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
        </div>
            <%}
        }%>

        <!-- EP 섹션: 데이터 있을 때만 표시 -->
        <%
        if(epSongs != null && !epSongs.isEmpty()) {
        %>
        <div class="content-title">
            <h2>EP</h2>
        </div>
        <%
            String currentEP = "";
            for(SongDto dto : epSongs) {
                if(!currentEP.equals(dto.getAlbum_title())) {
                    if(!currentEP.equals("")) { %>
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
            </div>
                    <%}
                    currentEP = dto.getAlbum_title();
        %>
        <div class="content-list">
            <div class="content-album-title">
                <p class="info"><%=dto.getAlbum_title()%> | <%=dto.getAlbum_number()%> | EP</p>
            </div>
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
                }
                String imagePath = "";
                if(dto.getSong_cover_image() != null && !dto.getSong_cover_image().trim().isEmpty()) {
                    imagePath = request.getContextPath() + "/image/song/" + dto.getSong_cover_image();
                } else {
                    imagePath = request.getContextPath() + "/image/song/default-song.jpg";
                }
        %>
                            <a href="javascript:goSongView(<%=dto.getSong_id()%>)" class="music-card">
                                <div class="card-image">
                                    <img src="<%=imagePath%>" alt="<%=dto.getSong_title()%>" onerror="this.src='<%=request.getContextPath()%>/image/song/default-song.jpg'">
                                </div>
                                <div class="card-content">
                                    <h3 class="song-title"><%=dto.getSong_title()%></h3>
                                </div>
                            </a>
        <%
            }
            if(!currentEP.equals("")) { %>
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
        </div>
            <%}
        }%>

        <!-- 싱글 섹션 -->
        <div class="content-title">
            <h2>싱글</h2>
        </div>
        <div class="content-list">
            <div class="content-album-title">
                <p class="info">싱글 | <%=singleSongs != null ? singleSongs.size() : 0%>곡</p>
            </div>
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
                            if(singleSongs != null && !singleSongs.isEmpty()) {
                                for(SongDto dto : singleSongs) {
                                    String imagePath = "";
                                    if(dto.getSong_cover_image() != null && !dto.getSong_cover_image().trim().isEmpty()) {
                                        imagePath = request.getContextPath() + "/image/song/" + dto.getSong_cover_image();
                                    } else {
                                        imagePath = request.getContextPath() + "/image/song/default-song.jpg";
                                    }
                            %>
                            <a href="javascript:goSongView(<%=dto.getSong_id()%>)" class="music-card">
                                <div class="card-image">
                                    <img src="<%=imagePath%>" alt="<%=dto.getSong_title()%>" onerror="this.src='<%=request.getContextPath()%>/image/song/default-song.jpg'">
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

<%@ include file="../footer.jsp" %>