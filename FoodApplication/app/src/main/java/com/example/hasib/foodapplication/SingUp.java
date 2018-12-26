package com.example.hasib.foodapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hasib.foodapplication.Common.Common;
import com.example.hasib.foodapplication.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SingUp extends AppCompatActivity {
    EditText UserName,UserPhone,UserPassword;
    Button SingUp;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }                                                                                                     //02.ViewHolder Class


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/food.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );



        UserName=(MaterialEditText)findViewById(R.id.edtName);//Input name
        UserPhone=(MaterialEditText)findViewById(R.id.edtNumber);//input bnumber
        UserPassword=(MaterialEditText)findViewById(R.id.edtPassword);//input password

        SingUp=(Button)findViewById(R.id.btnSingUp);//button sing up for registation



        FirebaseDatabase db=FirebaseDatabase.getInstance(); //firebase Instence
        final DatabaseReference user_table=db.getReference("User"); //firebase reference for user child

        SingUp.setOnClickListener(new View.OnClickListener() {   //on click listener for dingup
            @Override
            public void onClick(View v) {
                final ProgressDialog mDilog=new ProgressDialog(SingUp.this);  //progressbardialog object create
                 mDilog.setMessage("Writing...");

                if(Common.usConnectionInternet(getBaseContext())) {  // checking internet connection ..


                    user_table.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(UserPhone.getText().toString()).exists()) { // if getUser phone is exists on firbase child class phone number
                                mDilog.dismiss();
                                Toast.makeText(getApplicationContext(), "Already exsist", Toast.LENGTH_SHORT).show();
                            } else {
                                mDilog.dismiss();
                                User user = new User(UserName.getText().toString(), UserPassword.getText().toString()); //put value in firebase database using User Class method
                                user_table.child(UserPhone.getText().toString()).setValue(user);  // userPhone will be childe name


                                Toast.makeText(getApplicationContext(), "Sing up Successfully", Toast.LENGTH_SHORT).show();
                                finish();
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
        });



    }
}
