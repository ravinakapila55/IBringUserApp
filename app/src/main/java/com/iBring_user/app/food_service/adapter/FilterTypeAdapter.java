package com.iBring_user.app.food_service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iBring_user.app.R;
import com.iBring_user.app.services.adapter.FurtherListAdapter;

import java.util.logging.Filter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterTypeAdapter extends RecyclerView.Adapter<FilterTypeAdapter.MyViewHolder> {

    Context context;

    public FilterTypeAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view= LayoutInflater.from(context).inflate(R.layout.custom_filtertype,parent,false);
        View view= LayoutInflater.from(context).inflate(R.layout.custom_favourite_location,parent,false);
        return new FilterTypeAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {

        if (position==0)
        {
            holder.ivFilter.setVisibility(View.VISIBLE);
        }
        else {
            holder.ivFilter.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivFilter)
        ImageView ivFilter;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
