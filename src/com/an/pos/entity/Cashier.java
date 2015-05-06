package com.an.pos.entity;

import com.an.sys.entity.User;

public class Cashier extends User {

    /**
     *
     */
    private static final long serialVersionUID = 8010136231803458983L;
    private String cashierType = "3";
    // Operation level (0:Super 1: Admin 2:Setting 3:Cashier)
    private String orgCode;
    private String canAmountDiscount = "enabled"; // 金额折扣
    private String canPercentageDiscount = "enabled"; // 比例折扣
    private String canVoid = "enabled"; // 免单
    private String canRefund = "enabled"; // 退货
    private String canTender = "disabled"; // 支付方式操作
    private String canCancelTransaction = "enabled"; // 取消交易

    public String getorgCode() {
        return orgCode;
    }

    public void setorgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getCashierType() {
        return cashierType;
    }

    public void setCashierType(String cashierType) {
        this.cashierType = cashierType;
    }

    public String getCanAmountDiscount() {
        return canAmountDiscount;
    }

    public void setCanAmountDiscount(String canAmountDiscount) {
        this.canAmountDiscount = canAmountDiscount;
    }

    public String getCanPercentageDiscount() {
        return canPercentageDiscount;
    }

    public void setCanPercentageDiscount(String canPercentageDiscount) {
        this.canPercentageDiscount = canPercentageDiscount;
    }

    public String getCanVoid() {
        return canVoid;
    }

    public void setCanVoid(String canVoid) {
        this.canVoid = canVoid;
    }

    public String getCanRefund() {
        return canRefund;
    }

    public void setCanRefund(String canRefund) {
        this.canRefund = canRefund;
    }

    public String getCanTender() {
        return canTender;
    }

    public void setCanTender(String canTender) {
        this.canTender = canTender;
    }

    public String getCanCancelTransaction() {
        return canCancelTransaction;
    }

    public void setCanCancelTransaction(String canCancelTransaction) {
        this.canCancelTransaction = canCancelTransaction;
    }

}
