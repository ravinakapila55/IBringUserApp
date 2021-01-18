package com.iBring_user.app.food_service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iBring_user.app.Models.MenuItems;
import com.iBring_user.app.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    Context context;
    ArrayList<MenuItems> list;

    public CartAdapter(Context context,ArrayList<MenuItems> list) {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_cart,parent,false);
        return new CartAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
            holder.tvName.setText(list.get(position).getName());
            holder.tvQuant.setText(list.get(position).getCount());
            holder.tvPrice.setText("$ "+list.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvPrice)
        TextView tvPrice;

        @BindView(R.id.tvMinus)
        TextView tvMinus;

        @BindView(R.id.tvQuant)
        TextView tvQuant;

        @BindView(R.id.tvAdd)
        TextView tvAdd;


        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);

            tvAdd.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    click.onAddClick(getAdapterPosition(),v);
                }
            });

            tvMinus.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    click.onMinusClick(getAdapterPosition(),v);
                }
            });
        }
    }
    onMyClick click;


    public void onItemSelectedListener(onMyClick myClick)
    {
        this.click=myClick;
    }
    public interface onMyClick{
        public void onAddClick(int layoutPosition,View view);
        public void onMinusClick(int layoutPosition,View view);
    }
}
