package com.example.hasib.foodapplication.ViewHolder;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andremion.counterfab.CounterFab;
import com.example.hasib.foodapplication.Interface.ItemClickListener;
import com.example.hasib.foodapplication.R;

/**
 * Created by HASIB on 11/21/2017.
 */

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView food_title; // object of food_title
    public ImageView food_img; // object of food_image
    public ImageView food_fav;
    public ImageView food_share;// object of food_favourite
    public TextView price;
    public ImageView countfb;


    private ItemClickListener itemClickListener;

    public FoodViewHolder(View itemView) {
        super(itemView);
        food_title=(TextView)itemView.findViewById(R.id.food_name); //set title
        food_img=(ImageView)itemView.findViewById(R.id.food_image);// set img
        food_fav=(ImageView)itemView.findViewById(R.id.food_favourite);//set favourite
        food_share=(ImageView)itemView.findViewById(R.id.shareToFacebook);
         price=(TextView)itemView.findViewById(R.id.food_price);
         countfb=(ImageView)itemView.findViewById(R.id.count_chart);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
       itemClickListener.OnClick(v,getAdapterPosition(),false);
    }
}
