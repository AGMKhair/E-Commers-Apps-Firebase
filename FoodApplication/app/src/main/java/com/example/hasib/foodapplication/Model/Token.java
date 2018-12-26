package com.example.hasib.foodapplication.Model;

/**
 * Created by HASIB on 12/15/2017.
 */

public class Token  {
    private String  token;
    private boolean isServerToken;

    public Token() {
    }

    public String getToken() {

        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isServerToken() {
        return isServerToken;
    }

    public void setServerToken(boolean serverToken) {
        isServerToken = serverToken;
    }

    public Token(String token, boolean isServerToken) {

        this.token = token;
        this.isServerToken = isServerToken;
    }
}
