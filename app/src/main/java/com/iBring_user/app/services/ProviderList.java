package com.iBring_user.app.services;

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
import com.iBring_user.app.R;
import com.iBring_user.app.services.adapter.ProviderListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProviderList extends AppCompatActivity
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
        tvTitle.setText("Plumber Listing");
        setAdapter();
    }

    public void setAdapter()
    {
        recyclerServices.setLayoutManager(new LinearLayoutManager(this));
        ProviderListAdapter adapter=new ProviderListAdapter(this);
        recyclerServices.setAdapter(adapter);

        adapter.onItemSelectedLsitener(new ProviderListAdapter.onCLickListener()
        {
            @Override
            public void onItemClick(int layoutPosition, View view)
            {
                Intent intent=new Intent(ProviderList.this,ProviderSummary.class);
                startActivity(intent);
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
