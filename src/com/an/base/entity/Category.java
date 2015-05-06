package com.an.base.entity;

import com.an.core.model.TreeNode;

import java.util.Date;

/**
 * 商品分类
 *
 * @author Karas
 */
public class Category extends TreeNode {
    private String code;
    private String barcode;
    private String name;
    private String shortName;
    private String remark;
    private String status;
    private Date enterDt;
    private Integer enterBy;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getEnterDt() {
        return enterDt;
    }

    public void setEnterDt(Date enterDt) {
        this.enterDt = enterDt;
    }

    public Integer getEnterBy() {
        return enterBy;
    }

    public void setEnterBy(Integer enterBy) {
        this.enterBy = enterBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
