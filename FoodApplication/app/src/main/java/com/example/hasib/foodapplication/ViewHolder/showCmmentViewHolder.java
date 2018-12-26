package com.example.hasib.foodapplication.ViewHolder;

import android.nfc.TagLostException;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hasib.foodapplication.Model.Rating;
import com.example.hasib.foodapplication.R;

/**
 * Created by HASIB on 6/20/2018.
 */

public  class showCmmentViewHolder extends RecyclerView.ViewHolder{
    public  TextView user_number;
    public  TextView user_comment;
    public  RatingBar user_ratting;

    public showCmmentViewHolder(View itemView) {
        super(itemView);

        user_number=(TextView)itemView.findViewById(R.id.user_pone_number);
        user_comment=(TextView)itemView.findViewById(R.id.comment_text);
        user_ratting=(RatingBar)itemView.findViewById(R.id.ratting);
    }
}
