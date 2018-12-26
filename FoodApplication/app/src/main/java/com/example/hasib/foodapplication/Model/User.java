package com.example.hasib.foodapplication.Model;

import java.security.PrivateKey;

/**
 * Created by HASIB on 11/18/2017.
 */

public class User {
    private  String name;
    private String password;
    private String phone;
    private String forgetPassword;

    public String getHomeAdress() {
        return homeAdress;
    }

    public void setHomeAdress(String homeAdress) {
        this.homeAdress = homeAdress;
    }

    private String homeAdress;

    public String getForgetPassword() {
        return forgetPassword;
    }

    public void setForgetPassword(String forgetPassword) {
        this.forgetPassword = forgetPassword;
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
       // this.forgetPassword=forgetPassword;
    }

    public User() {
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User(String name, String password, String phone) {

        this.name = name;
        this.password = password;
        this.phone = phone;
    }
}
