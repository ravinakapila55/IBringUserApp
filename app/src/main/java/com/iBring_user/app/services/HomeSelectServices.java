package com.iBring_user.app.services;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.iBring_user.app.Constants.URLHelper;
import com.iBring_user.app.Models.MatrixServices;
import com.iBring_user.app.R;
import com.iBring_user.app.retrofit.RetrofitResponse;
import com.iBring_user.app.retrofit.RetrofitService;
import com.iBring_user.app.services.adapter.SelectServicesAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeSelectServices extends AppCompatActivity implements RetrofitResponse {

    @BindView(R.id.cc_services)
    ConstraintLayout cc_services;

    @BindView(R.id.ivBack)
    ImageView ivBack;

    /*@BindView(R.id.toolbar_title)
    TextView toolbar_title;*/

    @BindView(R.id.recyclerServices)
    RecyclerView recyclerServices;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_select_service);
        ButterKnife.bind(this);
//      mm
        callServiceList();
        //        setAdapter();
    }

    public void callServiceList()
    {
        new RetrofitService(this, this, URLHelper.GET_APP_SERVICES ,
                105, 1,"1").callService(true);
    }

    public void setAdapter()
    {
        recyclerServices.setLayoutManager(new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,
                false));
        SelectServicesAdapter selectServicesAdapter=new SelectServicesAdapter(this,othersList);
        recyclerServices.setAdapter(selectServicesAdapter);
        selectServicesAdapter.onItemSelectedListener(new SelectServicesAdapter.myclickListener() {
            @Override
            public void onitemCLick(int layoutPosition, View view) {
                Intent intent=new Intent(HomeSelectServices.this,SubServiceFurtherList.class);
                intent.putExtra("key",othersList.get(layoutPosition));
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.ivBack,R.id.cc_services})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ivBack:
                Log.e("backClick ","backClck");
                onBackPressed();
               break;

                case R.id.cc_services:
//                 callOther();
                break;
        }
    }

    public void callOther()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.other_service, null);

        final TextView tvSubmit = (TextView) dialogView.findViewById(R.id.tvSubmit);
        final ImageView ivCancel = (ImageView) dialogView.findViewById(R.id.ivCancel);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.getWindow().setGravity(Gravity.TOP);

        int width = WindowManager.LayoutParams.WRAP_CONTENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;

        tvSubmit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                alertDialog.dismiss();
            }
        });

        ivCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onResponse(int RequestCode, String response)
    {
        switch (RequestCode)
        {
            case 105:
                Log.e("ResponseServiceList ",response);

                try
                {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.e("jsonObject ",jsonObject.toString());

                    othersList.clear();
                    JSONArray others=jsonObject.getJSONArray("others");

                    if (others.length()>0)
                    {
                        for (int j = 0; j <others.length() ; j++)
                        {
                            MatrixServices other=new MatrixServices();
                            JSONObject jsonObject1=others.getJSONObject(j);

                            other.setId(jsonObject1.getString("id"));
                            other.setName(jsonObject1.getString("name"));
                            other.setImage(jsonObject1.getString("image"));

                            othersList.add(other);

                        }
                    }

                    if (othersList.size()>0)
                    {
                        setAdapter();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                break;
        }
    }

    ArrayList<MatrixServices> othersList=new ArrayList<>();
}
