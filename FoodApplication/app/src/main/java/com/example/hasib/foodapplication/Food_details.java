package com.example.hasib.foodapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.hasib.foodapplication.Common.Common;
import com.example.hasib.foodapplication.Database.DetailasDB;
import com.example.hasib.foodapplication.Model.Food;
import com.example.hasib.foodapplication.Model.Order;
import com.example.hasib.foodapplication.Model.Rating;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;
import com.stepstone.apprating.ratingbar.StarButton;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Created by HASIB on 11/21/2017.
 */

public class Food_details extends AppCompatActivity implements RatingDialogListener
{
    TextView food_name,food_price,food_discreption; //object create of food_name,food,price,fooddiscrteption
    ImageView foodImage;//object of foodImage

    CollapsingToolbarLayout collapsingToolbarLayout;  //object o CollapsingToolbarLayout

    CounterFab counterFab;  //object of floatingActionButton

    ElegantNumberButton elegantNumberButton;  // object of  ElegantNumberButton

    String food_id=""; // make a veriable for food_id for puting ref_key from foodList

    FirebaseDatabase db;  // object create for firebase Database

    DatabaseReference reference; // create a referance for database referance for food deteles

    DatabaseReference ratingRefarance;

    Food currentFood;// make a object of Food Class
    FloatingActionButton Fstrbutton;// make a objcet of FlotingActoionbar
    RatingBar ratbt;
    Button commentBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        db=FirebaseDatabase.getInstance();  //Firebase dtabase Instance

        reference=db.getReference("Foods");   //Firebase database referance where thr child class is Foods

        ratingRefarance=db.getReference("Rating");

        elegantNumberButton=(ElegantNumberButton)findViewById(R.id.numberbtn);   //initialished of ElegantNumberButton

        counterFab=(CounterFab) findViewById(R.id.floting_button);//initialished FlottingActionButton

        food_discreption=(TextView)findViewById(R.id.discreption);  //intialished of food discreption TextView

        food_name=(TextView)findViewById(R.id.food_name); //initialished food discreption of food_name

        food_price=(TextView)findViewById(R.id.money);   // initiualised food_price Object

        foodImage=(ImageView)findViewById(R.id.food_img); // initialished foodImage

        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.coordinatorLayout); //initialish collapsingToolbarLayout


        //problem setCollapsedTitleTextAppearance



        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);   //set setExpandedTitleTextAppearance

        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar); //setCollapsedTitleTextAppearance

        Fstrbutton=(FloatingActionButton)findViewById(R.id.rattingFlotingActionbar);

        ratbt=(RatingBar)findViewById(R.id.ratingBar) ;

        commentBtn=(Button)findViewById(R.id.commentShowBtn);


        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Food_details.this,FoodComment.class);
                in.putExtra(Common.UNIC_FOOD_ID,food_id);
                startActivity(in);
            }
        });




        Fstrbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //Coment and ratting Action dialog
                new AppRatingDialog.Builder()
                        .setPositiveButtonText("Submit")
                        .setNegativeButtonText("Cancle")
                        .setTitle("Rate the Food")
                        .setDefaultRating(1)
                        .setNoteDescriptions(Arrays.asList("Very Good","Vary Bad","Quite Ok","Excellent","Nice"))
                        .setDescriptionTextColor(R.color.colorPrimary)
                        .setCommentTextColor(R.color.colorPrimary)
                        .setCommentBackgroundColor(R.color.colorAccent)
                        .setHint("Please write your comment")
                        .setTitleTextColor(R.color.colorPrimary)
                        //.setWindowAnimation(R.style.RatingDialogButtonStyle)
                        .create(Food_details.this)
                        .show();

            }
        });



        counterFab.setOnClickListener(new View.OnClickListener() {     /// add data to the sqlliteDatabase
            @Override
            public void onClick(View v) {  // save to the sqllite database
                new DetailasDB(getBaseContext()).addToChart(new Order(food_id,  // pass the food id which are get from food list
                                currentFood.getName(),   //food name
                                elegantNumberButton.getNumber(), // number get from the elegantNumberButton
                                currentFood.getPrice(),  //  price
                                currentFood.getDiscount() ,
                                currentFood.getImage()//discount
                        )


                );
                Toast.makeText(getApplicationContext(),"Added Order",Toast.LENGTH_SHORT).show();
            }
        });


        counterFab.setCount(new DetailasDB(this).getCounterCart());


        if(getIntent() != null){     // if passing intent Value is not null
            food_id=getIntent().getStringExtra("FoodsId"); // the passing value put into the food_id variable
        }
        if(!food_id.isEmpty()){   // is passing intent or food_id is not empty
            getDetailsFood(food_id);     // call method getDetailsFood with the peramiter food_id for get all detils about food

            getRatingFood(food_id);
        }

    }

 private void getRatingFood(String food_id) {
        final Query foodRating=ratingRefarance.orderByChild("foodId").equalTo(food_id);
        foodRating.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count=0;
                int sum=0;
                for(DataSnapshot postChildren:dataSnapshot.getChildren()){
                     Rating item=postChildren.getValue(Rating.class);
                     sum+=Integer.parseInt(item.getRateValue());
                     Toast.makeText(getApplicationContext(),item.getComment(),Toast.LENGTH_SHORT).show();
                     count++;
                }
               if(count!=0){
                   float avarage=sum/count;
                   ratbt.setRating(avarage);
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getDetailsFood(String food_id) {
        reference.child(food_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentFood=dataSnapshot.getValue(Food.class);

                Picasso.with(getBaseContext()).load(currentFood.getImage()).into(foodImage);
                collapsingToolbarLayout.setTitle(currentFood.getName());
                food_price.setText(currentFood.getPrice());
                food_discreption.setText(currentFood.getDescription());
                food_name.setText(currentFood.getName());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



   public void onPositiveButtonClicked(int value,String comment) {

        final Rating rating=new Rating(Common.currentUser.getPhone(),food_id,String.valueOf(value),comment);


       ratingRefarance.push()
               .setValue(rating)
               .addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       Toast.makeText(getApplicationContext(),"Successfully Save Ratting",Toast.LENGTH_SHORT).show();
                   }
               });

        /*ratingRefarance.child(Common.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(Common.currentUser.getPhone()).exists()){
                    ratingRefarance.child(Common.currentUser.getPhone()).removeValue();

                    ratingRefarance.child(Common.currentUser.getPhone()).setValue(rating);


                }else {
                    ratingRefarance.child(Common.currentUser.getPhone()).setValue(rating);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/

    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onNeutralButtonClicked() {

    }
}