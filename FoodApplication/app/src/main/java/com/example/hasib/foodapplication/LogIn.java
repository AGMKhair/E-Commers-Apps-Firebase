package com.example.hasib.foodapplication;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hasib.foodapplication.Common.Common;
import com.example.hasib.foodapplication.Model.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Queue;
import java.util.zip.Inflater;

import io.paperdb.Paper;

public class LogIn extends AppCompatActivity {
    EditText phone,password; // variable create for name and password
    Button Login;// veriable create for LogIn bnutton

    CheckBox checkBox; //veriable create for checkbox
    MaterialEditText forgetPass;     //veriable create for forget password
    CallbackManager callbackManager;
    String fb_email;
    String fb_img_resource;

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkBox=(CheckBox)findViewById(R.id.checkbox); //initialisd checkbox
        forgetPass=(MaterialEditText)findViewById(R.id.forgetPass) ;////initialisd forget password

        setContentView(R.layout.activity_log_in); //this content view is activity log in
        phone=(MaterialEditText)findViewById(R.id.number); //initialise name
        password=(MaterialEditText)findViewById(R.id.Password);// initialised the password
        Login=(Button)findViewById(R.id.buttonSingIn); //initialished the log in button

        Paper.init(this); //////////////////////////////problem

        FirebaseDatabase db=FirebaseDatabase.getInstance(); //get instenche of firebase database
        final DatabaseReference user_table=db.getReference("User");// get referance of firebase database with child USER
        callbackManager=CallbackManager.Factory.create();

        LoginButton loginButton=(LoginButton) findViewById(R.id.fb_logIn);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final ProgressDialog progressDialog=new ProgressDialog(LogIn.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                String token=loginResult.getAccessToken().getToken();

                GraphRequest request=GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        progressDialog.dismiss();

                       getdata(object);

                    }
                });

                Bundle parameters=new Bundle();
                parameters.putString("id","email");
                request.setParameters(parameters);
                request.executeAsync();
            }




            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });



       /* forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alrt=new AlertDialog.Builder(LogIn.this);
                alrt.setMessage("Forget Password?");
                alrt.setTitle("Type Security code");
               // LayoutInflater inflater=new
            }
        });*/

       if (AccessToken.getCurrentAccessToken()!=null){
           Toast.makeText(getApplicationContext(),AccessToken.getCurrentAccessToken().getUserId(),Toast.LENGTH_SHORT).show();
       }


        Login.setOnClickListener(new View.OnClickListener() {





            @Override
            public void onClick(View v) { //log in button clickListener

                if (Common.usConnectionInternet(getBaseContext())) {// check interner connection



                    final ProgressDialog mDilog = new ProgressDialog(LogIn.this); //progress bar dialog object create
                     mDilog.setMessage("Writing..."); //progress bar message

                    user_table.addListenerForSingleValueEvent(new ValueEventListener() {   // referance with addValue Even Listener

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(phone.getText().toString()).exists()) {   ///if getNumber from userInput is exist with firebase Child Name




                                mDilog.dismiss();
                                User user = dataSnapshot.child(phone.getText().toString()).getValue(User.class);// getAll vale from firebaseDatabase and put in user Cass
                                user.setPhone(phone.getText().toString());   ///set Phone number to the user Class
                                if (user.getPassword().equals(password.getText().toString())) {  ///check password

                                    Intent in = new Intent(LogIn.this, Home.class);  //intent object for leave the class and go to Home class

                                    Common.currentUser = user;    //put user class all vale to the currentUser object of the Common Class
                                    startActivity(in);

                                    user_table.removeEventListener(this);



                                    Toast.makeText(getApplicationContext(), user.getPhone(), Toast.LENGTH_SHORT).show();
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
        });




      /*  DatabaseReference re=user_table.child(us.getPhone());
        DatabaseReference pd=re.child("password");
        pd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String data=dataSnapshot.toString();
               // Toast.makeText()

                 Toast.makeText(getApplicationContext(),"Password is "+data,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

*/


    }

    private void getdata(JSONObject object) {
        try {
           // URL profile_picture=new URL("https://graph.facebook.com/"+object.getString("id")+"/picture?width=250&height=250");
            fb_email=object.getString("email");
           // fb_img_resource=profile_picture.toString();


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
