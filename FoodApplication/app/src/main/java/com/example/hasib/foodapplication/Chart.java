package com.example.hasib.foodapplication;

import android.*;
import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hasib.foodapplication.Common.Common;
import com.example.hasib.foodapplication.Database.DetailasDB;
import com.example.hasib.foodapplication.Model.Myresponce;
import com.example.hasib.foodapplication.Model.Notification;
import com.example.hasib.foodapplication.Model.Order;
import com.example.hasib.foodapplication.Model.Request;
import com.example.hasib.foodapplication.Model.Sender;
import com.example.hasib.foodapplication.Model.Token;
import com.example.hasib.foodapplication.Remote.APIService;
import com.example.hasib.foodapplication.Remote.IGoogleService;
import com.example.hasib.foodapplication.ViewHolder.chartAdepter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.example.hasib.foodapplication.R.id.cart;

import static com.example.hasib.foodapplication.R.id.order;

public class Chart extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private String TAG = "Problem";


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }                                                                                                     //02.ViewHolder Class


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferenc;
    public TextView showPrice;
    Button sendOder;
    List<Order> chart = new ArrayList<>();
    chartAdepter adepter;
    APIService mServer;
    Place shippingPlace;
    IGoogleService mGoogleService;
  //  Location mLastlocation;
   // LocationManager mLocationManager;
    String address;
    String lattitude;
    String longgitude;


    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClint;
    private LocationRequest mLocationRequest;



    private static final int PLAY_SERVICE_RESULATION_REQUEST=1000;
    private static final int LOCATION_PERMISSION_REQUEST=1001;
    private static final int UPDATE_INTERVAAL=5000;
    private static final int FATEST_INTERVAL=5001;
    private static final int DISPLAY_ACMENT=5002;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/food.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );







        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            requestRunTimePermission();

        }else {

            if(checkPlayService()){
                builGoogleApiClint();
                createLocationRequst();
            }
        }




        mGoogleService=Common.getGoogleMapApi();
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReferenc=firebaseDatabase.getReference("Request");

        mServer=Common.getFCMservice();

        recyclerView=(RecyclerView)findViewById(R.id.listChart);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        showPrice=(TextView)findViewById(R.id.totalAmount);
        sendOder =(Button)findViewById(R.id.btnPlaceOrder);

        sendOder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chart.size()>0) {
                    showAlertdialog();
                }else {
                    Toast.makeText(getApplicationContext(),"Chart is empty",Toast.LENGTH_SHORT).show();
                }

            }

        });


        
        loadListFood();//problem

    }






    private void requestRunTimePermission() {

        ActivityCompat.requestPermissions(this,new String[]{

                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION

                },LOCATION_PERMISSION_REQUEST
        );

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (checkPlayService()) {
                        builGoogleApiClint();
                        createLocationRequst();
                        displayLocation();
                    }


                }
                break;

        }
    }

    private boolean checkPlayService() {

        int requestCode= GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(requestCode != ConnectionResult.SUCCESS){

            if(GooglePlayServicesUtil.isUserRecoverableError(requestCode)){
                GooglePlayServicesUtil.getErrorDialog(requestCode,this,PLAY_SERVICE_RESULATION_REQUEST).show();

            }else {
                Toast.makeText(getApplicationContext(),"This device dosenot support",Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }

        return true;
    }

    protected synchronized void builGoogleApiClint() {

        mGoogleApiClint=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClint.connect();

    }

    private void createLocationRequst() {
        mLocationRequest=new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLAY_ACMENT);
    }






    private void showAlertdialog() {

        final AlertDialog.Builder alrt=new AlertDialog.Builder(Chart.this);
        alrt.setTitle("One More Step");
        alrt.setMessage("Enter Your Adress And Comment");
        //final EditText editAdress=new EditText(Chart.this);

        LayoutInflater inflater=this.getLayoutInflater();
        final View comment_address=inflater.inflate(R.layout.order_address_comment,null);

       // final MaterialEditText edtAddress=(MaterialEditText)comment_address.findViewById(R.id.adress);

        final PlaceAutocompleteFragment edtAdress=(PlaceAutocompleteFragment)getFragmentManager().
                findFragmentById(R.id.places_autocomplete_fragment);
        edtAdress.getView().findViewById(R.id.place_autocomplete_search_button).setVisibility(View.GONE);
        ((EditText)edtAdress.getView().findViewById(R.id.place_autocomplete_search_input)).setHint("Enter The Place Name");
        ((EditText)edtAdress.getView().findViewById(R.id.place_autocomplete_search_input)).setTextSize(14);

        edtAdress.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                shippingPlace=place;
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getApplicationContext(),"Error For Place Selection :-"+status,Toast.LENGTH_SHORT).show();
                Log.i(TAG, "An error occurred: " + status);
            }

        });



      //  final MaterialEditText edtComment=(MaterialEditText)comment_address.findViewById(R.id.Comment);

        final RadioButton shiptoThisAddress=(RadioButton)comment_address.findViewById(R.id.shiptothisAdress);
        final RadioButton shiptoHomeAddress=(RadioButton)comment_address.findViewById(R.id.shipToHome);
        shiptoHomeAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (TextUtils.isEmpty(Common.currentUser.getHomeAdress())||Common.currentUser.getHomeAdress()==null){
                        Toast.makeText(getApplicationContext(),"Please Save your Home Address First",Toast.LENGTH_SHORT).show();

                    }else {
                        edtAdress.setText(Common.currentUser.getHomeAdress());
                    }
                }
            }
        });


        shiptoThisAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mGoogleService.getAddressName(String.
                            format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&sensor=false",mLastLocation.getLatitude(),mLastLocation.getLongitude())).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            try {
                                JSONObject jsonObject=new JSONObject(response.body().toString());

                                JSONArray jsonArray=jsonObject.getJSONArray("results");
                                JSONObject firstJesonArray=jsonArray.getJSONObject(0);

                               address =firstJesonArray.getString("formatted_address");

                                ((EditText)edtAdress.getView().findViewById(R.id.place_autocomplete_search_input)).setText(address);
                               // shippingPlace=address.get

                               // address=

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {

                        }
                    });
                }
            }
        });
        alrt.setView(comment_address);
        alrt.setIcon(R.drawable.ic_chat_bubble_outline_black_24dp);

        alrt.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                if (!shiptoHomeAddress.isChecked() &&!shiptoThisAddress.isChecked()){
                    if (shippingPlace!=null){

                        address=shippingPlace.getAddress().toString();
                        lattitude= String.valueOf(shippingPlace.getLatLng().latitude);
                        longgitude= String.valueOf(shippingPlace.getLatLng().longitude);

                    }else {
                        Toast.makeText(getApplicationContext(),"Plase Select yor location",Toast.LENGTH_SHORT).show();
                        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.places_autocomplete_fragment))
                                .commit();

                    }
                }
                else {


                     Toast.makeText(getApplicationContext(),"This is my address ,"+address,Toast.LENGTH_SHORT).show();
                    Geocoder geocoder=new Geocoder(getApplicationContext(),Locale.getDefault());
                    try {
                        List<android.location.Address> listAddress=geocoder.getFromLocationName(Common.currentUser.getHomeAdress(),1);

                        if (listAddress.size()>0){
                            Address ad=listAddress.get(0);
                            lattitude=String.valueOf(ad.getLatitude());
                            longgitude=String.valueOf(ad.getLongitude());

                        }else {Toast.makeText(getApplicationContext(),"Not Found Address latlag",Toast.LENGTH_SHORT).show();}
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                if (!TextUtils.isEmpty(address)){
                    Toast.makeText(getApplicationContext(),"Plase Select yor location",Toast.LENGTH_SHORT).show();
                    getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.places_autocomplete_fragment))
                            .commit();
                }



                Request re = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        showPrice.getText().toString(),
                        address,
                        chart,
                        "0",
                      //  String.format("%s,%s",shippingPlace.getLatLng().latitude,shippingPlace.getLatLng().longitude)
                        "Latitude :-"+lattitude+" Longitude :-"+longgitude



                );
  // public Request(String phone, String name, String total, String adress, List<Order> foods, String status, String latLng)


                String order_number=String.valueOf(System.currentTimeMillis());
                databaseReferenc.child(order_number).setValue(re);
                new DetailasDB(getBaseContext()).DeleteChart();





               Toast.makeText(getApplicationContext(),"Thank You, Order Please",Toast.LENGTH_SHORT).show();
               finish();
                sendNotificationOrder(order_number);

                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.places_autocomplete_fragment))
                        .commit();


            }

            });

        alrt.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.places_autocomplete_fragment))
                        .commit();
            }
        });


