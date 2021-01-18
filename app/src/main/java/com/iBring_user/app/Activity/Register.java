package com.iBring_user.app.Activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;
import com.iBring_user.app.R;
import com.iBring_user.app.Constants.URLHelper;
import com.iBring_user.app.Helper.ConnectionHelper;
import com.iBring_user.app.Helper.CustomDialog;
import com.iBring_user.app.Helper.SharedHelper;
import com.iBring_user.app.Utils.Utilities;
import com.iBring_user.app.XuberServicesApplication;
import com.iBring_user.app.paypal.PaypalMain;
import com.iBring_user.app.retrofit.RetrofitResponse;
import com.iBring_user.app.retrofit.RetrofitService;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static com.iBring_user.app.XuberServicesApplication.trimMessage;



public class Register extends AppCompatActivity  implements RetrofitResponse
{
    public Context context = Register.this;
    public Activity activity = Register.this;
    String TAG = "Register";
    String device_token, device_UDID;
     // ImageView backArrow;
    Button btnSignUp;
    EditText email, first_name, last_name, mobile_no, password,Cpassword;
    CustomDialog customDialog;
    ConnectionHelper helper;
    Boolean isInternet;
    Utilities utils = new Utilities();
//    LinearLayout lnrRegister;
    RelativeLayout lnrRegister;
    ImageView ivEye,ivCEye;
    TextView tvSignin;
    TextView lblforgotpassword;

    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%*-_]).{6,20})";
    private Pattern pattern;
    private Matcher matcher;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }*/
