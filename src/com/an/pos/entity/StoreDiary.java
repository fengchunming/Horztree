package com.an.pos.entity;

import java.math.BigDecimal;

public class StoreDiary {
    private Integer id;
    private String visitDate;
    private String orgCode;
    private String terminalCode;
    private String openTime;
    private String closeTime;
    private BigDecimal openBalance;
    private BigDecimal closeBalance;
    private BigDecimal dailyRevenue;
    private BigDecimal pettyCashUse;
    private BigDecimal escortAmount;
    private String remark;
    private String timestamp;
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public BigDecimal getOpenBalance() {
        return openBalance;
    }

    public void setOpenBalance(BigDecimal openBalance) {
        this.openBalance = openBalance;
    }

    public BigDecimal getCloseBalance() {
        return closeBalance;
    }

    public void setCloseBalance(BigDecimal closeBalance) {
        this.closeBalance = closeBalance;
    }

    public BigDecimal getDailyRevenue() {
        return dailyRevenue;
    }

    public void setDailyRevenue(BigDecimal dailyRevenue) {
        this.dailyRevenue = dailyRevenue;
    }

    public BigDecimal getPettyCashUse() {
        return pettyCashUse;
    }

    public void setPettyCashUse(BigDecimal pettyCashUse) {
        this.pettyCashUse = pettyCashUse;
    }

    public BigDecimal getEscortAmount() {
        return escortAmount;
    }

    public void setEscortAmount(BigDecimal escortAmount) {
        this.escortAmount = escortAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}