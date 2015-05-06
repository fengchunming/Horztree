package com.an.wm.entity;

import java.math.BigDecimal;

public class LocationSet {
    private Integer id = 0;
    private String location;
    private String locationBarcode;
    private String warehouse;
    private String section;
    private String goodsCode;
    private String goodsBarcode;
    private BigDecimal stockBaseline;
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationBarcode() {
        return locationBarcode;
    }

    public void setLocationBarcode(String locationBarcode) {
        this.locationBarcode = locationBarcode;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsBarcode() {
        return goodsBarcode;
    }

    public void setGoodsBarcode(String goodsBarcode) {
        this.goodsBarcode = goodsBarcode;
    }

    public BigDecimal getStockBaseline() {
        return stockBaseline;
    }

    public void setStockBaseline(BigDecimal stockBaseline) {
        this.stockBaseline = stockBaseline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
