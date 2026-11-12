<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String level = (String)session.getAttribute("sessionLevel");
    if (!"top".equals(level)) {
        response.sendRedirect("Scheduled");
        return;
    }
%>

<%@ include file="../header.jsp" %>
<link rel="stylesheet" href="css/scheduled.css">

<script>
function previewPoster(event) {
    const file = event.target.files[0];
    const preview = document.getElementById('preview');
    const container = document.getElementById('preview_container');
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

function validateForm() {
    const title = document.getElementById('title').value.trim();
    const venue = document.getElementById('venue').value.trim();
    const date  = document.getElementById('performance_date').value;
    if (!title) { alert('공연 제목을 입력하세요.'); return false; }
    if (!venue)  { alert('공연 장소를 입력하세요.'); return false; }
    if (!date)   { alert('공연 날짜를 선택하세요.'); return false; }
    return true;
}
</script>

<form name="writeForm" method="post" action="Scheduled" enctype="multipart/form-data">
    <input type="hidden" name="t_gubun" value="save">

    <div class="content-row">
        <main class="main-content song-form-container">
            <div class="content-title">
                <h2>공연 등록</h2>
            </div>

            <div class="content-form">
                <div class="form-section">
                    <h3>포스터 이미지</h3>
                    <div class="form-group">
                        <label for="poster_image">포스터 이미지</label>
                        <input type="file" name="t_poster_image" id="poster_image"
                               accept="image/*" onchange="previewPoster(event)">
                        <small style="color:#666;">JPG, PNG (최대 10MB)</small>
                        <div id="preview_container" style="margin-top:15px; display:none;">
                            <img id="preview" src="" alt="미리보기"
                                 style="max-width:300px; border-radius:8px; box-shadow:0 4px 8px rgba(0,0,0,0.2);">
                        </div>
                    </div>
                </div>

                <div class="form-section">
                    <h3>공연 정보</h3>
                    <div class="form-group">
                        <label for="title">공연 제목 <span style="color:red;">*</span></label>
                        <input type="text" name="t_title" id="title" placeholder="공연 제목을 입력하세요" required>
                    </div>
                    <div class="form-group">
                        <label for="venue">공연 장소 <span style="color:red;">*</span></label>
                        <input type="text" name="t_venue" id="venue" placeholder="예: 홍대 롤링홀, 서울 올림픽공원" required>
                    </div>
                    <div class="form-group">
                        <label for="performance_date">공연 날짜 <span style="color:red;">*</span></label>
                        <input type="date" name="t_performance_date" id="performance_date" required>
                    </div>
                    <div class="form-group">
                        <label for="performance_time">공연 시간</label>
                        <input type="time" name="t_performance_time" id="performance_time">
                    </div>
                    <div class="form-group">
                        <label for="ticket_url">티켓 구매 링크</label>
                        <input type="url" name="t_ticket_url" id="ticket_url"
                               placeholder="https://tickets.interpark.com/...">
                    </div>
                    <div class="form-group">
                        <label for="description">공연 설명</label>
                        <textarea name="t_description" id="description" rows="5"
                                  placeholder="공연에 대한 설명을 입력하세요"></textarea>
                    </div>
                </div>

                <div class="form-buttons">
                    <button type="submit" class="btn-submit" onclick="return validateForm()">공연 등록</button>
                    <button type="button" class="btn-cancel" onclick="location.href='Scheduled'">취소</button>
                </div>
            </div>
        </main>
    </div>
</form>

<%@ include file="../footer.jsp" %>
