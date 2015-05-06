package com.an.crm.entity;

import com.an.trade.entity.Trade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 条件
 * Created by karas on 1/4/15.
 */
public class Clause {
    private List<Integer> items = new ArrayList<>();
    private BigDecimal amount = new BigDecimal(0);
    private BigDecimal quantity = new BigDecimal(0);
    private Integer level = 0;
    private Boolean birthday = false;
    private Integer times = 0;

    public boolean check(Trade order) {
        return false;
    }

    public List<Integer> getItems() {
        return items;
    }

    public void setItems(List<Integer> items) {
        this.items = items;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }


    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Boolean getBirthday() {
        return birthday;
    }

    public void setBirthday(Boolean birthday) {
        this.birthday = birthday;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }
}
