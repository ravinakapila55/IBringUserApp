package com.iBring_user.app.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import com.iBring_user.app.R;
import com.iBring_user.app.Adapter.AutoCompleteAdapter;
import com.iBring_user.app.Models.PlacePredictions;
import com.iBring_user.app.Utils.GeoLocation;
import com.iBring_user.app.Utils.Utilities;
import com.iBring_user.app.XuberServicesApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class CustomGooglePlacesSearch extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    double latitude;
    double longitude;
    private ListView mAutoCompleteList;
    private EditText txtaddressSource;
    private String GETPLACESHIT = "places_hit";
    private PlacePredictions predictions = new PlacePredictions();
    private Location mLastLocation;
    private AutoCompleteAdapter mAutoCompleteAdapter;
    private static final int MY_PERMISSIONS_REQUEST_LOC = 30;
    private Handler handler;
    private GoogleApiClient mGoogleApiClient;
    TextView txtPickLocation;
    Utilities utils = new Utilities();
    ImageView backArrow, imgDestClose,imgSourceClose;
    Activity thisActivity;
    String strSource = "";
    String type = "";

    String strSelected = "";
    private PlacePredictions placePredictions = new PlacePredictions();
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        setContentView(R.layout.fragment_soruce_and_destination);
        thisActivity = this;
        txtaddressSource = (EditText) findViewById(R.id.txtaddressSource);
        mAutoCompleteList = (ListView) findViewById(R.id.searchResultLV);

        backArrow = (ImageView) findViewById(R.id.backArrow);
        imgSourceClose = (ImageView) findViewById(R.id.imgSourceClose);

        txtPickLocation = (TextView) findViewById(R.id.txtPickLocation);

        String cursor = getIntent().getExtras().getString("cursor");
        String s_address = getIntent().getExtras().getString("s_address");
        type = getIntent().getExtras().getString("type");
        Log.e("TypeCustom ",type);


        if (type.equalsIgnoreCase("pick"))
        {
            txtaddressSource.setHint("Enter Service Location");
        }
        else {
            txtaddressSource.setHint("Enter Destination Location");
        }

        txtaddressSource.setText(s_address);
        imgSourceClose.setVisibility(View.VISIBLE);
        if(cursor.equalsIgnoreCase("source"))
        {
            txtaddressSource.requestFocus();
            txtaddressSource.setText("");
            imgSourceClose.setVisibility(View.GONE);
        }

        imgSourceClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                txtaddressSource.setText("");
                mAutoCompleteList.setVisibility(View.GONE);
                txtPickLocation.setVisibility(View.GONE);
                imgSourceClose.setVisibility(View.GONE);
                txtaddressSource.requestFocus();
            }
        });

        txtPickLocation.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                utils.hideKeypad(thisActivity, thisActivity.getCurrentFocus());
                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Intent intent = new Intent();
                        intent.putExtra("pick_location","yes");
                        intent.putExtra("type",strSelected);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }, 500);
            }
        });


        //get permission for Android M

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            fetchLocation();
        }
        else
          {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOC);
            }
            else
            {
                fetchLocation();
            }
        }

        //Add a text change listener to implement autocomplete functionality
        txtaddressSource.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // optimised way is to start searching for laction after user has typed minimum 3 chars
                strSelected = "source";
                if (txtaddressSource.getText().length() > 0) {
                    txtPickLocation.setVisibility(View.GONE);
                    imgSourceClose.setVisibility(View.VISIBLE);
                    txtPickLocation.setText("Pick Source");
                    Runnable run = new Runnable() {


                        @Override
                        public void run() {
                            // cancel all the previous requests in the queue to optimise your network calls during autocomplete search
                            XuberServicesApplication.getInstance().cancelRequestInQueue(GETPLACESHIT);

                            JSONObject object = new JSONObject();
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                                    getPlaceAutoCompleteUrl(txtaddressSource.getText().toString()),
                                    object, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.v("PayNowRequestResponse", response.toString());
                                    Log.v("PayNowRequestResponse1", response.toString());
                                    Gson gson = new Gson();
                                    predictions = gson.fromJson(response.toString(), PlacePredictions.class);
                                    if (mAutoCompleteAdapter == null) {
                                        mAutoCompleteAdapter = new AutoCompleteAdapter(CustomGooglePlacesSearch.this, predictions.getPlaces(), CustomGooglePlacesSearch.this);
                                        mAutoCompleteList.setAdapter(mAutoCompleteAdapter);
                                    } else {
                                        mAutoCompleteList.setVisibility(View.VISIBLE);
                                        mAutoCompleteAdapter.clear();
                                        mAutoCompleteAdapter.addAll(predictions.getPlaces());
                                        mAutoCompleteAdapter.notifyDataSetChanged();
                                        mAutoCompleteList.invalidate();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.v("PayNowRequestResponse", error.toString());
                                }
                            });
                            XuberServicesApplication.getInstance().addToRequestQueue(jsonObjectRequest);

                        }

                    };

                    // only canceling the network calls will not help, you need to remove all callbacks as well
                    // otherwise the pending callbacks and messages will again invoke the handler and will send the request
                    if (handler != null) {
                        handler.removeCallbacksAndMessages(null);
                    } else {
                        handler = new Handler();
                    }
                    handler.postDelayed(run, 1000);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        mAutoCompleteList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                // pass the result to the calling activity


                String placeId=predictions.getPlaces().get(position).getPlaceID();

                Log.e("PlacesId ",placeId);

             /*   final String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" +
                        placeId + "&key=AIzaSyAK5It4p1CiJ2gFzWRbfs24Cibo2QTcPRU"; */

                final String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" +
                        placeId + "&key=AIzaSyAAgL20f3ydrQcvvl77vldDinOqPNjv0LY";

                Log.e("url ",url);

                serviceArrive(url);

                Places.GeoDataApi.getPlaceById(mGoogleApiClient, predictions.getPlaces().get(position).getPlaceID())
                        .setResultCallback(new ResultCallback<PlaceBuffer>()
                        {
                            @Override
                            public void onResult(PlaceBuffer places)
                            {
                              /*  if (places.getStatus().isSuccess()) {
                                    Place myPlace = places.get(0);
                                    LatLng queriedLocation = myPlace.getLatLng();
                                    Log.v("Latitude is", "" + queriedLocation.latitude);
                                    Log.v("Longitude is", "" + queriedLocation.longitude);
                                        placePredictions.strSourceAddress = myPlace.getAddress().toString();
                                        placePredictions.strSourceLatLng = myPlace.getLatLng().toString();
                                        placePredictions.strSourceLatitude = myPlace.getLatLng().latitude+"";
                                        placePredictions.strSourceLongitude = myPlace.getLatLng().longitude+"";
                                        txtaddressSource.setText(placePredictions.strSourceAddress);
                                        txtaddressSource.setSelection(0);

                                }
                                mAutoCompleteList.setVisibility(View.GONE);
                                txtPickLocation.setVisibility(View.GONE);
                                places.release();
                                setAddress();*/
                            }
                        });
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setAddress();
                finish();
            }
        });

    }

    private void serviceArrive(String url1)
    {
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            URL url = new URL(url1);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            Log.e("url ", "" + url);
            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1)
            {
                jsonResults.append(buff, 0, read);
            }
        }
        catch (MalformedURLException e)
        {
            Log.e("EXC", "Error processing Places API URL", e);
        }
        catch (IOException e)
        {
            Log.e("EXC", "Error connecting to Places API", e);
        }
        finally
        {
            if (conn != null)
            {
                conn.disconnect();
            }
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            Log.e("JSONObjectValue ",jsonObj+"");

            JSONObject res = jsonObj.getJSONObject("result");
            JSONObject geo = res.getJSONObject("geometry");
            JSONObject loc = geo.getJSONObject("location");


            String address= GeoLocation.getAddress(this,
                    Double.parseDouble(loc.getString("lat")),
                    Double.parseDouble(loc.getString("lng")));

//            Log.e("SelectedAddress ",address);
            Log.e("After ",type);

            if (type.equalsIgnoreCase("pick"))
            {
                placePredictions.strSourceAddress = address;
                placePredictions.strSourceLatitude = loc.getString("lat")+"";
                placePredictions.strSourceLongitude = loc.getString("lng")+"";
                Log.e("SelectedAddressLat ",placePredictions.strSourceLatitude);
                Log.e("SelectedAddressLng ",placePredictions.strSourceLongitude);
                txtaddressSource.setText(placePredictions.strSourceAddress);
                txtaddressSource.setSelection(0);
                mAutoCompleteList.setVisibility(View.GONE);
                txtPickLocation.setVisibility(View.GONE);
            }

            else if (type.equalsIgnoreCase("drop"))
            {
                placePredictions.strDestAddress = address;
                placePredictions.strDestLatitude = loc.getString("lat")+"";
                placePredictions.strDestLongitude = loc.getString("lng")+"";
                Log.e("DestLattitude ",placePredictions.strDestLatitude);
                Log.e("DestLongitude ",placePredictions.strDestLongitude);
                txtaddressSource.setText(placePredictions.strDestAddress);
                txtaddressSource.setSelection(0);
                mAutoCompleteList.setVisibility(View.GONE);
                txtPickLocation.setVisibility(View.GONE);
            }
//            places.release();
            setAddress();


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public String getPlaceAutoCompleteUrl(String input) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/place/autocomplete/json");
        urlString.append("?input=");
        try {
            urlString.append(URLEncoder.encode(input, "utf8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        urlString.append("&location=");
        urlString.append(latitude + "," + longitude); // append lat long of current location to show nearby results.
        urlString.append("&radius=500&language=en");
        //urlString.append("&key=" + "AIzaSyA6WMsVft37l07lwjuM9HpzwvxG08Rt4pE");//AIzaSyCiHMKnzPt86tPDGr7HBBonth3KQgSMOqw
//        urlString.append("&key=" + "AIzaSyCiHMKnzPt86tPDGr7HBBonth3KQgSMOqw");//AIzaSyCiHMKnzPt86tPDGr7HBBonth3KQgSMOqw
        urlString.append("&key=" + getResources().getString(R.string.google_map_api));

        Log.d("FINAL URL:::   ", urlString.toString());
        return urlString.toString();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);

            if (mLastLocation != null) {
                latitude = mLastLocation.getLatitude();
                longitude = mLastLocation.getLongitude();
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void fetchLocation(){
        //Build google API client to use fused location
        buildGoogleApiClient();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults)
    {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOC:
                {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission granted!
                    fetchLocation();
                } else {
                    // permission denied!
                    Toast.makeText(this, "Please grant permission for using this app!",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onBackPressed() {
        setAddress();
        super.onBackPressed();
    }

    void setAddress()
    {
        utils.hideKeypad(thisActivity, getCurrentFocus());
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent();
                if(placePredictions != null)
                {
                    intent.putExtra("Location Address", placePredictions);
                    intent.putExtra("pick_location","no");
                    intent.putExtra("type",type);
                    setResult(RESULT_OK, intent);
                }
                else
                {
                    setResult(RESULT_CANCELED, intent);
                }
                finish();
            }
        }, 500);
    }

}
