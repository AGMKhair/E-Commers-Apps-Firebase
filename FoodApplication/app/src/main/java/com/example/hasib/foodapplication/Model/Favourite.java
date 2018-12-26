package com.example.hasib.foodapplication.Model;

/**
 * Created by HASIB on 6/27/2018.
 */

public class Favourite {
    public Favourite() {
    }

    public String getFood_id() {
        return Food_id;
    }

    public void setFood_id(String food_id) {
        Food_id = food_id;
    }

    public String getFood_name() {
        return Food_name;
    }

    public void setFood_name(String food_name) {
        Food_name = food_name;
    }

    public String getFood_price() {
        return Food_price;
    }

    public void setFood_price(String food_price) {
        Food_price = food_price;
    }

    public String getFood_image() {
        return Food_image;
    }

    public void setFood_image(String food_image) {
        Food_image = food_image;
    }

    public String getFood_description() {
        return Food_description;
    }

    public void setFood_description(String food_description) {
        Food_description = food_description;
    }

    public String getFood_menu_id() {
        return Food_menu_id;
    }

    public void setFood_menu_id(String food_menu_id) {
        Food_menu_id = food_menu_id;
    }

    public String getFood_discount() {
        return Food_discount;
    }

    public void setFood_discount(String food_discount) {
        Food_discount = food_discount;
    }

    public String getUser_number() {
        return User_number;
    }

    public void setUser_number(String user_number) {
        User_number = user_number;
    }

    private  String Food_id,Food_name,Food_price,Food_image,Food_description,Food_menu_id,Food_discount,User_number;

    public Favourite(String food_id, String food_name, String food_price, String food_image, String food_description, String food_menu_id, String food_discount, String user_number) {
        Food_id = food_id;
        Food_name = food_name;
        Food_price = food_price;
        Food_image = food_image;
        Food_description = food_description;
        Food_menu_id = food_menu_id;
        Food_discount = food_discount;
        User_number = user_number;
    }
}
