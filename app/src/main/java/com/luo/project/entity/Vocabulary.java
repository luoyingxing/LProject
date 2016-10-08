package com.luo.project.entity;

import java.util.List;

/**
 * Vocabulary
 * <p/>
 * Created by luoyingxing on 16/9/12.
 */
public class Vocabulary {

    /**
     * sid : 2307
     * tts : http://news.iciba.com/admin/tts/2016-09-12-day.mp3
     * content : Spread love everywhere you go. Let no one ever come to you without leaving happier.
     * note : 无论你去哪里，都要让每一个来找你的人离开时都比之前更快乐些。
     * love : 1403
     * translation : 投稿人：神之子牛顿
     * 词霸小编：最好的满足就是给别人以满足。快乐是会传染的哦~如果因为你给别人带来了快乐，自己的那份满足感就足以快乐一整天哈哈~小伙伴们，你们是否当过志愿者呢？尝试过后就会明白，哪怕苦、哪怕累，当你看到他人的笑脸后，就会觉得无比满足哦~
     * picture : http://cdn.iciba.com/news/word/20160912.jpg
     * picture2 : http://cdn.iciba.com/news/word/big_20160912b.jpg
     * caption : 词霸每日一句
     * dateline : 2016-09-12
     * s_pv : 11
     * sp_pv : 0
     * tags : [{"id":"13","name":"名人名言"}]
     * fenxiang_img : http://cdn.iciba.com/web/news/longweibo/imag/2016-09-12.jpg
     */

    private String sid;
    private String tts;
    private String content;
    private String note;
    private String love;
    private String translation;
    private String picture;
    private String picture2;
    private String caption;
    private String dateline;
    private String s_pv;
    private String sp_pv;
    private String fenxiang_img;
    /**
     * id : 13
     * name : 名人名言
     */

    private List<TagsBean> tags;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTts() {
        return tts;
    }

    public void setTts(String tts) {
        this.tts = tts;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getLove() {
        return love;
    }

    public void setLove(String love) {
        this.love = love;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getPicture2() {
        return picture2;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDateline() {
        return dateline;
    }

    public void setDateline(String dateline) {
        this.dateline = dateline;
    }

    public String getS_pv() {
        return s_pv;
    }

    public void setS_pv(String s_pv) {
        this.s_pv = s_pv;
    }

    public String getSp_pv() {
        return sp_pv;
    }

    public void setSp_pv(String sp_pv) {
        this.sp_pv = sp_pv;
    }

    public String getFenxiang_img() {
        return fenxiang_img;
    }

    public void setFenxiang_img(String fenxiang_img) {
        this.fenxiang_img = fenxiang_img;
    }

    public List<TagsBean> getTags() {
        return tags;
    }

    public void setTags(List<TagsBean> tags) {
        this.tags = tags;
    }

    public static class TagsBean {
        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Override
    public String toString() {
        return "Vocabulary{" +
                "sid='" + sid + '\'' +
                ", tts='" + tts + '\'' +
                ", content='" + content + '\'' +
                ", note='" + note + '\'' +
                ", love='" + love + '\'' +
                ", translation='" + translation + '\'' +
                ", picture='" + picture + '\'' +
                ", picture2='" + picture2 + '\'' +
                ", caption='" + caption + '\'' +
                ", dateline='" + dateline + '\'' +
                ", s_pv='" + s_pv + '\'' +
                ", sp_pv='" + sp_pv + '\'' +
                ", fenxiang_img='" + fenxiang_img + '\'' +
                ", tags=" + tags +
                '}';
    }
}
