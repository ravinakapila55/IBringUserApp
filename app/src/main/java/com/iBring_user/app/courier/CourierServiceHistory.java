package com.iBring_user.app.courier;

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
import com.iBring_user.app.Models.CourierModel;
import com.iBring_user.app.R;
import com.iBring_user.app.Utils.GeoLocation;
import com.iBring_user.app.food_service.adapter.FoodOrderHistoryAdapter;
import com.iBring_user.app.retrofit.RetrofitResponse;
import com.iBring_user.app.retrofit.RetrofitService;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.OnClick;

public class CourierServiceHistory extends AppCompatActivity implements RetrofitResponse
{
    RecyclerView recycler;
    TextView tvNoData;
    ImageView img_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courier_service_history);
        findIds();
    }

    public void findIds()
    {
        recycler=(RecyclerView)findViewById(R.id.recycler);
        img_back=(ImageView) findViewById(R.id.img_back);
        tvNoData=(TextView) findViewById(R.id.tvNoData);

        img_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        myorders();
    }

    public void setAdapter()
    {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        CourierHistoryAdapter  foodOrderHistoryAdapter=new CourierHistoryAdapter(this,courierList);
        recycler.setAdapter(foodOrderHistoryAdapter);
    }

    public void myorders()
    {
        new RetrofitService(this, CourierServiceHistory.this, URLHelper.COURIER_HISTORY
                , 500, 1,"1").callService(true);
    }

    @OnClick({R.id.img_back})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.img_back:
            Log.e("backClick ","backClick");
            onBackPressed();
            break;
        }
    }

    ArrayList<CourierModel> courierList=new ArrayList<>();

    @Override
    public void onResponse(int RequestCode, String response)
    {
        switch (RequestCode)
        {
            case 500:
            Log.e("Response ",response);
            try
            {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.e("CourierHistoryModel ",jsonObject.toString());
                    if (jsonObject.getString("status").equalsIgnoreCase("success"))
                    {
                        tvNoData.setVisibility(View.GONE);
                        recycler.setVisibility(View.VISIBLE);
                        JSONArray result=jsonObject.getJSONArray("result");

                        if (result.length()>0)
                        {
                            courierList.clear();

                            for (int i = 0; i <result.length() ; i++)
                            {
                                CourierModel courierModel=new CourierModel();

                                JSONObject jsonObject1=result.getJSONObject(i);

                                courierModel.setId(jsonObject1.getString("id"));
                                courierModel.setSenderName(jsonObject1.getString("sender_name"));
                                courierModel.setSenderContact(jsonObject1.getString("sender_contact"));
                                courierModel.setSenderEmail(jsonObject1.getString("sender_email"));
                                courierModel.setSenderLocation(jsonObject1.getString("sender_location"));
                                courierModel.setSenderLatt(jsonObject1.getString("sender_lattitude"));
                                courierModel.setSenderLong(jsonObject1.getString("sender_longitude"));
                                courierModel.setReciverName(jsonObject1.getString("receiver_name"));
                                courierModel.setReceiverContact(jsonObject1.getString("receiver_contact"));
                                courierModel.setReceiverAddress(jsonObject1.getString("receiver_location"));
                                courierModel.setReceiverEmail(jsonObject1.getString("receiver_email"));
                                courierModel.setReceiverLatt(jsonObject1.getString("receiver_lattitude"));
                                courierModel.setReceiverLng(jsonObject1.getString("receiver_longitude"));
                                courierModel.setOrder_id(jsonObject1.getString("order_id"));

                                courierModel.setWeight(jsonObject1.getString("weight"));
                                courierModel.setSpecialNote(jsonObject1.getString("special_note"));
                                courierModel.setDeliveryNote(jsonObject1.getString("delivery_note"));
                                courierModel.setIsFragile(jsonObject1.getString("is_fragile"));
                                courierModel.setImage(jsonObject1.getString("image"));
                                courierModel.setPrice(jsonObject1.getString("price"));
                                courierModel.setOrderStatus(jsonObject1.getString("status"));

                                JSONArray parcel_types=jsonObject1.getJSONArray("parcel_types");
                                JSONObject jsonObject2=parcel_types.getJSONObject(0);
                                courierModel.setParcelType(jsonObject2.getString("parcel_type"));



                                courierList.add(courierModel);
                            }

                            if (courierList.size()>0)
                            {
                                setAdapter();
                            }
                        }
                        else
                        {
                            tvNoData.setVisibility(View.VISIBLE);
                            recycler.setVisibility(View.GONE);
                        }
                    }
                    else
                    {
                        tvNoData.setVisibility(View.VISIBLE);
                        recycler.setVisibility(View.GONE);
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
