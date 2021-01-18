package com.iBring_user.app.food_service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.iBring_user.app.Constants.URLHelper;
import com.iBring_user.app.Models.RestaurantsList;
import com.iBring_user.app.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantLsitingAdapter extends RecyclerView.Adapter<RestaurantLsitingAdapter.MyViewHolder>
{
    Context context;
    ArrayList<RestaurantsList> list;
    onItemClick click;

    public RestaurantLsitingAdapter(Context context,ArrayList<RestaurantsList> list)
    {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
//        View view= LayoutInflater.from(context).inflate(R.layout.custom_restraunt_listing,parent,false);
        View view= LayoutInflater.from(context).inflate(R.layout.custom_restaraunts,parent,false);
        return new RestaurantLsitingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.tvName.setText(list.get(position).getName());
        holder.tvLocation.setText(list.get(position).getAddress());
        Picasso.with(context).load(URLHelper.BASE_URL_RESTAURANT_IMAGE+list.get(position).getImage()).
                placeholder(context.getResources().getDrawable(R.drawable.placeholder)).into(holder.iv_restraunt);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {

       @BindView(R.id.iv_restraunt)
        ImageView iv_restraunt;

        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvLocation)
        TextView tvLocation;

      /*  @BindView(R.id.tvCuisine)
        TextView tvCuisine;*/

      /*  @BindView(R.id.tvRating)
        TextView tvRating;*/


        @BindView(R.id.rating)
        RatingBar rating;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    click.onItemCLick(getAdapterPosition(),v);
                }
            });
        }
    }

    public void onItemSelectedListener(onItemClick itemClick)
    {
        this.click=itemClick;
    }

    public interface onItemClick
    {
        public void onItemCLick(int layoutPosition,View view);
    }
}
