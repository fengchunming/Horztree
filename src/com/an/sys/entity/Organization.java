package com.an.sys.entity;

import com.an.base.entity.Address;
import com.an.core.model.TreeNode;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 组织 (抽象)
 *
 * @author Karas
 */
public class Organization extends TreeNode implements Serializable {
 
    private static final long serialVersionUID = 3306148969294392852L;

    private Integer id;
    private String orgCode;// 组织编号
    private String orgName; // 组积名称
    private String orgType; // 组织类型
    private boolean isOuter;
    private String shortName;
    private String status;
    private String remark;
    private Address addr;

    private String storeType;
    private int storeLevel;
    private String openDate;// 开始营业日
    private String closeDate;// 结束营业日

    private ArrayList<String> bookingDay;
    private ArrayList<String> receiptDay;

    private String openTime;// 开始营业时间
    private String closeTime;// 结束营业时间

    private String statusStartDate;
    private String statusEndDate;
    private String houseBank;
    private String houseBankAccount;
    private String latitude;
    private String longitude;

    private String token;
    private String syncStamp;
    private String secrect;
    private String type;
    private String partnerNo;

    private String lastEditor;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSecrect() {
        return secrect;
    }

    public void setSecrect(String secrect) {
        this.secrect = secrect;
    }

    public String getPartnerNo() {
        return partnerNo;
    }

    public void setPartnerNo(String partnerNo) {
        this.partnerNo = partnerNo;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isOuter() {
        return isOuter;
    }

    public void setOuter(boolean isOuter) {
        this.isOuter = isOuter;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public int getStoreLevel() {
        return storeLevel;
    }

    public void setStoreLevel(int storeLevel) {
        this.storeLevel = storeLevel;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public ArrayList<String> getBookingDay() {
        return bookingDay;
    }

    public void setBookingDay(ArrayList<String> bookingDay) {
        this.bookingDay = bookingDay;
    }

    public ArrayList<String> getReceiptDay() {
        return receiptDay;
    }

    public void setReceiptDay(ArrayList<String> receiptDay) {
        this.receiptDay = receiptDay;
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

    public String getStatusStartDate() {
        return statusStartDate;
    }

    public void setStatusStartDate(String statusStartDate) {
        this.statusStartDate = statusStartDate;
    }

    public String getStatusEndDate() {
        return statusEndDate;
    }

    public void setStatusEndDate(String statusEndDate) {
        this.statusEndDate = statusEndDate;
    }

    public String getHouseBank() {
        return houseBank;
    }

    public void setHouseBank(String houseBank) {
        this.houseBank = houseBank;
    }

    public String getHouseBankAccount() {
        return houseBankAccount;
    }

    public void setHouseBankAccount(String houseBankAccount) {
        this.houseBankAccount = houseBankAccount;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLastEditor() {
        return lastEditor;
    }

    public void setLastEditor(String lastEditor) {
        this.lastEditor = lastEditor;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSyncStamp() {
        return syncStamp;
    }

    public void setSyncStamp(String syncStamp) {
        this.syncStamp = syncStamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Address getAddr() {
        return addr;
    }

    public void setAddr(Address addr) {
        this.addr = addr;
    }
}
