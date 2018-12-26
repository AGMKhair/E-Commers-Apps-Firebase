package com.example.hasib.foodapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hasib.foodapplication.Common.Common;
import com.example.hasib.foodapplication.Database.DetailasDB;
import com.example.hasib.foodapplication.Interface.ItemClickListener;
import com.example.hasib.foodapplication.Model.Favourite;
import com.example.hasib.foodapplication.Model.Food;
import com.example.hasib.foodapplication.Model.Order;
import com.example.hasib.foodapplication.ViewHolder.FoodViewHolder;
import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Search_activity extends AppCompatActivity {

    RecyclerView recyclerView;       // make object of RecycularView
    RecyclerView.LayoutManager layoutManager;  // make object of RecycularView LayoutManager
    FirebaseDatabase firebaseDatabase;  // // make object of FirebaseDatbase
    DatabaseReference food_referance;
    // // make object of Database Referance
    FirebaseRecyclerAdapter<Food,FoodViewHolder> serchAdepter;
    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;

    MaterialSearchBar materialSearchBar;
    List<String> foodSuggestName=new ArrayList<>();
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    DetailasDB detailasDB;

    Target target=new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto photo=new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            if (ShareDialog.canShow(SharePhotoContent.class)){
                SharePhotoContent content=new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();

                shareDialog.show(content);
            }

            // from.compareTo(1);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
        adapter.stopListening();

    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.startListening();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_activity);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/food.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        firebaseDatabase = FirebaseDatabase.getInstance();
        food_referance = firebaseDatabase.getReference("Foods");


        detailasDB=new DetailasDB(getApplicationContext());

        recyclerView = (RecyclerView) findViewById(R.id.recycular_search);//initilished recycularView object
        callbackManager = CallbackManager.Factory.create();
        shareDialog=new ShareDialog(this);

        //initialished the firebase database referance whwere the child name is Foods
        layoutManager = new LinearLayoutManager(this);      //layout mananager
        recyclerView.setLayoutManager(layoutManager);   //set Layout Manager
        recyclerView.setHasFixedSize(true);


        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter Food Name");
        loadSuggest();
        materialSearchBar.setLastSuggestions(foodSuggestName);
        materialSearchBar.setCardViewElevation(10);

        loadFood();

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

                if (!enabled) {

                    recyclerView.setAdapter(adapter);
                    //serchAdepter.startListening();
                }

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                startSerch(text);


            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSerch(CharSequence text) {
        Query nameQury=food_referance.orderByChild("name").equalTo(text.toString());

        FirebaseRecyclerOptions<Food> food_option=new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(nameQury,Food.class)
                .build();

        serchAdepter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(food_option) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder viewHolder, final int position, @NonNull Food model) {

                viewHolder.food_title.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_img);

                // final Food food=model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int possisiton, boolean isLongClick) {
                        Intent in = new Intent(Search_activity.this, Food_details.class);
                        in.putExtra("FoodsId", adapter.getRef(position).getKey());
                        startActivity(in);
                    }
                });
            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View viewItem= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
                return new FoodViewHolder(viewItem);
            }
        };
        recyclerView.setAdapter(serchAdepter);
        serchAdepter.startListening();

        // serchAdepter.startListening();
    }

    private void loadSuggest(){

        food_referance.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnap:dataSnapshot.getChildren()){
                    Food food=dataSnap.getValue(Food.class);
                    foodSuggestName.add(food.getName());


                }
                materialSearchBar.setLastSuggestions(foodSuggestName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void loadFood() {

        // showing data to the screen of Food List activity


          // initialish the firebase datbase Instance

        Query quryById= food_referance;

        FirebaseRecyclerOptions<Food> option=new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(quryById,Food.class)
                .build();


        adapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull final FoodViewHolder viewHolder, final int position, @NonNull final Food model) {

                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_img);
                viewHolder.price.setText(model.getPrice());

                viewHolder.food_title.setText(model.getName());

                viewHolder.countfb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new DetailasDB(getBaseContext()).addToChart(new Order(
                                adapter.getRef(position).getKey(),  // pass the food id which are get from food list
                                model.getName(),   //food name
                                "1", // number get from the elegantNumberButton
                                model.getPrice(),  //  price
                                model.getDiscount() , //discount
                                model.getImage()
                        ));

                    }
                });




                if(detailasDB.isFavourite(adapter.getRef(position).getKey())){
                    //   viewHolder.food_fav.setImageResource(R.drawable.ic_favorite_black_24dp);  //add favourite
                }

                viewHolder.food_fav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Favourite favourite=new Favourite();

                        favourite.setFood_id(adapter.getRef(position).getKey());
                        favourite.setFood_image(model.getImage());
                        favourite.setFood_name(model.getName());
                        favourite.setFood_price(model.getPrice());
                        favourite.setFood_description(model.getDescription());
                        favourite.setFood_menu_id(model.getMenuId());
                        favourite.setFood_discount(model.getDiscount());
                        favourite.setUser_number(Common.currentUser.getPhone());



                        //changing
                        if(!detailasDB.isFavourite(adapter.getRef(position).getKey())){
                            detailasDB.AddToFavourite(favourite);
                            viewHolder.food_fav.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(getApplicationContext(), "" + model.getName() + " is Add to the Favourite", Toast.LENGTH_SHORT).show();
                        }else {
                            detailasDB.DeleteToFavourite(adapter.getRef(position).getKey());
                            viewHolder.food_fav.setImageResource(R.drawable.ic_favorite_black_24dp);

                            Toast.makeText(getApplicationContext(), "" + model.getName() + " delete to favurite", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                viewHolder.food_share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(),"Share In Faebook",Toast.LENGTH_SHORT).show();
                        Picasso.with(getApplicationContext()).load(model.getImage()).into(target);

                       /* Picasso p=Picasso.with(getApplicationContext());
                        RequestCreator r=p.load(model.getImage());
                        r.into(target);
*/



                    }
                });


                final Food food = model;

                viewHolder.setItemClickListener(new ItemClickListener() {      // setItemClickListener
                    @Override
                    public void OnClick(View view, int possisiton, boolean isLongClick) {

                        Intent in = new Intent(Search_activity.this, Food_details.class);   //intent this class to Food_details class
                        in.putExtra("FoodsId", adapter.getRef(position).getKey());       //pass data or refaranceId to the Food_details class
                        startActivity(in);
                    }
                });




            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View viewItem= LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);



                return new FoodViewHolder(viewItem);
            }
        };
        recyclerView.setAdapter(adapter);
        adapter.startListening();





       /* recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();*/

//        swipeRefreshLayout.setRefreshing(false);

    }


}
