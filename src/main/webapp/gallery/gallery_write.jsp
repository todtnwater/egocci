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
function previewImage(event) {
    const file = event.target.files[0];
    const preview = document.getElementById('img_preview');
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
    if (!document.getElementById('title').value.trim()) { alert("제목을 입력해주세요."); return false; }
    if (!document.getElementById('image').files[0])     { alert("이미지를 선택해주세요."); return false; }
    return true;
}
function submitForm() {
    if (!validateForm()) return;
    document.galleryWriteForm.submit();
}
</script>

<form name="galleryWriteForm" method="post" action="Gallery" enctype="multipart/form-data">
    <input type="hidden" name="t_gubun" value="save">
    <div class="content-row">
        <main class="main-content song-form-container">
            <div class="content-title"><h2>사진 등록</h2></div>
            <div class="content-form">
                <div class="form-section">
                    <div class="form-group">
                        <label for="title">제목 <span style="color:red;">*</span></label>
                        <input type="text" name="t_title" id="title" placeholder="사진 제목을 입력하세요">
                    </div>
                    <div class="form-group">
                        <label for="image">이미지 <span style="color:red;">*</span></label>
                        <input type="file" name="t_image" id="image" accept="image/*" onchange="previewImage(event)">
                        <div id="preview_container" style="display:none;margin-top:12px;">
                            <img id="img_preview" src="" alt="미리보기"
                                 style="max-width:400px;max-height:300px;border-radius:8px;object-fit:contain;">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="description">설명 (선택)</label>
                        <textarea name="t_description" id="description" rows="4"
                                  placeholder="사진에 대한 설명을 입력하세요"></textarea>
                    </div>
                    <div class="form-group">
                        <label for="display_order">정렬 순서</label>
                        <input type="number" name="t_display_order" id="display_order" value="0" min="0">
                        <small style="color:#aaa;">숫자가 작을수록 앞에 표시됩니다.</small>
                    </div>
                </div>
                <div class="form-buttons">
                    <button type="button" class="btn-submit" onclick="submitForm()">등록</button>
                    <button type="button" class="btn-cancel" onclick="location.href='Gallery?t_gubun=list'">취소</button>
                </div>
            </div>
        </main>
    </div>
</form>

<%@ include file="../footer.jsp" %>
