package com.an.base.entity;

import java.io.Serializable;
import java.util.Date;

public class Region implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer parent;
	private String code;
	private String name;
	private String fullName;
	private String type;
	private Integer step;
	private boolean isLeaf;
	private String path;
	private String depth;
	private Integer sort;
	private String businessStatus;
	private String status = "t";
	private Integer enterBy;
	private Date enterAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStep() {
		return step;
	}

	public void setStep(Integer step) {
		this.step = step;
	}
	
	public String getDepth() {
		return depth;
	}

	public void setDepth(String depth) {
		this.depth = depth;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getBusinessStatus() {
		return businessStatus;
	}

	public void setBusinessStatus(String businessStatus) {
		this.businessStatus = businessStatus;
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

	public Date getEnterAt() {
		return enterAt;
	}

	public void setEnterAt(Date enterAt) {
		this.enterAt = enterAt;
	}

}
