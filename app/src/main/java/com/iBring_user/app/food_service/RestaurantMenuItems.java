package com.iBring_user.app.food_service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iBring_user.app.Constants.URLHelper;
import com.iBring_user.app.Models.MenuItems;
import com.iBring_user.app.Models.RestaurantsList;
import com.iBring_user.app.R;
import com.iBring_user.app.food_service.adapter.RestaurantsMenuItemAdapter;
import com.iBring_user.app.retrofit.RetrofitResponse;
import com.iBring_user.app.retrofit.RetrofitService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RestaurantMenuItems extends AppCompatActivity implements RetrofitResponse
{

    @BindView(R.id.recyclerRestraunt)
    RecyclerView recyclerRestraunt;

    @BindView(R.id.tvView)
    TextView tvView;


    @BindView(R.id.tvName)
    TextView tvName;

    RestaurantsList restaurantsList;

    @BindView(R.id.rating)
    RatingBar rating;

    @BindView(R.id.tvDesc)
    TextView tvDesc;

    @BindView(R.id.tvItems)
    TextView tvItems;

    @BindView(R.id.tvPrice)
    TextView tvPrice;

    @BindView(R.id.tvNoData)
    TextView tvNoData;

    @BindView(R.id.cc_bottom)
    ConstraintLayout cc_bottom;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurant_menu_item);
        ButterKnife.bind(this);

        if (getIntent().hasExtra("key"))
        {
            restaurantsList=(RestaurantsList)getIntent().getSerializableExtra("key");
            Log.e("Iddd ",restaurantsList.getId());
            setData();
        }
        restaurantList();
