package com.an.mm.entity;

import com.an.sys.entity.Organization;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品主数据
 *
 * @author Karas
 */
public class Goods implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer goodsId;
    private String pn;// 商品编码
    private List<Picture> images;
    private String description;
    private String barcode; // 商品条码
    private String name; // 商品名称
    private String goodsType; // 成品，半成品，原料
    private String title;// 简称
    private String tips;

    private List<Integer> category; // 分类（小类）
    private Integer shelfLife; // 保质期
    private Integer brand; // 品牌

    private Organization belongTo;

    private String unit;// 基本单位

    private BigDecimal standPrice; // 基准销价
    private BigDecimal salePrice; // 售价
    private BigDecimal marketPrice; // 市场价格

    private List<Spec> specs;
    private BigDecimal quantity;
    private Integer groupId;
    private Integer maxLimit;
    private BigDecimal weight;
    private Integer soldVolume;
    private Integer sort;
    private String remark; // 备注
    private String status; // 状态
    private String enterAt;



    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public List<Picture> getImages() {
        return images;
    }

    public void setImages(List<Picture> images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getCategory() {
        return category;
    }

    public void setCategory(List<Integer> category) {
        this.category = category;
    }

    public Integer getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Integer shelfLife) {
        this.shelfLife = shelfLife;
    }

    public Integer getBrand() {
        return brand;
    }

    public void setBrand(Integer brand) {
        this.brand = brand;
    }

    public Organization getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(Organization belongTo) {
        this.belongTo = belongTo;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getStandPrice() {
        return standPrice;
    }

    public void setStandPrice(BigDecimal standPrice) {
        this.standPrice = standPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
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

    public String getEnterAt() {
        return enterAt;
    }

    public void setEnterAt(String enterAt) {
        this.enterAt = enterAt;
    }


    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public List<Spec> getSpecs() {
        return specs;
    }

    public void setSpecs(List<Spec> specs) {
        this.specs = specs;
    }

    public Integer getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(Integer maxLimit) {
        this.maxLimit = maxLimit;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getSoldVolume() {
        return soldVolume;
    }

    public void setSoldVolume(Integer soldVolume) {
        this.soldVolume = soldVolume;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
