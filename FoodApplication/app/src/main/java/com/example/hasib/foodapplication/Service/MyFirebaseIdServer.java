package com.example.hasib.foodapplication.Service;

import android.media.session.MediaSession;

import com.example.hasib.foodapplication.Common.Common;
import com.example.hasib.foodapplication.Model.Token;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Created by HASIB on 12/16/2017.
 */

public class MyFirebaseIdServer extends FirebaseInstanceIdService{
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String receiveTokenRefresh= FirebaseInstanceId.getInstance().getToken();

        if(Common.currentUser != null) {
            updetFrebaseToken(receiveTokenRefresh);
        }
    }

    private void updetFrebaseToken(String receiveTokenRefresh) {
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference referance=db.getReference("Token");
        Token token=new Token(receiveTokenRefresh,true);
        referance.child(Common.currentUser.getPhone()).setValue(token);
    }
}
