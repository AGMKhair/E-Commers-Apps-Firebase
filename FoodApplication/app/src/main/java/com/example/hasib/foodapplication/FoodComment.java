package com.example.hasib.foodapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hasib.foodapplication.Common.Common;
import com.example.hasib.foodapplication.Model.Rating;
import com.example.hasib.foodapplication.ViewHolder.showCmmentViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FoodComment extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference ratingdbref;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView.LayoutManager layoutManager;
    String foodId="food_id";

    FirebaseRecyclerAdapter<Rating,showCmmentViewHolder> adepter;




    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }//02.ViewHolder Class



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/food.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );



        setContentView(R.layout.activity_food_comment);
        firebaseDatabase=FirebaseDatabase.getInstance();
        ratingdbref=firebaseDatabase.getReference("Rating");

        recyclerView=(RecyclerView)findViewById(R.id.recycular_view);
       /* layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);*/

        layoutManager = new LinearLayoutManager(this);      //layout mananager
        recyclerView.setLayoutManager(layoutManager);   //set Layout Manager
        recyclerView.setHasFixedSize(true);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(true);
                if (getIntent()!=null){
                    foodId=getIntent().getStringExtra(Common.UNIC_FOOD_ID);

                }
                if (!foodId.isEmpty() && foodId !=null){

                    Query query=ratingdbref.orderByChild("foodId").equalTo(foodId);

                    FirebaseRecyclerOptions<Rating> options=new FirebaseRecyclerOptions.Builder<Rating>()
                            .setQuery(query,Rating.class)
                            .build();

                    adepter=new FirebaseRecyclerAdapter<Rating, showCmmentViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull showCmmentViewHolder holder, int position, @NonNull Rating model) {

                        float rat=  Float.parseFloat(model.getRateValue());

                            Toast.makeText(getApplicationContext(), "Ratting :-" + rat, Toast.LENGTH_SHORT).show();


                         holder.user_ratting.setRating(rat);
                         holder.user_comment.setText(model.getComment());
                         holder.user_number.setText(model.getUserPhone());



                        }

                        @Override
                        public showCmmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_show_style,parent,false);
                            return new showCmmentViewHolder(view);
                        }
                    };
                    loadComment(foodId);
                }
            }
        });


       swipeRefreshLayout.post(new Runnable() {
           @Override
           public void run() {

               swipeRefreshLayout.setRefreshing(true);


               if (getIntent()!=null){
                   foodId=getIntent().getStringExtra(Common.UNIC_FOOD_ID);

               }
               if (!foodId.isEmpty() && foodId !=null){

                   Query query=ratingdbref.orderByChild("foodId").equalTo(foodId);

                   FirebaseRecyclerOptions<Rating> options=new FirebaseRecyclerOptions.Builder<Rating>()
                           .setQuery(query,Rating.class)
                           .build();

                   adepter=new FirebaseRecyclerAdapter<Rating, showCmmentViewHolder>(options) {
                       @Override
                       protected void onBindViewHolder(@NonNull showCmmentViewHolder holder, int position, @NonNull Rating model) {
                           holder.user_ratting.setRating(Float.parseFloat(model.getRateValue()));
                           holder.user_comment.setText(model.getComment());
                           holder.user_number.setText(model.getUserPhone());



                       }

                       @Override
                       public showCmmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



                           View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_show_style,parent,false);
                           return new showCmmentViewHolder(view);
                       }
                   };
                   loadComment(foodId);
               }

           }
       });


    }



    private void loadComment(String foodId) {

        adepter.startListening();

        recyclerView.setAdapter(adepter);
        swipeRefreshLayout.setRefreshing(false);
    }






}
