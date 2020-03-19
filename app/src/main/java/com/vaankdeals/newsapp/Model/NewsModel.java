package com.vaankdeals.newsapp.Model;

public class NewsModel {
    private String mNewsHead;
    private String mNewsDesc;
    private String mNewsImage;
    private String mNewsSource;
    private String mNewsDay;
    private String mNewsId;
    private String mNewslink;
    private String mNewsType;

    public NewsModel(String newshead, String newsdesc , String newsimage, String newssource, String newsday, String newsid , String newslink,String newstype){
    mNewsHead = newshead;
    mNewsDesc = newsdesc;
    mNewsImage = newsimage;
    mNewsSource = newssource;
    mNewsDay = newsday;
    mNewsId = newsid;
    mNewslink = newslink;
    mNewsType = newstype;
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

}
