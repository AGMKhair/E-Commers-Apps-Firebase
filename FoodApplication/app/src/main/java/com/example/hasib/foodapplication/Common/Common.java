package com.example.hasib.foodapplication.Common;

import android.content.Context;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.hasib.foodapplication.Model.User;
import com.example.hasib.foodapplication.R;
import com.example.hasib.foodapplication.Remote.APIService;
import com.example.hasib.foodapplication.Remote.IGoogleService;
import com.example.hasib.foodapplication.Remote.RetrofitClient;

import java.util.Calendar;
import java.util.Locale;


/**
 * Created by HASIB on 11/20/2017.
 */

public class Common {
    public static String topicName="News";
   public static User currentUser;

   public static String PHONE_TEXT="user_number";

   public static String UNIC_FOOD_ID="food_id";

   public static String currentKey;

    public static final String baseUrl="https://fcm.googleapis.com/";
    public static final String GOOGLE_API_URL="https://maps.googleapis.com/";
    public  static final String DELLETE="delete";
    public  static final String USER_KEY="User";
    public  static final String PASSWORD="Password";

    public static APIService getFCMservice(){
        return RetrofitClient.getClient(baseUrl).create(APIService.class);
    }
    public static IGoogleService getGoogleMapApi(){
        return RetrofitClient.getGoogleClient(GOOGLE_API_URL).create(IGoogleService.class);
    }



    public  static  String ConvertCodeToStatus(String code){

        if(code.equals("0"))
            return "Placed";
        else if(code.equals("1"))
            return "On my way";
        else if(code.equals("2"))
            return "Shipping";
        else
            return "Shipped";


    }


    public static int convertCodeToImag(String img) {
        if(img.equals(0))
            return R.drawable.ic_favorite_border_black_24dp;
        else
            return R.drawable.ic_favorite_black_24dp;

    }






   public static boolean usConnectionInternet(Context contex){  //for checking Internet connection method
      ConnectivityManager connectivityManager=(ConnectivityManager)contex.getSystemService(Context.CONNECTIVITY_SERVICE);
       if(connectivityManager != null){
         NetworkInfo[] info=connectivityManager.getAllNetworkInfo();  //get all network connection
         if(info!=null) {
            for (int i = 0; i < info.length; i++) {

               if (info[i].getState()== NetworkInfo.State.CONNECTED) // if network connection equal connected
                  return true;

            }
         }
      }
     return false;

   }



}
