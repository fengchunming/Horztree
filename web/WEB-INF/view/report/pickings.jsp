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
<title>订单打印</title> 
<style>
*{list-style: none; margin:0;padding:0; border:0; font-family:微软雅黑,黑体,tahoma,sans-serif; font-size:13px;}
.container {line-height:20px; width:750px;  margin:0px auto; page-break-after: always;}
#pagehead {width:100%; border-bottom:1px solid #888;float:left;}
#nav {width:100%; margin-top:2px;}
#productinfo {width:100%;border-bottom:1px solid #888;border-top:1px solid #888;border-collapse: collapse;} 
#productinfo thead{ border-bottom:1px solid #888;}
#productinfo th{ font-size:12px;}
.title {float:left;width:70px;clear:both;padding-right:10px;} 
.leftinfo{float:left;}
.rightinfo{float:right;text-align:right;}
.number{text-align:right;}

.footer {width:100%; color:#000;clear:both;} 
.total {float:right; text-align:right }
@page {margin:20px;
    mso-header-margin:0;
    mso-footer-margin:0;
}
</style>
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
<c:forEach items="${trades}" var="trade" >
<div class="container" >
	<div id="pagehead">
		<div style="float:left">
			${trade.org.orgName}<br />
		</div>
		<div style="float:right">
            <img src="/barcode.svg?msg=${trade.billCode}&height=15&mw=0.5">
        </div>
	</div>
  	<div id="nav"> <!--上部分-->
       <div style="width:60%;float:left;">
			<div class="title">送货信息:</div>
			<div style="float:left;min-height:60px;width:300px;line-height:17px;word-break:break-all;">
				${trade.target.linkman}<br />
				${trade.target.region}&nbsp;&nbsp;${trade.target.zipCode}<br />
				${trade.target.address}
			</div>
			<div class="title">联系方式:</div>
			<div class="leftinfo">
				${trade.target.telephone} &nbsp;&nbsp;
				${trade.target.mobile}
			</div>
       </div>
       
       <div style="width:39%;float:right;">
            <ul>
            	<li class="title">订单号：</li>
                <li class="rightinfo">${trade.originBill.billCode}</li>
	            <li class="title">订购时间：</li>
                <li class="rightinfo">${trade.billDate}</li>
            	<li class="title">快递：</li>
                <li class="rightinfo">${trade.shipment}&nbsp;</li>
				<li class="title">总金额：</li>
                <li class="rightinfo"><fmt:formatNumber pattern="0.00" value="${trade.itemTotal}"/></li>
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
		<c:forEach items="${trade.items}" var="detail" varStatus="status">
			<tr>
			<td>${status.index + 1}</td>
			<td>${detail.pn }</td>
			<td>${detail.goodsName }</td>
			<td align="right"><fmt:formatNumber pattern="0.00" value="${detail.standardSalePrice }"/></td>
			<td align="right"><fmt:formatNumber pattern="0.00" value="${detail.planQuantity }"/></td>
			<td data-local="mm/loadUomKV.do">${detail.goodsUnit.code }</td>
			<td align="right"><fmt:formatNumber pattern="0.00" value="${detail.saleTotal }"/></td>
			</tr>
		</c:forEach>
		</tbody>
   </table>
   <!--中部分结束-->
   <div class="footer"> <!--底部分开始-->
   		备注：${trade.remark}
   </div> <!--底部分结束-->
</div>
<div class="pageBreak"></div>
</c:forEach>
</body>
</html>