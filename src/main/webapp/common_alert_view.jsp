<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<form name="work">
	<input type="hidden" name="t_gubun">
	<input type="hidden" name="t_no" value="${t_no}">
</form>
<script>
	alert("${t_msg}")
	work.t_gubun.value="${t_gubun}";
	work.method="post";
	work.action="${t_url}";
	work.submit();
</script>