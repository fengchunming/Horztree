package com.an.mm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.an.utils.JSONDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class WorkBillDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer billId;
	private Integer goodsId;
	private String goodsCode;
	private String goodsBarcode;
	private String goodsName;
	private Integer uomId;
	private BigDecimal costPrice;
	private BigDecimal quantity;
	private BigDecimal subTotal;
	private String status = "t";
	private Integer enterBy;
	private Date enterAt;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBillId() {
		return billId;
	}
	public void setBillId(Integer billId) {
		this.billId = billId;
	}
	public Integer getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsCode() {
		return goodsCode;
	}
	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}
	public String getGoodsBarcode() {
		return goodsBarcode;
	}
	public void setGoodsBarcode(String goodsBarcode) {
		this.goodsBarcode = goodsBarcode;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public Integer getUomId() {
		return uomId;
	}
	public void setUomId(Integer uomId) {
		this.uomId = uomId;
	}
	public BigDecimal getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getEnterBy() {
		return enterBy;
	}
	public void setEnterBy(Integer enterBy) {
		this.enterBy = enterBy;
	}
	@JsonSerialize(using = JSONDateTimeSerializer.class)
	public Date getEnterAt() {
		return enterAt;
	}
	public void setEnterAt(Date enterAt) {
		this.enterAt = enterAt;
	}
	
}
