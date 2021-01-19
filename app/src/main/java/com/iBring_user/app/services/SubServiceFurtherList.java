package com.iBring_user.app.services;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iBring_user.app.Models.MatrixServices;
import com.iBring_user.app.R;
import com.iBring_user.app.services.adapter.FurtherListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubServiceFurtherList extends AppCompatActivity
{

    @BindView(R.id.img_back)
    ImageView img_back;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.recyclerServices)
    RecyclerView recyclerServices;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subservices_further_list);
        ButterKnife.bind(this);
        setAdapter();
    }

    public void setAdapter()
    {
        recyclerServices.setLayoutManager(new LinearLayoutManager(this));
        FurtherListAdapter adapter=new FurtherListAdapter(this);
        recyclerServices.setAdapter(adapter);

        adapter.onItemSelectedLsistner(new FurtherListAdapter.onCLickListener()
        {
            @Override
            public void onItemCLick(int layoutPostion, View view) {
                Intent intent=new Intent(SubServiceFurtherList.this,MakeOtherServices.class);
            }
        });
    }

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
}
