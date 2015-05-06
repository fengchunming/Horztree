package com.an.crm.entity;

import com.an.utils.JSONDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public class Member {
    private Integer id;
    private String userName;
    private String nickName;
    private String avatar;
    private String password;

    private Integer level;
    private BigDecimal point;
    private BigDecimal balance;

    private String realName;
    private String region;
    private String address;
    private String mobile;
    private String email;
    private String telephone;
    private String zipCode;

    private char gender;
    private Date birthday;

    private String qqId;
    private Map qqDesc;

    private String wechatId;
    private String wechatName;
    private Map wechatDesc;

    private String weiboId;
    private String weiboName;
    private Map weiboDesc;

    private String renrenId;
    private String renrenName;
    private Map renrenDesc;

    private String status;
    private Date enterAt;
    private Date registAt;
    private Date loginAt;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public BigDecimal getPoint() {
        return point;
    }

    public void setPoint(BigDecimal point) {
        this.point = point;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getQqId() {
        return qqId;
    }

    public void setQqId(String qqId) {
        this.qqId = qqId;
    }

    public Map getQqDesc() {
        return qqDesc;
    }

    public void setQqDesc(Map qqDesc) {
        this.qqDesc = qqDesc;
    }

    public String getWechatId() {
        return wechatId;
    }

    public void setWechatId(String wechatId) {
        this.wechatId = wechatId;
    }

    public String getWechatName() {
        return wechatName;
    }

    public void setWechatName(String wechatName) {
        this.wechatName = wechatName;
    }

    public Map getWechatDesc() {
        return wechatDesc;
    }

    public void setWechatDesc(Map wechatDesc) {
        this.wechatDesc = wechatDesc;
    }

    public String getWeiboId() {
        return weiboId;
    }

    public void setWeiboId(String weiboId) {
        this.weiboId = weiboId;
    }

    public String getWeiboName() {
        return weiboName;
    }

    public void setWeiboName(String weiboName) {
        this.weiboName = weiboName;
    }

    public Map getWeiboDesc() {
        return weiboDesc;
    }

    public void setWeiboDesc(Map weiboDesc) {
        this.weiboDesc = weiboDesc;
    }

    public String getRenrenId() {
        return renrenId;
    }

    public void setRenrenId(String renrenId) {
        this.renrenId = renrenId;
    }

    public String getRenrenName() {
        return renrenName;
    }

    public void setRenrenName(String renrenName) {
        this.renrenName = renrenName;
    }

    public Map getRenrenDesc() {
        return renrenDesc;
    }

    public void setRenrenDesc(Map renrenDesc) {
        this.renrenDesc = renrenDesc;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonSerialize(using = JSONDateTimeSerializer.class)
    public Date getRegistAt() {
        return registAt;
    }

    public void setRegistAt(Date registAt) {
        this.registAt = registAt;
    }

    @JsonSerialize(using = JSONDateTimeSerializer.class)
    public Date getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(Date loginAt) {
        this.loginAt = loginAt;
    }

    public Date getEnterAt() {
        return enterAt;
    }

    public void setEnterAt(Date enterAt) {
        this.enterAt = enterAt;
    }
}
