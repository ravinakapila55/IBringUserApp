package com.iBring_user.app.services.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iBring_user.app.Constants.URLHelper;
import com.iBring_user.app.Models.MatrixServices;
import com.iBring_user.app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectServicesAdapter extends RecyclerView.Adapter<SelectServicesAdapter.MyViewHolder>
{

    Context context;
    ArrayList<MatrixServices> list;
    myclickListener myclickListener;

    public SelectServicesAdapter(Context context,ArrayList<MatrixServices> list)
    {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_other_service,parent,false);
        return new SelectServicesAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {

        holder.tvService.setText(list.get(position).getName());
        Picasso.with(context).load(URLHelper.BASE_IMAGE_LOAD_URL_WITH_STORAGE+list.get(position).getImage()).
                placeholder(context.getResources().getDrawable(R.drawable.placeholder)).into(holder.iv_service);


    /*    if (position==0)
        {
            holder.iv_service.setImageDrawable(context.getResources().getDrawable(R.drawable.plumber));
            holder.tvService.setText("Plumber");
        }
        else if (position==1)
        {
            holder.iv_service.setImageDrawable(context.getResources().getDrawable(R.drawable.chef));
            holder.tvService.setText("Chef");
        }else if (position==2)
        {
            holder.iv_service.setImageDrawable(context.getResources().getDrawable(R.drawable.carpentar));
            holder.tvService.setText("Carpenter");
        }else if (position==3)
        {
            holder.iv_service.setImageDrawable(context.getResources().getDrawable(R.drawable.driver));
            holder.tvService.setText("Driver");
        }else if (position==4)
        {
            holder.iv_service.setImageDrawable(context.getResources().getDrawable(R.drawable.electrician));
            holder.tvService.setText("Electricians");
        }*/
    }

    @Override
    public int getItemCount()
    {
//        return 5;
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.iv_service)
        ImageView iv_service;

        @BindView(R.id.tvService)
        TextView tvService;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myclickListener.onitemCLick(getAdapterPosition(),v);
                }
            });
        }
    }

    public void onItemSelectedListener(myclickListener listener)
    {
        this.myclickListener=listener;
    }

    public interface myclickListener{
        public void onitemCLick(int layoutPosition,View view);
    }
}
