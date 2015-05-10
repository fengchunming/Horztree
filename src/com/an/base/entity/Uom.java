package com.an.base.entity;

import java.io.Serializable;

public class Uom implements Serializable {

	private static final long serialVersionUID = 1L;

	public enum UomType {
        QTY, VOL, WGT
    }

    private Integer id;
    private String code;
    private String name;
    private String abbr;
    private UomType type;
    private String symbol;
    private String remark;
    private String status;

    public Uom() {

    }

    public Uom(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public UomType getType() {
        return type;
    }

    public void setType(UomType type) {
        this.type = type;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean equals(Uom uom) {
        return this.code.equalsIgnoreCase(uom.getCode());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}