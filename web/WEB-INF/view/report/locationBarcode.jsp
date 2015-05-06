<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML> 
<html>
<head>
<meta charset=UTF-8/> 
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="expires" content="Wed, 23 Aug 2006 12:40:27 UTC" />
<link rel="stylesheet" href="/css/report.css"/>
</head>
<body>
<c:forEach items="${locations}" var="location">
	<div>
		<div style="float:left;padding:10px;"><img src="/barcode.svg?msg=${location.barcode}&height=25&mw=1&hrsize=15mm" style="width:220px"></div>
	</div>
</c:forEach>
</body>
</html>