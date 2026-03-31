<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dto.ScheduledDto" %>
<%
    ArrayList<ScheduledDto> upcomingList = (ArrayList<ScheduledDto>)request.getAttribute("upcomingList");
    ArrayList<ScheduledDto> pastList     = (ArrayList<ScheduledDto>)request.getAttribute("pastList");
%>

<%@ include file="../header.jsp" %>
<link rel="stylesheet" href="css/scheduled.css">

<script>
function goWriteForm() {
    sc.t_gubun.value = "writeForm";
    sc.method = "post";
    sc.action = "Scheduled";
    sc.submit();
}
function goUpdateForm(id) {
    sc.t_gubun.value = "updateForm";
    sc.t_no.value = id;
    sc.method = "post";
    sc.action = "Scheduled";
    sc.submit();
}
function goDelete(id) {
    if (!confirm("정말로 이 공연을 삭제하시겠습니까?")) return;
    sc.t_gubun.value = "delete";
    sc.t_no.value = id;
    sc.method = "post";
    sc.action = "Scheduled";
    sc.submit();
}
</script>

<form name="sc">
    <input type="hidden" name="t_gubun">
    <input type="hidden" name="t_no">
</form>

<div class="content-row">
    <main class="main-content">

        <!-- 관리자 메뉴 -->
        <%
        String level = (String)session.getAttribute("sessionLevel");
        if ("top".equals(level)) {
        %>
        <div class="content-title2">
            <div class="admin-menu">
                <a href="javascript:goWriteForm()" class="btn-submit">공연 등록</a>
            </div>
        </div>
        <%}%>

        <!-- 예정 공연 섹션 -->
        <div class="sc-section-title">
            <h2>Scheduled Performance</h2>
        </div>

        <%
        if (upcomingList != null && !upcomingList.isEmpty()) {
            for (ScheduledDto dto : upcomingList) {
                String posterPath = (dto.getPoster_image() != null && !dto.getPoster_image().trim().isEmpty())
                    ? request.getContextPath() + "/image/scheduled/" + dto.getPoster_image() : null;
        %>
        <div class="sc-card">
            <%if(posterPath != null) {%>
            <div class="sc-card-poster">
                <img src="<%=posterPath%>" alt="<%=dto.getTitle()%>">
            </div>
            <%}%>
            <div class="sc-card-info">
                <h3 class="sc-title"><%=dto.getTitle()%></h3>
                <p class="sc-date">
                     <%=dto.getPerformance_date()%>
                    <%if (dto.getPerformance_time() != null && !dto.getPerformance_time().isEmpty()) {%>
                    &nbsp; <%=dto.getPerformance_time().substring(0, 5)%>
                    <%}%>
                </p>
                <p class="sc-venue"> <%=dto.getVenue()%></p>
                <%if (dto.getDescription() != null && !dto.getDescription().trim().isEmpty()) {%>
                <p class="sc-desc"><%=dto.getDescription()%></p>
                <%}%>
                <div class="sc-buttons">
                    <%if (dto.getTicket_url() != null && !dto.getTicket_url().trim().isEmpty()) {%>
                    <a href="<%=dto.getTicket_url()%>" target="_blank" class="btn-ticket">🎟 티켓 구매</a>
                    <%}%>
                    <%if ("top".equals(level)) {%>
                    <a href="javascript:goUpdateForm(<%=dto.getPerformance_id()%>)" class="btn-warning">수정</a>
                    <a href="javascript:goDelete(<%=dto.getPerformance_id()%>)" class="btn-danger">삭제</a>
                    <%}%>
                </div>
            </div>
        </div>
        <%
            }
        } else {
        %>
        <div class="sc-empty">
            <p>현재 예정된 공연이 없습니다.</p>
        </div>
        <%}%>

        <!-- 지난 공연 섹션 -->
        <%if (pastList != null && !pastList.isEmpty()) {%>
        <div class="sc-section-title sc-past-title">
            <h2>Past Performance</h2>
        </div>
        <%
            for (ScheduledDto dto : pastList) {
                String posterPath = (dto.getPoster_image() != null && !dto.getPoster_image().trim().isEmpty())
                    ? request.getContextPath() + "/image/scheduled/" + dto.getPoster_image() : null;
        %>
        <div class="sc-card sc-past-card">
            <%if(posterPath != null) {%>
            <div class="sc-card-poster">
                <img src="<%=posterPath%>" alt="<%=dto.getTitle()%>">
                <span class="sc-past-badge">종료</span>
            </div>
            <%}%>
            <div class="sc-card-info">
                <h3 class="sc-title"><%=dto.getTitle()%></h3>
                <p class="sc-date">
                     <%=dto.getPerformance_date()%>
                    <%if (dto.getPerformance_time() != null && !dto.getPerformance_time().isEmpty()) {%>
                    &nbsp; <%=dto.getPerformance_time().substring(0, 5)%>
                    <%}%>
                </p>
                <p class="sc-venue"> <%=dto.getVenue()%></p>
                <%if (dto.getDescription() != null && !dto.getDescription().trim().isEmpty()) {%>
                <p class="sc-desc"><%=dto.getDescription()%></p>
                <%}%>
                <div class="sc-buttons">
                    <%if (dto.getTicket_url() != null && !dto.getTicket_url().trim().isEmpty()) {%>
                    <a href="<%=dto.getTicket_url()%>" target="_blank" class="btn-ticket">🎟 공연 정보</a>
                    <%}%>
                    <%if ("top".equals(level)) {%>
                    <a href="javascript:goUpdateForm(<%=dto.getPerformance_id()%>)" class="btn-warning">수정</a>
                    <a href="javascript:goDelete(<%=dto.getPerformance_id()%>)" class="btn-danger">삭제</a>
                    <%}%>
                </div>
            </div>
        </div>
        <%
            }
        }%>

    </main>
</div>

<%@ include file="../footer.jsp" %>
