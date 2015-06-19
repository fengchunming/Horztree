package com.an.mm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商品主数据
 *
 * @author Karas
 */
public class Goods implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String code;
	private String barcode;
	private String name; 
	private String title;
	private String tips;
	private String type;
	private BigDecimal weight;
	private Integer shelfLife; 
	private String storageType;
	private Integer uomId;
	private Integer brandId;
	private String producer;
	private BigDecimal costPrice;
	private BigDecimal salePrice;
	private BigDecimal marketPrice;
	private Integer categoryId; 
	private String categoryCode;
	private Integer sort;
	private String remark;
	private Integer maxLimit;
	private Integer soldVolume;
	private BigDecimal cmtPoint;
	private String image;
	private List<Picture> images;
	private String description;
	private String status = "t";
	private Integer enterBy;
	private Date enterAt;

	private Integer regionId;
	private String regionCode;
	private String regionName;
	private Integer stockSum;
	
	private String isReserved;
	
	
	
	public String getIsReserved() {
		return isReserved;
	}
	public void setIsReserved(String isReserved) {
		this.isReserved = isReserved;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public String getRegionCode() {
		return regionCode;
	}
	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}
	private Integer stockLocked;
	private Integer safeLine;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public Integer getShelfLife() {
		return shelfLife;
	}
	public void setShelfLife(Integer shelfLife) {
		this.shelfLife = shelfLife;
	}
	public String getStorageType() {
		return storageType;
	}
	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}
	public Integer getUomId() {
		return uomId;
	}
	public void setUomId(Integer uomId) {
		this.uomId = uomId;
	}
	public Integer getBrandId() {
		return brandId;
	}
	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}
	public String getProducer() {
		return producer;
	}
	public void setProducer(String producer) {
		this.producer = producer;
	}
	public BigDecimal getCostPrice() {
		return costPrice;
	}
	public void setCostPrice(BigDecimal costPrice) {
		this.costPrice = costPrice;
	}
	public BigDecimal getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getMaxLimit() {
		return maxLimit;
	}
	public void setMaxLimit(Integer maxLimit) {
		this.maxLimit = maxLimit;
	}
	public Integer getSoldVolume() {
		return soldVolume;
	}
	public void setSoldVolume(Integer soldVolume) {
		this.soldVolume = soldVolume;
	}
	public BigDecimal getCmtPoint() {
		return cmtPoint;
	}
	public void setCmtPoint(BigDecimal cmtPoint) {
		this.cmtPoint = cmtPoint;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public List<Picture> getImages() {
		return images;
	}
	public void setImages(List<Picture> images) {
		this.images = images;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Integer getRegionId() {
		return regionId;
	}
	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}
	public Integer getStockSum() {
		return stockSum;
	}
	public void setStockSum(Integer stockSum) {
		this.stockSum = stockSum;
	}
	public Integer getStockLocked() {
		return stockLocked;
	}
	public void setStockLocked(Integer stockLocked) {
		this.stockLocked = stockLocked;
	}
	public Integer getSafeLine() {
		return safeLine;
	}
	public void setSafeLine(Integer safeLine) {
		this.safeLine = safeLine;
	}

}
