package com.example.hasib.foodapplication.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.hasib.foodapplication.Chart;
import com.example.hasib.foodapplication.Database.DetailasDB;
import com.example.hasib.foodapplication.Model.Order;
import com.example.hasib.foodapplication.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class chartAdepter extends RecyclerView.Adapter<ChartViewHolder>  {

    private List<Order> listData=new ArrayList<>();
    private Chart chart;

    public chartAdepter(List<Order> listData,  Chart chart) {
        this.listData = listData;
        this.chart = chart;
    }


    @Override
    public ChartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(chart);
        View itemView=inflater.inflate(R.layout.cart_view,parent,false);

        return new ChartViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ChartViewHolder holder, final int position) {
      //  TextDrawable drawable=TextDrawable.builder().buildRound(""+listData.get(position).getQuantity(), Color.RED);
        //holder.item_count.setImageDrawable(drawable);

        Picasso.with(chart.getBaseContext()).load(listData.get(position).getImage())
                .centerCrop()
                .resize(70,70)
                .into(holder.item_image);
        Locale locale=new Locale("en","us");
        NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);
        int price=(Integer.parseInt(listData.get(position).getProductPrice()))*(Integer.parseInt(listData.get(position).getQuantity()));
        holder.item_price.setText(fmt.format(price));
        holder.item_name.setText(listData.get(position).getProductName());
      //  holder.item_image.setIm


        holder.elegantNumberButton.setNumber(listData.get(position).getQuantity());


        holder.elegantNumberButton.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                Order order=listData.get(position);
                order.setQuantity(String.valueOf(newValue));
                new DetailasDB(chart).updateChart(order);

                int total=0;
                List<Order> orders=new DetailasDB(chart).getCart();

                for(Order item:orders){
                    total+=(Integer.parseInt(order.getProductPrice()))*(Integer.parseInt(item.getQuantity()));


                    Locale locale=new Locale("en","us");
                    NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);
                    chart.showPrice.setText(fmt.format(total));
                }
            }
        });



    }



    @Override
    public int getItemCount() {
        return listData.size();
    }
}
