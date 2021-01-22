package com.iBring_user.app.services;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.iBring_user.app.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MakeOtherServices extends AppCompatActivity
{

    @BindView(R.id.img_back)
    ImageView img_back;


    @BindView(R.id.lnrNext)
    RelativeLayout lnrNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_other_services);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.img_back,R.id.lnrNext})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.img_back:
                Log.e("backClick ","backClck");
                onBackPressed();
                break;

            case R.id.lnrNext:
                 Log.e("NextClick ","nextClick");
                 Intent intent=new Intent(MakeOtherServices.this,ProviderList.class);
                 startActivity(intent);
                 break;

        }
    }
}
