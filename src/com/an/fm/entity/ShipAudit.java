package com.an.fm.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ShipAudit {
    private Integer id = 0;

    private String orgCode;
    private String shipCode;
    private String billCode = "";
    private String shipment;
    private BigDecimal weight;
    private BigDecimal quantity = new BigDecimal(0);
    private BigDecimal amount = new BigDecimal(0);
    private String remark;
    private BigDecimal hadPaid = new BigDecimal(0);
    private String status;
    private String timestamp;
    private String billDate;
    private String shipDate;
    private String address;
    private BigDecimal shipFee;
    private BigDecimal audit1;
    private BigDecimal audit2;
    private BigDecimal audit3;
    private BigDecimal audit4;
    private String region;
    private BigDecimal dustFee;
    private BigDecimal addPrice;
    private BigDecimal basePrice;

    public void addBillCode(String billCode) {
        this.billCode += billCode;
    }

    public void addHadPaid(BigDecimal hadPaid) {
        this.hadPaid = this.hadPaid.add(hadPaid);
    }

    public BigDecimal getAudit3() {
        return audit3;
    }

    public void setAudit3(BigDecimal audit3) {
        this.audit3 = audit3.setScale(4, RoundingMode.HALF_UP);
    }

    public BigDecimal getAudit4() {
        return audit4;
    }

    public void setAudit4(BigDecimal audit4) {
        this.audit4 = audit4.setScale(4, RoundingMode.HALF_UP);
    }

    public void addAmount(BigDecimal amount) {
        this.amount = this.amount.add(amount);
    }

    public void addQuantity(BigDecimal qty) {
        if (qty != null)
            this.quantity = this.quantity.add(qty);
    }

    public BigDecimal getAudit1() {
        return audit1;
    }

    public void setAudit1(BigDecimal audit1) {
        if (audit1 != null)
            this.audit1 = audit1.setScale(4, RoundingMode.HALF_UP);
    }

    public BigDecimal getAudit2() {
        return audit2;
    }

    public void setAudit2(BigDecimal audit2) {
        if (audit2 != null)
            this.audit2 = audit2.setScale(4, RoundingMode.HALF_UP);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShipCode() {
        return shipCode;
    }

    public void setShipCode(String shipCode) {
        this.shipCode = shipCode;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getHadPaid() {
        return hadPaid;
    }

    public void setHadPaid(BigDecimal hadPaid) {
        this.hadPaid = hadPaid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getShipDate() {
        return shipDate;
    }

    public void setShipDate(String shipDate) {
        this.shipDate = shipDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getShipFee() {
        return shipFee;
    }

    public void setShipFee(BigDecimal shipFee) {
        this.shipFee = shipFee;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public BigDecimal getAddPrice() {
        return addPrice;
    }

    public void setAddPrice(BigDecimal addPrice) {
        this.addPrice = addPrice;
    }

    public String getShipment() {
        return shipment;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public BigDecimal getDustFee() {
        return dustFee;
    }

    public void setDustFee(BigDecimal dustFee) {
        this.dustFee = dustFee;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }
}