package com.luo.project.entity;

import java.io.Serializable;

/**
 * CollegeNotify 学院通知
 * <p/>
 * Created by luoyingxing on 16/3/15.
 */
public class CollegeNotify implements Serializable {
    private int id;
    private String title;
    private String content;
    private String imgUrl;
    private String createTime;

    public CollegeNotify() {
    }

    public CollegeNotify(int id, String title, String content, String imgUrl, String createTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "CollegeNotify{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
