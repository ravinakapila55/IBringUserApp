package com.iBring_user.app.food_service.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.iBring_user.app.Constants.URLHelper;
import com.iBring_user.app.Models.MenuItems;
import com.iBring_user.app.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantsMenuItemAdapter extends RecyclerView.Adapter<RestaurantsMenuItemAdapter.MyViewHolder>
{
    Context context;
    ArrayList<MenuItems> list;
    int count;


    public RestaurantsMenuItemAdapter(Context context,ArrayList<MenuItems> list)
    {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(context).inflate(R.layout.sutom_menu_item,parent,false);
        return new RestaurantsMenuItemAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
      holder.tvDesc.setText(list.get(position).getDesc());
      holder.tvName.setText(list.get(position).getName());
      holder.tvQuant.setText(list.get(position).getCount());
      holder.tvPrice.setText("$ "+list.get(position).getPrice());


      Picasso.with(context).load(URLHelper.BASE_IMAGE_FOOD_MENU+list.get(position).getImage()).
              placeholder(context.getResources().getDrawable(R.drawable.placeholder)).into(holder.iv_restraunt);



       /* holder.tvAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                count=Integer.parseInt(holder.tvQuant.getText().toString().trim());
                count++;
                holder.tvQuant.setText(String.valueOf(count));
                list.get(position).setCount(String.valueOf(count));
                notifyDataSetChanged();
            }
        });

        holder.tvMinus.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (list.get(position).getCount().equalsIgnoreCase("0")|| list.get(position).getCount().equalsIgnoreCase("1"))
                {

                }
                else
                {
                    count=Integer.parseInt(holder.tvQuant.getText().toString().trim());
                    count-=1;
                    holder.tvQuant.setText(String.valueOf(count));
                    list.get(position).setCount(String.valueOf(count));
                    notifyDataSetChanged();
                }

            }
        });*/

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.iv_restraunt)
        RoundedImageView iv_restraunt;

        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.tvDesc)
        TextView tvDesc;

        @BindView(R.id.rating)
        RatingBar rating;

        @BindView(R.id.tvPrice)
        TextView tvPrice;

        @BindView(R.id.tvAdd)
        TextView tvAdd;

        @BindView(R.id.tvQuant)
        TextView tvQuant;

        @BindView(R.id.tvMinus)
        TextView tvMinus;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);

            tvAdd.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onClickListener.onAddClick(getAdapterPosition(),v);
                }
            });

            tvMinus.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    onClickListener.onSubtractClick(getAdapterPosition(),v);
                }
            });
        }
    }

    public void onItemSelectedListener(onClickListener listener)
    {
        this.onClickListener=listener;
    }

    onClickListener onClickListener;


    public interface onClickListener
    {
         void onAddClick(int layoutPosition,View view);
         void onSubtractClick(int layoutPosition,View view);
    }

}
