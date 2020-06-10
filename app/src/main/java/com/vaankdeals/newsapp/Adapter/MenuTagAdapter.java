package com.vaankdeals.newsapp.Adapter;

import android.content.Context;
import android.nfc.Tag;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.vaankdeals.newsapp.Model.GameModel;
import com.vaankdeals.newsapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MenuTagAdapter extends RecyclerView.Adapter {
    private ArrayList<String> TagList;
    private Context mContext;
    private tagClickListener mTagClickListener;
    public interface tagClickListener{
        void tagClick(int position);
    }
    public void setmTagClickListener(tagClickListener listener){
        mTagClickListener = listener;
    }
    public MenuTagAdapter(Context context, ArrayList<String> tagList){
        this.TagList = tagList;
        this.mContext = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_tag_item, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        String tagText= TagList.get(position);
        TagViewHolder tagViewHolder = (TagViewHolder) holder;

        if(position==0)
            tagViewHolder.mTagButton.setBackgroundResource(R.drawable.menu_small_card_first);
        tagViewHolder.mTagButton.setText(tagText);
    }

    @Override
    public int getItemCount() {
        return TagList.size();
    }
    private class TagViewHolder extends RecyclerView.ViewHolder{

        Button mTagButton;
        TagViewHolder(@NonNull View itemView) {
            super(itemView);

            mTagButton=itemView.findViewById(R.id.tag_button);
            mTagButton.setOnClickListener(v -> mTagClickListener.tagClick(getAdapterPosition()));
        }
    }
}
