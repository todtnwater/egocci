<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.SongDto" %>
<%@ page import="java.util.ArrayList" %>
<%
    String level = (String)session.getAttribute("sessionLevel");
    if(!"top".equals(level)) {
        response.sendRedirect("index.jsp");
        return;
    }
    
    SongDto dto = (SongDto)request.getAttribute("t_dto");
%>

<%@ include file="../header.jsp" %>

<script>
// 이미지 미리보기
function previewImageUpdate(event) {
    const file = event.target.files[0];
    const preview = document.getElementById('new_preview');
    const container = document.getElementById('new_preview_container');
    
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            preview.src = e.target.result;
            container.style.display = 'block';
        }
        reader.readAsDataURL(file);
    } else {
        container.style.display = 'none';
    }
}

// 취소 버튼
function goToView(songId) {
    if (!songId || isNaN(songId)) {
        alert("유효하지 않은 곡 ID입니다.");
        return;
    }
    
    var form = document.createElement('form');
    form.method = 'POST';
    form.action = 'Song';
    
    var gubunInput = document.createElement('input');
    gubunInput.type = 'hidden';
    gubunInput.name = 't_gubun';
    gubunInput.value = 'view';
    form.appendChild(gubunInput);
    
    var noInput = document.createElement('input');
    noInput.type = 'hidden';
    noInput.name = 't_no';
    noInput.value = songId;
    form.appendChild(noInput);
    
    document.body.appendChild(form);
    form.submit();
}

// 유효성 검사
function validateUpdateForm() {
    const title = document.getElementById('song_title').value.trim();
    const artist = document.getElementById('artist_name').value.trim();
    
    if (title === "") {
        alert("곡 제목을 입력해주세요.");
        return false;
    }
    
    if (artist === "") {
        alert("아티스트명을 입력해주세요.");
        return false;
    }
    
    return true;
}

// 폼 제출
function submitUpdateForm() {
    if (!validateUpdateForm()) return;
    
    document.updateForm.method = "post";
    document.updateForm.action = "Song";
    document.updateForm.enctype = "multipart/form-data";
    document.updateForm.submit();
}
</script>

<form name="updateForm" method="post" action="Song">
    <input type="hidden" name="t_gubun" value="update">
    <input type="hidden" name="t_no" value="<%=dto.getSong_id()%>">
    
    <div class="content-row">
        <main class="main-content song-form-container">
            <%if(dto != null) {%>
            <div class="content-title">
                <h2>곡 수정</h2>
            </div>

            <div class="content-form">
                <!-- 현재 앨범 정보 표시 -->
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
                        <label for="cover_image">커버 이미지</label>
                        <%if(dto.getSong_cover_image() != null && !dto.getSong_cover_image().isEmpty()) {%>
                        <div style="margin-bottom: 10px;">
                            <img src="images/song/<%=dto.getSong_cover_image()%>" alt="현재 커버" style="max-width: 200px; border-radius: 8px;">
                            <p style="color: #666; font-size: 12px;">현재 이미지: <%=dto.getSong_cover_image()%></p>
                        </div>
                        <%}%>
                        <input type="file" name="t_cover_image" id="cover_image" accept="image/*" onchange="previewImageUpdate(event)">
                        <small style="color: #666;">새 이미지를 선택하지 않으면 기존 이미지 유지</small>
                        <div id="new_preview_container" style="margin-top: 15px; display: none;">
                            <p style="color: #666; font-size: 12px;">새 이미지 미리보기:</p>
                            <img id="new_preview" src="" alt="새 이미지 미리보기" style="max-width: 30%; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.2);">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="song_title">곡 제목 <span style="color:red;">*</span></label>
                        <input type="text" name="t_song_title" id="song_title" value="<%=dto.getSong_title()%>" required placeholder="곡 제목을 입력하세요">
                    </div>
                    <div class="form-group">
                        <label for="artist_name">아티스트명 <span style="color:red;">*</span></label>
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
                
                <!-- 스트리밍 링크 -->
                <div class="form-section">
                    <h3>스트리밍 링크 (선택사항)</h3>
                    <%
                    ArrayList<String[]> streamingLinks = (ArrayList<String[]>)request.getAttribute("streamingLinks");
                    String spotifyUrl = "", melonUrl = "", appleMusicUrl = "", youtubeUrl = "", soundcloudUrl = "";
                    
                    if(streamingLinks != null) {
                        for(String[] link : streamingLinks) {
                            if("Spotify".equals(link[0])) spotifyUrl = link[1];
                            else if("Melon".equals(link[0])) melonUrl = link[1];
                            else if("Apple Music".equals(link[0])) appleMusicUrl = link[1];
                            else if("YouTube Music".equals(link[0])) youtubeUrl = link[1];
                            else if("SoundCloud".equals(link[0])) soundcloudUrl = link[1];
                        }
                    }
                    %>
                    <div class="form-group">
                        <label for="spotify_url">Spotify URL</label>
                        <input type="url" name="t_spotify_url" id="spotify_url" value="<%=spotifyUrl%>" placeholder="https://open.spotify.com/track/...">
                    </div>
                    <div class="form-group">
                        <label for="melon_url">멜론 URL</label>
                        <input type="url" name="t_melon_url" id="melon_url" value="<%=melonUrl%>" placeholder="https://www.melon.com/song/detail.htm?songId=...">
                    </div>
                    <div class="form-group">
                        <label for="apple_music_url">Apple Music URL</label>
                        <input type="url" name="t_apple_music_url" id="apple_music_url" value="<%=appleMusicUrl%>" placeholder="https://music.apple.com/...">
                    </div>
                    <div class="form-group">
                        <label for="youtube_url">YouTube Music URL</label>
                        <input type="url" name="t_youtube_url" id="youtube_url" value="<%=youtubeUrl%>" placeholder="https://music.youtube.com/watch?v=...">
                    </div>
                    <div class="form-group">
                        <label for="soundcloud_url">SoundCloud URL</label>
                        <input type="url" name="t_soundcloud_url" id="soundcloud_url" value="<%=soundcloudUrl%>" placeholder="https://soundcloud.com/...">
                    </div>
                </div>
                
                <div class="form-buttons">
                    <button type="button" class="btn-submit" onclick="submitUpdateForm()">수정 완료</button>
                    <button type="button" class="btn-cancel" onclick="goToView(<%=dto.getSong_id()%>)">취소</button>
                </div>
            </div>
            <%} else {%>
            <div class="content-title">
                <h2>곡 정보를 찾을 수 없습니다.</h2>
            </div>
            <div class="navigation">
                <button type="button" class="btn-cancel" onclick="location.href='Song?t_gubun=list'">목록으로</button>
            </div>
            <%}%>
        </main>
    </div>
</form>

<%@ include file="../footer.jsp" %>