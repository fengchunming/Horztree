package com.an.pos.entity;

import com.an.wm.entity.Item;

public class StockChange extends Item {
    private String terminalCode;
    private String businessDate;
    private String logStamp;
    private String cashierCode;
    private String cashierName;
    private String inboundno;
    private String clientSeqNo;
    private String reasonCode;
    private String reasonName;

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    public String getLogStamp() {
        return logStamp;
    }

    public void setLogStamp(String logStamp) {
        this.logStamp = logStamp;
    }

    public String getCashierCode() {
        return cashierCode;
    }

    public void setCashierCode(String cashierCode) {
        this.cashierCode = cashierCode;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
    }

    public String getInboundno() {
        return inboundno;
    }

    public void setInboundno(String inboundno) {
        this.inboundno = inboundno;
    }

    public String getClientSeqNo() {
        return clientSeqNo;
    }

    public void setClientSeqNo(String clientSeqNo) {
        this.clientSeqNo = clientSeqNo;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonName() {
        return reasonName;
    }

    public void setReasonName(String reasonName) {
        this.reasonName = reasonName;
    }

}
