package com.an.sys.entity;

import java.io.Serializable;

/**
 * 职位
 *
 * @author Karas
 */
public class Position implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1040306029879217585L;
    private Integer id = 0;
    private String positionCode;
    private String positionName;
    private String shortName;
    private String status;
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPositionCode() {
        return positionCode;
    }

    public void setPositionCode(String positionCode) {
        this.positionCode = positionCode;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
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

}
