package com.an.trade.entity;

import com.an.utils.JSONDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TradeReport implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer regionId;
	private String regionCode;
	private Date billDate;
	private String payment;
	private String billType;
	private BigDecimal amount;
	private Integer billCount;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRegionId() {
		return regionId;
	}
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
    @JsonSerialize(using = JSONDateSerializer.class)
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public Integer getBillCount() {
		return billCount;
	}
	public void setBillCount(Integer billCount) {
		this.billCount = billCount;
	}

}
