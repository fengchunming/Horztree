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
<title>采购订单${bill.billCode}</title>
<link rel="stylesheet" href="/css/report.css"/>
<script src="/js/libs/jquery.min.js"></script>
<script src="/js/libs/underscore-min.js"></script>
<script src="/js/localData.js" ></script>
<script>
$(function() {
	convertLocal();
});
</script>
</head>
<body>
<header>
<div class="title">
	<h1>采购订单</h1>
	<span data-local="setting">setName</span>
</div>
<div class="billCode"><img src="/barcode.svg?msg=${bill.billCode}&height=12&mw=0.5"></div>
<jsp:useBean id="now" class="java.util.Date" />
<div class="printTime">打印时间：<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm"/></div>
</header>
<nav>
<ol>
	<li><label class="let4">订 货 日 期:</label>${bill.billDate}</li>
	<li><label>要求到货日期:</label>${bill.planDate}</li>
	<li><label>供 应 商:</label>${bill.partyB.orgName}</li>
	<li><label class="let4">送 货 地 点:</label>${bill.warehouse.address}</li>
</ol>
<ol>
	<li><label>订单编号:</label>${bill.billCode}</li>
	<li><label>电话:</label>${bill.warehouse.telephone}</li>
	<li><label>传真:</label></li>
</ol>
</nav>
<article>
	<table>
		<thead>
			<tr>
				<th>序号</th>
				<th>物料编号</th>
				<th>物料名称</th>
				<th align="right">单价</th>
				<th align="right">订单数量</th>
				<th>单位</th>
				<th align="right">小计</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="detail" varStatus="status">
			<tr>
			<td>${status.index + 1}</td>
			<td>${detail.pn }</td>
			<td>${detail.goodsName }</td>
			<td align="right"><fmt:formatNumber pattern="0.00" value="${detail.standardCostPrice }"/></td>
			<td align="right"><fmt:formatNumber pattern="0.00" value="${detail.planQuantity }"/></td>
			<td data-local="mm/loadUomKV.do">${detail.goodsUnit.uomCode }</td>
			<td align="right"><fmt:formatNumber pattern="0.00" value="${detail.saleTotal }"/></td>
			</tr>
		</c:forEach>
		</tbody>
		<tfoot>
			<tr>
			<td colspan="5" align="right">整单总计：</td>
			<td>${count}</td>
			<td><fmt:formatNumber pattern="0.00" value="${bill.amount}"/></td>
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