<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String queryString = request.getQueryString();
    response.sendRedirect(request.getContextPath() + "/Index" + (queryString != null ? "?" + queryString : ""));
%>