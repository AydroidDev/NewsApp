package com.vaankdeals.newsapp.Model;

public class VideoModel {

    private String video_type;
    private String video_image;
    private String video_id;
    private String video_cat;

    public String getVideo_cat() {
        return video_cat;
    }
    public String getVideo_type() {
        return video_type;
    }

    public String getVideo_image() {
        return video_image;
    }

    public String getVideo_id() {
        return video_id;
    }

    public VideoModel(String video_type, String video_image, String video_id,String video_cat) {
        this.video_type = video_type;
        this.video_image = video_image;
        this.video_id = video_id;
        this.video_cat=video_cat;
    }


}
