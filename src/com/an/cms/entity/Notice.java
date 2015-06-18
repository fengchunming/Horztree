package com.an.cms.entity;

import com.an.utils.JSONDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.util.Date;

public class Notice implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
    private String title;
    private String type;
    private Integer readTimes;
    private String status = "0";
    private Integer createBy;
    private Date createAt;
    private String content;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getReadTimes() {
		return readTimes;
	}
	public void setReadTimes(Integer readTimes) {
		this.readTimes = readTimes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}
	@JsonSerialize(using = JSONDateSerializer.class)
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
    
}