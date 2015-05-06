package com.an.sys.entity;

import com.an.core.model.TreeNode;
import com.an.utils.JSONDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Date;

/**
 * 系统功能模块
 * <p>
 * created by Karas 2012-3-8
 */

public class Module extends TreeNode {

    private String name; // 功能名称
    private String tips; // 功能提示，简介
    private String url; // URL路径
    private String param; // 参数
    private String method;
    private String buttonId;
    private boolean isMenu; // 是否菜单
    private boolean isShortcut; // 是否快捷方式
    private String icon; // 图标路径
    private String parentId;
    private String status; // 状态 d:已删除,t:启用,f:停用
    private String remark;
    private Integer createBy;
    private Date createAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getButtonId() {
        return buttonId;
    }

    public void setButtonId(String buttonId) {
        this.buttonId = buttonId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public boolean isMenu() {
        return isMenu;
    }

    public void setMenu(boolean isMenu) {
        this.isMenu = isMenu;
    }

    public boolean isShortcut() {
        return isShortcut;
    }

    public void setShortcut(boolean isShortcut) {
        this.isShortcut = isShortcut;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    @JsonSerialize(using = JSONDateSerializer.class)
    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

}
