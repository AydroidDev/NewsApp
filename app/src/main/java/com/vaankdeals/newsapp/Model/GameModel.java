package com.vaankdeals.newsapp.Model;

public class GameModel {
    String game_url;
    String game_image;
    public GameModel(String url,String img){
        this.game_url=url;
        this.game_image=img;
    }

    public String getGame_image() {
        return game_image;
    }

    public String getGame_url() {
        return game_url;
    }
}
