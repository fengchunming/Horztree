package com.an.trade.entity;


public class TradeConfig {
	private Integer id;
	private String billDate;
	private Integer maxAmount;
	private Integer existAmount;
	private String status = "t";
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getExistAmount() {
		return existAmount;
	}
	public void setExistAmount(Integer existAmount) {
		this.existAmount = existAmount;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public Integer getMaxAmount() {
		return maxAmount;
	}
	public void setMaxAmount(Integer maxAmount) {
		this.maxAmount = maxAmount;
	}
	
	

	

}
