package com.example.hasib.foodapplication.Model;

/**
 * Created by HASIB on 11/23/2017.
 */


public class Order {


    private  int ID;
    private String ProductId;
    private String ProductName;
    private String ProductPrice;
    private String Quantity;
    private String Discount;
    private String Image;

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public Order() {
    }

    public Order(int ID, String productId, String productName, String productPrice, String quantity, String discount, String image) {
        this.ID = ID;
        ProductId = productId;
        ProductName = productName;
        ProductPrice = productPrice;
        Quantity = quantity;
        Discount = discount;
        Image=image;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String  getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public Order(String productId, String productName, String productPrice, String quantity, String discount,String image) {

        ProductId = productId;
        ProductName = productName;
        ProductPrice = productPrice;
        Quantity = quantity;
        Discount = discount;
        Image=image;
    }


}
