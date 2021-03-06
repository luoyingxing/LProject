package com.luo.project.entity;

import java.io.Serializable;

/**
 * Info
 * <p>
 * Created by luoyingxing on 2017/8/25.
 */

public class Info implements Serializable {
    private Integer infoId;
    private String title;
    private String url;

    public Info() {
    }

    public Info(Integer infoId, String title, String url) {
        this.infoId = infoId;
        this.title = title;
        this.url = url;
    }

    public Integer getInfoId() {
        return infoId;
    }

    public void setInfoId(Integer infoId) {
        this.infoId = infoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
