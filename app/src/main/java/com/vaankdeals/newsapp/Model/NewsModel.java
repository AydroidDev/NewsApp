package com.vaankdeals.newsapp.Model;

import java.io.Serializable;

public class NewsModel implements Serializable{
    private String mNewsHead;
    private String mNewsDesc;
    private String mNewsImage;
    private String mNewsSource;
    private String mNewsDay;
    private String mNewsId;
    private String mNewslink;
    private String mNewsType;
    private String mNewsVideo;

    public NewsModel(String newshead, String newsdesc , String newsimage, String newssource, String newsday, String newsid , String newslink,String newstype,String newsvideo){
    mNewsHead = newshead;
    mNewsDesc = newsdesc;
    mNewsImage = newsimage;
    mNewsSource = newssource;
    mNewsDay = newsday;
    mNewsId = newsid;
    mNewslink = newslink;
    mNewsType = newstype;
    mNewsVideo = newsvideo;
}

public String getmNewsType(){
        return mNewsType;
}
    public String getmNewsHead(){
        return mNewsHead;
    }
    public String getmNewsDesc(){
        return mNewsDesc;
    }
    public String getmNewsImage(){
        return mNewsImage;
    }

    public String getmNewsSource() {
        return mNewsSource;
    }

    public String getmNewsDay() {
        return mNewsDay;
    }

    public String getmNewsId() {
        return mNewsId;
    }

    public String getmNewslink() {
        return mNewslink;
    }
    public String getmNewsVideo() {
        return mNewsVideo;
    }

}
