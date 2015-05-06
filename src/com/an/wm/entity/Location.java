package com.an.wm.entity;


import java.util.Date;

public class Location {
    private Integer id;

    private Integer warehouse;
    private String barcode;
    private String sectionCode;
    private String sectionType;

    private String locationCode;
    private String locationType;

    private String pallet; // 托盘码

    private String status;

    private String useStatus;
    private String storageType; // 存储类型
    private String abcType; // 周转率类型
    private String usageType; // 用途类型

    private String remark;

    private boolean moveAble;

    // 以下为对存放货物的限制
    private Float volume = 0.0f;
    private Integer weight = 0;
    private Integer quantity = 0;

    private Integer parent;
    private String enterBy;

    public Location(){

    }
    public Location(Integer warehouse) {
        this.warehouse = warehouse;
    }

    public Date getEnterDt() {
        return enterDt;
    }

    public void setEnterDt(Date enterDt) {
        this.enterDt = enterDt;
    }

    public String getEnterBy() {
        return enterBy;
    }

    public void setEnterBy(String enterBy) {
        this.enterBy = enterBy;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    private Date enterDt;

    public String getAbcType() {
        return abcType;
    }

    public String getBarcode() {
        return barcode;
    }


    public Integer getId() {
        return id;
    }


    public String getRemark() {
        return remark;
    }

    public String getStatus() {
        return status;
    }

    public String getStorageType() {
        return storageType;
    }


    public String getUsageType() {
        return usageType;
    }

    public String getUseStatus() {
        return useStatus;
    }

    public Float getVolume() {
        return volume;
    }


    public double getWeight() {
        return weight;
    }

    public void setAbcType(String abcType) {
        this.abcType = abcType;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public void setUseStatus(String useStatus) {
        this.useStatus = useStatus;
    }

    public void setVolume(Float volume) {
        this.volume = volume;
    }


    public void setWeight(Integer weight) {
        this.weight = weight;
    }


    public String getSectionCode() {
        return sectionCode;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public boolean isMoveAble() {
        return moveAble;
    }

    public void setMoveAble(boolean moveAble) {
        this.moveAble = moveAble;
    }

    public String getSectionType() {
        return sectionType;
    }

    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Integer warehouse) {
        this.warehouse = warehouse;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getPallet() {
        return pallet;
    }

    public void setPallet(String pallet) {
        this.pallet = pallet;
    }
}
