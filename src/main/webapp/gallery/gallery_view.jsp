<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.GalleryDto" %>
<%
    GalleryDto dto = (GalleryDto)request.getAttribute("t_dto");
    String level = (String)session.getAttribute("sessionLevel");
    boolean isAdmin = "top".equals(level);
%>

<%@ include file="../header.jsp" %>

<form name="galleryForm">
    <input type="hidden" name="t_gubun">
    <input type="hidden" name="t_no">
</form>

<script>
function goGalleryDelete(galleryId) {
    if (!confirm("정말 삭제하시겠습니까?")) return;
    galleryForm.t_gubun.value = "delete";
    galleryForm.t_no.value = galleryId;
    galleryForm.method = "post";
    galleryForm.action = "Gallery";
    galleryForm.submit();
}
</script>

<div class="content-row">
    <main class="main-content">
        <%if(dto != null) {%>
        <div class="gallery-view-container">
            <div class="gallery-view-img-wrap">
                <img src="<%=request.getContextPath()%>/image/gallery/<%=dto.getImage_file()%>"
                     alt="<%=dto.getTitle()%>"
                     class="gallery-view-img"
                     onerror="this.src='<%=request.getContextPath()%>/image/song/default-song.jpg'">
            </div>
            <div class="gallery-view-info">
                <h2 class="gallery-view-title"><%=dto.getTitle()%></h2>
                <p class="gallery-view-date"><%=dto.getCreated_at() != null ? dto.getCreated_at().substring(0,10) : ""%></p>
                <%if(dto.getDescription() != null && !dto.getDescription().isEmpty()) {%>
                <p class="gallery-view-desc"><%=dto.getDescription()%></p>
                <%}%>
                <div class="form-buttons" style="margin-top:24px;">
                    <%if(isAdmin) {%>
                    <button class="btn-danger" onclick="goGalleryDelete(<%=dto.getGallery_id()%>)">삭제</button>
                    <%}%>
                    <button class="btn-cancel" onclick="location.href='Gallery?t_gubun=list'">목록으로</button>
                </div>
            </div>
        </div>
        <%} else {%>
        <div class="content-title"><h2>사진을 찾을 수 없습니다.</h2></div>
        <button class="btn-cancel" onclick="location.href='Gallery?t_gubun=list'">목록으로</button>
        <%}%>
    </main>
</div>

<%@ include file="../footer.jsp" %>
