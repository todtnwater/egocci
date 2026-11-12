<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.VideoDto" %>
<%
    String level = (String)session.getAttribute("sessionLevel");
    if(!"top".equals(level)) {
        response.sendRedirect("index.jsp");
        return;
    }
    VideoDto dto = (VideoDto)request.getAttribute("t_dto");
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
    document.videoUpdateForm.submit();
}
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

<form name="videoUpdateForm" method="post" action="Video">
    <div class="content-row">
        <main class="main-content song-form-container">
            <%if(dto != null) {%>
            <input type="hidden" name="t_gubun" value="update">
            <input type="hidden" name="t_no" value="<%=dto.getVideo_id()%>">
            <div class="content-title"><h2>영상 수정</h2></div>
            <div class="content-form">
                <div class="form-section">
                    <div class="form-group">
                        <label for="video_type">영상 타입 <span style="color:red;">*</span></label>
                        <select name="t_video_type" id="video_type">
                            <option value="mv"   <%="mv".equals(dto.getVideo_type())   ? "selected" : ""%>>MV (뮤직비디오)</option>
                            <option value="live" <%="live".equals(dto.getVideo_type()) ? "selected" : ""%>>Live (라이브 공연)</option>
                            <option value="etc"  <%="etc".equals(dto.getVideo_type())  ? "selected" : ""%>>기타</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="title">제목 <span style="color:red;">*</span></label>
                        <input type="text" name="t_title" id="title" value="<%=dto.getTitle()%>">
                    </div>
                    <div class="form-group">
                        <label for="youtube_url">YouTube URL <span style="color:red;">*</span></label>
                        <input type="url" name="t_youtube_url" id="youtube_url"
                               value="<%=dto.getYoutube_url()%>" onblur="previewThumbnail()">
                        <img id="thumb_preview" src="<%=dto.getAutoThumbnail()%>" alt="썸네일"
                             style="margin-top:10px;max-width:320px;border-radius:8px;<%=dto.getAutoThumbnail().isEmpty() ? "display:none;" : ""%>">
                    </div>
                    <div class="form-group">
                        <label for="thumbnail_url">썸네일 URL (선택)</label>
                        <input type="url" name="t_thumbnail_url" id="thumbnail_url"
                               value="<%=dto.getThumbnail_url() != null ? dto.getThumbnail_url() : ""%>"
                               placeholder="비워두면 YouTube 썸네일 자동 사용">
                    </div>
                    <div class="form-group">
                        <label for="description">설명 (선택)</label>
                        <textarea name="t_description" id="description" rows="4"><%=dto.getDescription() != null ? dto.getDescription() : ""%></textarea>
                    </div>
                    <div class="form-group">
                        <label for="display_order">정렬 순서</label>
                        <input type="number" name="t_display_order" id="display_order"
                               value="<%=dto.getDisplay_order()%>" min="0">
                    </div>
                </div>
                <div class="form-buttons">
                    <button type="button" class="btn-submit" onclick="submitVideoForm()">수정 완료</button>
                    <button type="button" class="btn-cancel" onclick="location.href='Video?t_gubun=list'">취소</button>
                </div>
            </div>
            <%} else {%>
            <div class="content-title"><h2>영상 정보를 찾을 수 없습니다.</h2></div>
            <div class="navigation">
                <button type="button" class="btn-cancel" onclick="location.href='Video?t_gubun=list'">목록으로</button>
            </div>
            <%}%>
        </main>
    </div>
</form>

<%@ include file="../footer.jsp" %>
