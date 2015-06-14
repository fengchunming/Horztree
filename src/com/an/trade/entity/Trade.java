package com.an.trade.entity;

import com.an.base.entity.Address;
import com.an.crm.entity.Member;
import com.an.utils.JSONDateSerializer;
import com.an.utils.JSONDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Trade {

	private Integer id;
	private String billCode;
	private String billType;
	private String originBillCode;
	private Member member;
	private Integer memberId;
	private String memberName;
	private String target;
	private Date billDate;
	private String shipment;
	private Address addr;
	private String shipName;
	private String shipRegion;
	private String shipTel;
	private String shipMobile;
	private String shipEmail;
	private String shipAddr;
	private BigDecimal shipCost;
	private BigDecimal shipTotal;
	private String shipCode;
	private BigDecimal amount;
	private BigDecimal quantity;
	private BigDecimal weight;
	private BigDecimal itemTotal;
	private String discountObj;
	private BigDecimal discountTotal;
	private String status = "0";
	private String dealStatus;
	private Date dealStamp;
	private Integer groupId;
	private String warehouse;
	private String payment;
	private BigDecimal paidTotal;
	private String paidCode;
	private Date paidStamp;
	private String payStatus;
	private String remark;
	private String comment;
	private Integer saleBy;
	private Integer enterBy;
	private Date planDate;
    private Date enterAt;
	private String usedCoupon;
	private Integer regionId;
	private String regionCode;
	
	private List<TradeBillDetail> items = new ArrayList<>();
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
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getOriginBillCode() {
		return originBillCode;
	}
	public void setOriginBillCode(String originBillCode) {
		this.originBillCode = originBillCode;
	}
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public Integer getMemberId() {
		return memberId;
	}
	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
    @JsonSerialize(using = JSONDateSerializer.class)
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}
	public String getShipment() {
		return shipment;
	}
	public void setShipment(String shipment) {
		this.shipment = shipment;
	}
	public Address getAddr() {
		return addr;
	}
	public void setAddr(Address addr) {
		this.addr = addr;
	}
	public String getShipName() {
		return shipName;
	}
	public void setShipName(String shipName) {
		this.shipName = shipName;
	}
	public String getShipRegion() {
		return shipRegion;
	}
	public void setShipRegion(String shipRegion) {
		this.shipRegion = shipRegion;
	}
	public String getShipTel() {
		return shipTel;
	}
	public void setShipTel(String shipTel) {
		this.shipTel = shipTel;
	}
	public String getShipMobile() {
		return shipMobile;
	}
	public void setShipMobile(String shipMobile) {
		this.shipMobile = shipMobile;
	}
	public String getShipEmail() {
		return shipEmail;
	}
	public void setShipEmail(String shipEmail) {
		this.shipEmail = shipEmail;
	}
	public String getShipAddr() {
		return shipAddr;
	}
	public void setShipAddr(String shipAddr) {
		this.shipAddr = shipAddr;
	}
	public BigDecimal getShipCost() {
		return shipCost;
	}
	public void setShipCost(BigDecimal shipCost) {
		this.shipCost = shipCost;
	}
	public BigDecimal getShipTotal() {
		return shipTotal;
	}
	public void setShipTotal(BigDecimal shipTotal) {
		this.shipTotal = shipTotal;
	}
	public String getShipCode() {
		return shipCode;
	}
	public void setShipCode(String shipCode) {
		this.shipCode = shipCode;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getQuantity() {
		return quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public BigDecimal getItemTotal() {
		return itemTotal;
	}
	public void setItemTotal(BigDecimal itemTotal) {
		this.itemTotal = itemTotal;
	}
	public String getDiscountObj() {
		return discountObj;
	}
	public void setDiscountObj(String discountObj) {
		this.discountObj = discountObj;
	}
	public BigDecimal getDiscountTotal() {
		return discountTotal;
	}
	public void setDiscountTotal(BigDecimal discountTotal) {
		this.discountTotal = discountTotal;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}
    @JsonSerialize(using = JSONDateTimeSerializer.class)
	public Date getDealStamp() {
		return dealStamp;
	}
	public void setDealStamp(Date dealStamp) {
		this.dealStamp = dealStamp;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public BigDecimal getPaidTotal() {
		return paidTotal;
	}
	public void setPaidTotal(BigDecimal paidTotal) {
		this.paidTotal = paidTotal;
	}
	public String getPaidCode() {
		return paidCode;
	}
	public void setPaidCode(String paidCode) {
		this.paidCode = paidCode;
	}
    @JsonSerialize(using = JSONDateTimeSerializer.class)
	public Date getPaidStamp() {
		return paidStamp;
	}
	public void setPaidStamp(Date paidStamp) {
		this.paidStamp = paidStamp;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getSaleBy() {
		return saleBy;
	}
	public void setSaleBy(Integer saleBy) {
		this.saleBy = saleBy;
	}
	public Integer getEnterBy() {
		return enterBy;
	}
	public void setEnterBy(Integer enterBy) {
		this.enterBy = enterBy;
	}
    @JsonSerialize(using = JSONDateSerializer.class)
	public Date getPlanDate() {
		return planDate;
	}
	public void setPlanDate(Date planDate) {
		this.planDate = planDate;
	}
    @JsonSerialize(using = JSONDateSerializer.class)
    public Date getEnterAt() {
        return enterAt;
    }
    public void setEnterAt(Date enterAt) {
        this.enterAt = enterAt;
    }
	public String getUsedCoupon() {
		return usedCoupon;
	}
	public void setUsedCoupon(String usedCoupon) {
		this.usedCoupon = usedCoupon;
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
	public List<TradeBillDetail> getItems() {
		return items;
	}
	public void setItems(List<TradeBillDetail> items) {
		this.items = items;
	}

}
