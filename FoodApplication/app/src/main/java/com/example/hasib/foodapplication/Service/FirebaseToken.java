package com.example.hasib.foodapplication.Service;

import com.example.hasib.foodapplication.Common.Common;
import com.example.hasib.foodapplication.Model.Token;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
//import com.google.firebase.iid.zzd;

/**
 * Created by HASIB on 12/15/2017.
 */

public class FirebaseToken extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String tokenRefrashed=FirebaseInstanceId.getInstance().getToken();
        updateFirebaseTotoken(tokenRefrashed);
    }

    private void updateFirebaseTotoken(String tokenRefrashed) {
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference referance=db.getReference("Token");
        Token token=new Token(tokenRefrashed,false);
        referance.child(Common.currentUser.getPhone()).setValue(token);

    }
}
