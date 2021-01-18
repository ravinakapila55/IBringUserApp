package com.iBring_user.app.services.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iBring_user.app.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FurtherListAdapter extends RecyclerView.Adapter<FurtherListAdapter.MyViewHolder> {

    Context context;

    public FurtherListAdapter(Context context)
    {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_further_list,parent,false);
        return new FurtherListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }
    onCLickListener listener;
    @Override
    public int getItemCount()
    {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_service)
        ImageView iv_service;

        @BindView(R.id.tvService)
        TextView tvService;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemCLick(getLayoutPosition(),v);
                }
            });
        }
    }

    public void onItemSelectedLsistner(onCLickListener  cLickListener)
    {
        this.listener=cLickListener;
    }


    public interface onCLickListener{
        public void onItemCLick(int layoutPostion,View view);
    }

}
