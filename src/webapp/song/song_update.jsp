<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.SongDto" %>
<%
    SongDto dto = (SongDto)request.getAttribute("t_dto");
%>

<%@ include file="../header.jsp" %>

<div class="content-row">
    <main class="main-content">
        <%if(dto != null) {%>
        <div class="content-title">
            <h2>곡 수정</h2>
        </div>

        <div class="content-form">
            <form name="updateForm" action="Song" method="post" onsubmit="return validateUpdateForm();">
                <input type="hidden" name="t_gubun" value="update">
                <input type="hidden" name="t_no" value="<%=dto.getSong_id()%>">
                
                <!-- 현재 앨범 정보 표시 (수정 불가) -->
                <div class="form-section">
                    <h3>현재 앨범 정보</h3>
                    <div class="album-info-display">
                        <p><strong>앨범:</strong> <%=dto.getAlbum_title()%> (<%=dto.getAlbum_type()%>)</p>
                        <p><strong>앨범 번호:</strong> <%=dto.getAlbum_number()%></p>
                        <small>* 앨범 정보는 수정할 수 없습니다. 다른 앨범으로 이동하려면 새로 등록해주세요.</small>
                    </div>
                </div>
                
                <!-- 곡 정보 수정 -->
                <div class="form-section">
                    <h3>곡 정보 수정</h3>
                    
                    <div class="form-group">
                        <label for="song_title">곡 제목</label>
                        <input type="text" name="t_song_title" id="song_title" value="<%=dto.getSong_title()%>" required placeholder="곡 제목을 입력하세요">
                    </div>
                    
                    <div class="form-group">
                        <label for="artist_name">아티스트명</label>
                        <input type="text" name="t_artist_name" id="artist_name" value="<%=dto.getArtist_name()%>" required placeholder="아티스트명을 입력하세요">
                    </div>
                    
                    <%
					    String genre = dto.getGenre() != null ? dto.getGenre() : "";
					%>
					<div class="form-group">
					    <label for="genre">장르</label>
					    <select name="t_genre" id="genre">
					        <option value="">장르를 선택하세요</option>
					        <option value="Ballad" <%= "Ballad".equals(genre) ? "selected" : "" %>>발라드</option>
					        <option value="R&B" <%= "R&B".equals(genre) ? "selected" : "" %>>R&B</option>
					        <option value="Pop" <%= "Pop".equals(genre) ? "selected" : "" %>>팝</option>
					        <option value="Folk" <%= "Folk".equals(genre) ? "selected" : "" %>>포크</option>
					        <option value="Rock" <%= "Rock".equals(genre) ? "selected" : "" %>>록</option>
					        <option value="Hip-Hop" <%= "Hip-Hop".equals(genre) ? "selected" : "" %>>힙합</option>
					        <option value="Indie" <%= "Indie".equals(genre) ? "selected" : "" %>>인디</option>
					        <option value="Electronic" <%= "Electronic".equals(genre) ? "selected" : "" %>>일렉트로닉</option>
					    </select>
					</div>
                    
                    <div class="form-group">
                        <label for="lyrics">가사</label>
                        <textarea name="t_lyrics" id="lyrics" rows="15" placeholder="가사를 입력하세요"><%=dto.getLyrics() != null ? dto.getLyrics() : ""%></textarea>
                    </div>
                </div>

                <div class="form-buttons">
                    <button type="submit" class="btn-submit">수정 완료</button>
                    <!-- 취소 버튼도 POST 방식으로 변경 -->
                    <button type="button" class="btn-cancel" onclick="goToView(<%=dto.getSong_id()%>)">취소</button>
                </div>
            </form>
        </div>
        <%} else {%>
        <div class="content-title">
            <h2>곡 정보를 찾을 수 없습니다.</h2>
        </div>
        <div class="navigation">
            <form method="post" action="Song" style="display:inline;">
                <input type="hidden" name="t_gubun" value="list">
                <button type="submit" class="btn-cancel">목록으로</button>
            </form>
        </div>
        <%}%>
    </main>
</div>

<!-- 취소 버튼용 숨겨진 폼 -->
<form id="cancelForm" method="post" action="Song" style="display:none;">
    <input type="hidden" name="t_gubun" value="view">
    <input type="hidden" name="t_no" id="cancelSongId">
</form>

<script>
function goToView(songId) {
    if (!songId || isNaN(songId)) {
        alert("유효하지 않은 곡 ID입니다.");
        return;
    }
    document.getElementById('cancelSongId').value = songId;
    document.getElementById('cancelForm').submit();
}
function validateUpdateForm() {
    const title = document.getElementById('song_title').value.trim();
    if (title === "") {
        alert("곡 제목을 입력해주세요.");
        return false;
    }
    return true;
}
</script>

<%@ include file="../footer.jsp" %>