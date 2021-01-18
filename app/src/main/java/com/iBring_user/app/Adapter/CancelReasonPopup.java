package com.iBring_user.app.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iBring_user.app.Fragments.ServiceFlowFragment;
import com.iBring_user.app.Models.CancalReasonModel;
import com.iBring_user.app.R;

import java.util.ArrayList;

public class CancelReasonPopup extends RecyclerView.Adapter<CancelReasonPopup.MyViewHolder>
{

    Context context;

    public CancelReasonPopup(Context context, ArrayList<CancalReasonModel> list) {
        this.context = context;
        this.list = list;
    }

    ArrayList<CancalReasonModel> list;


    private void selectCurrItem(int position)
    {
        int size = list.size();
        for (int i = 0; i < size; i++)
        {
            if (i == position){
                list.get(i).setFlag(true);

                ServiceFlowFragment.reason_id=list.get(i).getId();
                Log.e("IDDD ",ServiceFlowFragment.reason_id);
            }
            else
            {
                list.get(i).setFlag(false);
            }

            notifyDataSetChanged();
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_cancel_reason,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {

        holder.tvReason.setText(list.get(position).getReason());

        if (list.get(position).isFlag())
        {
            holder.ivTick.setImageDrawable(context.getResources().getDrawable(R.drawable.check_tick));
        }
        else
            {
            holder.ivTick.setImageDrawable(context.getResources().getDrawable(R.drawable.tick_black));
        }

        holder.ivTick.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectCurrItem(position);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

        TextView tvReason;
        ImageView ivTick;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvReason=(TextView)itemView.findViewById(R.id.tvReason);
            ivTick=(ImageView) itemView.findViewById(R.id.ivTick);
        }
    }


}
