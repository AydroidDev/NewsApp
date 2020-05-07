package com.vaankdeals.newsapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.vaankdeals.newsapp.Model.GameModel;
import com.vaankdeals.newsapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GameAdapter extends RecyclerView.Adapter {
    private ArrayList<GameModel> GameList;
    private Context mContext;
    private gameListener mGameListener;
    public interface gameListener{
        void gameClick(int position);
    }
    public void setmGameListener(gameListener listener){
        mGameListener = listener;
    }
    public GameAdapter(Context context, ArrayList<GameModel> gameList){
        this.GameList = gameList;
        this.mContext = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GameModel gameModel= GameList.get(position);
        GameViewHolder gameViewHolder = (GameViewHolder) holder;
        Glide.with(mContext)
                .load(gameModel.getGame_image())
                .into(gameViewHolder.mGameImage);

    }

    private class GameViewHolder extends RecyclerView.ViewHolder{

        ImageView mGameImage;
        GameViewHolder(@NonNull View itemView) {
            super(itemView);

            mGameImage=itemView.findViewById(R.id.game_image);
            mGameImage.setOnClickListener(v -> mGameListener.gameClick(getAdapterPosition()));
        }
    }
    @Override
    public int getItemCount() {
        return GameList.size();
    }
}
