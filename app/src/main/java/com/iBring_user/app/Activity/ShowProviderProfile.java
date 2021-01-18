package com.iBring_user.app.Activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iBring_user.app.R;
import com.iBring_user.app.Adapter.CertificationAdapter;
import com.iBring_user.app.Adapter.ReviewListAdapter;
import com.iBring_user.app.Adapter.ServiceProviderListAdapter;
import com.iBring_user.app.Constants.URLHelper;
import com.iBring_user.app.Helper.ConnectionHelper;
import com.iBring_user.app.Helper.CustomDialog;
import com.iBring_user.app.Helper.SharedHelper;
import com.iBring_user.app.Models.ServiceListModel;
import com.iBring_user.app.Utils.Utilities;
import com.iBring_user.app.XuberServicesApplication;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static com.iBring_user.app.XuberServicesApplication.trimMessage;

public class ShowProviderProfile extends AppCompatActivity
{
    public Context context = ShowProviderProfile.this;
    public Activity activity = ShowProviderProfile.this;
    String TAG = "ShowActivity";
    CustomDialog customDialog;
    ConnectionHelper helper;
    Boolean isInternet;
    ImageView backArrow;
    TextView email, first_name, last_name, mobile_no, services_provided, lblAbout,see_all;
    ImageView profile_Image;
    String strProviderId = "";
    RatingBar ratingProvider;
    private RecyclerView recyclerView,highlightRecyclerview,certificateRecyclerview;

