package com.example.hasib.foodapplication.Model;

/**
 * Created by HASIB on 11/20/2017.
 */

public class Category {
    private   String Name;
    private  String Image;
    //private String food_img;

/*

    public String getFood_img() {
        return food_img;
    }

    public void setFood_img(String food_img) {
        this.food_img = food_img;
    }
*/



    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public Category() {
    }

    public Category(String name, String image) {

        Name = name;
        Image = image;
      //  this.food_img="0";
    }





}
