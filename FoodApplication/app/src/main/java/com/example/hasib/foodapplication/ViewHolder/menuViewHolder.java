package com.example.hasib.foodapplication.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hasib.foodapplication.Interface.ItemClickListener;
import com.example.hasib.foodapplication.R;

/**
 * Created by HASIB on 11/20/2017.
 */

public class menuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public  TextView titile;
    public ImageView imag;


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    private ItemClickListener itemClickListener;

    public menuViewHolder(View itemView) {
        super(itemView);
        titile=(TextView)itemView.findViewById(R.id.menu_name);  //initealished title
        imag=(ImageView)itemView.findViewById(R.id.menu_image); // initialish imag

        itemView.setOnClickListener(this);
    }


    /* public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }*/

    @Override
    public void onClick(View v) {
        itemClickListener.OnClick(v,getAdapterPosition(),false);

    }


}
