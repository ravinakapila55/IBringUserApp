package com.iBring_user.app.courier;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.iBring_user.app.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CourierServiceHome extends AppCompatActivity
{
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;

    @BindView(R.id.card__create_courier)
    CardView card__create_courier;

    @BindView(R.id.card_track_courier)
    CardView card_track_courier;

    @BindView(R.id.imgMenu)
    ImageView imgMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courier_home);
        ButterKnife.bind(this);
        toolbar_title.setText("Courier Service");
    }

    @OnClick({R.id.imgMenu,R.id.card__create_courier,R.id.card_track_courier})
    public void onClick(View view)
    {
        switch (view.getId())
        {
           case R.id.imgMenu:
           Log.e("backClick ","backClck");
           onBackPressed();
           break;

           case R.id.card__create_courier:
           Log.e("createCourier  ","creationClick");
           Intent intent=new Intent(CourierServiceHome.this,CourierAddSenderDetails.class);
           startActivity(intent);
           break;

           case R.id.card_track_courier:
           Log.e("trackCourier  ","trackClick");
           break;
        }
    }

}