//        setContentView(R.layout.activity_begin_register);
        setContentView(R.layout.signup);
        findViewById();
        GetToken();

        if (Build.VERSION.SDK_INT > 15)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        tvSignin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                GoToSignInActivity();
            }
        });

        lblforgotpassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharedHelper.putKey(Register.this, "password", "");
                Intent mainIntent = new Intent(Register.this, ForgetPassword.class);
                startActivity(mainIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            }
        });

        lnrRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
              Log.e("Mobile No.",""+mobile_no.getText().toString());
              Log.e("Mobile Lng",""+mobile_no.getText().toString().length());

                pattern = Pattern.compile(PASSWORD_PATTERN);
                matcher=pattern.matcher(password.getText().toString().trim());
                Log.e("matcher",""+matcher+"");

                Utilities.hideKeypad(Register.this,lnrRegister);
                Pattern ps = Pattern.compile(".*[0-9].*");
                Matcher firstName = ps.matcher(first_name.getText().toString());




               // Matcher lastName = ps.matcher(last_name.getText().toString());
                if (first_name.getText().toString().equals("") ||
                        first_name.getText().toString().equalsIgnoreCase(getString(R.string.first_name)))
                {

                    Toast.makeText(Register.this,getString(R.string.first_name_empty), Toast.LENGTH_SHORT).show();
                    // displayMessage(getString(R.string.first_name_empty));
                }
                else if (firstName.matches())
                {
                    Toast.makeText(Register.this,getString(R.string.first_name_no_number), Toast.LENGTH_SHORT).show();
//                    displayMessage(getString(R.string.first_name_no_number));
                }
                else if(first_name.getText().toString().trim().length()<3 || first_name.getText().toString().trim().length()>30)
                {
                    Toast.makeText(Register.this, "First name must be between 3 and 30 characters", Toast.LENGTH_SHORT).show();
                }
               else if (email.getText().toString().equals(""))
               {
//                    displayMessage(getString(R.string.email_validation));
                    Toast.makeText(Register.this,"Please enter email address",
                            Toast.LENGTH_SHORT).show();

                }
                else if (!email.getText().toString().trim().matches(Patterns.EMAIL_ADDRESS.pattern()))
                {
                    Toast.makeText(Register.this,"Please enter valid email address",
                            Toast.LENGTH_SHORT).show();
                }

               else if (mobile_no.getText().toString().equals("") || mobile_no.getText().toString().equalsIgnoreCase(getString(R.string.mobile_no)))
               {
//                    displayMessage(getString(R.string.mobile_number_empty));
                    Toast.makeText(Register.this,getString(R.string.mobile_number_empty), Toast.LENGTH_SHORT).show();
               }
                else if (mobile_no.getText().toString().length() < 10 || mobile_no.getText().toString().length() > 15) {
//                    displayMessage(getString(R.string.mobile_no_length));
                    Toast.makeText(Register.this,getString(R.string.mobile_no_length), Toast.LENGTH_SHORT).show();
                }

                else if (password.getText().toString().equals("")) {
//                    displayMessage(getString(R.string.password_validation));
                    Toast.makeText(Register.this,getString(R.string.password_validation), Toast.LENGTH_SHORT).show();

                }
                else if (password.getText().toString().length() < 6) {
                    Toast.makeText(Register.this,getString(R.string.passwd_length), Toast.LENGTH_SHORT).show();

//                    displayMessage(getString(R.string.passwd_length));
                }else if (!matcher.matches())
                {
                    Toast.makeText(Register.this,"Please enter atleast one small letter, one capital letter," +
                            "one digit and one special character.", Toast.LENGTH_SHORT).show();

                }
               else if (Cpassword.getText().toString().equals("")) {
//                    displayMessage(getString(R.string.cpassword_validation));

                    Toast.makeText(Register.this,getString(R.string.cpassword_validation), Toast.LENGTH_SHORT).show();

                }

                else if (!password.getText().toString().trim().equalsIgnoreCase(Cpassword.getText().toString().trim()))
                {
//                    displayMessage(getString(R.string.confirm_equal));

                    Toast.makeText(Register.this,getString(R.string.confirm_equal), Toast.LENGTH_SHORT).show();

                }

                else
                {
                    if (isInternet)
                    {
//                        registerAPI();
                        callRegisterrrr();
                    }
                    else
                    {
//               displayMessage(getString(R.string.something_went_wrong_net));
                 Toast.makeText(Register.this, getString(R.string.something_went_wrong_net), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

/*        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SharedHelper.getKey(context, "from").equalsIgnoreCase("email")){
                    Intent mainIntent = new Intent(Register.this, ActivityEmail.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                    Register.this.finish();
                }else{
                    Intent mainIntent = new Intent(Register.this, ActivityPassword.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                    Register.this.finish();
                }

            }
        });*/


    }

    public void findViewById()
    {
        email = (EditText) findViewById(R.id.email);
        first_name = (EditText) findViewById(R.id.first_name);
       // last_name = (EditText) findViewById(R.id.last_name);
        mobile_no = (EditText) findViewById(R.id.mobile_no);
        password = (EditText) findViewById(R.id.password);
        Cpassword = (EditText) findViewById(R.id.Cpassword);
        ivEye = (ImageView) findViewById(R.id.ivEye);
        ivCEye = (ImageView) findViewById(R.id.ivCEye);
//        btnSignUp = (Button) findViewById(R.id.btnSignUp);
//        lnrRegister = (LinearLayout) findViewById(R.id.lnrRegister);
        lnrRegister = (RelativeLayout) findViewById(R.id.lnrRegister);
        tvSignin = (TextView) findViewById(R.id.tvSignin);
        lblforgotpassword = (TextView) findViewById(R.id.lblforgotpassword);
//        backArrow = (ImageView) findViewById(R.id.backArrow);
        helper = new ConnectionHelper(context);
        isInternet = helper.isConnectingToInternet();
       // email.setText(SharedHelper.getKey(context, "email"));


        password.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });

        Cpassword.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });


      /* password.setCustomSelectionActionModeCallback(new ActionMode.Callback()
      {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode)
            {

            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu)
            {
                return true;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item)
            {
                return false;
            }

        });
        */

        ivEye.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance()))
                {
                    ivEye.setImageResource(R.drawable.hide_eye);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password.setSelection(password.length());
                }
                else {
                    ivEye.setImageResource(R.drawable.visible_eye);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    password.setSelection(password.length());
                }
            }
        });

        ivCEye.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (Cpassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance()))
                {
                    ivCEye.setImageResource(R.drawable.hide_eye);
                    Cpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    Cpassword.setSelection(Cpassword.length());
                }
                else
                    {
                    ivCEye.setImageResource(R.drawable.visible_eye);
                    Cpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        Cpassword.setSelection(Cpassword.length());
                }
            }
        });
    }


    private void registerAPI()
    {
        customDialog = new CustomDialog(Register.this);
        customDialog.setCancelable(false);
        customDialog.show();
        JSONObject object = new JSONObject();
        try
        {
            object.put("device_type", "android");
            object.put("device_id", device_UDID);
            object.put("device_token", "" + device_token);
            object.put("login_by", "manual");
            object.put("first_name", first_name.getText().toString());
            object.put("last_name", first_name.getText().toString());
            object.put("email", email.getText().toString());
            object.put("password", password.getText().toString());
            object.put("mobile", mobile_no.getText().toString());
            object.put("picture", "");
            object.put("social_unique_id", "");

            utils.print("InputToRegisterAPI", "" + object);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                URLHelper.REGISTER, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {



                customDialog.dismiss();
                utils.print("SignInResponse", response.toString());


                try {
                    if (response.getString("status").equalsIgnoreCase("error"))
                    {
                        Toast.makeText(Register.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {
                        SharedHelper.putKey(Register.this, "email", email.getText().toString());
                        SharedHelper.putKey(Register.this, "password", password.getText().toString());
                        signIn();
                    }
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                utils.print("RegisterError ", "" + error);
                customDialog.dismiss();
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;

                if (error instanceof TimeoutError)
                {

                    Log.e("InsideTimeOut ", "" + error);
                    registerAPI();
                    /*try {
                        JSONObject errorObj = new JSONObject(new String(response.data));
                        utils.print("errorObj", "" + error);
                        if (errorObj.getString("status").equalsIgnoreCase("error"))
                        {
                            Toast.makeText(Register.this, errorObj.getString("message"), Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }catch (Exception exx)
                    {
                        exx.printStackTrace();
                    }*/
                }

                if (response != null && response.data != null)
                {
                    utils.print("MyTest", "" + error);
                    utils.print("MyTestError", "" + error.networkResponse);
                    utils.print("MyTestError1", "" + response.statusCode);
                    try {
                        JSONObject errorObj = new JSONObject(new String(response.data));
                        if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500)
                        {
                            try {
//                                displayMessage(errorObj.optString("message"));
                                Toast.makeText(Register.this, errorObj.optString("message"), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
//                                displayMessage(getString(R.string.something_went_wrong));
                                Toast.makeText(Register.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                            }
                        } else if (response.statusCode == 401) {
                            try {
                                if (errorObj.optString("message").equalsIgnoreCase("invalid_token")) {
                                    //Call Refresh token
                                    Toast.makeText(Register.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                } else {
//                                    displayMessage(errorObj.optString("message"));
                                    Toast.makeText(Register.this, errorObj.optString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
//                                displayMessage(getString(R.string.something_went_wrong));
                                Toast.makeText(Register.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                            }

                        } else if (response.statusCode == 422) {

                            json = trimMessage(new String(response.data));
                            if (json != "" && json != null) {
//                                displayMessage(json);
                                Toast.makeText(Register.this, json, Toast.LENGTH_SHORT).show();
                            } else {
//                                displayMessage(getString(R.string.please_try_again));
                                Toast.makeText(Register.this, getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
                            }

                        } else {
//                            displayMessage(getString(R.string.please_try_again));
                            Toast.makeText(Register.this, getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();

                        }

                    } catch (Exception e) {
//                        displayMessage(getString(R.string.something_went_wrong));
                        Toast.makeText(Register.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                    }


                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                return headers;
            }
        };

        XuberServicesApplication.getInstance().addToRequestQueue(jsonObjectRequest);

    }

    public void showVerificationPopUp()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.payment_done_success, null);

        TextView tvLabel=(TextView)dialogView.findViewById(R.id.tvLabel);
        TextView tvCancel=(TextView)dialogView.findViewById(R.id.tvCancel);
        TextView tvOk=(TextView)dialogView.findViewById(R.id.tvOk);
        ImageView ivLabel=(ImageView) dialogView.findViewById(R.id.ivLabel);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        int width = WindowManager.LayoutParams.WRAP_CONTENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;


        tvLabel.setText("A verification e-mail has been sent. \n please check to activate your account");
//        tvLabel.setText("We are excited to have you get started.First you need \n to confirm your account.\n Just check your email to activate your account");
        tvCancel.setVisibility(View.GONE);
        tvOk.setVisibility(View.GONE);
        ivLabel.setImageDrawable(getResources().getDrawable(R.drawable.cancel_req_icon));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                GoToMainActivity1();
            }
        },3000);



        alertDialog.show();
    }


    public void callRegisterrrr()
    {
        try
        {
            JSONObject object=new JSONObject();

            object.put("device_type", "android");
            object.put("device_id", device_UDID);
            object.put("device_token", "" + device_token);
            object.put("login_by", "manual");
            object.put("first_name", first_name.getText().toString());
            object.put("last_name", first_name.getText().toString());
            object.put("email", email.getText().toString());
            object.put("password", password.getText().toString());
            object.put("mobile", mobile_no.getText().toString());
            object.put("picture", "");
            object.put("social_unique_id", "");

            Log.e("registerrrrParams ", "" + object);

            new RetrofitService(this, this, URLHelper.REGISTER ,
                    object,
                    100, 2,"1").callService(true);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void signIn()
    {
        if (isInternet)
        {
            customDialog = new CustomDialog(Register.this);
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();
            try
            {
               /* object.put("grant_type", "password");
                object.put("client_id", URLHelper.CLIENT_ID);
                object.put("client_secret", URLHelper.CLIENT_SECRET_KEY);*/
//                object.put("username", SharedHelper.getKey(Register.this, "email"));
                object.put("email", SharedHelper.getKey(Register.this, "email"));
                object.put("password", SharedHelper.getKey(Register.this, "password"));
//                object.put("scope", "");

                object.put("device_type", "android");
                object.put("device_id", device_UDID);
                object.put("device_token", device_token);
                utils.print("InputToLoginAPI", "" + object);

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.LOGIN, object,
                    new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response)
                {
                    customDialog.dismiss();

                    try {
                        utils.print("SignUpResponse", response.toString());
                        SharedHelper.putKey(context, "access_token", response.optString("access_token"));
                        SharedHelper.putKey(context, "refresh_token", response.optString("refresh_token"));
                        SharedHelper.putKey(context, "token_type", response.optString("token_type"));



                        if (response.getString("status").equalsIgnoreCase("error"))
                        {
                            showVerificationPopUp();
                        }
                        else {
                            getProfile();

                        }
                    }catch (Exception ex)
                    {
                        ex.printStackTrace();
                    }



//                    GoToMainActivity();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    customDialog.dismiss();
                    String json = null;
                    NetworkResponse response = error.networkResponse;
                    if (response != null && response.data != null) {
                        try {
                            JSONObject errorObj = new JSONObject(new String(response.data));

                            if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500)
                            {
                                try
                                {
//                                    displayMessage(errorObj.optString("message"));
                                    Toast.makeText(Register.this,errorObj.optString("message") , Toast.LENGTH_SHORT).show();
                                } catch (Exception e)
                                {
//                                    displayMessage(getString(R.string.something_went_wrong));
                                    Toast.makeText(Register.this,getString(R.string.something_went_wrong) , Toast.LENGTH_SHORT).show();
                                }
                            } else if (response.statusCode == 401) {
                                try {
                                    if (errorObj.optString("message").equalsIgnoreCase("invalid_token")) {
                                        //Call Refresh token
                                    } else {
//                                        displayMessage(errorObj.optString("message"));
                                        Toast.makeText(Register.this,errorObj.optString("message") , Toast.LENGTH_SHORT).show();

                                    }
                                } catch (Exception e) {
//                                    displayMessage(getString(R.string.something_went_wrong));
                                    Toast.makeText(Register.this,getString(R.string.something_went_wrong) , Toast.LENGTH_SHORT).show();

                                }

                            } else if (response.statusCode == 422) {

                                json = trimMessage(new String(response.data));
                                if (json != "" && json != null) {
//                                    displayMessage(json);
                                    Toast.makeText(Register.this,json , Toast.LENGTH_SHORT).show();

                                } else {
//                                    displayMessage(getString(R.string.please_try_again));
                                    Toast.makeText(Register.this,getString(R.string.please_try_again) , Toast.LENGTH_SHORT).show();

                                }

                            } else {
//                                displayMessage(getString(R.string.please_try_again));
                                Toast.makeText(Register.this,json , Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
//                            displayMessage(getString(R.string.something_went_wrong));
                            Toast.makeText(Register.this,getString(R.string.something_went_wrong) , Toast.LENGTH_SHORT).show();
                        }


                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    return headers;
                }
            };

            XuberServicesApplication.getInstance().addToRequestQueue(jsonObjectRequest);
        } else {
//            displayMessage(getString(R.string.something_went_wrong_net));
            Toast.makeText(Register.this,getString(R.string.something_went_wrong_net) , Toast.LENGTH_SHORT).show();

        }

    }

    public void getProfile()
    {
        if (isInternet) {
            customDialog = new CustomDialog(Register.this);
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URLHelper.GET_USER_PROFILE, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response)
                {
                    customDialog.dismiss();
                    utils.print("GetProfile", response.toString());
                    SharedHelper.putKey(Register.this, "id", response.optString("id"));
                    SharedHelper.putKey(Register.this, "first_name", response.optString("first_name"));
                    //SharedHelper.putKey(Register.this, "last_name", response.optString("last_name"));
                    SharedHelper.putKey(Register.this, "email", response.optString("email"));
                    SharedHelper.putKey(Register.this, "picture", Utilities.getImageURL(response.optString("picture")));
                    SharedHelper.putKey(Register.this, "gender", response.optString("gender"));
                    SharedHelper.putKey(Register.this, "mobile", response.optString("mobile"));
                    SharedHelper.putKey(Register.this, "wallet_balance", response.optString("wallet_balance"));
                    SharedHelper.putKey(Register.this, "payment_mode", response.optString("payment_mode"));
                    SharedHelper.putKey(context, "currency", response.optString("currency"));
                    SharedHelper.putKey(Register.this, "loggedIn", getString(R.string.True));
                    GoToMainActivity();

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
//                                    displayMessage(errorObj.optString("message"));
                                    Toast.makeText(Register.this,errorObj.optString("message") , Toast.LENGTH_SHORT).show();

                                } catch (Exception e)
                                {
//                                    displayMessage(getString(R.string.something_went_wrong));
                                    Toast.makeText(Register.this,getString(R.string.something_went_wrong) , Toast.LENGTH_SHORT).show();
                                }
                            } else if (response.statusCode == 401) {
                                try {
                                    if (errorObj.optString("message").equalsIgnoreCase("invalid_token")) {
                                        //Call Refresh token
                                    } else {
//                                        displayMessage(errorObj.optString("message"));
                                        Toast.makeText(Register.this,errorObj.optString("message") , Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
//                                    displayMessage(getString(R.string.something_went_wrong));
                                    Toast.makeText(Register.this,getString(R.string.something_went_wrong) , Toast.LENGTH_SHORT).show();

                                }

                            } else if (response.statusCode == 422) {

                                json = trimMessage(new String(response.data));
                                if (json != "" && json != null) {
//                                    displayMessage(json);
                                    Toast.makeText(Register.this,json , Toast.LENGTH_SHORT).show();

                                } else {
//                                    displayMessage(getString(R.string.please_try_again));
                                    Toast.makeText(Register.this,getString(R.string.please_try_again) , Toast.LENGTH_SHORT).show();
                                }

                            } else {
//                                displayMessage(getString(R.string.please_try_again));
                                Toast.makeText(Register.this,getString(R.string.please_try_again) , Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
//                            displayMessage(getString(R.string.something_went_wrong));
                            Toast.makeText(Register.this,getString(R.string.something_went_wrong) , Toast.LENGTH_SHORT).show();

                        }


                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Authorization", "" + SharedHelper.getKey(Register.this, "token_type") +
                            " " + SharedHelper.getKey(Register.this, "access_token"));
                    return headers;
                }
            };

            XuberServicesApplication.getInstance().addToRequestQueue(jsonObjectRequest);
        }
        else
        {
//            displayMessage(getString(R.string.something_went_wrong_net));
            Toast.makeText(Register.this,getString(R.string.something_went_wrong_net) , Toast.LENGTH_SHORT).show();
        }
    }



    public void GoToMainActivity()
    {
        Intent mainIntent = new Intent(Register.this, Home.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        Register.this.finish();
    }

    public void GoToMainActivity1()
    {
        Intent mainIntent = new Intent(Register.this, SignIn.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        Register.this.finish();
    }

    public void displayMessage(String toastString)
    {
        utils.print("displayMessage", "" + toastString);
        try {
            if (getCurrentFocus() != null) {
                Snackbar snackbar = Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT);
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));
       /* TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(ContextCompat.getColor(this, R.color.white));*/
                snackbar.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    protected void onRestart()
    {
        super.onRestart();
    }

    @Override
    public void onBackPressed()
    {
        // overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        if (SharedHelper.getKey(context, "from").equalsIgnoreCase("email"))
        {
            Intent mainIntent = new Intent(Register.this, SignIn.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            Register.this.finish();
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);

        }
        else
        {
            /*Intent mainIntent = new Intent(Register.this, ActivityPassword.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            Register.this.finish();*/
        }
    }

    public void GoToSignInActivity()
    {
        Intent mainIntent = new Intent(activity, SignIn.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        activity.finish();
    }

    public void GetToken()
    {
        try
        {
            if (!SharedHelper.getKey(activity, "device_token").equals("") &&
                    SharedHelper.getKey(activity, "device_token") != null)
            {
                device_token = SharedHelper.getKey(activity, "device_token");
                Log.e("@#@#","Device token:"+device_token);
                utils.print(TAG, "GCM Registration Token: " + device_token);
            }
            else
            {
                device_token = "COULD NOT GET FCM TOKEN";
                utils.print(TAG, "Failed to complete token refresh: " + device_token);
            }
        }
        catch (Exception e)
        {
            device_token = "COULD NOT GET FCM TOKEN";
            utils.print(TAG, "Failed to complete token refresh");
        }

        try
        {
            device_UDID = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
           Log.e("@#@#","Device UDID:"+device_UDID);
            utils.print(TAG, "Device UDID:" + device_UDID);
        }
        catch (Exception e)
        {
            device_UDID = "COULD NOT GET UDID";
            e.printStackTrace();
            utils.print(TAG, "Failed to complete device UDID");
        }
    }

    @Override
    public void onResponse(int RequestCode, String response)
    {
        switch (RequestCode)
        {
            case 100:
                try {
                    Log.e("SignUpResponseeeee ", response.toString());

                    JSONObject jsonObject=new JSONObject(response);


                    if (jsonObject.has("status"))
                    {
                        if (jsonObject.getString("status").equalsIgnoreCase("error"))
                        {
                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        showVerificationPopUp();
                        }
                        else {
                            showVerificationPopUp();
                        }
                    }
                    else {
                        if (jsonObject.has("device_token"))
                        {
                            showVerificationPopUp();
                        }
                        /*SharedHelper.putKey(context, "access_token", jsonObject.optString("access_token"));
                        SharedHelper.putKey(context, "refresh_token", jsonObject.optString("refresh_token"));
                        SharedHelper.putKey(context, "token_type", jsonObject.optString("token_type"));
                        getProfile();*/


                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                break;
        }
    }
}

