package com.vaankdeals.newsapp.ViewTypes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.astritveliu.boom.Boom;
import com.bumptech.glide.Glide;
import com.vaankdeals.newsapp.Model.NewsModel;
import com.vaankdeals.newsapp.R;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import spencerstudios.com.bungeelib.Bungee;

public class FragmentProdTag extends Fragment {

    public FragmentProdTag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);
        ImageView cardView = view.findViewById(R.id.prod_image);

        CardView card1 = view.findViewById(R.id.card1);
        TextView head1 = view.findViewById(R.id.head1);
        TextView desc1 = view.findViewById(R.id.desc1);
        TextView price1 = view.findViewById(R.id.price1);
        ImageView image1 = view.findViewById(R.id.image1);

        CardView card2 = view.findViewById(R.id.card2);
        TextView head2 = view.findViewById(R.id.head2);
        TextView desc2 = view.findViewById(R.id.desc2);
        TextView price2 = view.findViewById(R.id.price2);
        ImageView image2 = view.findViewById(R.id.image2);

        CardView card3 = view.findViewById(R.id.card3);
        TextView head3 = view.findViewById(R.id.head3);
        TextView desc3 = view.findViewById(R.id.desc3);
        TextView price3 = view.findViewById(R.id.price3);
        ImageView image3 = view.findViewById(R.id.image3);

        assert getArguments() != null;
        NewsModel model = (NewsModel) getArguments().getSerializable("model");
        assert model != null;
        Glide.with(requireActivity()).load(model.getmNewsImage()).into(cardView);

        String data1 = model.getmNewsData1();
        String data2 = model.getmNewsData2();
        String data3 = model.getmNewsData3();

        String link1 = model.getmNewsSource();
        String link2 = model.getmNewslink();
        String link3 = model.getmNewsVideo();

        if (!data1.isEmpty()) {
            card1.setVisibility(View.VISIBLE);
            String[] dataArray = data1.split("#", 4);
            Glide.with(requireContext()).load(dataArray[3]).into(image1);
            head1.setText(dataArray[0]);
            desc1.setText(dataArray[1]);
            price1.setText(dataArray[2]);
            if (!link1.isEmpty())
                card1.setOnClickListener(v -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link1));
                    startActivity(browserIntent);
                });
        }
        if (!data2.isEmpty()) {
            card2.setVisibility(View.VISIBLE);
            String[] dataArray = data2.split("#", 4);
            Glide.with(requireContext()).load(dataArray[3]).into(image2);
            head2.setText(dataArray[0]);
            desc2.setText(dataArray[1]);
            price2.setText(dataArray[2]);
            if (!link2.isEmpty()) {
                card2.setOnClickListener(v -> {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link2));
                    startActivity(browserIntent);
                });
            }
        }

        if (!data3.isEmpty()) {
            card3.setVisibility(View.VISIBLE);
            new Boom(card1);
            String[] dataArray = data3.split("#", 4);
            Glide.with(requireContext()).load(dataArray[3]).into(image3);
            head3.setText(dataArray[0]);
            desc3.setText(dataArray[1]);
            price3.setText(dataArray[2]);
            if (!link3.isEmpty()) {
                {
                    card3.setOnClickListener(v -> {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(link3));
                        startActivity(browserIntent);
                    });
                }
            }
        }
            return view;
        }

    }
