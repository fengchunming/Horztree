package com.an.trade.entity;

import com.an.base.entity.Address;
import com.an.base.entity.Bill;
import com.an.crm.entity.Coupon;
import com.an.crm.entity.Member;
import com.an.utils.JSONDateSerializer;
import com.an.utils.JSONDateTimeSerializer;
import com.an.wm.entity.Item;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Trade extends Bill{
    private Integer id;
    private String outCode;

    private String billCode;
    private String billType;
    private Date billDate;

    private String status = "0";
    private String dealStatus;
    private String remark;

    private Member member;
    private Integer enterBy;
    private Integer saleBy;
    private Integer groupId;
    private Date dealStamp;

    private BigDecimal quantity;
    private BigDecimal weight;

    private BigDecimal shipCost;
    private String shipCode;
    private String shipment;
    private Address addr;
    private Coupon coupon;

    private BigDecimal shipTotal;
    private BigDecimal discountTotal;
    private BigDecimal amount;
    private BigDecimal paidTotal;
    private BigDecimal itemTotal;

    private String payment;
    private String paidCode;
    private Date paidStamp;
    private String payStatus;

    private String invoiceTitle;
    private int printStatus = 0;
    private String comment;

    private List<Item> items = new ArrayList<>();


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


    @JsonSerialize(using = JSONDateSerializer.class)
    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Integer getEnterBy() {
        return enterBy;
    }

    public void setEnterBy(Integer enterBy) {
        this.enterBy = enterBy;
    }

    public Integer getSaleBy() {
        return saleBy;
    }

    public void setSaleBy(Integer saleBy) {
        this.saleBy = saleBy;
    }


    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    @JsonSerialize(using = JSONDateTimeSerializer.class)
    public Date getDealStamp() {
        return dealStamp;
    }

    public void setDealStamp(Date dealStamp) {
        this.dealStamp = dealStamp;
    }

    public BigDecimal getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(BigDecimal itemTotal) {
        this.itemTotal = itemTotal;
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


    public BigDecimal getShipCost() {
        return shipCost;
    }

    public void setShipCost(BigDecimal shipCost) {
        this.shipCost = shipCost;
    }

    public String getShipCode() {
        return shipCode;
    }

    public void setShipCode(String shipCode) {
        this.shipCode = shipCode;
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

    public BigDecimal getShipTotal() {
        return shipTotal;
    }

    public void setShipTotal(BigDecimal shipTotal) {
        this.shipTotal = shipTotal;
    }

    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(BigDecimal discountTotal) {
        this.discountTotal = discountTotal;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPaidTotal() {
        return paidTotal;
    }

    public void setPaidTotal(BigDecimal paidTotal) {
        this.paidTotal = paidTotal;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
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

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public int getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(int printStatus) {
        this.printStatus = printStatus;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public String getOutCode() {
        return outCode;
    }

    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }
}
