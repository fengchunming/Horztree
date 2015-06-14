package com.an.trade.entity;

import java.util.Date;

public class TradeBillDetail {
	private Integer id;
	private String billCode;
	private Integer billId;
	private Date billDate;
	private Integer groupId;
	private Integer goodsId;
	private String goodsName;
	private Integer uom;
	private Float quantity;
	private Float subTotal;
	private Float weight;
	private String status;
	private Date enterAt;
	private Float salePrice;
	private String category;
	private Float packQuantity;
	private Float realQuantity;
	private String barcode;
	private Integer owner;
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
	public Integer getBillId() {
		return billId;
	}
	public void setBillId(Integer billId) {
		this.billId = billId;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public Integer getUom() {
		return uom;
	}
	public void setUom(Integer uom) {
		this.uom = uom;
	}
	public Float getQuantity() {
		return quantity;
	}
	public void setQuantity(Float quantity) {
		this.quantity = quantity;
	}
	public Float getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(Float subTotal) {
		this.subTotal = subTotal;
	}
	public Float getWeight() {
		return weight;
	}
	public void setWeight(Float weight) {
		this.weight = weight;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getEnterAt() {
		return enterAt;
	}
	public void setEnterAt(Date enterAt) {
		this.enterAt = enterAt;
	}
	public Float getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Float salePrice) {
		this.salePrice = salePrice;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Float getPackQuantity() {
		return packQuantity;
	}
	public void setPackQuantity(Float packQuantity) {
		this.packQuantity = packQuantity;
	}
	public Float getRealQuantity() {
		return realQuantity;
	}
	public void setRealQuantity(Float realQuantity) {
		this.realQuantity = realQuantity;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Integer getOwner() {
		return owner;
	}
	public void setOwner(Integer owner) {
		this.owner = owner;
	}

}
