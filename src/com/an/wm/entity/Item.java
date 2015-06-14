package com.an.wm.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品(实例化商品,可数名词)
 *
 * @author Karas
 */
public class Item implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String pn;
	private String barcode;
	private String name;
	private String type;
	private Integer skuUom;
	private BigDecimal costPrice;
	private BigDecimal salePrice;
	private String supplier;
	private Integer storageType;
	private Integer shelfLife;
	private BigDecimal weight;
	private String remark;
	private String status = "t";
	private Integer enterBy;
	private Date enterAt;
	
	//用于商品货品映射
	private Integer goodsId;
	private BigDecimal combQuantity = BigDecimal.ONE;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPn() {
		return pn;
	}
	public void setPn(String pn) {
		this.pn = pn;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getSkuUom() {
		return skuUom;
	}
	public void setSkuUom(Integer skuUom) {
		this.skuUom = skuUom;
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
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public Integer getStorageType() {
		return storageType;
	}
	public void setStorageType(Integer storageType) {
		this.storageType = storageType;
	}
	public Integer getShelfLife() {
		return shelfLife;
	}
	public void setShelfLife(Integer shelfLife) {
		this.shelfLife = shelfLife;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
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
	public Date getEnterAt() {
		return enterAt;
	}
	public void setEnterAt(Date enterAt) {
		this.enterAt = enterAt;
	}
	public Integer getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}
	public BigDecimal getCombQuantity() {
		return combQuantity;
	}
	public void setCombQuantity(BigDecimal combQuantity) {
		this.combQuantity = combQuantity;
	}
	
}
