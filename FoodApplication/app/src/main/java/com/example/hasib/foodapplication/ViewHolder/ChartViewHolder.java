package com.example.hasib.foodapplication.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.hasib.foodapplication.Common.Common;
import com.example.hasib.foodapplication.Interface.ItemClickListener;
import com.example.hasib.foodapplication.Model.Order;
import com.example.hasib.foodapplication.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by HASIB on 11/23/2017.
 */

 public class ChartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {
 public    TextView item_name,item_price;

 ImageView item_image;
// public   ImageView item_count;

 public ElegantNumberButton elegantNumberButton;

    private ItemClickListener itemClickListener;

    public void set_cart_text_name(TextView item_name){
        this.item_name=item_name;
    }

    public ChartViewHolder(View itemView) {
        super(itemView);
        item_name=(TextView)itemView.findViewById(R.id.cart_item_name);
        item_price=(TextView)itemView.findViewById(R.id.cart_item_price);
        item_image=(ImageView)itemView.findViewById(R.id.orderImage);

      //  item_count=(ImageView)itemView.findViewById(R.id.cart_item_count);
        elegantNumberButton=(ElegantNumberButton)itemView.findViewById(R.id.chartButton);

        itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select Action");
        menu.add(0,0,getAdapterPosition(), Common.DELLETE);
    }
}

