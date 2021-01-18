package com.iBring_user.app.food_service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.iBring_user.app.Constants.URLHelper;
import com.iBring_user.app.Helper.SharedHelper;
import com.iBring_user.app.Models.RestaurantsList;
import com.iBring_user.app.R;
import com.iBring_user.app.food_service.adapter.RestaurantLsitingAdapter;
import com.iBring_user.app.retrofit.RetrofitResponse;
import com.iBring_user.app.retrofit.RetrofitService;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodServiceHome extends AppCompatActivity implements RetrofitResponse
{

   /* @BindView(R.id.recyclerFilter)
    RecyclerView recyclerFilter;*/

    @BindView(R.id.recyclerRestraunt)
    RecyclerView recyclerRestraunt;

    @BindView(R.id.tvNoData)
    TextView tvNoData;

    ArrayList<RestaurantsList> list=new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_home);
        ButterKnife.bind(this);
        restaurantList();
//        setFilterTypeAdapter();
//        setRestaurantListingAdapter();
    }

    @BindView(R.id.img_back)
    ImageView img_back;

    @OnClick({R.id.img_back})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.img_back:
                Log.e("backClick ","backClck");
                onBackPressed();
                break;
        }
    }

    public void restaurantList()
    {
        try
        {
            JSONObject jo=new JSONObject();
            jo.put("latitude", SharedHelper.getKey(this, "current_lat"));
            jo.put("longitude", SharedHelper.getKey(this, "current_lng"));
            Log.e("paramsRestaurantList ",jo.toString());
            new RetrofitService(this, FoodServiceHome.this, URLHelper.RESTAURANT_LISTING ,jo,
             500, 2,"1").callService(true);
        }
        catch(Exception exx)
        {
            exx.printStackTrace();
        }

      /* new RetrofitService(this, FoodServiceHome.this, URLHelper.RESTAURANT_LISTING ,
         500, 1,"1").callService(true); */

    }

    public void setRestaurantListingAdapter()
    {
        RestaurantLsitingAdapter adapter=new RestaurantLsitingAdapter(this,list);
        recyclerRestraunt.setLayoutManager(new LinearLayoutManager(this));
        recyclerRestraunt.setAdapter(adapter);

        adapter.onItemSelectedListener(new RestaurantLsitingAdapter.onItemClick()
        {
            @Override
            public void onItemCLick(int layoutPosition, View view)
            {
                Intent intent=new Intent(FoodServiceHome.this,RestaurantMenuItems.class);
                intent.putExtra("key",list.get(layoutPosition));
                startActivity(intent);
            }
        });
    }

   /* public void setFilterTypeAdapter()
    {
        FilterTypeAdapter filterTypeAdapter=new FilterTypeAdapter(this);
        recyclerFilter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        recyclerFilter.setAdapter(filterTypeAdapter);
    }
    */

    @Override
    public void onResponse(int RequestCode, String response)
    {
        switch (RequestCode)
        {
            case 500:

                Log.e("ResponseRestaurantList ",response);

                try {
                    JSONObject jo=new JSONObject(response);

                    if (jo.getString("status").equalsIgnoreCase("success"))
                    {
                        tvNoData.setVisibility(View.GONE);
                        recyclerRestraunt.setVisibility(View.VISIBLE);

                        list.clear();
                        JSONArray jsonArray=jo.getJSONArray("result");

                        for (int i = 0; i <jsonArray.length() ; i++)
                        {

                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            RestaurantsList restaurantsList=new RestaurantsList();

                            restaurantsList.setId(jsonObject.getString("id"));
                            restaurantsList.setName(jsonObject.getString("title"));
                            restaurantsList.setImage(jsonObject.getString("image"));
                            restaurantsList.setDescription(jsonObject.getString("description"));
                            restaurantsList.setAddress(jsonObject.getString("address"));
                            restaurantsList.setRating(jsonObject.getString("rating"));
                            restaurantsList.setLattitude(jsonObject.getString("lattitude"));
                            restaurantsList.setLongitude(jsonObject.getString("longitude"));
                            restaurantsList.setRating(jsonObject.getString("longitude"));
                            restaurantsList.setHours(jsonObject.getString("hours"));
                            restaurantsList.setMinutes(jsonObject.getString("minutes"));
                            list.add(restaurantsList);
                        }

                        if (list.size()>0)
                        {
                            setRestaurantListingAdapter();
                        }

                        else {
                            tvNoData.setVisibility(View.VISIBLE);
                            recyclerRestraunt.setVisibility(View.GONE);
                        }
                    }

                    else {
                        tvNoData.setVisibility(View.VISIBLE);
                        recyclerRestraunt.setVisibility(View.GONE);
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
