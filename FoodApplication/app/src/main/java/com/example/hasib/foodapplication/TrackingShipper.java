package com.example.hasib.foodapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.hasib.foodapplication.Common.Common;
import com.example.hasib.foodapplication.Common.DirectionJSONParser;
import com.example.hasib.foodapplication.Model.Request;
import com.example.hasib.foodapplication.Model.Shipper;
import com.example.hasib.foodapplication.Model.ShipperInformation;
import com.example.hasib.foodapplication.Remote.IGoogleService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class TrackingShipper extends FragmentActivity implements OnMapReadyCallback,ValueEventListener {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference requestRef,shipperRef;
    Request currentOrder;
    IGoogleService mService;

    Marker shipperMarker;

    Polyline polyline;






    private GoogleMap mMap;

    @Override
    protected void onStart() {
        super.onStart();

        trackingLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        trackingLocation();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_shipper);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        firebaseDatabase=FirebaseDatabase.getInstance();
        requestRef=firebaseDatabase.getReference("Request");
        shipperRef=firebaseDatabase.getReference("ShippingOrderTable");
        shipperRef.addValueEventListener(this);

        mService=Common.getGoogleMapApi();

    }

    @Override
    protected void onStop() {
        shipperRef.removeEventListener(this);
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

      mMap.getUiSettings().setZoomControlsEnabled(true);
      trackingLocation();
    }

    private void trackingLocation() {

        if (polyline != null) {
            polyline.remove();
        }

       try {

           requestRef.child(Common.currentKey)
                   .addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           currentOrder = dataSnapshot.getValue(Request.class);

                           if (currentOrder.getAddress() != null && !currentOrder.getAddress().isEmpty()) {

                               mService.getLocationFromAddress(new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json?address=")
                                       .append(currentOrder.getAddress()).toString())
                                       .enqueue(new Callback<String>() {
                                           @Override
                                           public void onResponse(Call<String> call, Response<String> response) {
                                               try {
                                                   JSONObject jsonObject = new JSONObject(response.body());

                                                   String lat = ((JSONArray) jsonObject.get("results"))
                                                           .getJSONObject(0)
                                                           .getJSONObject("geometry")
                                                           .getJSONObject("location")
                                                           .get("lat").toString();
                                                   String lng = ((JSONArray) jsonObject.get("results"))
                                                           .getJSONObject(0)
                                                           .getJSONObject("geometry")
                                                           .getJSONObject("location")
                                                           .get("lng").toString();

                                                   LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                                                   mMap.addMarker(new MarkerOptions().position(latLng)
                                                           .title("Order Destination")
                                                           .icon(BitmapDescriptorFactory.defaultMarker()));


                                                   shipperRef.child(Common.currentKey)
                                                           .addListenerForSingleValueEvent(new ValueEventListener() {
                                                               @Override
                                                               public void onDataChange(DataSnapshot dataSnapshot) {
                                                                   ShipperInformation shipperInformation = dataSnapshot.getValue(ShipperInformation.class);

                                                                   LatLng shipperLocation = new LatLng(shipperInformation.getLat(), shipperInformation.getLng());

                                                                   if (shipperMarker == null) {
                                                                       shipperMarker = mMap.addMarker(new MarkerOptions()
                                                                               .position(shipperLocation)
                                                                               .title("Shipper #" + shipperInformation.getOrderId())
                                                                               .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                                                                   } else {
                                                                       shipperMarker.setPosition(shipperLocation);
                                                                   }
                                                                   //update camera
                                                                   CameraPosition cameraPosition = new CameraPosition.Builder()
                                                                           .target(shipperLocation)
                                                                           .zoom(16)
                                                                           .bearing(0)
                                                                           .tilt(45)
                                                                           .build();

                                                                   mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                                                  /* if (polyline != null) {
                                                                       polyline.remove();*/

                                                                       Toast.makeText(getApplicationContext(),""+shipperLocation.longitude +","+shipperLocation.longitude+","+currentOrder.getAddress(),Toast.LENGTH_SHORT).show();
                                                                       Log.d("DIRECTION_VALUE",shipperLocation.latitude+""+shipperLocation.longitude+","+currentOrder.getAddress());
                                                                       mService.getDirections(shipperLocation.latitude +","+ shipperLocation.longitude
                                                                               , currentOrder.getAddress())
                                                                               .enqueue(new Callback<String>() {
                                                                                   @Override
                                                                                   public void onResponse(Call<String> call, Response<String> response) {
                                                                                       new ParserTask().execute(response.body().toString());
                                                                                   }

                                                                                   @Override
                                                                                   public void onFailure(Call<String> call, Throwable t) {

                                                                                   }
                                                                               });
                                                                   }

                                                           //     }

                                                               @Override
                                                               public void onCancelled(DatabaseError databaseError) {

                                                               }
                                                           });


                                               } catch (JSONException e) {
                                                   e.printStackTrace();
                                               }


                                           }

                                           @Override
                                           public void onFailure(Call<String> call, Throwable t) {

                                           }
                                       });

                           } else if (currentOrder.getLatLng() != null && !currentOrder.getLatLng().isEmpty()) {

                               mService.getLocationFromAddress(new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json?latlng=")
                                       .append(currentOrder.getLatLng()).toString())
                                       .enqueue(new Callback<String>() {
                                           @Override
                                           public void onResponse(Call<String> call, Response<String> response) {
                                               try {
                                                   JSONObject jsonObject = new JSONObject(response.body());

                                                   String lat = ((JSONArray) jsonObject.get("results"))
                                                           .getJSONObject(0)
                                                           .getJSONObject("geometry")
                                                           .getJSONObject("location")
                                                           .get("lat").toString();
                                                   String lng = ((JSONArray) jsonObject.get("results"))
                                                           .getJSONObject(0)
                                                           .getJSONObject("geometry")
                                                           .getJSONObject("location")
                                                           .get("lng").toString();

                                                   LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                                                   mMap.addMarker(new MarkerOptions().position(latLng)
                                                           .title("Order Destination")
                                                           .icon(BitmapDescriptorFactory.defaultMarker()));


                                                   shipperRef.child(Common.currentKey)
                                                           .addListenerForSingleValueEvent(new ValueEventListener() {
                                                               @Override
                                                               public void onDataChange(DataSnapshot dataSnapshot) {
                                                                   ShipperInformation shipperInformation = dataSnapshot.getValue(ShipperInformation.class);

                                                                   LatLng shipperLocation = new LatLng(shipperInformation.getLat(), shipperInformation.getLng());

                                                                   if (shipperMarker == null) {
                                                                       shipperMarker = mMap.addMarker(new MarkerOptions()
                                                                               .position(shipperLocation)
                                                                               .title("Shipper #" + shipperInformation.getOrderId())
                                                                               .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                                                                   } else {
                                                                       shipperMarker.setPosition(shipperLocation);
                                                                   }
                                                                   //update camera
                                                                   CameraPosition cameraPosition = new CameraPosition.Builder()
                                                                           .target(shipperLocation)
                                                                           .zoom(16)
                                                                           .bearing(0)
                                                                           .tilt(45)
                                                                           .build();

                                                                   mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                                                                       mService.getDirections(shipperLocation.latitude +","+ shipperLocation.longitude
                                                                               ,currentOrder.getLatLng())
                                                                               .enqueue(new Callback<String>() {
                                                                                   @Override
                                                                                   public void onResponse(Call<String> call, Response<String> response) {
                                                                                       new ParserTask().execute(response.body().toString());
                                                                                   }

                                                                                   @Override
                                                                                   public void onFailure(Call<String> call, Throwable t) {

                                                                                   }
                                                                               });
                                                                   }



                                                               @Override
                                                               public void onCancelled(DatabaseError databaseError) {

                                                               }
                                                           });


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

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });

       }catch (Exception e){

       }finally {
         onRestart();
       }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    private class ParserTask extends AsyncTask<String,Integer,List<List<HashMap<String,String>>>> {


      //  ProgressDialog mProgressDialog=new ProgressDialog(TrackingShipper.this);

        ProgressDialog mProgressDialog=new ProgressDialog(TrackingShipper.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage("Please waiting....");
            mProgressDialog.show();
        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... params) {
            JSONObject jsonObject;
            List<List<HashMap<String, String>>> routes=null;
            try {
                jsonObject=new JSONObject(params[0]);

                DirectionJSONParser parser=new DirectionJSONParser();
                routes= parser.parse(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }



        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            mProgressDialog.dismiss();;
            ArrayList points=null;
            PolylineOptions lineOperation = null;
            for(int i=0; i<lists.size();i++){
                points=new ArrayList();
                lineOperation=new PolylineOptions();
                List<HashMap<String,String>> path= lists.get(i);
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point=path.get(j);
                    double lat=Double.parseDouble(point.get("lat"));
                    double lng=Double.parseDouble(point.get("lng"));
                    LatLng location=new LatLng(lat,lng);
                    points.add(location);
                }
                lineOperation.addAll(points);
                lineOperation.width(12);
                lineOperation.color(Color.RED);
                lineOperation.geodesic(true);

            }
          polyline=  mMap.addPolyline(lineOperation);
        }
    }
}
