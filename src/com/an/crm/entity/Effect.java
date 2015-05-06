package com.an.crm.entity;

import java.math.BigDecimal;

/**
 * 效果
 * Created by karas on 1/4/15.
 */
public class Effect {
    private BigDecimal percent;
    private BigDecimal subtract;
    private String reward;
    private BigDecimal naddOne;

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    public BigDecimal getSubtract() {
        return subtract;
    }

    public void setSubtract(BigDecimal subtract) {
        this.subtract = subtract;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public BigDecimal getNaddOne() {
        return naddOne;
    }

    public void setNaddOne(BigDecimal naddOne) {
        this.naddOne = naddOne;
    }
}
