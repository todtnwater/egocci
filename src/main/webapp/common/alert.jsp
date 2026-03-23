<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String msg = (String)request.getAttribute("t_msg");
    String url = (String)request.getAttribute("t_url");
    if(msg == null) msg = "처리되었습니다.";
    if(url == null) url = "javascript:history.back()";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>알림</title>
</head>
<body>
    <script>
        alert("<%=msg%>");
        location.href = "<%=url%>";
    </script>
</body>
</html>