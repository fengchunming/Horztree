package com.an.crm.entity;

import com.an.utils.JSONDateSerializer;
import com.an.utils.JSONDateTimeDeserializer;
import com.an.utils.JSONDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;
import java.util.List;

/**
 * 营销策略
 * Created by karas on 1/5/15.
 */
public class Strategy {
    private Integer id;
    private String name;
    private String remark;
    private List<Integer> groups;

    private Clause clause; //条件
    private Effect effect; //效果

    private String include;
    private String exclude;

    private String task;
    private String type;

    private Date expireDate;
    private Date effectDate;

    private String status;
    private Integer enterBy;
    private Date enterAt;

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

    @JsonSerialize(using = JSONDateTimeSerializer.class)
    public Date getExpireDate() {
        return expireDate;
    }

    @JsonDeserialize(using = JSONDateTimeDeserializer.class)
    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    @JsonSerialize(using = JSONDateTimeSerializer.class)
    public Date getEffectDate() {
        return effectDate;
    }

    @JsonDeserialize(using = JSONDateTimeDeserializer.class)
    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }

    public Clause getClause() {
        return clause;
    }

    public void setClause(Clause clause) {
        this.clause = clause;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getInclude() {
        return include;
    }

    public void setInclude(String include) {
        this.include = include;
    }

    public String getExclude() {
        return exclude;
    }

    public void setExclude(String exclude) {
        this.exclude = exclude;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getGroups() {
        return groups;
    }

    public void setGroups(List<Integer> groups) {
        this.groups = groups;
    }
}
