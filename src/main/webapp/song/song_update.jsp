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

function goToView(songId) {
    if (!songId || isNaN(songId)) { alert("유효하지 않은 곡 ID입니다."); return; }
    var form = document.createElement('form');
    form.method = 'POST'; form.action = 'Song';
    var g = document.createElement('input'); g.type='hidden'; g.name='t_gubun'; g.value='view'; form.appendChild(g);
    var n = document.createElement('input'); n.type='hidden'; n.name='t_no'; n.value=songId; form.appendChild(n);
    document.body.appendChild(form); form.submit();
}

function toggleAlbumChange() {
    const changeChecked = document.getElementById('change_album').checked;
    document.getElementById('album_change_area').style.display = changeChecked ? 'block' : 'none';
    document.getElementById('current_album_id').disabled = changeChecked;
    if (!changeChecked) document.getElementById('new_album_id').value = '';
}

function loadAlbumsForUpdate() {
    const albumType = document.getElementById('new_album_type').value;
    const albumSelect = document.getElementById('new_album_id');
    albumSelect.innerHTML = '<option value="">앨범을 선택하세요</option>';
    if (!albumType) return;
    fetch('Song?t_gubun=getAlbumList&type=' + albumType)
        .then(r => r.json())
        .then(data => {
            data.forEach(album => {
                const opt = document.createElement('option');
                opt.value = album.album_id;
                opt.textContent = album.album_title + ' (' + album.album_number + ')';
                albumSelect.appendChild(opt);
            });
        })
        .catch(() => alert('앨범 목록을 불러오는데 실패했습니다.'));
}

function validateUpdateForm() {
    if (!document.getElementById('song_title').value.trim()) { alert("곡 제목을 입력해주세요."); return false; }
    if (!document.getElementById('artist_name').value.trim()) { alert("아티스트명을 입력해주세요."); return false; }
    if (document.getElementById('change_album').checked && !document.getElementById('new_album_id').value) {
        alert("변경할 앨범을 선택해주세요."); return false;
    }
    return true;
}

function submitUpdateForm() {
    if (!validateUpdateForm()) return;
    document.updateForm.submit();
}
</script>

