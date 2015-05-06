package com.an.core.model;

import java.io.Serializable;

public abstract class TreeNode implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer parent = 0;
    private String path;
    private Integer step = 1;
    private Integer level = 1;
    private Integer sort = 50;
    private String depth;
    private boolean isLeaf = true;

    public Integer getParent() {
        return parent;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
