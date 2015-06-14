package com.an.mm.entity;

import com.an.utils.JSONDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

public class Brand {
    private Integer id;
    private String name;
    private String icon;
    private String description;
    private Integer enterBy;
    private Date enterAt;
    private String status = "t";
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getEnterBy() {
		return enterBy;
	}
	public void setEnterBy(Integer enterBy) {
		this.enterBy = enterBy;
	}
    @JsonSerialize(using = JSONDateSerializer.class)
	public Date getEnterAt() {
		return enterAt;
	}
	public void setEnterAt(Date enterAt) {
		this.enterAt = enterAt;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}