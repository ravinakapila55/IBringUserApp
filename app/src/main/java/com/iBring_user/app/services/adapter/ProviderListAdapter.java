package com.iBring_user.app.services.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iBring_user.app.R;

public class ProviderListAdapter extends RecyclerView.Adapter<ProviderListAdapter.Viewholder>
{


    Context context;

    onCLickListener onCLickListener;

    public ProviderListAdapter(Context context)
    {
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.custom_provider_list,parent,false);
        return new ProviderListAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

    }

    @Override
    public int getItemCount()
    {
        return 9;
    }

    public class  Viewholder extends RecyclerView.ViewHolder
    {
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCLickListener.onItemClick(getAdapterPosition(),v);
                }
            });

        }
    }


    public void onItemSelectedLsitener(onCLickListener listener)
    {
        this.onCLickListener=listener;
    }


    public interface onCLickListener{
        public void onItemClick(int layoutPosition,View view);
    }
}
