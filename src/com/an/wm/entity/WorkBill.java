package com.an.wm.entity;

import com.an.base.entity.Bill;
import com.an.sys.entity.Organization;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class WorkBill extends Bill {

    private Organization target;
    private Organization belong;
    private Location from;
    private Location to;
    private String shipment;
    private String shipCode;
    private BigDecimal shipCost;
    private String batchCode;
    private Integer printStatus = 0;
    private String realTime;

    private List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        items.add(item);
    }

    public String getShipment() {
        return shipment;
    }

    public void setShipment(String shipment) {
        this.shipment = shipment;
    }

    public String getShipCode() {
        return shipCode;
    }

    public void setShipCode(String shipCode) {
        this.shipCode = shipCode;
    }

    public Organization getTarget() {
        return target;
    }

    public void setTarget(Organization target) {
        this.target = target;
    }


    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public Integer getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(Integer printStatus) {
        this.printStatus = printStatus;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public BigDecimal getShipCost() {
        return shipCost;
    }

    public void setShipCost(BigDecimal shipCost) {
        this.shipCost = shipCost;
    }

    public String getRealTime() {
        return realTime;
    }

    public void setRealTime(String realTime) {
        this.realTime = realTime;
    }


    public Organization getBelong() {
        return belong;
    }

    public void setBelong(Organization belong) {
        this.belong = belong;
    }

    public Location getFrom() {
        return from;
    }

    public void setFrom(Location from) {
        this.from = from;
    }

    public Location getTo() {
        return to;
    }

    public void setTo(Location to) {
        this.to = to;
    }
}
