package com.an.mm.entity;

import com.an.utils.JSONDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkBill implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String billCode;
	private String billName;
	private String billType;
	private Integer fromRegion;
	private Integer toRegion;
	private Integer createBy;
	private Date createTime;
	private Integer dealBy;
	private Date dealTime;
	private String dealStatus = "0";;
	private String remark;
	private String status = "t";
	private Integer enterBy;
	private Date enterAt;
	private List<WorkBillDetail> details = new ArrayList<WorkBillDetail>();

	public void addDetail(WorkBillDetail detail) {
		details.add(detail);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public Integer getFromRegion() {
		return fromRegion;
	}

	public void setFromRegion(Integer fromRegion) {
		this.fromRegion = fromRegion;
	}

	public Integer getToRegion() {
		return toRegion;
	}

	public void setToRegion(Integer toRegion) {
		this.toRegion = toRegion;
	}

	public Integer getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}

	@JsonSerialize(using = JSONDateTimeSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getDealBy() {
		return dealBy;
	}

	public void setDealBy(Integer dealBy) {
		this.dealBy = dealBy;
	}

	@JsonSerialize(using = JSONDateTimeSerializer.class)
	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}

	public String getDealStatus() {
		return dealStatus;
	}

	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public List<WorkBillDetail> getDetails() {
		return details;
	}

	public void setDetails(List<WorkBillDetail> details) {
		this.details = details;
	}

}
