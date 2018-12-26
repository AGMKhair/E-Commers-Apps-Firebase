package com.example.hasib.foodapplication;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.hasib.foodapplication.Database.DetailasDB;
import com.example.hasib.foodapplication.Model.Favourite;
import com.example.hasib.foodapplication.Model.Food;
import com.example.hasib.foodapplication.ViewHolder.FavoureFoodViewHolder;
import com.example.hasib.foodapplication.ViewHolder.FavouriteFoodAdeper;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFood extends AppCompatActivity  {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<Favourite> foodList=new ArrayList<>();

    FavouriteFoodAdeper adeper;
    DetailasDB db;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_food);

        db=new DetailasDB(getApplicationContext());

        recyclerView=(RecyclerView)findViewById(R.id.favourite_food_recycularview);
        layoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        getAlldata();




        adeper= new FavouriteFoodAdeper(getApplicationContext(),foodList);

        recyclerView.setAdapter(adeper);



    }

    public void getAlldata(){

        Cursor c=db.getAllFavourite();

        if (c !=null){

            if (c.moveToFirst()){
                do {
                  Favourite fb=  new Favourite(

  //(String food_id, String food_name, String food_price, String food_image, String food_description, String food_menu_id, String food_discount, String user_number
                            c.getString(c.getColumnIndex("FoodId")),
                            c.getString(c.getColumnIndex("FoodName")),
                            c.getString(c.getColumnIndex("FoodPrice")),
                            c.getString(c.getColumnIndex("FoodImage")),
                            c.getString(c.getColumnIndex("FoodDiscription")),
                            c.getString(c.getColumnIndex("FoodMenuId")),
                            c.getString(c.getColumnIndex("FoodDiscount")),
                            c.getString(c.getColumnIndex("UserPhone"))


                            );
                //  );
                    //Favourite fb=new Favourite();


                    foodList.add(fb);

                }while (c.moveToNext());
            }

        }
    }




    }

