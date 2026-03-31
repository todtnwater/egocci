<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dto.GalleryDto" %>
<%
    ArrayList<GalleryDto> galleryList = (ArrayList<GalleryDto>)request.getAttribute("galleryList");
    String level = (String)session.getAttribute("sessionLevel");
    boolean isAdmin = "top".equals(level);
    int INITIAL_SHOW = 12; // 처음에 보여줄 개수
    int totalCount = (galleryList != null) ? galleryList.size() : 0;
%>

<%@ include file="../header.jsp" %>

<form name="galleryForm">
    <input type="hidden" name="t_gubun">
    <input type="hidden" name="t_no">
</form>

<script>
function goGalleryView(galleryId) {
    galleryForm.t_gubun.value = "view";
    galleryForm.t_no.value = galleryId;
    galleryForm.method = "post";
    galleryForm.action = "Gallery";
    galleryForm.submit();
}
function goGalleryWrite() {
    galleryForm.t_gubun.value = "writeForm";
    galleryForm.method = "post";
    galleryForm.action = "Gallery";
    galleryForm.submit();
}
function goGalleryDelete(galleryId) {
    if (!confirm("정말 삭제하시겠습니까?")) return;
    galleryForm.t_gubun.value = "delete";
    galleryForm.t_no.value = galleryId;
    galleryForm.method = "post";
    galleryForm.action = "Gallery";
    galleryForm.submit();
}

function showMoreGallery() {
    const hiddenItems = document.querySelectorAll('.gallery-item.hidden');
    hiddenItems.forEach(item => item.classList.remove('hidden'));
    document.getElementById('load-more-btn').style.display = 'none';
}
</script>

<style>
/* 더 보기 버튼 */
#load-more-btn {
    display: block;
    margin: 24px auto 0;
    padding: 10px 32px;
    background: rgba(255,255,255,0.15);
    color: #fff;
    border: 1px solid rgba(255,255,255,0.3);
    border-radius: 24px;
    font-size: 14px;
    cursor: pointer;
    transition: background 0.2s;
}
#load-more-btn:hover {
    background: rgba(255,255,255,0.25);
}
/* 숨겨진 아이템 */
.gallery-item.hidden {
    display: none;
}
/* 갤러리 그리드 중앙 정렬 보완 */
.gallery-grid {
    justify-content: center;
}
</style>

<div class="content-row">
    <main class="main-content">
        <div class="content-title">
            <h2>Gallery</h2>
        </div>

        <%if(isAdmin) {%>
        <div class="gallery-admin-wrap">
            <a href="javascript:goGalleryWrite()" class="btn-submit">사진 등록</a>
        </div>
        <%}%>

        <%if(galleryList != null && !galleryList.isEmpty()) {%>
        <div class="gallery-grid">
            <%
            int idx = 0;
            for(GalleryDto dto : galleryList) {
                boolean isHidden = (idx >= INITIAL_SHOW);
                idx++;
            %>
            <div class="gallery-item<%=isHidden ? " hidden" : ""%>">
                <div class="gallery-thumb" onclick="goGalleryView(<%=dto.getGallery_id()%>)">
                    <img src="<%=request.getContextPath()%>/image/gallery/<%=dto.getImage_file()%>"
                         alt="<%=dto.getTitle()%>"
                         onerror="this.src='<%=request.getContextPath()%>/image/song/default-song.jpg'">
                    <div class="gallery-overlay">
                        <span class="material-icons">zoom_in</span>
                    </div>
                </div>
                <div class="gallery-item-info">
                    <p class="gallery-item-title"><%=dto.getTitle()%></p>
                    <%if(isAdmin) {%>
                    <div class="gallery-admin-btns">
                        <button class="btn-danger" onclick="goGalleryDelete(<%=dto.getGallery_id()%>)">삭제</button>
                    </div>
                    <%}%>
                </div>
            </div>
            <%}%>
        </div>

        <%if(totalCount > INITIAL_SHOW) {%>
        <button id="load-more-btn" onclick="showMoreGallery()">
            더 보기 (<%=totalCount - INITIAL_SHOW%>장 더)
        </button>
        <%}%>

        <%} else {%>
        <p class="no-content">등록된 사진이 없습니다.</p>
        <%}%>
    </main>
</div>

<%@ include file="../footer.jsp" %>
