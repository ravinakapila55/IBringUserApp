package com.iBring_user.app.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.iBring_user.app.R;
import com.iBring_user.app.Constants.URLHelper;
import com.iBring_user.app.Helper.ConnectionHelper;
import com.iBring_user.app.Helper.CustomDialog;
import com.iBring_user.app.Helper.SharedHelper;
import com.iBring_user.app.Utils.Utilities;
import com.iBring_user.app.XuberServicesApplication;
import com.iBring_user.app.tutorial.TutorialScreens;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;


public class SplashScreen extends AppCompatActivity
{
    String TAG = "SplashActivity";
    public Activity activity = SplashScreen.this;
    public Context context = SplashScreen.this;
    ConnectionHelper helper;
    Boolean isInternet;
    String device_token, device_UDID;
    Handler handleCheckStatus;
    AlertDialog alert;
    CustomDialog customDialog;
    int retryCount = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorBlue));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorBlue));
        }*/
        setContentView(R.layout.activity_splash);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final NetworkInfo mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();
        handleCheckStatus = new Handler();
        //check status every 3 sec


        if (Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);




        Log.e("KeyHash ",generateKeyHash());



        handleCheckStatus.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.w("Handler", "Called");
                if (helper.isConnectingToInternet()) {
                    if (SharedHelper.getKey(context,"loggedIn").equalsIgnoreCase(getString(R.string.True)))
                    {
                            GetToken();
                            getProfile();
                    }
                    else {

                        Log.e("TutorialSharedValue ",SharedHelper.getBoolean(context,"tutorial",false)+"");

                        boolean value=SharedHelper.getBoolean(context,"tutorial",false);

                        Log.e("valueeee ",value+"");

                        if (!value)
                        {
                            Log.e("Valueee ","ist");
                            //show start activity
                            GoToBeginActivity();
                        }
                        else {
                            Log.e("Valueee ","2nd");

                            Intent intent=new Intent(SplashScreen.this, SignIn.class);
                            startActivity(intent);
                        }

                    }
                    if(alert != null && alert.isShowing()){
                        alert.dismiss();
                    }
                }else{
                    showDialog();
                    handleCheckStatus.postDelayed(this, 3000);
                }
            }
        }, 3000);



    }

    private String generateKeyHash()
    {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager.GET_SIGNATURES); for (Signature signature : info.signatures)
            { MessageDigest md = (MessageDigest.getInstance("SHA")); md.update(signature.toByteArray());
                return new String(Base64.encode(md.digest(), 0)); } }
        catch (Exception e)
        {
            Log.e("exception", e.toString());
        }
        return "key hash not found";
    }


    public void getProfile(){

           Log.e("GetPostAPI",""+ URLHelper.GET_USER_PROFILE +"?device_type=android&device_id="+device_UDID+"&device_token="+device_token);
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    URLHelper.GET_USER_PROFILE +"?device_type=android&device_id="+device_UDID+"&device_token="+device_token,
                    object , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.v("GetProfile",response.toString());
                    SharedHelper.putKey(context, "id", response.optString("id"));
                    SharedHelper.putKey(context, "first_name", response.optString("first_name"));
                    SharedHelper.putKey(context, "last_name", response.optString("last_name"));
                    SharedHelper.putKey(context, "email", response.optString("email"));
                    SharedHelper.putKey(context, "picture", Utilities.getImageURL(response.optString("picture")));
                    SharedHelper.putKey(context, "gender", response.optString("gender"));
                    SharedHelper.putKey(context, "mobile", response.optString("mobile"));
                    SharedHelper.putKey(context, "wallet_balance", response.optString("wallet_balance"));
                    SharedHelper.putKey(context, "payment_mode", response.optString("payment_mode"));
                    SharedHelper.putKey(context, "currency",response.optString("currency"));
                    SharedHelper.putKey(context,"loggedIn",getString(R.string.True));
                    GoToMainActivity();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String json = null;
                    String Message;
                    NetworkResponse response = error.networkResponse;
                    if(response != null && response.data != null) {
                        try {
                            JSONObject errorObj = new JSONObject(new String(response.data));
                            if(response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500 || response.statusCode == 422 ||
                                    response.statusCode == 503)
                            {
                               retry();
                            }
                            else if(response.statusCode == 401)
                            {
                                refreshAccessToken();
                            }
                            else {
                                retry();
                            }
                        }
                        catch (Exception e)
                        {
                            retry();
                        }
                    }else{
                           retry();
                    }
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Authorization",""+SharedHelper.getKey(context, "token_type")+" "+
                            SharedHelper.getKey(context, "access_token"));
                    return headers;
                }
            };

            XuberServicesApplication.getInstance().addToRequestQueue(jsonObjectRequest);

        }


    @Override
    protected void onDestroy() {
        handleCheckStatus.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private void refreshAccessToken()
    {
            JSONObject object = new JSONObject();
            try
            {
                object.put("grant_type", "refresh_token");
                object.put("client_id", URLHelper.CLIENT_ID);
                object.put("client_secret", URLHelper.CLIENT_SECRET_KEY);
                object.put("refresh_token", SharedHelper.getKey(context, "refresh_token"));
                object.put("scope", "");
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    URLHelper.LOGIN, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response)
                {

                    Log.v("SignUpResponse", response.toString());
                    SharedHelper.putKey(context, "access_token", response.optString("access_token"));
                    SharedHelper.putKey(context, "refresh_token", response.optString("refresh_token"));
                    SharedHelper.putKey(context, "token_type", response.optString("token_type"));
                    getProfile();
                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    String json = null;
                    String Message;
                    NetworkResponse response = error.networkResponse;

                    if (response != null && response.data != null)
                    {
                        GoToBeginActivity();
                    }
                }
            })
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    return headers;
                }
            };

            XuberServicesApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    public void GoToMainActivity()
    {
        Intent mainIntent = new Intent(activity, Home.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }

    public void GoToBeginActivity()
    {
        SharedHelper.putKey(context,"loggedIn",getString(R.string.False));
//        Intent mainIntent = new Intent(activity, BeginScreen.class);
        Intent mainIntent = new Intent(activity, TutorialScreens.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }

    public void displayMessage(String toastString)
    {
        Log.e("displayMessage",""+toastString);
        Toast.makeText(activity, toastString, Toast.LENGTH_SHORT).show();
    }

    public void GetToken()
    {
        try
        {
            if(!SharedHelper.getKey(context,"device_token").equals("") &&
                    SharedHelper.getKey(context,"device_token") != null)
            {
                device_token = SharedHelper.getKey(context, "device_token");
                Log.i(TAG, "GCM Registration Token: " + device_token);
            } else
           {
                device_token = "COULD NOT GET FCM TOKEN";
                Log.i(TAG, "Failed to complete token refresh: " + device_token);
           }
        }
        catch (Exception e)
        {
            device_token = "COULD NOT GET FCM TOKEN";
            Log.d(TAG, "Failed to complete token refresh", e);
        }

        try
        {
            device_UDID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.i(TAG, "Device UDID:" + device_UDID);
        }
        catch (Exception e)
        {
            device_UDID = "COULD NOT GET UDID";
            e.printStackTrace();
            Log.d(TAG, "Failed to complete device UDID");
        }
    }


    public void retry()
    {
        if(retryCount != 0)
        {
            retryCount = retryCount -1;
            getProfile();
        }
        else {
            GoToBeginActivity();
        }
    }

    private void showDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.connect_to_network))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.connect_to_wifi), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton(getString(R.string.quit), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        finish();
                    }
                });
        if(alert == null)
        {
            alert = builder.create();
            //alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            alert.setOnShowListener(new DialogInterface.OnShowListener()
            {
                @Override
                public void onShow(DialogInterface arg)
                {
                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                }
            });
            alert.show();
        }
    }

}
