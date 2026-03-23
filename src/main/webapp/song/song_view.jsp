<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dto.SongDto" %>
<%
    SongDto dto = (SongDto)request.getAttribute("t_dto");
    ArrayList<String[]> streamingLinks = (ArrayList<String[]>)request.getAttribute("streamingLinks");
    
    if(dto == null) {
        response.sendRedirect("Song?t_gubun=list");
        return;
    }
%>

<%@ include file="../header.jsp" %>
<script type="text/javascript">
	var songId = <%=dto.getSong_id()%>;

	function goback(){
		song.t_gubun.value = "list";
        song.method = "post";
        song.action = "Song";
        song.submit();
	}
	function goUpdateForm(){
		song.t_gubun.value = "updateForm";
        song.t_no.value = songId;
        song.method = "post";
        song.action = "Song";
        song.submit();
	}
	function goDelete(){
		if(!confirm('정말로 이 곡을 삭제하시겠습니까?')) return;
        
        song.t_gubun.value = "delete";
        song.t_no.value = songId;
        song.method = "post";
        song.action = "Song";
        song.submit();
	}
</script>
<div class="content-row">
    <main class="main-content song-view-container">
        <!-- 상단 네비게이션 -->
        <div class="navigation-buttons">
           	<form name="song">
                <input type="hidden" name="t_gubun">
                <input type="hidden" name="t_no" value="<%=dto.getSong_id()%>">
                <a href="javascript:goback()"  class="btn-cancel">← 목록으로</a>
            <%
            String level = (String)session.getAttribute("sessionLevel");
            if("top".equals(level)) {
            %>
            <div style="display:inline;">

                <a href="javascript:goUpdateForm()" class="btn-warning">수정</a>
                <a href="javascript:goDelete()" class="btn-danger">삭제</a>
            </div>
            <%}%>
            </form>
        </div>

        <!-- 곡 상세 정보 -->
        <div class="song-detail-container">
            <div class="song-detail-header">
                <h1><%=dto.getSong_title()%></h1>
                <%if(dto.getIs_title_track()) {%>
                <span class="title-track-badge">타이틀곡</span>
                <%}%>
            </div>

            <div class="song-detail-content">
                <!-- 왼쪽: 이미지 -->
                <div class="song-detail-image">
                    <%
                    String imagePath = "";
                    if(dto.getSong_cover_image() != null && !dto.getSong_cover_image().trim().isEmpty()) {
                        imagePath = request.getContextPath() + "/image/song/" + dto.getSong_cover_image();
                    } else {
                        imagePath = request.getContextPath() + "/image/song/default-song.jpg";
                    }
                    %>
                    <img src="<%=imagePath%>" alt="<%=dto.getSong_title()%>" 
                         onerror="this.src='<%=request.getContextPath()%>/image/song/default-song.jpg'">
                </div>

                <!-- 오른쪽: 정보 -->
                <div class="song-detail-info">
                    <div class="info-section">
                        <h3>곡 정보</h3>
                        <table>
                            <tr>
                                <th>아티스트</th>
                                <td><%=dto.getArtist_name()%></td>
                            </tr>
                            <tr>
                                <th>앨범</th>
                                <td><%=dto.getAlbum_title()%> (<%=dto.getAlbum_type().toUpperCase()%>)</td>
                            </tr>
                            <tr>
                                <th>앨범 번호</th>
                                <td><%=dto.getAlbum_number()%></td>
                            </tr>
                            <tr>
                                <th>트랙 번호</th>
                                <td><%=dto.getTrack_number()%>번</td>
                            </tr>
                            <tr>
                                <th>장르</th>
                                <td><%=dto.getGenre() != null ? dto.getGenre() : "미분류"%></td>
                            </tr>
                            <tr>
                                <th>재생시간</th>
                                <td><%=dto.getDuration() != null ? dto.getDuration() : "정보없음"%></td>
                            </tr>
                            <tr>
                                <th>발매일</th>
                                <td><%=dto.getRelease_date()%></td>
                            </tr>
                        </table>
                    </div>

                    <!-- 스트리밍 링크 -->
					<%if(streamingLinks != null && !streamingLinks.isEmpty()) {%>
					<div class="info-section">
					    <h3>스트리밍 플랫폼</h3>
					    <div class="streaming-links">
					        <%for(String[] link : streamingLinks) {
					            String iconPath = "";
					            if(link[0].contains("Spotify")) iconPath = "Spotify.png";
					            else if(link[0].contains("Melon")) iconPath = "Melon.png";
					            else if(link[0].contains("Apple")) iconPath = "Applemusic.png";
					            else if(link[0].contains("YouTube")) iconPath = "Youtube.png";
					            else if(link[0].contains("SoundCloud")) iconPath = "Soundcloud.png";
					            else if(link[0].contains("Bugs")) iconPath = "Bugs.png";
					            else if(link[0].contains("Genie")) iconPath = "Ginie.png";
					        %>
					        <a href="<%=link[1]%>" target="_blank" class="streaming-link" title="<%=link[0]%>">
					            <%if(!iconPath.equals("")) {%>
					            <img src="<%=request.getContextPath()%>/image/song/<%=iconPath%>" alt="<%=link[0]%>">
					            <%}%>
					        </a>
					        <%}%>
					    </div>
					</div>
					<%}%>
                </div>
            </div>

            <!-- 가사 -->
            <div class="lyrics-section">
                <h3>가사</h3>
                <div>
                    <%if(dto.getLyrics() != null && !dto.getLyrics().trim().isEmpty()) {%>
                        <pre><%=dto.getLyrics()%></pre>
                    <%} else {%>
                        <p>가사 정보가 없습니다.</p>
                    <%}%>
                </div>
            </div>
        </div>
    </main>
</div>

<%@ include file="../footer.jsp" %>