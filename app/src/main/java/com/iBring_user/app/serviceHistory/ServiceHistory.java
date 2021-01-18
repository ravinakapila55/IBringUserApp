package com.iBring_user.app.serviceHistory;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.iBring_user.app.Activity.HistoryActivity;
import com.iBring_user.app.R;
import com.iBring_user.app.courier.CourierServiceHistory;
import com.iBring_user.app.food_service.FoodServiceHistory;
import com.iBring_user.app.retrofit.RetrofitResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceHistory extends AppCompatActivity implements RetrofitResponse
{

    @BindView(R.id.card_taxi)
    ConstraintLayout card_taxi;

    @BindView(R.id.card_courier)
    ConstraintLayout card_courier;

    @BindView(R.id.card_food)
    ConstraintLayout card_food;

    @BindView(R.id.card_services)
    ConstraintLayout card_services;



    @BindView(R.id.img_back)
    ImageView img_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_history);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_back,R.id.card_taxi,R.id.card_food,R.id.card_courier})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.img_back:
                Log.e("backClick ","backClick");
                onBackPressed();
                break;


            case R.id.card_taxi:
                Intent intent = new Intent(ServiceHistory.this, HistoryActivity.class);
                intent.putExtra("tag","past");
                startActivity(intent);
                break;

            case R.id.card_food:

                Intent intent1 = new Intent(ServiceHistory.this, FoodServiceHistory.class);
                startActivity(intent1);
                break;


                case R.id.card_courier:

                Intent intent22 = new Intent(ServiceHistory.this, CourierServiceHistory.class);
                startActivity(intent22);
                break;


        }
    }

    @Override
    public void onResponse(int RequestCode, String response)
    {
        switch (RequestCode)
        {

        }
    }


}