<form name="updateForm" method="post" action="Song" enctype="multipart/form-data">
    <input type="hidden" name="t_gubun" value="update">
    <input type="hidden" name="t_no" value="<%=dto.getSong_id()%>">
    <input type="hidden" id="current_album_id" name="t_album_id" value="<%=dto.getAlbum_id()%>">
    
    <div class="content-row">
        <main class="main-content song-form-container">
            <%if(dto != null) {%>
            <div class="content-title">
                <h2>곡 수정</h2>
            </div>

            <div class="content-form">
                <!-- 앨범 정보 섹션 -->
                <div class="form-section">
                    <h3>앨범 정보</h3>
                    <div class="album-info-display">
                        <p><strong>현재 앨범:</strong> <%=dto.getAlbum_title()%> (<%=dto.getAlbum_type()%>)</p>
                        <p><strong>앨범 번호:</strong> <%=dto.getAlbum_number()%></p>
                    </div>

                    <%-- 현재 앨범 정보 직접 편집 --%>
                    <div style="margin-top:14px;padding:15px;background:rgba(255,255,255,0.05);border-radius:8px;">
                        <p style="color:#aaa;font-size:13px;margin-bottom:12px;">※ 아래에서 현재 앨범 정보를 직접 수정할 수 있습니다.</p>
                        <div class="form-group">
                            <label>앨범명</label>
                            <input type="text" name="t_edit_album_title" value="<%=dto.getAlbum_title() != null ? dto.getAlbum_title() : ""%>">
                        </div>
                        <div class="form-group">
                            <label>앨범 번호</label>
                            <input type="text" name="t_edit_album_number" value="<%=dto.getAlbum_number() != null ? dto.getAlbum_number() : ""%>" placeholder="예: 1집, 2집, Mini">
                        </div>
                        <div class="form-group">
                            <label>발매일</label>
                            <input type="date" name="t_edit_release_date" value="<%=dto.getRelease_date() != null ? dto.getRelease_date() : ""%>">
                        </div>
                        <div class="form-group">
                            <label>앨범 설명</label>
                            <textarea name="t_edit_album_description" rows="3" placeholder="앨범 설명"><%=dto.getAlbum_description() != null ? dto.getAlbum_description() : ""%></textarea>
                        </div>
                    </div>

                    <div class="form-group" style="margin-top:10px;">
                        <label>
                            <input type="checkbox" id="change_album" onchange="toggleAlbumChange()">
                            다른 앨범으로 변경하기
                        </label>
                    </div>
                    <div id="album_change_area" style="display:none;margin-top:10px;padding:15px;background:rgba(255,255,255,0.05);border-radius:8px;">
                        <div class="form-group">
                            <label for="new_album_type">앨범 타입 <span style="color:red;">*</span></label>
                            <select id="new_album_type" onchange="loadAlbumsForUpdate()">
                                <option value="">앨범 타입을 선택하세요</option>
                                <option value="album">앨범</option>
                                <option value="ep">EP</option>
                                <option value="single">싱글</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="new_album_id">앨범 선택 <span style="color:red;">*</span></label>
                            <select id="new_album_id" name="t_new_album_id">
                                <option value="">앨범 타입을 먼저 선택하세요</option>
                            </select>
                        </div>
                        <small style="color:#aaa;">※ 선택한 앨범으로 변경됩니다.</small>
                    </div>
                </div>
                
                <!-- 곡 정보 수정 -->
                <div class="form-section">
                    <h3>곡 정보 수정</h3>
                    <div class="form-group">
                        <label for="cover_image">커버 이미지</label>
                        <%if(dto.getSong_cover_image() != null && !dto.getSong_cover_image().isEmpty()) {%>
                        <div style="margin-bottom:10px;">
                            <img src="<%=request.getContextPath()%>/image/song/<%=dto.getSong_cover_image()%>" alt="현재 커버" style="max-width:200px;border-radius:8px;">
                            <p style="color:#666;font-size:12px;">현재 이미지: <%=dto.getSong_cover_image()%></p>
                        </div>
                        <%}%>
                        <input type="file" name="t_cover_image" id="cover_image" accept="image/*" onchange="previewImageUpdate(event)">
                        <small style="color:#666;">새 이미지를 선택하지 않으면 기존 이미지 유지</small>
                        <div id="new_preview_container" style="margin-top:15px;display:none;">
                            <p style="color:#666;font-size:12px;">새 이미지 미리보기:</p>
                            <img id="new_preview" src="" alt="새 이미지 미리보기" style="max-width:30%;border-radius:8px;box-shadow:0 4px 8px rgba(0,0,0,0.2);">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="song_title">곡 제목 <span style="color:red;">*</span></label>
                        <input type="text" name="t_song_title" id="song_title" value="<%=dto.getSong_title()%>" required>
                    </div>
                    <div class="form-group">
                        <label for="artist_name">아티스트명 <span style="color:red;">*</span></label>
                        <input type="text" name="t_artist_name" id="artist_name" value="<%=dto.getArtist_name()%>" required>
                    </div>
                    <%String genre = dto.getGenre() != null ? dto.getGenre() : "";%>
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
				    <label for="behind_note">비하인드 (선택)</label>
				    <textarea name="t_behind_note" id="behind_note" rows="10"
				        placeholder="이 곡에 대한 이야기, 제작 비하인드를 자유롭게 적어주세요"><%= dto != null && dto.getBehind_note() != null ? dto.getBehind_note().replaceAll("(?i)<br\\s*/?>", "\n") : "" %></textarea>
				</div>
				
				<div class="form-group">
				    <label for="lyrics">가사</label>
				    <textarea name="t_lyrics" id="lyrics" rows="15"
				        placeholder="가사를 입력하세요"><%= dto != null && dto.getLyrics() != null ? dto.getLyrics().replaceAll("(?i)<br\\s*/?>", "\n") : "" %></textarea>
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
                        <label>Spotify URL</label>
                        <input type="url" name="t_spotify_url" value="<%=spotifyUrl%>" placeholder="https://open.spotify.com/track/...">
                    </div>
                    <div class="form-group">
                        <label>멜론 URL</label>
                        <input type="url" name="t_melon_url" value="<%=melonUrl%>" placeholder="https://www.melon.com/song/detail.htm?songId=...">
                    </div>
                    <div class="form-group">
                        <label>Apple Music URL</label>
                        <input type="url" name="t_apple_music_url" value="<%=appleMusicUrl%>" placeholder="https://music.apple.com/...">
                    </div>
                    <div class="form-group">
                        <label>YouTube Music URL</label>
                        <input type="url" name="t_youtube_url" value="<%=youtubeUrl%>" placeholder="https://music.youtube.com/watch?v=...">
                    </div>
                    <div class="form-group">
                        <label>SoundCloud URL</label>
                        <input type="url" name="t_soundcloud_url" value="<%=soundcloudUrl%>" placeholder="https://soundcloud.com/...">
                    </div>
                </div>
                
                <div class="form-buttons">
                    <button type="button" class="btn-submit" onclick="submitUpdateForm()">수정 완료</button>
                    <button type="button" class="btn-cancel" onclick="goToView(<%=dto.getSong_id()%>)">취소</button>
                </div>
            </div>
            <%} else {%>
            <div class="content-title"><h2>곡 정보를 찾을 수 없습니다.</h2></div>
            <div class="navigation">
                <button type="button" class="btn-cancel" onclick="location.href='Song?t_gubun=list'">목록으로</button>
            </div>
            <%}%>
        </main>
    </div>
</form>

<%@ include file="../footer.jsp" %>
