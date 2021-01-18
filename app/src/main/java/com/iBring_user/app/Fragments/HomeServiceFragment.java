package com.iBring_user.app.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.iBring_user.app.Activity.Home;
import com.iBring_user.app.Constants.URLHelper;
import com.iBring_user.app.Helper.ConnectionHelper;
import com.iBring_user.app.Helper.SharedHelper;
import com.iBring_user.app.Models.CabType;
import com.iBring_user.app.R;
import com.iBring_user.app.Utils.Utilities;
import com.iBring_user.app.View.TBarView;
import com.iBring_user.app.courier.CourierServiceHome;
import com.iBring_user.app.food_service.FoodServiceHome;
import com.iBring_user.app.retrofit.RetrofitResponse;
import com.iBring_user.app.retrofit.RetrofitService;
import com.iBring_user.app.services.HomeSelectServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeServiceFragment extends Fragment implements View.OnClickListener, RetrofitResponse
{
    public static final String TAG = "HomeServiceAdapter";
    Context context;
    Utilities utils = new Utilities();
    Activity thisActivity;
    View view;
    int value = 0;
    ConnectionHelper helper;
    String  token;
    Activity activity;
    CardView card_taxi,card_courier,card_food,card_services;
    ImageView imgMenu;
    HomeServiceFRagmentListener mListener;
    RecyclerView recycler;

    TextView toolbar_title;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = getActivity();
        thisActivity = getActivity();
        token = SharedHelper.getKey(context, "access_token");
        Log.e("@#@#@","get token"+token);
    }

    public static HomeServiceFragment newInstance()
    {
        HomeServiceFragment homeServiceFragment=new HomeServiceFragment();
        Bundle bundle=new Bundle();
        homeServiceFragment.setArguments(bundle);
        return homeServiceFragment;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (context instanceof HomeServiceFRagmentListener)
        {
            mListener = (HomeServiceFRagmentListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + " must implement ServiceFlowFgmtListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        activity = getActivity();

        if (view == null)
        {
//            view = inflater.inflate(R.layout.fragment_serviceflow, container, false);
            view = inflater.inflate(R.layout.home_services, container, false);
        }

        android.widget.Toolbar toolbar = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
        {
            toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        }


        TBarView tBarView = new TBarView(getActivity(), toolbar);
        tBarView.setupToolbar(R.drawable.menu, getString(R.string.menu_home), false, false);
        findViewById(view);

        return view;
    }


    public void addFragment(Fragment fragment, String tag, boolean popBackStack, boolean hideCurrent, boolean addToBackStack)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        if (fragment != null)
        {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.container, fragment, tag);
            if (popBackStack)
            {
                fragmentManager.popBackStack();
            }
            if (hideCurrent)
            {
                if (fragmentManager.getBackStackEntryCount() > 0)
                {
                    String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()
                            - 1).getName();
                    transaction.hide(fragmentManager.findFragmentByTag(fragmentTag));
                } else {
                    Fragment currentFgmt = fragmentManager.findFragmentById(R.id.container);
                    if (currentFgmt != null)
                    {
                        transaction.hide(currentFgmt);
                    }
                }
            }
            if (addToBackStack)
            {
                transaction.addToBackStack(tag);
            }
            transaction.commit();
        }
    }

    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.card_taxi:

                ServiceFlowFragment subHomeFragment = ServiceFlowFragment.newInstance();
                addFragment(subHomeFragment, HomeServiceFragment.TAG, false, true, true);

                break;

            case R.id.card_services:

                Intent intent11=new Intent(getActivity(), HomeSelectServices.class);
                startActivity(intent11);

//                Toast.makeText(getActivity(), "This feature will be added soon.", Toast.LENGTH_SHORT).show();

                break;

            case R.id.card_courier:

//                Toast.makeText(getActivity(), "This feature will be added soon.", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(), CourierServiceHome.class);
                startActivity(intent);

                break;

            case R.id.card_food:

                Intent it=new Intent(getActivity(), FoodServiceHome.class);
                startActivity(it);
//              Toast.makeText(getActivity(), "This feature will be added soon.", Toast.LENGTH_SHORT).show();
                break;

           case R.id.imgMenu:

            mListener.handleDrawer();

           break;
        }
    }




    ArrayList<CabType> cablist=new ArrayList<>();
    @Override
    public void onResponse(int RequestCode, String response)
    {

        switch (RequestCode)
        {
            case 500:
                Log.e("CabTYpeList ",response);
                try
                {
                    cablist.clear();
                    JSONArray jsonArray=new JSONArray(response);
                    Log.e("jsonArrayLength ",jsonArray.length()+"");


                    if (jsonArray.length()>0)
                    {
                        for (int i = 0; i <jsonArray.length() ; i++)
                        {
                            JSONObject cabJson=jsonArray.getJSONObject(i);
                            CabType cabType=new CabType();
                            cabType.setId(cabJson.getString("id"));
                            cabType.setName(cabJson.getString("cab_name"));
                            cabType.setPrice(cabJson.getString("price"));
                            cabType.setImage(cabJson.getString("image"));
                            cablist.add(cabType);
                        }
                    }

                    Log.e("cablist ",cablist.size()+"");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                break;
        }

    }

    public interface HomeServiceFRagmentListener
    {
         void handleDrawer();
    }

    public void GoToMainActivity()
    {
        Intent mainIntent = new Intent(thisActivity, Home.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        thisActivity.finish();
    }

    private void findViewById(View view)
    {
        imgMenu = (ImageView) view.findViewById(R.id.imgMenu);

        toolbar_title = (TextView) view.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Home");
        card_taxi = (CardView) view.findViewById(R.id.card_taxi);
        card_courier = (CardView) view.findViewById(R.id.card_courier);
        card_food = (CardView) view.findViewById(R.id.card_food);
        card_services = (CardView) view.findViewById(R.id.card_services);


        card_taxi.setOnClickListener(this);
        card_courier.setOnClickListener(this);
        card_food.setOnClickListener(this);
        card_services.setOnClickListener(this);
        imgMenu.setOnClickListener(this);
    }




}
