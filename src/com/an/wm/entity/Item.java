package com.an.wm.entity;

import com.an.fm.entity.Tax;
import com.an.sys.entity.Organization;
import com.an.utils.JSONDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 商品(实例化商品,可数名词)
 *
 * @author Karas
 */
public class Item implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer itemId;
    private Integer goodsId;
    private String type;
    private String pn;
    private String name;
    private String shortName;
    private String barcode;
    private Integer category;
    private MaterialUom uom; // 度量单位
    private MaterialUom skuUom;

    private BigDecimal lossRate; // 损耗率
    private BigDecimal weight; // 重量
    private BigDecimal volume; // 体积

    private Integer shelfLife;
    private Integer remainLife;

    private Organization partyA;
    private Organization partyB;
    private Organization supplier;
    private Organization belong;

    private Integer groupId;

    private BigDecimal planQuantity;
    private BigDecimal realQuantity;
    private BigDecimal dealQuantity; // 数量
    private BigDecimal adjustQuantity;

    private WorkBill bill;
    private WorkBill origin;
    private Integer billId;
    private Integer stockId;
    private String batchCode;
    private Map<String, String> batchAttr;

    private Date productDate; // 生产日期
    private Date receiptDate;

    private Date planDate;
    private Date actualDate;

    private BigDecimal costPrice;
    private BigDecimal salePrice;
    private BigDecimal avgPrice;

    private BigDecimal saleTotal; // 小计
    private BigDecimal costTotal;

    private Integer originId;
    private Integer lockId = 0;
    private String originNo;

    private Tax inputTax;
    private Tax outputTax;

    private Location location;
    private Location from;
    private Location target;

    private String usageType; // 用途属性
    private String storageType; // 存储属性
    private Integer turnover; // 周转率


    private BigDecimal safeLine;

    private BigDecimal summary = new BigDecimal(0);
    private BigDecimal lockedSummary = new BigDecimal(0);
    private BigDecimal freezeSummary = new BigDecimal(0);
    private BigDecimal trashSummary = new BigDecimal(0);
    private BigDecimal transitSummary = new BigDecimal(0);
    private BigDecimal originSummary = new BigDecimal(0);

    private boolean canSale = false;
    private boolean canBooking = false;


    private String status;
    private String remark;
    private Date enterAt;


    public Object clone() {
        Item o = null;
        try {
            o = (Item) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }

    public MaterialUom getSkuUom() {
        return skuUom;
    }

    public void setSkuUom(MaterialUom skuUom) {
        this.skuUom = skuUom;
    }

    @JsonSerialize(using = JSONDateSerializer.class)
    public Date getProductDate() {
        return productDate;
    }

    public void setProductDate(Date productDate) {
        this.productDate = productDate;
    }

    @JsonSerialize(using = JSONDateSerializer.class)
    public Date getActualDate() {
        return actualDate;
    }

    public void setActualDate(Date actualDate) {
        this.actualDate = actualDate;
    }

    public BigDecimal getSafeLine() {
        return safeLine;
    }

    public void setSafeLine(BigDecimal safeLine) {
        this.safeLine = safeLine;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getEnterAt() {
        return enterAt;
    }

    public void setEnterAt(Date enterAt) {
        this.enterAt = enterAt;
    }

    public BigDecimal getCostTotal() {
        return costTotal;
    }

    public MaterialUom getUom() {
        return uom;
    }

    public Integer getId() {
        return id;
    }

    public Integer getLockId() {
        return lockId;
    }

    public BigDecimal getPlanQuantity() {
        return planQuantity;
    }


    public BigDecimal getRealQuantity() {
        return realQuantity;
    }

    public BigDecimal getSaleTotal() {
        return saleTotal;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }


    public BigDecimal getVolume() {
        return volume;
    }

    public BigDecimal getWeight() {
        return weight;
    }


    public void setCostTotal(BigDecimal costTotal) {
        this.costTotal = costTotal;
    }

    public void setUom(MaterialUom uom) {
        this.uom = uom;
    }

    public void setId(Integer itemId) {
        this.id = itemId;
    }

    public void setLockId(Integer lockId) {
        this.lockId = lockId;
    }

    public void setPlanQuantity(BigDecimal planQuantity) {
        this.planQuantity = planQuantity;
    }

    public BigDecimal getDealQuantity() {
        return dealQuantity;
    }

    public void setDealQuantity(BigDecimal dealQuantity) {
        this.dealQuantity = dealQuantity;
    }

    @JsonSerialize(using = JSONDateSerializer.class)
    public Date getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        this.receiptDate = receiptDate;
    }

    @JsonSerialize(using = JSONDateSerializer.class)
    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    public void setRealQuantity(BigDecimal realQuantity) {
        this.realQuantity = realQuantity;
    }

    public void setSaleTotal(BigDecimal saleTotal) {
        this.saleTotal = saleTotal;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getOriginId() {
        return originId;
    }

    public void setOriginId(Integer originId) {
        this.originId = originId;
    }

    public BigDecimal getAdjustQuantity() {
        return adjustQuantity;
    }

    public void setAdjustQuantity(BigDecimal adjustQuantity) {
        this.adjustQuantity = adjustQuantity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public Integer getStockId() {
        return stockId;
    }

    public void setStockId(Integer stockId) {
        this.stockId = stockId;
    }

    public String getUsageType() {
        return usageType;
    }

    public void setUsageType(String usageType) {
        this.usageType = usageType;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public Location getTarget() {
        return target;
    }

    public void setTarget(Location target) {
        this.target = target;
    }

    public boolean isCanSale() {
        return canSale;
    }

    public void setCanSale(boolean canSale) {
        this.canSale = canSale;
    }

    public boolean isCanBooking() {
        return canBooking;
    }

    public void setCanBooking(boolean canBooking) {
        this.canBooking = canBooking;
    }


    public String getOriginNo() {
        return originNo;
    }

    public void setOriginNo(String originNo) {
        this.originNo = originNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPn() {
        return pn;
    }

    public void setPn(String pn) {
        this.pn = pn;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }


    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(BigDecimal avgPrice) {
        this.avgPrice = avgPrice;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public BigDecimal getSummary() {
        return summary;
    }

    public void setSummary(BigDecimal summary) {
        this.summary = summary;
    }

    public BigDecimal getLockedSummary() {
        return lockedSummary;
    }

    public void setLockedSummary(BigDecimal lockedSummary) {
        this.lockedSummary = lockedSummary;
    }

    public BigDecimal getFreezeSummary() {
        return freezeSummary;
    }

    public void setFreezeSummary(BigDecimal freezeSummary) {
        this.freezeSummary = freezeSummary;
    }

    public BigDecimal getTransitSummary() {
        return transitSummary;
    }

    public void setTransitSummary(BigDecimal transitSummary) {
        this.transitSummary = transitSummary;
    }

    public BigDecimal getOriginSummary() {
        return originSummary;
    }

    public void setOriginSummary(BigDecimal originSummary) {
        this.originSummary = originSummary;
    }

    public BigDecimal getTrashSummary() {
        return trashSummary;
    }

    public void setTrashSummary(BigDecimal trashSummary) {
        this.trashSummary = trashSummary;
    }

    public Map getBatchAttr() {
        return batchAttr;
    }

    public void setBatchAttr(Map batchAttr) {
        this.batchAttr = batchAttr;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Tax getOutputTax() {
        return outputTax;
    }

    public void setOutputTax(Tax outputTax) {
        this.outputTax = outputTax;
    }

    public Tax getInputTax() {
        return inputTax;
    }

    public void setInputTax(Tax inputTax) {
        this.inputTax = inputTax;
    }

    public BigDecimal getLossRate() {
        return lossRate;
    }

    public void setLossRate(BigDecimal lossRate) {
        this.lossRate = lossRate;
    }

    public WorkBill getBill() {
        return bill;
    }

    public void setBill(WorkBill bill) {
        this.bill = bill;
    }

    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Organization getPartyA() {
        return partyA;
    }

    public void setPartyA(Organization partyA) {
        this.partyA = partyA;
    }

    public Organization getPartyB() {
        return partyB;
    }

    public void setPartyB(Organization partyB) {
        this.partyB = partyB;
    }

    public Organization getSupplier() {
        return supplier;
    }

    public void setSupplier(Organization supplier) {
        this.supplier = supplier;
    }

    public Integer getTurnover() {
        return turnover;
    }

    public void setTurnover(Integer turnover) {
        this.turnover = turnover;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Integer shelfLife) {
        this.shelfLife = shelfLife;
    }

    public Integer getRemainLife() {
        return remainLife;
    }

    public void setRemainLife(Integer remainLife) {
        this.remainLife = remainLife;
    }

    public Organization getBelong() {
        return belong;
    }

    public void setBelong(Organization belong) {
        this.belong = belong;
    }

    public WorkBill getOrigin() {
        return origin;
    }

    public void setOrigin(WorkBill origin) {
        this.origin = origin;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getBillId() {
        return billId;
    }

    public void setBillId(Integer billId) {
        this.billId = billId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }
}