//        callAdapter();
    }

    public void restaurantList()
    {
        new RetrofitService(this, RestaurantMenuItems.this, URLHelper.RESTAURANT_MENU_LISTING +"/"+
                restaurantsList.getId(), 500, 1,"1").callService(true);
    }

    public void setData()
    {
        tvName.setText(restaurantsList.getName());
        tvDesc.setText(restaurantsList.getDescription());
    }

    int count,price;
    int totalCount,totalPrice;
    public void callAdapter()
    {
        RestaurantsMenuItemAdapter restaurantsMenuItemAdapter=new RestaurantsMenuItemAdapter(this,list);
        recyclerRestraunt.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,
                false));
        recyclerRestraunt.setAdapter(restaurantsMenuItemAdapter);

        restaurantsMenuItemAdapter.onItemSelectedListener(new RestaurantsMenuItemAdapter.onClickListener()
        {
            @Override
            public void onAddClick(int layoutPosition, View view)
            {
                int valuee=0;
                valuee= Integer.parseInt(list.get(layoutPosition).getPrice());

                count=Integer.parseInt(list.get(layoutPosition).getCount());
                count++;
                totalCount++;

                price=valuee*totalCount;
//                price=totalPrice+(valuee*totalCount);


                tvPrice.setText("$ "+String.valueOf(price));
                list.get(layoutPosition).setCount(String.valueOf(count));
                restaurantsMenuItemAdapter.notifyDataSetChanged();

                if (totalCount==1|| totalCount==0)
                {
                    tvItems.setText(String.valueOf(totalCount)+" Item");
                }
                else
                {
                    tvItems.setText(String.valueOf(totalCount)+" Items");
                }
            }

            @Override
            public void onSubtractClick(int layoutPosition, View view)
            {
                int valuee=0;
                valuee= Integer.parseInt(list.get(layoutPosition).getPrice());


                if (list.get(layoutPosition).getCount().equalsIgnoreCase("0"))
                {
                    tvItems.setText("0"+" Item");
                }
                else
                {
                    count=Integer.parseInt(list.get(layoutPosition).getCount());
                    count-=1;
                    totalCount-=1;
                   /* price=valuee*totalCount;
                    totalPrice=price+totalPrice;*/


                    price=valuee*totalCount;
//                    price=totalPrice-(valuee*totalCount);

                    tvPrice.setText("$ "+String.valueOf(price));


                    list.get(layoutPosition).setCount(String.valueOf(count));
                   restaurantsMenuItemAdapter. notifyDataSetChanged();
                    if (totalCount==1)
                    {
                        tvItems.setText(String.valueOf(totalCount)+" Item");
                    }
                    else
                        {
                        tvItems.setText(String.valueOf(totalCount)+" Items");
                    }

                }
            }
        });
    }

    @BindView(R.id.img_back)
    ImageView img_back;

    @OnClick({R.id.img_back,R.id.tvView})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.img_back:
                Log.e("backClick ","backClick");
                onBackPressed();
                break;

                case R.id.tvView:
                    Sublist.clear();
                    int countValue=0;
                    int totalFare=0;
                    for (int i = 0; i <list.size() ; i++) {
                        countValue=Integer.parseInt(list.get(i).getCount());
                        if (countValue>0)
                        {
                            MenuItems menuItems=new MenuItems();

                            menuItems.setId(list.get(i).getId());
                            menuItems.setName(list.get(i).getName());
                            menuItems.setPrice(list.get(i).getPrice());
                            menuItems.setCount(list.get(i).getCount());
                            menuItems.setDesc(list.get(i).getDesc());
                            menuItems.setImage(list.get(i).getImage());

                            Sublist.add(menuItems);
                        }
                    }
                    Log.e("SubListSize ",Sublist.size()+"");


                    int valueee=0;
                    int counting=0;


                    for (int k = 0; k <Sublist.size() ; k++) {


                        valueee=Integer.parseInt(Sublist.get(k).getPrice());
                        counting=Integer.parseInt(Sublist.get(k).getCount());
                        totalFare=totalFare+(valueee*counting);
                    }
                    Log.e("TotalFare ",totalFare+"");

                    if (checkValidations())
                    {
                        Log.e("backClick ","backClck");
                        Intent intent=new Intent(RestaurantMenuItems.this,CartScreen.class);
                        intent.putExtra("list",(Serializable)Sublist);
                        intent.putExtra("id",restaurantsList.getId());
                        intent.putExtra("hours",restaurantsList.getHours());
                        intent.putExtra("mins",restaurantsList.getMinutes());
                        intent.putExtra("fare",String.valueOf(totalFare));
                        startActivity(intent);
                    }
                break;
        }
    }

    public boolean checkValidations()
    {
        if (Sublist.size()==0)
        {
            Toast.makeText(this, "Select atleast one Item", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    ArrayList<MenuItems> list=new ArrayList<>();
    ArrayList<MenuItems> Sublist=new ArrayList<>();
    ArrayList<MenuItems> newList=new ArrayList<>();

    @Override
    public void onResponse(int RequestCode, String response)
    {
        switch (RequestCode)
        {
            case 500:

                Log.e("ResponseRestaurantList ",response);

                try
                {
                    list.clear();
                    JSONArray jsonArray=new JSONArray(response);

                    if (jsonArray.length()>0)
                    {
                        for (int i = 0; i <jsonArray.length() ; i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            MenuItems restaurantsList=new MenuItems();
                            restaurantsList.setId(jsonObject.getString("id"));
                            restaurantsList.setName(jsonObject.getString("name"));
                            restaurantsList.setDesc(jsonObject.getString("item_name"));
                            restaurantsList.setPrice(jsonObject.getString("price"));
                            restaurantsList.setImage(jsonObject.getString("image"));
                            restaurantsList.setRating(jsonObject.getString("rating"));
                            restaurantsList.setCount("0");
                            list.add(restaurantsList);
                        }
                        if (list.size()>0)
                        {
                            tvNoData.setVisibility(View.GONE);
                            recyclerRestraunt.setVisibility(View.VISIBLE);
                            cc_bottom.setVisibility(View.VISIBLE);
                            callAdapter();
                        }
                        else {
                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerRestraunt.setVisibility(View.GONE);
                            cc_bottom.setVisibility(View.GONE);
                        }
                    }

                    else {
                        tvNoData.setVisibility(View.VISIBLE);
                        recyclerRestraunt.setVisibility(View.GONE);
                        cc_bottom.setVisibility(View.GONE);
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                break;
        }
    }
}
