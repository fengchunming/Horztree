<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML> 
<html> 
<head> 
<meta charset=UTF-8/> 
<meta http-equiv="Pragma" content="No-cache" />
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate" />
<meta http-equiv="Expires" content="-1" />
<title>快递单打印</title> 
<link href="/templates/screen.css" rel="stylesheet" media="screen" />
<link href="/templates/print.css" rel="stylesheet" media="print" />
<link rel="stylesheet" href="/templates/screen.${trades[0].shipment}.css" media="screen"/>
<link rel="stylesheet" href="/templates/print.${trades[0].shipment}.css" media="print"/>
</head>
<body>
<div id="main">
<c:forEach items="${trades}" var="trade" >
	<div class="express">
		<div class="fromName field">${trade.org.orgName}</div>
		<div class="fromPhone field">${trade.org.telephone}</div>
		<div class="fromArea field">${trade.org.region}­</div>
		<div class="fromAddr field">${trade.org.address}­</div>
		<div class="pay field"></div>
		<div class="toName field">${trade.target.linkman}</div>
		<div class="toMobile field">${trade.target.mobile}</div>
		<div class="toPhone field">${trade.target.telephone}</div>
		<div class="toAddr field">${trade.target.region} &nbsp;${trade.target.address}</div>
		<div class="mark field"></div>
	</div>
</c:forEach>
</div>
</body>
</html>