package com.an.wm.entity;

import com.an.base.entity.Uom;

import java.math.BigDecimal;

/**
 * 商品单位
 *
 * @author Karas
 */

public class MaterialUom extends Uom {

    private Integer id;
    private String pn;
    private String barcode;
    private BigDecimal packQuantity = new BigDecimal(1); // 数量(SKU数量）
    private BigDecimal weight; // 重量
    private BigDecimal height; // 体积
    private BigDecimal length; // 皮数量
    private BigDecimal width; // 皮重
    private boolean isPrintDefault; // 打印默认
    private boolean isReceiptDefault; // 收货默认
    private boolean isIssueDefault; // 发货默认
    private boolean isSaleDefault; // 销售默认


    public BigDecimal getPackQuantity() {
        return packQuantity;
    }

    public void setPackQuantity(BigDecimal packQuantity) {
        this.packQuantity = packQuantity;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public boolean isPrintDefault() {
        return isPrintDefault;
    }

    public void setPrintDefault(boolean isPrintDefault) {
        this.isPrintDefault = isPrintDefault;
    }

    public boolean isReceiptDefault() {
        return isReceiptDefault;
    }

    public void setReceiptDefault(boolean isReceiptDefault) {
        this.isReceiptDefault = isReceiptDefault;
    }

    public boolean isIssueDefault() {
        return isIssueDefault;
    }

    public void setIssueDefault(boolean isIssueDefault) {
        this.isIssueDefault = isIssueDefault;
    }

    public boolean isSaleDefault() {
        return isSaleDefault;
    }

    public void setSaleDefault(boolean isSaleDefault) {
        this.isSaleDefault = isSaleDefault;
    }

    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

}
