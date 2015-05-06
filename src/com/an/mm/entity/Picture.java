package com.an.mm.entity;

/**
 * 图片
 * Created by karas on 1/1/15.
 */
public class Picture {
    private boolean isDefault;
    private String path;
    private String title;

    public Picture() {

    }

    public Picture(String path) {
        this.path = path;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
