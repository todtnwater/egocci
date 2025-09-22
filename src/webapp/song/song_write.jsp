<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../header.jsp" %>

<div class="content-row">
    <main class="main-content">
        <div class="content-title">
            <h2>곡 등록</h2>
        </div>

        <div class="content-form">
            <form name="writeForm" action="Song" method="post" enctype="multipart/form-data">
                <input type="hidden" name="t_gubun" value="save">
                
                <!-- 앨범 타입 선택 -->
                <div class="form-group">
                    <label for="album_type">앨범 타입</label>
                    <select name="t_album_type" id="album_type" onchange="loadAlbums()" required>
                        <option value="">앨범 타입을 선택하세요</option>
                        <option value="album">앨범</option>
                        <option value="ep">EP</option>
                        <option value="single">싱글</option>
                    </select>
                </div>

                <!-- 기존 앨범 or 신규 앨범 선택 -->
                <div class="form-group" id="album_choice_group" style="display:none;">
                    <label>앨범 선택</label>
                    <div class="radio-group">
                        <input type="radio" name="album_choice" id="existing_album" value="existing" onchange="toggleAlbumForm()">
                        <label for="existing_album">기존 앨범에 추가</label>
                        
                        <input type="radio" name="album_choice" id="new_album" value="new" onchange="toggleAlbumForm()">
                        <label for="new_album">신규 앨범 생성</label>
                    </div>
                </div>

                <!-- 기존 앨범 선택 -->
                <div class="form-group" id="existing_album_group" style="display:none;">
                    <label for="album_id">기존 앨범 선택</label>
                    <select name="t_album_id" id="album_id">
                        <option value="">앨범을 선택하세요</option>
                    </select>
                </div>

                <!-- 신규 앨범 정보 입력 -->
                <div id="new_album_form" style="display:none;">
                    <div class="form-group">
                        <label for="new_album_title">신규 앨범명</label>
                        <input type="text" name="t_new_album_title" id="new_album_title" placeholder="앨범명을 입력하세요">
                    </div>
                    
                    <div class="form-group">
                        <label for="album_number">앨범 번호</label>
                        <input type="text" name="t_album_number" id="album_number" placeholder="예: 1집, 2집, Mini">
                    </div>
                    
                    <div class="form-group">
                        <label for="release_date">발매일</label>
                        <input type="date" name="t_release_date" id="release_date">
                    </div>
                    
                    <div class="form-group">
                        <label for="album_description">앨범 설명</label>
                        <textarea name="t_album_description" id="album_description" rows="3" placeholder="앨범에 대한 설명을 입력하세요"></textarea>
                    </div>
                </div>

                <!-- 곡 정보 입력 -->
                <div class="form-section">
                    <h3>곡 정보</h3>
                    
                    <div class="form-group">
                        <label for="song_title">곡 제목</label>
                        <input type="text" name="t_song_title" id="song_title" required placeholder="곡 제목을 입력하세요">
                    </div>
                    
                    <div class="form-group">
                        <label for="artist_name">아티스트명</label>
                        <input type="text" name="t_artist_name" id="artist_name" value="고경빈 (KOKYOUNGBIN)" placeholder="아티스트명을 입력하세요">
                    </div>
                    
                    <div class="form-group">
                        <label for="genre">장르</label>
                        <select name="t_genre" id="genre">
                            <option value="">장르를 선택하세요</option>
                            <option value="Ballad">발라드</option>
                            <option value="R&B">R&B</option>
                            <option value="Pop">팝</option>
                            <option value="Folk">포크</option>
                            <option value="Rock">록</option>
                            <option value="Hip-Hop">힙합</option>
                            <option value="Indie">인디</option>
                            <option value="Electronic">일렉트로닉</option>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label for="track_number">트랙 번호</label>
                        <input type="number" name="t_track_number" id="track_number" min="1" placeholder="트랙 번호">
                    </div>
                    
                    <div class="form-group">
                        <label for="duration">곡 길이 (분:초)</label>
                        <input type="text" name="t_duration" id="duration" step="1">
                    </div>
                    
                    <div class="form-group">
                        <label for="is_title_track">타이틀곡 여부</label>
                        <input type="checkbox" name="t_is_title_track" id="is_title_track" value="true">
                        <label for="is_title_track" class="checkbox-label">타이틀곡</label>
                    </div>
                    
                    <div class="form-group">
                        <label for="lyrics">가사</label>
                        <textarea name="t_lyrics" id="lyrics" rows="15" placeholder="가사를 입력하세요"></textarea>
                    </div>
                </div>

                <!-- 스트리밍 링크 -->
                <div class="form-section">
                    <h3>스트리밍 링크 (선택사항)</h3>
                    
                    <div class="form-group">
                        <label for="spotify_url">Spotify URL</label>
                        <input type="url" name="t_spotify_url" id="spotify_url" placeholder="https://open.spotify.com/track/...">
                    </div>
                    
                    <div class="form-group">
                        <label for="apple_music_url">Apple Music URL</label>
                        <input type="url" name="t_apple_music_url" id="apple_music_url" placeholder="https://music.apple.com/...">
                    </div>
                    
                    <div class="form-group">
                        <label for="melon_url">멜론 URL</label>
                        <input type="url" name="t_melon_url" id="melon_url" placeholder="https://www.melon.com/song/detail.htm?songId=...">
                    </div>
                    
                    <div class="form-group">
                        <label for="youtube_url">YouTube Music URL</label>
                        <input type="url" name="t_youtube_url" id="youtube_url" placeholder="https://music.youtube.com/watch?v=...">
                    </div>
                    
                    <div class="form-group">
                        <label for="soundcloud_url">SoundCloud URL</label>
                        <input type="url" name="t_soundcloud_url" id="soundcloud_url" placeholder="https://soundcloud.com/...">
                    </div>
                </div>
            </form>
                <div class="form-buttons">
				    <button type="submit" class="btn-submit">곡 등록</button>
				
				    <form method="post" action="Song" style="display:inline;">
				        <input type="hidden" name="t_gubun" value="list">
				        <button type="submit" class="btn-cancel">취소</button>
				    </form>
				</div>
        </div>
    </main>
