package com.example.hasib.foodapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;


import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.hasib.foodapplication.Common.Common;
import com.example.hasib.foodapplication.Database.DetailasDB;
import com.example.hasib.foodapplication.Interface.ItemClickListener;
import com.example.hasib.foodapplication.Model.Banner;
import com.example.hasib.foodapplication.Model.Category;
import com.example.hasib.foodapplication.Model.Order;
import com.example.hasib.foodapplication.Model.Request;
import com.example.hasib.foodapplication.Model.Token;

import com.example.hasib.foodapplication.ViewHolder.OrderViewHolder;
import com.example.hasib.foodapplication.ViewHolder.menuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

//import info.hoang8f.widget.FButton;
import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.example.hasib.foodapplication.R.id.favouriteFood;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseDatabase database; //firebase Database ObjectCreate
    DatabaseReference  category; //create F irebase Referance Object
    TextView txtFullName;   //Text View Object
    RecyclerView recyclerView;  //RwecyCularView Object
    RecyclerView.LayoutManager layoutManager;  //RecycularView LayoutManager
    SwipeRefreshLayout swipeRefreshLayout;
    CounterFab fab;
    SliderLayout sliderLayout;
    HashMap<String,String> imagelist;

    FirebaseRecyclerAdapter<Category,menuViewHolder> adepter; //RecycuarView Adepter where are two parmiter o1. is Model

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }//02.ViewHolder Class



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); //that is ac tivity home Layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //Toolbar Object
        toolbar.setTitle("Menu");  //
        setSupportActionBar(toolbar);///title set




        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/food.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );



       /* swipeRefreshLayout=(SwipeRefreshLayout) findViewById(R.id.swipe_layout);

        *//*swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);


        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);*//*

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Common.usConnectionInternet(getBaseContext())) {//check internet
                    loadMenu();   //Load  Menu Menthod
                }else {

                //    Toast.makeText(getBaseContext(),"Please check your internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if(Common.usConnectionInternet(getBaseContext())) {//check internet
                    loadMenu();   //Load  Menu Menthod
                }else {

                //    Toast.makeText(getApplicationContext(),"Please check your internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });*/
        Paper.init(this);
        

        fab = (CounterFab) findViewById(R.id.fab); ///initialished of FloatingButton
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   Intent in=new Intent(Home.this,Chart.class);  //go to this class to Chart Class;
                startActivity(in);

            }
        });

        fab.setCount(new DetailasDB(this).getCounterCart());



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout); //nitialished of DrawerLayout
        //ActionBarDrawerToggle toggle=new ActionBarDrawerToggle()
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View nabView = navigationView.getHeaderView(0);
        txtFullName = (TextView) nabView.findViewById(R.id.fullTextName);  // set The name of the navView
        txtFullName.setText(Common.currentUser.getName());
        recyclerView=(RecyclerView)findViewById(R.id.recycular_menu); //intialished recycular view

        //layoutManager=new LinearLayoutManager(this);  //LinearLayout Manager
      //  recyclerView.setLayoutManager(layoutManager);  // setLayoutManager

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));






       /* if (Common.usConnectionInternet(getApplication())){
            loadMenu();
        }else {
            Toast.makeText(getApplicationContext(),"Please Check the Internet connection",Toast.LENGTH_SHORT);
        }*/


        LayoutAnimationController controller= AnimationUtils.
                loadLayoutAnimation(recyclerView.getContext(),R.anim.animation_falldown_for_home);
        recyclerView.setLayoutAnimation(controller);


        database = FirebaseDatabase.getInstance(); //initialished  of Firebase instamnce
        category = database.getReference("Category");//set referance with child name from  FirebaseDatbase whare child name is Category




        FirebaseRecyclerOptions<Category> option=new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(category,Category.class)
                .build();


        adepter=new FirebaseRecyclerAdapter<Category, menuViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull menuViewHolder holder, final int position, @NonNull Category model) {

                holder.titile.setText(model.getName()); // set Title  in the menu_item layout
                //  viewHolder.favourite.setBackgroundColor(Color.BLUE);
                Picasso.with(getBaseContext()).load(model.getImage()).into(holder.imag); //setImage  in menu_item layout
                final Category clickItem=model;



                holder.setItemClickListener(new ItemClickListener() { //item_click
                    @Override
                    public void OnClick(View view, int possisiton, boolean isLongClick) {
                        // Toast.makeText(Home.this,""+clickItem.getName(),Toast.LENGTH_SHORT).show();
                        Intent in=new Intent(Home.this,FoodList.class);        // intent this class to FoodList class

                        in.putExtra("CategoryId",adepter.getRef(position).getKey()); //pass possition key

                        // viewHolder.favourite.setImageResource(Integer.parseInt(String.valueOf(Common.convertCodeToImag(model.getFood_img()))));
                        startActivity(in);





                    }
                });


            }

            @NonNull
            @Override
            public menuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View itemview=LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.menu_item,parent,false);
                return  new menuViewHolder(itemview);
            }
        };

       loadMenu();

       setupSlider();



        updataeToken(FirebaseInstanceId.getInstance().getToken());


    }

    private void setupSlider() {

        sliderLayout=(SliderLayout)findViewById(R.id.slider);
        imagelist=new HashMap<>();
        final DatabaseReference banners=database.getReference("Bannar");

        banners.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshort:dataSnapshot.getChildren()){

                    Banner banner=postSnapshort.getValue(Banner.class);

                    imagelist.put(banner.getName()+"@@@"+banner.getId(),banner.getImage());
                }

                for (String key:imagelist.keySet()){
                    String[] keySplit=key.split("@@@");
                    String nameOfFood=keySplit[0];
                    String idOfImage=keySplit[1];

                     final TextSliderView textSliderView=new TextSliderView(getBaseContext());
                     textSliderView
                            .description(nameOfFood)
                            .image(imagelist.get(key))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                     Intent intent=new Intent(Home.this,Food_details.class);
                                     intent.putExtras(textSliderView.getBundle());

                                    startActivity(intent);
                                }
                            });
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("FoodsId",idOfImage);

                    sliderLayout.addSlider(textSliderView);
                   banners.removeEventListener(this);



                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);

    }


    private void loadMenu() {

        recyclerView.setAdapter(adepter);
        adepter.startListening();

        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();

        //  swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setCount(new DetailasDB(this).getCounterCart());
        if (adepter!=null){
            adepter.startListening();
        }
    }

    private void  updataeToken(String token) {

        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference referance=db.getReference("Token");
        Token toke=new Token(token,false);
        referance.child(Common.currentUser.getPhone()).setValue(toke);

    }



    @Override
    protected void onStop() {
        super.onStop();
        adepter.stopListening();
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.


        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id==R.id.item_search){
           startActivity(new Intent(Home.this,Search_activity.class));
        }


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {

        } else if (id == R.id.addyourAdress) {
              //  changePasswordDialog();
            showAddHomeAddressDialog();

        } else if (id == R.id.chart) {

        } else if (id == R.id.cart) {
            Intent in=new Intent(Home.this,Chart.class);
            startActivity(in);



        } else if (id == R.id.order) {
            Intent in=new Intent(Home.this, OrderStatus.class);
            startActivity(in);

        } else if (id == R.id.exit) {
            Paper.book().destroy();

            Intent in=new Intent(Home.this,LogIn.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(in);

        }
        else if(id==R.id.suscrib){
           showDialogForSuscribe();
        }
        else if(id==R.id.favouriteFood){
            startActivity(new Intent(Home.this,FavouriteFood.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showDialogForSuscribe() {


        final AlertDialog.Builder alrt=new AlertDialog.Builder(Home.this);
        alrt.setTitle("Check Box To Suscribation");
        alrt.setMessage("You will get notification for any update");
        final LayoutInflater inflater=LayoutInflater.from(this);
        View v=inflater.inflate(R.layout.suscripr_layout_desig,null);

        final CheckBox checkBox=(CheckBox)v.findViewById(R.id.Subscribe_box) ;
        Paper.init(this);

        String isSuscripe=Paper.book().read("sub_news");

        if (isSuscripe==null|| TextUtils.isEmpty(isSuscripe)||isSuscripe.equals("false")){
            checkBox.setChecked(false);

        }else {
            checkBox.setChecked(true);
        }

        // final FButton save=(FButton)changePassword.findViewById(R.id.save) ;
        alrt.setView(v);
        alrt.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();

                if (checkBox.isChecked()){

                    FirebaseMessaging.getInstance().subscribeToTopic(Common.topicName);
                    Paper.book().write("sub_news","true");

                }else {

                    FirebaseMessaging.getInstance().unsubscribeFromTopic(Common.topicName);
                    Paper.book().write("sub_news","flse");

                }





            }



        });
        alrt.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        alrt.show();




    }

    private void showAddHomeAddressDialog() {



        final AlertDialog.Builder alrt=new AlertDialog.Builder(Home.this);
        alrt.setTitle("Home Adress");
        alrt.setMessage("Please add your Home Adress");
        final LayoutInflater inflater=LayoutInflater.from(this);
        View home_adress=inflater.inflate(R.layout.home_address,null);

        final MaterialEditText inputHomeAdress=(MaterialEditText)home_adress.findViewById(R.id.addHomeAdress);

        // final FButton save=(FButton)changePassword.findViewById(R.id.save) ;
        alrt.setView(home_adress);
        alrt.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {

                dialogInterface.dismiss();
                Common.currentUser.setHomeAdress(inputHomeAdress.getText().toString());

                     FirebaseDatabase.getInstance().getReference("User")
                             .child(Common.currentUser.getPhone())
                             .setValue(Common.currentUser)
                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {

                                     Toast.makeText(getApplicationContext(),"Successfully Add Home Address",Toast.LENGTH_SHORT).show();

                                 }
                             });




                    }



        });
        alrt.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


        alrt.show();





    }

    private void changePasswordDialog() {
        final AlertDialog.Builder alrt=new AlertDialog.Builder(Home.this);
        alrt.setTitle("CHANGE PASSWORD");
        alrt.setMessage("Please fill all infromation");
        final LayoutInflater inflater=LayoutInflater.from(this);
        View changePassword=inflater.inflate(R.layout.change_password,null);

        final MaterialEditText oldPassword=(MaterialEditText)changePassword.findViewById(R.id.presentPassword);
        final  MaterialEditText newPassword=(MaterialEditText)changePassword.findViewById(R.id.newPassword);
        final  MaterialEditText ReNewPassword=(MaterialEditText)changePassword.findViewById(R.id.confromPassword);
       // final FButton save=(FButton)changePassword.findViewById(R.id.save) ;
        alrt.setView(changePassword);
        alrt.setPositiveButton("SAE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
              //  dialogInterface.dismiss();

                if(newPassword.getText().toString().equals(ReNewPassword.getText().toString()))  {
                    if(oldPassword.getText().toString().equals(Common.currentUser.getPassword())){
                        //update database
                      Map<String,Object> passwordupdate=new HashMap<>();
                      passwordupdate.put("password",newPassword.getText().toString());
                      DatabaseReference user=database.getReference("User");
                      user.child(Common.currentUser.getPhone()).updateChildren(passwordupdate)
                              .addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {
                                      dialogInterface.dismiss();
                                      Toast.makeText(getApplicationContext(),"Successfully change password",Toast.LENGTH_SHORT).show();

                                  }
                              })
                              .addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {
                                      Toast.makeText(getApplicationContext(),"Fail !!!",Toast.LENGTH_SHORT).show();
                                  }
                              });

                    }

                }else {

                    Toast.makeText(getApplicationContext(),"Please set password in the box",Toast.LENGTH_SHORT).show();
                }
            }
        });
        alrt.setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });


    alrt.show();

    }


    protected void onStart() {
        super.onStart();

    }

}
