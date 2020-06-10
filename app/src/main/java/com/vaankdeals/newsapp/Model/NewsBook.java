package com.vaankdeals.newsapp.Model;

public class NewsBook {

    public NewsBook(int id, String mNewsHead, String mNewsDesc, String mNewsImage, String mNewsSource, String mNewsDay, String mNewslink, String mNewsId, String mNewsType, String mNewsVideo,String newsdata1,String newsdat2,String newsdata3) {
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
        mNewsData1=newsdata1;
        mNewsData2=newsdat2;
        mNewsData3=newsdata3;

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
    private String mNewsData1;
    private String mNewsData2;
    private String mNewsData3;
    public NewsBook() {

    }
    public String getmNewsData1() {
        return mNewsData1;
    }

    public void setmNewsData1(String mNewsData1) {
        this.mNewsData1 = mNewsData1;
    }

    public String getmNewsData2() {
        return mNewsData2;
    }

    public void setmNewsData2(String mNewsData2) {
        this.mNewsData2 = mNewsData2;
    }

    public String getmNewsData3() {
        return mNewsData3;
    }

    public void setmNewsData3(String mNewsData3) {
        this.mNewsData3 = mNewsData3;
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