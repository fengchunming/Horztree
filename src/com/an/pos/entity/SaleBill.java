package com.an.pos.entity;

import com.an.base.entity.Bill;

import java.math.BigDecimal;

public class SaleBill extends Bill {
    private String terminalCode = ""; //
    private Integer memberId;
    private String printNo = ""; //
    private boolean isInvoice;
    private BigDecimal refundAmount; //
    private BigDecimal cancelAmount; //
    private BigDecimal discountAmount;
    private String currencyCode;
    private BigDecimal payAble;
    private BigDecimal amount;
    private String saleStamp; //
    private Integer lineCount; //
    private BigDecimal quantity; //
    private BigDecimal weight; //
    private String cashierCode = ""; //
    private String refundReasonCode = ""; //
    private String payTypeCode;
    private BigDecimal costTotal;

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getPrintNo() {
        return printNo;
    }

    public void setPrintNo(String printNo) {
        this.printNo = printNo;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getPayAble() {
        return payAble;
    }

    public void setPayAble(BigDecimal payAble) {
        this.payAble = payAble;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public BigDecimal getCancelAmount() {
        return cancelAmount;
    }

    public void setCancelAmount(BigDecimal cancelAmount) {
        this.cancelAmount = cancelAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getSaleStamp() {
        return saleStamp;
    }

    public void setSaleStamp(String saleStamp) {
        this.saleStamp = saleStamp;
    }

    public Integer getLineCount() {
        return lineCount;
    }

    public void setLineCount(Integer lineCount) {
        this.lineCount = lineCount;
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

    public String getCashierCode() {
        return cashierCode;
    }

    public void setCashierCode(String cashierCode) {
        this.cashierCode = cashierCode;
    }

    public String getRefundReasonCode() {
        return refundReasonCode;
    }

    public void setRefundReasonCode(String refundReasonCode) {
        this.refundReasonCode = refundReasonCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayTypeCode() {
        return payTypeCode;
    }

    public void setPayTypeCode(String payTypeCode) {
        this.payTypeCode = payTypeCode;
    }

    public BigDecimal getCostTotal() {
        return costTotal;
    }

    public void setCostTotal(BigDecimal costTotal) {
        this.costTotal = costTotal;
    }

    public boolean isInvoice() {
        return isInvoice;
    }

    public void setInvoice(boolean isInvoice) {
        this.isInvoice = isInvoice;
    }

}