alrt.show();

    }

    private void sendNotificationOrder(final String order_number) {
        final DatabaseReference refarance=FirebaseDatabase.getInstance().getReference("Token");
        Query data=refarance.orderByChild("isServerToken").equalTo(true);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshort:dataSnapshot.getChildren()){
                    Token serverToken=postSnapshort.getValue(Token.class) ;

                    Notification notification=new Notification("Akon Resturent","You have neworder"+order_number);
                    Sender content=new Sender(serverToken.getToken(),notification);

                    mServer.sendNotification(content)
                           .enqueue(new Callback<Myresponce>() {
                               @Override
                               public void onResponse(Call<Myresponce> call, Response<Myresponce> response) {
                                   if (response.body().success==1){
                                       Toast.makeText(getApplicationContext(),"Tanak You, Order Place",Toast.LENGTH_LONG).show();

                                   }else {
                                       Toast.makeText(getApplicationContext(),"Fail !!!!!",Toast.LENGTH_LONG).show();

                                   }
                               }

                               @Override
                               public void onFailure(Call<Myresponce> call, Throwable t) {

                                   Log.e("Error",t.getMessage());

                               }
                           });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadListFood() {
        chart=new DetailasDB(this).getCart();//problem
        adepter=new chartAdepter(chart,this);
        recyclerView.setAdapter(adepter);
        adepter.notifyDataSetChanged();




        int total=0;

        for(Order order:chart){
            total+=(Integer.parseInt(order.getProductPrice()))*(Integer.parseInt(order.getQuantity()));


            Locale locale=new Locale("en","us");
            NumberFormat fmt=NumberFormat.getCurrencyInstance(locale);
            showPrice.setText(fmt.format(total));
        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals(Common.DELLETE)){
            deleteCart(item.getOrder());

        }

        return true;


    }

    private void deleteCart(int order) {

        chart.remove(order);
        new DetailasDB(this).DeleteChart();


            for (Order item : chart) {
                new DetailasDB(this).addToChart(item);


                loadListFood();

        }
        adepter.notifyDataSetChanged();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdate();

    }

    private void startLocationUpdate() {


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            return;

        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClint,mLocationRequest,this);


    }

    private void displayLocation() {

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            return;

        }
        mLastLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClint);
        if (mLastLocation!=null){

            Log.d("Location_Message","Your Location is "+mLastLocation.getLatitude()+","+mLastLocation.getLongitude());

        }else {
            Log.d("Location_Message","Your Location not found");
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

        mGoogleApiClint.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation=location;
        displayLocation();

    }
}
