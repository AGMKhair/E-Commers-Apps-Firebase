package com.example.hasib.foodapplication.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.hasib.foodapplication.Model.Favourite;
import com.example.hasib.foodapplication.Model.Order;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HASIB on 11/23/2017.
 */

public class DetailasDB extends SQLiteAssetHelper {
    private final static String  DATABASE_NAME="database.db";
    private  final static   int DB_VERSION=4;
    SQLiteDatabase db;



    public DetailasDB(Context context) {
        super(context, DATABASE_NAME,null,DB_VERSION);
    }



    public List<Order> getCart(){
        SQLiteDatabase db=getReadableDatabase();
        SQLiteQueryBuilder sqLiteQueryBuilder=new SQLiteQueryBuilder();//overView again

        String[] selection={"ID","productId","productName","quantity","price","discount","image"};
        String sqlTable="OrderDetails";
        sqLiteQueryBuilder.setTables(sqlTable);
        Cursor c=sqLiteQueryBuilder.query(db,selection,null,null,null,null,null);//problem
        final  List<Order> result=new ArrayList<>();
        if(c.moveToFirst()){
            do{
                result.add(new Order(
                        c.getInt(c.getColumnIndex("ID")),
                        c.getString(c.getColumnIndex("productId")),
                        c.getString(c.getColumnIndex("productName")),
                        c.getString(c.getColumnIndex("price")),
                        c.getString(c.getColumnIndex("quantity")),
                        c.getString(c.getColumnIndex("discount")),
                        c.getString(c.getColumnIndex("image"))

                ));

            }while (c.moveToNext());

        }
        return result;
        }


        public void addToChart(Order order){   // insurt data from food details  whare contain food_id,food name,food quantitiy,price,discount
              db=getReadableDatabase();
             String quary=String.format("INSERT INTO OrderDetails(productId,productName,quantity,price,discount,image) VALUES('%s','%s','%s','%s','%s','%s');",
                    order.getProductId(),
                    order.getProductName(),
                    order.getQuantity(),
                    order.getProductPrice(),
                    order.getDiscount(),
                    order.getImage()
                    );
            db.execSQL(quary);

        }
    public void DeleteChart(){
        SQLiteDatabase db=getReadableDatabase();
        String quary=String.format("DELETE FROM OrderDetails");
        db.execSQL(quary);

    }

    public void AddToFavourite(Favourite food){
        SQLiteDatabase db=getReadableDatabase();
        String quary=String.format("INSERT INTO Favourites(FoodId,UserPhone,FoodName,FoodPrice,FoodMenuId,FoodImage,FoodDiscount,FoodDiscription) VALUES('%s','%s','%s','%s','%s','%s','%s','%s');",
                food.getFood_id(),food.getUser_number(),food.getFood_name(),food.getFood_price(),food.getFood_menu_id(),food.getFood_image(),
                food.getFood_discount(),food.getFood_description());
        db.execSQL(quary);
    }

    public void DeleteToFavourite(String foodId){
        SQLiteDatabase db=getReadableDatabase();
        String quary=String.format("DELETE FROM Favourites WHERE FoodId='%s';",foodId);
        db.execSQL(quary);
    }
    public boolean isFavourite(String foodId){
        SQLiteDatabase db=getReadableDatabase();
        String quary=String.format("SELECT * FROM Favourites WHERE FoodId='%s';",foodId);
        Cursor cursor=db.rawQuery(quary,null);
        if(cursor.getCount()<0){
           cursor.close();
            return false;
        }
        cursor.close();
        return  true;

    }


    public int getCounterCart() {
        int count=0;

        SQLiteDatabase db=getReadableDatabase();
        String quary=String.format("SELECT COUNT(*) FROM OrderDetails");
        Cursor cursor=db.rawQuery(quary,null);
        if(cursor.moveToFirst()){
            do {
                count=cursor.getInt(0);
            }while (cursor.moveToNext());
        }
        return  count;
    }

    public void updateChart(Order order) {

        SQLiteDatabase db=this.getReadableDatabase();

        String qury=String.format("UPDATE OrderDetails SET quantity= %s WHERE ID = %d",order.getQuantity(),order.getID());
        db.execSQL(qury);
    }


    public Cursor getAllFavourite(){
        SQLiteDatabase db=getReadableDatabase();
        SQLiteQueryBuilder sqLiteQueryBuilder=new SQLiteQueryBuilder();//overView again

        String[] selection={"FoodId,UserPhone,FoodName,FoodPrice,FoodMenuId,FoodImage,FoodDiscount,FoodDiscription"};
        String sqlTable="Favourites";
        sqLiteQueryBuilder.setTables(sqlTable);
        Cursor c=sqLiteQueryBuilder.query(db,selection,null,null,null,null,null);//problem
        /*final  List<Favourite> result=new ArrayList<>();
        if(c.moveToFirst()){

            // Food_id,Food_name,Food_price,Food_image,Food_description,Food_menu_id,Food_discount,User_number
            do{
                result.add(new Favourite(
                        c.getString(c.getColumnIndex("FoodId")),
                        c.getString(c.getColumnIndex("UserPhone")),
                        c.getString(c.getColumnIndex("FoodName")),
                        c.getString(c.getColumnIndex("FoodPrice")),
                        c.getString(c.getColumnIndex("FoodMenuId")),
                        c.getString(c.getColumnIndex("FoodImage")),
                        c.getString(c.getColumnIndex("FoodDiscount")),
                        c.getString(c.getColumnIndex("FoodDiscription"))
                ));

            }while (c.moveToNext());

        }*/
        return c;
    }
   /* public List<Favourite> qyryForfavourite(int possition){
        SQLiteDatabase db=getReadableDatabase();
        SQLiteQueryBuilder sqLiteQueryBuilder=new SQLiteQueryBuilder();//overView again

        String[] selection={"FoodId,UserPhone,UserName,FoodPrice,FoodMenuId,FoodImage,FoodDiscount,FoodDiscription"};
        String sqlTable="Favourites";
        sqLiteQueryBuilder.setTables(sqlTable);
        Cursor c=sqLiteQueryBuilder.query(db,selection,null,null,null,null,null);//problem
        final  List<Favourite> result=new ArrayList<>();
        if(c.moveToFirst()){

            // Food_id,Food_name,Food_price,Food_image,Food_description,Food_menu_id,Food_discount,User_number
            do{
                result.add(new Favourite(
                        c.getString(c.getColumnIndex("FoodId")),
                        c.getString(c.getColumnIndex("UserName")),
                        c.getString(c.getColumnIndex("FoodPrice")),
                        c.getString(c.getColumnIndex("Food_image")),
                        c.getString(c.getColumnIndex("Food_description")),
                        c.getString(c.getColumnIndex("Food_menu_id")),
                        c.getString(c.getColumnIndex("Food_discount")),
                        c.getString(c.getColumnIndex("User_number"))
                ));

            }while (c.moveToNext());

        }
        return result;
    }*/
}