</div>

<script>
window.addEventListener("DOMContentLoaded", function() {
    const albumType = document.getElementById('album_type').value;
    if (albumType) {
        loadAlbums();
    }
});

// AJAX 앨범 목록 로드
function loadAlbums() {
    const albumType = document.getElementById('album_type').value;
    const choiceGroup = document.getElementById('album_choice_group');
    
    if (albumType) {
        choiceGroup.style.display = 'block';
        
        fetch('Song?t_gubun=getAlbumList&type=' + albumType)
            .then(response => response.json())
            .then(data => {
                const albumSelect = document.getElementById('album_id');
                albumSelect.innerHTML = '<option value="">앨범을 선택하세요</option>';
                
                data.forEach(album => {
                    const option = document.createElement('option');
                    option.value = album.album_id;
                    option.textContent = album.album_title + ' (' + album.album_number + ')';
                    albumSelect.appendChild(option);
                });
            })
            .catch(error => {
                console.error('앨범 목록 로드 실패:', error);
            });
    } else {
        choiceGroup.style.display = 'none';
        resetAlbumForm();
    }
}

// 앨범 폼 토글
function toggleAlbumForm() {
    const existingChecked = document.getElementById('existing_album').checked;
    const newChecked = document.getElementById('new_album').checked;
    
    const existingGroup = document.getElementById('existing_album_group');
    const newForm = document.getElementById('new_album_form');
    
    if (existingChecked) {
        existingGroup.style.display = 'block';
        newForm.style.display = 'none';
        document.getElementById('new_album_title').value = "";
        document.getElementById('album_number').value = "";
        document.getElementById('release_date').value = "";
        document.getElementById('album_description').value = "";
    } else if (newChecked) {
        existingGroup.style.display = 'none';
        newForm.style.display = 'block';
        document.getElementById('album_id').value = "";
    }

}

function resetAlbumForm() {
    document.getElementById('existing_album_group').style.display = 'none';
    document.getElementById('new_album_form').style.display = 'none';
    document.querySelectorAll('input[name="album_choice"]').forEach(radio => radio.checked = false);
}

function validateForm() {
    const title = document.getElementById('song_title').value.trim();
    const albumType = document.getElementById('album_type').value;
    if (!albumType) {
        alert("앨범 타입을 선택하세요.");
        return false;
    }
    if (title === "") {
        alert("곡 제목을 입력하세요.");
        return false;
    }
    return true;
}
</script>

<%@ include file="../footer.jsp" %>