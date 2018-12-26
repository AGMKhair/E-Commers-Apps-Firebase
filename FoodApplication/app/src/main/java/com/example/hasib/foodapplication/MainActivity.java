
package com.example.hasib.foodapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.hasib.foodapplication.Common.Common;
import com.example.hasib.foodapplication.Model.User;

import com.facebook.FacebookSdk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//import info.hoang8f.widget.FButton;
import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    Button singin, SingUp;



    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/food.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        printKeyHash();

        FacebookSdk.sdkInitialize(getApplicationContext());

        singin = (Button) findViewById(R.id.btsingIn);
        SingUp = (Button) findViewById(R.id.btSingUp);

        Paper.init(this);
     //  FacebookSdk.sdkInitialize(getApplicationContext() );
        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, LogIn.class);
                startActivity(in);

            }
        });
        SingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, com.example.hasib.foodapplication.SingUp.class);
                startActivity(in);

            }
        });

        String user = Paper.book().read(Common.USER_KEY);
        String password = Paper.book().read(Common.PASSWORD);
        if (user != null && password != null) {
            if (!user.isEmpty() && !password.isEmpty()) {
                logIn(user, password);
            }

        }

        getHashKey();


    }

    private void printKeyHash() {
        try{
            PackageInfo info=getPackageManager().getPackageInfo("com.example.hasib.foodapplication",
                    PackageManager.GET_SIGNATURES);

            for (Signature signature:info.signatures) {

                MessageDigest maessageDigest=MessageDigest.getInstance("SHA");
                maessageDigest.update(signature.toByteArray());
                Log.d("KeyHash",Base64.encodeToString(maessageDigest.digest(),Base64.DEFAULT));

            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private void getHashKey() {
        try {
            PackageInfo pi=getPackageManager().getPackageInfo("com.example.hasib.foodapplication", PackageManager.GET_SIGNATURES);
            for (Signature signature:pi.signatures){
                MessageDigest md=MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));

            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    private void logIn(final String name, final String password) {

        FirebaseDatabase db=FirebaseDatabase.getInstance();
        final DatabaseReference user_table=db.getReference("User");

        if (Common.usConnectionInternet(getBaseContext())) {




            final ProgressDialog mDilog = new ProgressDialog(MainActivity.this);
            mDilog.setMessage("Writing...");

            user_table.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(name).exists()) {




                        mDilog.dismiss();
                        User user = dataSnapshot.child(name).getValue(User.class);
                        user.setPhone(name);
                        if (user.getPassword().equals(password)) {

                            Intent in = new Intent(MainActivity.this, Home.class);

                            Common.currentUser = user;
                            startActivity(in);


                            Toast.makeText(getApplicationContext(), "Log In Sussessfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Wrong!!", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        mDilog.dismiss();
                        Toast.makeText(getApplicationContext(), "User not exiest database", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else {
            Toast.makeText(getApplicationContext(),"Please check your internet connection", Toast.LENGTH_SHORT).show();
            return;
        }


    }


}









