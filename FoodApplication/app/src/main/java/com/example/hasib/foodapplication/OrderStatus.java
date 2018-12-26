package com.example.hasib.foodapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hasib.foodapplication.Common.Common;

import com.example.hasib.foodapplication.Interface.ItemClickListener;
import com.example.hasib.foodapplication.Model.Request;
import com.example.hasib.foodapplication.ViewHolder.OrderViewHolder;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.android.gms.games.quest.QuestRef;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OrderStatus extends AppCompatActivity {
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference databaseReference;
    FirebaseRecyclerAdapter<Request,OrderViewHolder> adepter;


    @Override
    protected void onStart() {
        super.onStart();
        adepter.startListening();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        recyclerView =(RecyclerView)findViewById(R.id.listOrder);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("Request");
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/food.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        recyclerView.setLayoutManager(layoutManager);

       if(getIntent()==null)
            loadOrder(Common.currentUser.getPhone());
        else{
           if (getIntent().getStringExtra("userPhone")==null){
               loadOrder(Common.currentUser.getPhone());
           }else {
               loadOrder(getIntent().getStringExtra("userPhone"));
           }

       }
           // loadOrder(getIntent().getStringExtra("phoneNumber"));
       // loadOrder(Common.currentUser.getPhone());
    }

    private void loadOrder(String phone) {

        Query query=databaseReference.orderByChild("phone").equalTo(phone);

        FirebaseRecyclerOptions<Request> options=new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(query,Request.class)
                .build();


        adepter=new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder viewHolder, final int position, @NonNull Request model) {

                viewHolder.textOrderId.setText(adepter.getRef(position).getKey());
                viewHolder.TextOrderStatus.setText(Common.ConvertCodeToStatus(model.getStatus()));
                viewHolder.TextOrderPhone.setText(model.getPhone());
                viewHolder.TextOrderAdress.setText(model.getAddress());

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnClick(View view, int possisiton, boolean isLongClick) {
                        Common.currentKey=adepter.getRef(position).getKey();
                        startActivity(new Intent(OrderStatus.this,TrackingShipper.class));
                    }
                });


            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View viewItem= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout,parent,false);
                return new OrderViewHolder(viewItem);
            }
        };


        recyclerView.setAdapter(adepter);
        adepter.startListening();



    }

    @Override
    protected void onStop() {
        super.onStop();
        adepter.stopListening();
    }
}