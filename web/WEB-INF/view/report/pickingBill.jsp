<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML> 
<html>
<head>
<meta charset=UTF-8/> 
<meta http-equiv="Pragma" content="no-cache">
<meta http-equiv="expires" content="Wed, 23 Aug 2006 12:40:27 UTC" />
<title>配货单${bill.billCode}</title>
<link rel="stylesheet" href="/css/report.css"/>
</head>
<body>
<header>
<div class="title">
	<h1>配货单</h1>
	<span data-local="setting">setName</span>
</div>
<div class="billCode"><img src="/barcode.svg?msg=${bill.billCode}&height=12&mw=0.5"></div>
<jsp:useBean id="now" class="java.util.Date" />
<div class="printTime">打印时间：<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm"/></div>
</header>
<nav>
<ol>
	<li><label>配 货 单 号:</label>${bill.billCode}</li>
	<li><label>仓 库 编 码:</label>${bill.org.orgCode}</li>
	<li><label>仓 库 地 址:</label>${bill.org.orgName}</li>
	
</ol>
<ol>
	<li><label>配 送 地 址:</label>${bill.target.address}</li>
	<li><label>发 货 日 期:</label>${bill.planDate}</li>
</ol>
</nav>
<article>
	<table>
		<thead>
			<tr>
				<th>序号</th>
				<th>物料编号</th>
				<th>物料名称</th>
				<th>单位</th>
				<th>数量</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="detail" varStatus="status">
			<tr>
			<td>${status.index + 1}</td>
			<td>${detail.pn }</td>
			<td>${detail.goodsName }</td>
			<td>${detail.goodsUnit.uomCode }</td>
			<td><fmt:formatNumber pattern=".00" value="${detail.quantity }"/></td>
			</tr>
		</c:forEach>
		</tbody>
		<tfoot>
			<tr>
			<td colspan="4" align="right">总计：</td>
			<td align="right"></td>
			</tr>
		</tfoot>
	</table>
</article>
<footer>
<div><label>审核：</label></div>
<div><label>制单：</label></div>
</footer>
</body>
</html>