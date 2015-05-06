<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE HTML> 
<html> 
<head> 
<meta charset=UTF-8/> 
<meta http-equiv="Pragma" content="No-cache" />
<meta http-equiv="Cache-Control" content="no-cache, must-revalidate" />
<meta http-equiv="Expires" content="-1" />
<title>订单: ${bill.billCode}</title>
<link rel="stylesheet" href="/css/print.css"/>
<script src="/js/lib/jquery.min.js"></script>
<script src="/js/lib/underscore-min.js"></script>
<script src="/js/util.js"></script>
<script src="/js/localData.js" ></script>
<script>
$(function() {
	convertLocal();
});
</script>
</head>
<body>
<div class="container" >
	<div id="pagehead">
		<div style="float:left">
			门口头 - <span  data-local="sys/kv/group">${bill.groupId}</span>
		</div>
		<div style="float:right">
            <img src="/barcode.svg?msg=${bill.billCode}&height=15&mw=0.5">
        </div>
	</div>
  	<div id="nav"> <!--上部分-->
       <div style="width:60%;float:left;">
			<div class="title">送货信息:</div>
			<div style="float:left;min-height:60px;width:300px;line-height:17px;word-break:break-all;">
				${bill.addr.linkman}<br />
				${bill.addr.region}&nbsp;&nbsp;${bill.addr.zip}<br />
				${bill.addr.addr}
			</div>
			<div class="title">联系方式:</div>
			<div class="leftinfo">
				${bill.addr.tel} &nbsp;&nbsp;
				${bill.addr.mobile}
			</div>
       </div>
       
       <div style="width:39%;float:right;">
            <ul>
            	<li class="title">订单号：</li>
                <li class="rightinfo">${bill.billCode}</li>
	            <li class="title">订购时间：</li>
                <li class="rightinfo"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${bill.dealStamp}" /></li>
            	<li class="title">支付方式：</li>
                <li class="rightinfo" data-local="fm/kv/payment">${bill.payment}</li>
				<li class="title">总金额：</li>
                <li class="rightinfo"><fmt:formatNumber pattern="0.00" value="${bill.amount}"/>
					<c:if test="${bill.paidTotal > 0}">
						(已付:<fmt:formatNumber pattern="0.00" value="${bill.paidTotal}"/>)
					</c:if>
				</li>
			</ul>
       </div>
  </div> <!--上部分结束-->
  <!--中部分开始-->
  <table id="productinfo">
  		<thead>
		<tr>
			<th>序号</th>
			<th>商品编号</th>
			<th style="text-align:left;">商品名称</th>
			<th class="number">单价</th>
			<th class="number">数量</th>
			<th>单位</th>
			<th class="number">小计</th>
		</tr>
		</thead>
        <tbody>
		<c:forEach items="${list}" var="detail" varStatus="status">
			<tr>
			<td>${status.index + 1}</td>
			<td>${detail.pn }</td>
			<td>${detail.name }</td>
			<td align="right"><fmt:formatNumber pattern="0.00" value="${detail.salePrice }"/></td>
			<td align="right"><fmt:formatNumber pattern="0.00" value="${detail.planQuantity }"/></td>
			<td data-local="base/kv/uom">${detail.uom.id }</td>
			<td align="right"><fmt:formatNumber pattern="0.00" value="${detail.saleTotal }"/></td>
			</tr>
		</c:forEach>
		</tbody>
   </table>
   <div class="footer">
   		备注：${bill.remark}
   </div>
</div>
</body>
</html>