package com.example.hasib.foodapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.hasib.foodapplication.Common.Common;
import com.example.hasib.foodapplication.Database.DetailasDB;
import com.example.hasib.foodapplication.Interface.ItemClickListener;
import com.example.hasib.foodapplication.Model.Favourite;
import com.example.hasib.foodapplication.Model.Food;
import com.example.hasib.foodapplication.Model.Order;
import com.example.hasib.foodapplication.ViewHolder.FoodViewHolder;
//import com.facebook.CallbackManager;
//import com.facebook.share.model.SharePhoto;
//import com.facebook.share.model.SharePhotoContent;
//import com.facebook.share.widget.ShareDialog;
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
import com.rey.material.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.mancj.materialsearchbar.R.id.imageView;

public class FoodList extends AppCompatActivity {
    RecyclerView recyclerView;       // make object of RecycularView
    RecyclerView.LayoutManager layoutManager;  // make object of RecycularView LayoutManager
    FirebaseDatabase firebaseDatabase;  // // make object of FirebaseDatbase
    DatabaseReference food_referance;  // // make object of Database Referance

    SwipeRefreshLayout swipeRefreshLayout;


    String catagoryId=""; //make a veriable for put catagory key which was pass from Home class


    FirebaseRecyclerAdapter<Food,FoodViewHolder> adapter;     // make a adepter for showing Food picture and title

    FirebaseRecyclerAdapter<Food,FoodViewHolder> serchAdepter;

    MaterialSearchBar materialSearchBar;
    List<String> foodSuggestName=new ArrayList<>();
     DetailasDB detailasDB;


    ///Sqllitedatabase Instenc Object
    CallbackManager callbackManager;
    ShareDialog shareDialog;

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
    protected void onResume() {
        super.onResume();
        if (adapter!=null){
            adapter.startListening();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onStart() {
        super.onStart();
        loadFood(catagoryId);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        recyclerView = (RecyclerView) findViewById(R.id.menu_list);//initilished recycularView object


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/food.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

     /*   swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (getIntent() != null) {       //if the passing data is not null
                    catagoryId = getIntent().getStringExtra("CategoryId");    //put the passing value or key in the  catagoryId object
                    Toast.makeText(getApplicationContext(),catagoryId,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication(), "Wrong", Toast.LENGTH_SHORT).show();
                }
                if (!catagoryId.isEmpty() && catagoryId != null) {//if the passing data is not null

                    if(Common.usConnectionInternet(getBaseContext())) {     //check internet connnection

                        loadFood(catagoryId);      // call m,ethod load foood with paramiter catagoryId  which bear the key value

                    }else {

                        Toast.makeText(getApplicationContext(),"Please check your internet connection", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
            }
        });


        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {


                if (getIntent() != null) {       //if the passing data is not null
                    catagoryId = getIntent().getStringExtra("CategoryId");    //put the passing value or key in the  catagoryId object
                    Toast.makeText(getApplicationContext(),catagoryId,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication(), "Wrong", Toast.LENGTH_SHORT).show();
                }
                if (!catagoryId.isEmpty() && catagoryId != null) {//if the passing data is not null

                    if(Common.usConnectionInternet(getBaseContext())) {     //check internet connnection

                        loadFood(catagoryId);      // call m,ethod load foood with paramiter catagoryId  which bear the key value

                    }else {

                        Toast.makeText(getApplicationContext(),"Please check your internet connection", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }



            }
        });

*/
        callbackManager =CallbackManager.Factory.create();
        shareDialog=new ShareDialog(this);

          //initialished the firebase database referance whwere the child name is Foods
        layoutManager = new LinearLayoutManager(this);      //layout mananager
        recyclerView.setLayoutManager(layoutManager);   //set Layout Manager
        recyclerView.setHasFixedSize(true);


      /*  LayoutAnimationController controller= AnimationUtils.
                loadLayoutAnimation(recyclerView.getContext(),R.anim.animation_falldown_for_home);
        recyclerView.setLayoutAnimation(controller);

*/

        detailasDB=new DetailasDB(getApplicationContext());
        // initialish the Sqllite database     object


        if (getIntent() != null) {       //if the passing data is not null
            catagoryId = getIntent().getStringExtra("CategoryId");    //put the passing value or key in the  catagoryId object
            Toast.makeText(getApplicationContext(),catagoryId,Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplication(), "Wrong", Toast.LENGTH_SHORT).show();
        }

        if (!catagoryId.isEmpty() && catagoryId != null) {//if the passing data is not null

            if(Common.usConnectionInternet(getBaseContext())) {     //check internet connnection

                loadFood(catagoryId);      // call m,ethod load foood with paramiter catagoryId  which bear the key value

            }else {

                Toast.makeText(getApplicationContext(),"Please check your internet connection", Toast.LENGTH_SHORT).show();
                return;
            }

        }



        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter Food Name");
        loadSuggest();
        materialSearchBar.setLastSuggestions(foodSuggestName);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

                if (!enabled) {
                    recyclerView.setAdapter(adapter);
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

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               List<String> suggetion = new ArrayList<String>();
                for (String search : foodSuggestName) {

                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())) ;

                    suggetion.add(search);

                }
                materialSearchBar.setLastSuggestions(suggetion);


            }

            @Override
            public void afterTextChanged(Editable s) {

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
                        Intent in = new Intent(FoodList.this, Food_details.class);
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





    private void loadFood(String catagoryId) {

        // showing data to the screen of Food List activity


        firebaseDatabase = FirebaseDatabase.getInstance();    // initialish the firebase datbase Instance
        food_referance = firebaseDatabase.getReference("Foods");

        Query quryById= food_referance.orderByChild("menuId").equalTo(catagoryId);

        FirebaseRecyclerOptions<Food> option=new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(quryById,Food.class)
                .build();


        adapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull final FoodViewHolder viewHolder, final int position, @NonNull final Food model) {

                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.food_img);
                viewHolder.price.setText(model.getPrice());

                viewHolder.food_title.setText(model.getName());

                 boolean value=   detailasDB.isFavourite(adapter.getRef(position).getKey());


                boolean value2=   detailasDB.isFavourite(adapter.getRef(position).getKey());

                Toast.makeText(getApplicationContext(),""+value,Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),""+value2,Toast.LENGTH_SHORT).show();



               /* if(){

                    viewHolder.food_fav.setImageResource(R.drawable.ic_favorite_black_24dp);



                }else {
                   // detailasDB.DeleteToFavourite(adapter.getRef(position).getKey());
                    viewHolder.food_fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);

                   // Toast.makeText(getApplicationContext(), "" + model.getName() + " delete to favurite", Toast.LENGTH_SHORT).show();
                }*/




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





             /*   if(detailasDB.isFavourite(adapter.getRef(position).getKey())){
                    //   viewHolder.food_fav.setImageResource(R.drawable.ic_favorite_black_24dp);  //add favourite
                }*/


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
                       }

                        else {
                            detailasDB.DeleteToFavourite(adapter.getRef(position).getKey());
                            viewHolder.food_fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);

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

                        Intent in = new Intent(FoodList.this, Food_details.class);   //intent this class to Food_details class
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




  /*  @Override
    protected void onStart() {
        super.onStart();
        adapter.stopListening();
    }*/



    private void loadSuggest(){

        food_referance.orderByChild("menuId").equalTo(catagoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnap:dataSnapshot.getChildren()){
                    Food food=dataSnap.getValue(Food.class);
                    foodSuggestName.add(food.getName());


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
         adapter.stopListening();
        // serchAdepter.stopListening();
    }
}


