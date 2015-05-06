package com.an.pos.entity;

import com.an.base.entity.Bill;

public class StockChangeBill extends Bill {
    private String terminalCode;
    private String staffCode;

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }
}