    ArrayList<ServiceListModel> serviceListModels;
    ServiceProviderListAdapter serviceListAdapter;
    ReviewListAdapter reviewListAdapter;
    CertificationAdapter certificationAdapter;
    Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        thisActivity = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        setContentView(R.layout.activity_show_profile);
        findViewByIdandInitialization();
        backArrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void findViewByIdandInitialization() {
        email = (TextView) findViewById(R.id.email);
        first_name = (TextView) findViewById(R.id.first_name);
        last_name = (TextView) findViewById(R.id.last_name);
        mobile_no = (TextView) findViewById(R.id.mobile_no);
        see_all = (TextView) findViewById(R.id.see_all);
        services_provided = (TextView) findViewById(R.id.services_provided);
        lblAbout = (TextView) findViewById(R.id.lblAbout);
        backArrow = (ImageView) findViewById(R.id.backArrow);
        profile_Image = (ImageView) findViewById(R.id.img_profile);
        ratingProvider = (RatingBar) findViewById(R.id.ratingProvider);
        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();
       // strProviderId = getIntent().getExtras().getString("provider_id");
        strProviderId="1";
        recyclerView = (RecyclerView) findViewById(R.id.selectServiceRecyclerview);
        highlightRecyclerview = (RecyclerView) findViewById(R.id.highlightRecyclerview);
        certificateRecyclerview = (RecyclerView) findViewById(R.id.certificateRecyclerview);
        setupRecyclerView();
        setUpHighlightRecyclerview();
        setUpcertificateRecyclerview();
        getProviderProfile();

        see_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i=new Intent(getApplicationContext(),ReviewActivity.class);
               startActivity(i);
            }
        });
    }

    private void setupRecyclerView() {
        serviceListModels = new ArrayList<>();
        serviceListAdapter = new ServiceProviderListAdapter(serviceListModels, context);
        // serviceListAdapter.setServiceClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen._5sdp);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(serviceListAdapter);
    }

    private void setUpHighlightRecyclerview() {
       // serviceListModels = new ArrayList<>();
        reviewListAdapter = new ReviewListAdapter(context);
        // serviceListAdapter.setServiceClickListener(this);
        highlightRecyclerview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));;
        highlightRecyclerview.setHasFixedSize(true);

        highlightRecyclerview.setAdapter(reviewListAdapter);
    }

    private void setUpcertificateRecyclerview() {
        // serviceListModels = new ArrayList<>();
        certificationAdapter = new CertificationAdapter(context);
        // serviceListAdapter.setServiceClickListener(this);
        certificateRecyclerview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));;
        certificateRecyclerview.setHasFixedSize(true);

        certificateRecyclerview.setAdapter(certificationAdapter);
    }

    private class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }

    private void loadRecyclerView(JSONArray jsonArray) {
        int cnt = jsonArray.length();
        try {
            for (int i = 0; i < cnt; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ServiceListModel serviceListModel = new ServiceListModel();
                serviceListModel.setIdNew(jsonObject.optString("id") + "");
                serviceListModel.setName(jsonObject.optString("name"));
                serviceListModel.setImage(jsonObject.optString("image"));
                serviceListModel.setDescription(jsonObject.optString("description"));
                serviceListModel.setAvailable(jsonObject.optString("available"));
                serviceListModel.setPricePerHour(jsonObject.optString("price_per_hour"));
                serviceListModels.add(serviceListModel);

            }
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void getProviderProfile() {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        customDialog.show();

        Log.e("GetPostAPI", "" + URLHelper.GET_PROVIDER_PROFILE + "?provider_id=" + strProviderId);
        JSONObject object = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URLHelper.GET_PROVIDER_PROFILE + "?provider_id=" + strProviderId,
                object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("getProfile", response.toString());
                System.out.println("getProfile" + response.toString());
                customDialog.dismiss();
                try {


                    JSONObject jsonProviderObj = response.getJSONObject("provider");
                    email.setText(jsonProviderObj.optString("email"));
                    if (jsonProviderObj.optString("first_name") != null && !jsonProviderObj.optString("first_name").equalsIgnoreCase("null")
                            && jsonProviderObj.optString("first_name").length() > 0)
                        first_name.setText(jsonProviderObj.optString("first_name"));
                    if (jsonProviderObj.optString("last_name") != null && !jsonProviderObj.optString("last_name").equalsIgnoreCase("null")
                            && jsonProviderObj.optString("last_name").length() > 0)
                        last_name.setText(jsonProviderObj.optString("last_name"));
                    if (jsonProviderObj.optString("mobile") != null && !jsonProviderObj.optString("mobile").equalsIgnoreCase("null")
                            && jsonProviderObj.optString("mobile").length() > 0)
                        mobile_no.setText(jsonProviderObj.optString("mobile"));
                    else
                        mobile_no.setText("No mobile number");
                    if (jsonProviderObj.optString("description") != null && !jsonProviderObj.optString("description").equalsIgnoreCase("null")
                            && jsonProviderObj.optString("description").length() > 0)
                        lblAbout.setText(jsonProviderObj.optString("description"));
                    else
                        lblAbout.setText("No description");
                    JSONArray jsonServicesArr = response.getJSONArray("services");

                    String strServices = "";
                    for (int i = 0; i < jsonServicesArr.length(); i++) {
                        strServices += jsonServicesArr.getJSONObject(i).optString("name") + "\n";
                    }


                    services_provided.setText(strServices);
                    ratingProvider.setRating(Float.parseFloat(jsonProviderObj.optString("rating")));

                    if (jsonServicesArr != null && jsonServicesArr.length() > 0)
                        loadRecyclerView(jsonServicesArr);

                    Picasso.with(context).load(Utilities.getImageURL(jsonProviderObj.optString("avatar"))).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(profile_Image);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customDialog.dismiss();
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {

                    try {
                        JSONObject errorObj = new JSONObject(new String(response.data));

                        if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
                            try {
                                displayMessage(errorObj.optString("message"));
                            } catch (Exception e) {
                                displayMessage(getString(R.string.something_went_wrong));
                            }
                        } else if (response.statusCode == 401) {

                        } else if (response.statusCode == 422) {

                            json = trimMessage(new String(response.data));
                            if (json != "" && json != null) {
                                displayMessage(json);
                            } else {
                                displayMessage(getString(R.string.please_try_again));
                            }

                        } else if (response.statusCode == 503) {
                            displayMessage(getString(R.string.server_down));
                        } else {
                            displayMessage(getString(R.string.please_try_again));
                        }

                    } catch (Exception e) {
                        displayMessage(getString(R.string.something_went_wrong));
                    }

                } else {
                    displayMessage(getString(R.string.please_try_again));
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer" + " " + SharedHelper.getKey(context, "access_token"));
                return headers;
            }
        };

        XuberServicesApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }


    public void GoToMainActivity() {
        Intent mainIntent = new Intent(activity, Home.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void displayMessage(String toastString) {
        Toast.makeText(context, toastString + "", Toast.LENGTH_SHORT).show();
    }


}
