package com.iBring_user.app.courier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iBring_user.app.Constants.URLHelper;
import com.iBring_user.app.Models.CourierModel;
import com.iBring_user.app.R;
import com.iBring_user.app.Utils.GeoLocation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CourierHistoryAdapter extends RecyclerView.Adapter<CourierHistoryAdapter.MyViewHolder>
{
    Context context;
    ArrayList<CourierModel> list;

    public CourierHistoryAdapter(Context context,ArrayList<CourierModel> list)
    {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_courier,parent,false);
        return new CourierHistoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
            holder.tvPrice.setText("$ "+list.get(position).getPrice());
            holder.tvId.setText("ID: "+list.get(position).getOrder_id());
            holder.tvWeight.setText(list.get(position).getWeight()+" Kg");
            holder.tvParcelType.setText(list.get(position).getParcelType());

            String sendLoc="",receiverLoc="";
            double sendLat,sendLng,receiverLat,receiverLng;

            sendLat=Double.parseDouble(list.get(position).getSenderLatt());
            sendLng=Double.parseDouble(list.get(position).getSenderLong());

            receiverLat=Double.parseDouble(list.get(position).getReceiverLatt());
            receiverLng=Double.parseDouble(list.get(position).getReceiverLng());

            holder.tvSource.setText(GeoLocation.getCid(context,sendLat,sendLng));
            holder.tvDest.setText(GeoLocation.getCid(context,receiverLat,receiverLng));

           Picasso.with(context).load(URLHelper.BASE_URL_STORAGE_COURIER+list.get(position).getImage())
          .placeholder(context.getResources().getDrawable(R.drawable.placeholder)).into(holder.ivParcel);

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvId,tvParcelType,tvPrice,tvWeight,tvSource,tvDest;
        ImageView ivParcel;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvId=(TextView)itemView.findViewById(R.id.tvId);
            tvParcelType=(TextView)itemView.findViewById(R.id.tvParcelType);
            tvPrice=(TextView)itemView.findViewById(R.id.tvPrice);
            ivParcel=(ImageView) itemView.findViewById(R.id.ivParcel);
            tvWeight=(TextView) itemView.findViewById(R.id.tvWeight);
            tvSource=(TextView) itemView.findViewById(R.id.tvSource);
            tvDest=(TextView) itemView.findViewById(R.id.tvDest);
        }

    }
}
