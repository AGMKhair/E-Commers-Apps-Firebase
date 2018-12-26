package com.example.hasib.foodapplication.Remote;


import android.telecom.RemoteConference;

import com.example.hasib.foodapplication.Model.Myresponce;
import com.example.hasib.foodapplication.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


/**
 * Created by HASIB on 12/15/2017.
 */

public interface APIService  {
    //Call<Myresponce> sendNotificationI(@Body Sender body);
     @Headers(

             {
                     "Content-Type:application/json",
                     "Authorization:key=AAAAQ2Qrg9I:APA91bGUAplD_GlJHwwXmDOJc5xlyK52-F4_3c2eM8yXJvcUw-5yU1fm240T4ObqD2NRUegpBblluFAxEO1dOZLq69NsO6K9G8DFXauVV-dlPxanSl37xUYhfM0JwLmscqV9hJYP8rMY"
             }
     )
    @POST("fcm/send")
    Call<Myresponce> sendNotification(@Body Sender body);




}
