package com.an.sys.entity;

import com.an.utils.JSONDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 消息任务模板
 * Created by karas on 5/14/14.
 */
public class Message {

    private Integer id;
    private Integer bizId;
    private String type;
    private String category;
    private String title;
    private String note;
    private String param;
    private String taskUrl;
    private String code;
    private Integer remindLeft;
    private String priority;
    private String user = "karas";

    private List<Integer> toRole = new ArrayList<>();
    private List<Integer> toDept = new ArrayList<>();
    private List<Integer> toUser = new ArrayList<>();

    private String privateStatus = "0";
    private Date readAt;

    private Date expireAt;
    private Integer sendBy;
    private Date sendAt;
    private Integer receiptBy;
    private Date receiptAt;
    private Integer dealBy;
    private Date dealAt;

    private String status = "1";
    private Date createAt;
    private Integer createBy;
    private Integer merchId;

    //判断接收方的条件
    private Integer isNot;

    public Message() {

    }

    public Message(Integer id, String status, String privateStatus) {
        this.id = id;
        this.status = status;
        this.privateStatus = privateStatus;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getTaskUrl() {
        return taskUrl;
    }

    public void setTaskUrl(String taskUrl) {
        this.taskUrl = taskUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getRemindLeft() {
        return remindLeft;
    }

    public void setRemindLeft(Integer remindLeft) {
        this.remindLeft = remindLeft;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonSerialize(using = JSONDateSerializer.class)
    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public List<Integer> getToRole() {
        return toRole;
    }

    public void setToRole(List<Integer> toRole) {
        this.toRole = toRole;
    }

    public List<Integer> getToDept() {
        return toDept;
    }

    public void setToDept(List<Integer> toDept) {
        this.toDept = toDept;
    }

    public void addToDept(Integer toDept) {
        this.toDept.add(toDept);
    }

    public List<Integer> getToUser() {
        return toUser;
    }

    public void setToUser(List<Integer> toUser) {
        this.toUser = toUser;
    }

    public void addUser(Integer userId) {
        this.toUser.add(userId);
    }

    @JsonSerialize(using = JSONDateSerializer.class)
    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }

    public Integer getSendBy() {
        return sendBy;
    }

    public void setSendBy(Integer sendBy) {
        this.sendBy = sendBy;
    }

    @JsonSerialize(using = JSONDateSerializer.class)
    public Date getSendAt() {
        return sendAt;
    }

    public void setSendAt(Date sendAt) {
        this.sendAt = sendAt;
    }

    public Integer getReceiptBy() {
        return receiptBy;
    }

    public void setReceiptBy(Integer receiptBy) {
        this.receiptBy = receiptBy;
    }

    @JsonSerialize(using = JSONDateSerializer.class)
    public Date getReceiptAt() {
        return receiptAt;
    }

    public void setReceiptAt(Date receiptAt) {
        this.receiptAt = receiptAt;
    }

    public Integer getDealBy() {
        return dealBy;
    }

    public void setDealBy(Integer dealBy) {
        this.dealBy = dealBy;
    }

    @JsonSerialize(using = JSONDateSerializer.class)
    public Date getDealAt() {
        return dealAt;
    }

    public void setDealAt(Date dealAt) {
        this.dealAt = dealAt;
    }

    public Integer getMerchId() {
        return merchId;
    }

    public void setMerchId(Integer merchId) {
        this.merchId = merchId;
    }

    public Integer getBizId() {
        return bizId;
    }

    public void setBizId(Integer bizId) {
        this.bizId = bizId;
    }

    public String getPrivateStatus() {
        return privateStatus;
    }

    public void setPrivateStatus(String privateStatus) {
        this.privateStatus = privateStatus;
    }

    @JsonSerialize(using = JSONDateSerializer.class)
    public Date getReadAt() {
        return readAt;
    }

    public void setReadAt(Date readAt) {
        this.readAt = readAt;
    }

    public Integer getIsNot() {
        return isNot;
    }

    public void setIsNot(Integer isNot) {
        this.isNot = isNot;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
