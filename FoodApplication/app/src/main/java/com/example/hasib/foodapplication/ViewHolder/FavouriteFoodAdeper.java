package com.example.hasib.foodapplication.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.hasib.foodapplication.Database.DetailasDB;
import com.example.hasib.foodapplication.FoodList;
import com.example.hasib.foodapplication.Food_details;
import com.example.hasib.foodapplication.Interface.ItemClickListener;
import com.example.hasib.foodapplication.Model.Favourite;
import com.example.hasib.foodapplication.Model.Order;
import com.example.hasib.foodapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HASIB on 6/27/2018.
 */

public class FavouriteFoodAdeper extends RecyclerView.Adapter<FavoureFoodViewHolder> {
    private Context context;
    private List<Favourite> favouritefood_list=new ArrayList<>();

    public FavouriteFoodAdeper(Context context, List<Favourite> favouritefood_list) {
        this.context = context;
        this.favouritefood_list = favouritefood_list;
    }

    @Override
    public FavoureFoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_food_style,parent,false);
        return new FavoureFoodViewHolder(v);
    }


    @Override
    public void onBindViewHolder(FavoureFoodViewHolder holder, final int position) {

        final Favourite favourite=favouritefood_list.get(position);

        Picasso.with(context).load(favourite.getFood_image()).into(holder.food_img);

        holder.food_title.setText(favourite.getFood_name());
        holder.price.setText(String.format("$ %s",favourite.getFood_price()));


        holder.countfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new DetailasDB(context).addToChart(new Order(
                        favouritefood_list.get(position).getFood_id(),  // pass the food id which are get from food list
                        favourite.getFood_name(),   //food name
                        "1", // number get from the elegantNumberButton
                        favourite.getFood_price(),  //  price
                        favourite.getFood_discount() , //discount
                        favourite.getFood_image()
                ));


            }
        });


        holder.setItemClickListener(new ItemClickListener() {      // setItemClickListener
            @Override
            public void OnClick(View view, int possisiton, boolean isLongClick) {

                Intent in = new Intent(context, Food_details.class);
                Toast.makeText(context,""+favouritefood_list.get(position).getFood_menu_id(),Toast.LENGTH_SHORT).show();
                //intent this class to Food_details class
                in.putExtra("FoodsId", favouritefood_list.get(position).getFood_menu_id());
                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//pass data or refaranceId to the Food_details class
                context.startActivity(in);
            }
        });






    }


    @Override
    public int getItemCount() {
        return favouritefood_list.size();
    }

    public void removeItem(int position){

        favouritefood_list.remove(position);
        notifyItemRemoved(position);

    }
    public void addreStore(Favourite favourite,int position)
    {

        favouritefood_list.add(position,favourite);

        notifyItemInserted(position);

    }


}
