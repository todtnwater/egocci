<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String level = (String)session.getAttribute("sessionLevel");
    if(!"top".equals(level)) {
        response.sendRedirect("index.jsp");
        return;
    }
%>

<%@ include file="../header.jsp" %>

<script>
function validateVideoForm() {
    const title = document.getElementById('title').value.trim();
    const url   = document.getElementById('youtube_url').value.trim();
    const type  = document.getElementById('video_type').value;
    if (!type)  { alert("영상 타입을 선택해주세요."); return false; }
    if (!title) { alert("제목을 입력해주세요."); return false; }
    if (!url)   { alert("YouTube URL을 입력해주세요."); return false; }
    return true;
}
function submitVideoForm() {
    if (!validateVideoForm()) return;
    document.videoWriteForm.submit();
}

// URL 입력 시 썸네일 미리보기
function previewThumbnail() {
    const url = document.getElementById('youtube_url').value.trim();
    const preview = document.getElementById('thumb_preview');
    let videoId = '';
    if (url.includes('v=')) {
        videoId = url.substring(url.indexOf('v=') + 2);
        if (videoId.includes('&')) videoId = videoId.substring(0, videoId.indexOf('&'));
    } else if (url.includes('youtu.be/')) {
        videoId = url.substring(url.indexOf('youtu.be/') + 9);
        if (videoId.includes('?')) videoId = videoId.substring(0, videoId.indexOf('?'));
    }
    if (videoId) {
        preview.src = 'https://img.youtube.com/vi/' + videoId + '/mqdefault.jpg';
        preview.style.display = 'block';
    } else {
        preview.style.display = 'none';
    }
}
</script>

<form name="videoWriteForm" method="post" action="Video">
    <input type="hidden" name="t_gubun" value="save">
    <div class="content-row">
        <main class="main-content song-form-container">
            <div class="content-title"><h2>영상 등록</h2></div>
            <div class="content-form">
                <div class="form-section">
                    <div class="form-group">
                        <label for="video_type">영상 타입 <span style="color:red;">*</span></label>
                        <select name="t_video_type" id="video_type">
                            <option value="">타입을 선택하세요</option>
                            <option value="mv">MV (뮤직비디오)</option>
                            <option value="live">Live (라이브 공연)</option>
                            <option value="etc">기타</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="title">제목 <span style="color:red;">*</span></label>
                        <input type="text" name="t_title" id="title" placeholder="영상 제목을 입력하세요">
                    </div>
                    <div class="form-group">
                        <label for="youtube_url">YouTube URL <span style="color:red;">*</span></label>
                        <input type="url" name="t_youtube_url" id="youtube_url"
                               placeholder="https://www.youtube.com/watch?v=..."
                               onblur="previewThumbnail()">
                        <small style="color:#aaa;">URL 입력 후 포커스를 벗어나면 썸네일 미리보기가 나타납니다.</small>
                        <img id="thumb_preview" src="" alt="썸네일 미리보기"
                             style="display:none;margin-top:10px;max-width:320px;border-radius:8px;">
                    </div>
                    <div class="form-group">
                        <label for="thumbnail_url">썸네일 URL (선택)</label>
                        <input type="url" name="t_thumbnail_url" id="thumbnail_url"
                               placeholder="비워두면 YouTube 썸네일 자동 사용">
                        <small style="color:#aaa;">직접 지정하지 않으면 YouTube 썸네일이 자동으로 사용됩니다.</small>
                    </div>
                    <div class="form-group">
                        <label for="description">설명 (선택)</label>
                        <textarea name="t_description" id="description" rows="4"
                                  placeholder="영상에 대한 설명을 입력하세요"></textarea>
                    </div>
                    <div class="form-group">
                        <label for="display_order">정렬 순서</label>
                        <input type="number" name="t_display_order" id="display_order" value="0" min="0">
                        <small style="color:#aaa;">숫자가 작을수록 앞에 표시됩니다.</small>
                    </div>
                </div>
                <div class="form-buttons">
                    <button type="button" class="btn-submit" onclick="submitVideoForm()">등록</button>
                    <button type="button" class="btn-cancel" onclick="location.href='Video?t_gubun=list'">취소</button>
                </div>
            </div>
        </main>
    </div>
</form>

<%@ include file="../footer.jsp" %>
