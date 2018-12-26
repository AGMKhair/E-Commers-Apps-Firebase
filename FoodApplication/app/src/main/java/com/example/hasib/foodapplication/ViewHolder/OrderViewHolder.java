package com.example.hasib.foodapplication.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import com.example.hasib.foodapplication.Common.Common;
import com.example.hasib.foodapplication.Interface.ItemClickListener;
import com.example.hasib.foodapplication.R;

public class OrderViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener//,View.OnCreateContextMenuListener
         {
    public TextView textOrderId,TextOrderStatus,TextOrderPhone,TextOrderAdress;

             public void setItemClickListener(ItemClickListener itemClickListener) {
                 this.itemClickListener = itemClickListener;
             }

             private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);
        textOrderId=(TextView)itemView.findViewById(R.id.order_id);
        TextOrderStatus=(TextView)itemView.findViewById(R.id.order_status);
        TextOrderPhone=(TextView)itemView.findViewById(R.id.phone_number);
        TextOrderAdress=(TextView)itemView.findViewById(R.id.adress);

        itemView.setOnClickListener(this);
    //    itemView.setOnCreateContextMenuListener(this);

    }


    public OrderViewHolder (View itemView, ItemClickListener itemClickListener) {
        super(itemView);
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {

      itemClickListener.OnClick(v,getAdapterPosition(),false);

    }


   /* public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        menu.setHeaderTitle("Select action");
        menu.add(0,0,getAdapterPosition(), Common.DELLETE);

    }*/
}
