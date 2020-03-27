package com.vaankdeals.newsapp.Model;

public class NewsBook {

    public NewsBook(int id, String mNewsHead, String mNewsDesc, String mNewsImage, String mNewsSource, String mNewsDay, String mNewslink, String mNewsId, String mNewsType, String mNewsVideo) {
        this.id = id;
        this.mNewsHead = mNewsHead;
        this.mNewsDesc = mNewsDesc;
        this.mNewsImage = mNewsImage;
        this.mNewsSource = mNewsSource;
        this.mNewsDay = mNewsDay;
        this.mNewslink = mNewslink;
        this.mNewsId = mNewsId;
        this.mNewsType = mNewsType;
        this.mNewsVideo = mNewsVideo;
    }

    private int id;
    private String mNewsHead;
    private String mNewsDesc;
    private String mNewsImage;
    private String mNewsSource;
    private String mNewsDay;

    private String mNewslink;
    private String mNewsId;
    private String mNewsType;
    private String mNewsVideo;

    public NewsBook() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getmNewsHead() {
        return mNewsHead;
    }

    public void setmNewsHead(String mNewsHead) {
        this.mNewsHead = mNewsHead;
    }

    public String getmNewsDesc() {
        return mNewsDesc;
    }

    public void setmNewsDesc(String mNewsDesc) {
        this.mNewsDesc = mNewsDesc;
    }

    public String getmNewsImage() {
        return mNewsImage;
    }

    public void setmNewsImage(String mNewsImage) {
        this.mNewsImage = mNewsImage;
    }

    public String getmNewsSource() {
        return mNewsSource;
    }

    public void setmNewsSource(String mNewsSource) {
        this.mNewsSource = mNewsSource;
    }

    public String getmNewsDay() {
        return mNewsDay;
    }

    public void setmNewsDay(String mNewsDay) {
        this.mNewsDay = mNewsDay;
    }

    public String getmNewsId() {
        return mNewsId;
    }

    public void setmNewsId(String mNewsId) {
        this.mNewsId = mNewsId;
    }

    public String getmNewslink() {
        return mNewslink;
    }

    public void setmNewslink(String mNewslink) {
        this.mNewslink = mNewslink;
    }

    public String getmNewsType() {
        return mNewsType;
    }

    public void setmNewsType(String mNewsType) {
        this.mNewsType = mNewsType;
    }

    public String getmNewsVideo() {
        return mNewsVideo;
    }

    public void setmNewsVideo(String mNewsVideo) {
        this.mNewsVideo = mNewsVideo;
    }

}