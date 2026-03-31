<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dto.MemberDto" %>
<%
    String level = (String)session.getAttribute("sessionLevel");
    if(!"top".equals(level)) {
        response.sendRedirect("Index");
        return;
    }

    MemberDto dto = (MemberDto)request.getAttribute("t_dto");
    if(dto == null) {
        response.sendRedirect("Manager?t_gubun=list");
        return;
    }
%>

<%@ include file="../header.jsp" %>

<script>
function updateMember() {
    document.managerForm.t_gubun.value = "update";
    document.managerForm.submit();
}

function deleteMember() {
    if(confirm("정말 삭제 처리하시겠습니까?")) {
        document.managerForm.t_gubun.value = "delete";
        document.managerForm.submit();
    }
}
</script>

<div class="content-row">
    <main class="main-content">
        <div class="content-title"><h2>회원 상세 관리</h2></div>

        <form name="managerForm" method="post" action="Manager">
            <input type="hidden" name="t_gubun" value="">
            <div style="max-width:700px;margin:0 auto;color:#fff;">
                <p>Email<br><input type="text" name="t_email" value="<%=dto.getEmail()%>" readonly style="width:100%;"></p>
                <p>Name<br><input type="text" name="t_name" value="<%=dto.getName()%>" style="width:100%;"></p>
                <p>Phone<br>
                    <input type="text" name="t_phone1" value="<%=dto.getPhone1() == null ? "" : dto.getPhone1()%>" style="width:80px;">
                    -
                    <input type="text" name="t_phone2" value="<%=dto.getPhone2() == null ? "" : dto.getPhone2()%>" style="width:100px;">
                    -
                    <input type="text" name="t_phone3" value="<%=dto.getPhone3() == null ? "" : dto.getPhone3()%>" style="width:100px;">
                </p>
                <p>Gender<br>
                    <select name="t_gender">
                        <option value="M" <%= "M".equals(dto.getGender()) ? "selected" : "" %>>남</option>
                        <option value="F" <%= "F".equals(dto.getGender()) ? "selected" : "" %>>여</option>
                    </select>
                </p>
                <p>Role<br>
                    <select name="t_role">
                        <option value="user" <%= "user".equals(dto.getRole()) ? "selected" : "" %>>user</option>
                        <option value="top" <%= "top".equals(dto.getRole()) ? "selected" : "" %>>top</option>
                    </select>
                </p>
                <p>가입일: <%=dto.getCreated_at()%></p>
                <p>수정일: <%=dto.getUpdated_at()%></p>
                <p>탈퇴여부: <%=dto.getExited()%></p>

                <div style="margin-top:20px;">
                    <button type="button" onclick="updateMember()">수정</button>
                    <button type="button" onclick="deleteMember()">삭제</button>
                    <button type="button" onclick="location.href='Manager?t_gubun=list'">목록</button>
                </div>
            </div>
        </form>
    </main>
</div>

<%@ include file="../footer.jsp" %>