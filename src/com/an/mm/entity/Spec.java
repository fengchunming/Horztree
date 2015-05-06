package com.an.mm.entity;

import com.an.utils.JSONDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
import java.util.List;

/**
 * 商品规格
 * Created by karas on 1/2/15.
 */
public class Spec {
    private Integer id;
    private String name;
    private Integer quantity;
    private String remark;
    private String status;
    private List attrs;
    private String type;
    private Integer enterBy;
    private Date enterAt;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getEnterBy() {
        return enterBy;
    }

    public void setEnterBy(Integer enterBy) {
        this.enterBy = enterBy;
    }

    @JsonSerialize(using = JSONDateSerializer.class)
    public Date getEnterAt() {
        return enterAt;
    }

    public void setEnterAt(Date enterAt) {
        this.enterAt = enterAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List getAttrs() {
        return attrs;
    }

    public void setAttrs(List attrs) {
        this.attrs = attrs;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
