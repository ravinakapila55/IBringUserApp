package com.iBring_user.app.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.iBring_user.app.Fragments.ServiceFlowFragment;
import com.iBring_user.app.R;
import com.iBring_user.app.Constants.URLHelper;
import com.iBring_user.app.Helper.ConnectionHelper;
import com.iBring_user.app.Helper.CustomDialog;
import com.iBring_user.app.Helper.SharedHelper;
import com.iBring_user.app.Utils.Utilities;
import com.iBring_user.app.Utils.permission.Permission;
import com.iBring_user.app.Utils.permission.PermissionGranted;
import com.iBring_user.app.XuberServicesApplication;
import com.iBring_user.app.retrofit.RetrofitResponse;
import com.iBring_user.app.retrofit.RetrofitService;

import org.jetbrains.annotations.NonNls;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.iBring_user.app.XuberServicesApplication.trimMessage;
/**
 * Created by jayakumar on 22/06/17.
 */

public class SignIn extends AppCompatActivity implements RetrofitResponse, PermissionGranted
{

    EditText txtemail,txtpassword;
    TextView lblforgotpassword;
    TextView tvLogin;
    TextView tvSignup;
    RelativeLayout rlLogin;
    Activity thisActivity;
    Boolean isInternet;
    ConnectionHelper helper;
    CustomDialog customDialog;
    String TAG = "SignIn";
    ImageView ivEye;

    ImageView ivFb,ivGoogle;

    String device_token, device_UDID;
    Utilities utils =new Utilities();

    private CallbackManager callbackManager;

    public void showVerificationPopUp(String key)
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


        if (key.equalsIgnoreCase("login"))
        {
            tvLabel.setText("Your account is not activate. \n please check your email to  activate the account");
        }
        else
        {
            tvLabel.setText("A verification e-mail has been sent. \n please check to activate your account");
        }

        tvCancel.setVisibility(View.GONE);
        tvOk.setVisibility(View.GONE);
        ivLabel.setImageDrawable(getResources().getDrawable(R.drawable.cancel_req_icon));

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                alertDialog.dismiss();
            }
        },3000);

        alertDialog.show();
    }

    public void fblogin()
    {
  /*      LoginManager.getInstance().logInWithReadPermissions(
                this,
                Arrays.asList("email", "public_profile")
        );  */

        LoginManager.getInstance().logInWithReadPermissions(this,
                Arrays.asList("public_profile", "email")
        );

        Log.e("callbackManager ",callbackManager.toString());
        LoginManager.getInstance().registerCallback(
                callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        Log.e("FbLoginSuccess ", loginResult.toString());
                        Log.e("FbLoginSuccessAccess ", loginResult.getAccessToken().getApplicationId());
                        Log.e("FbLoginSuccessAccess ", loginResult.getAccessToken().getUserId());
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    Log.e("JSONObject Value ",object.toString());
                                    socialLoginId=object.getString("id");
                                    registerAPI("facebook",object.getString("email"),object.getString("first_name"),
                                            object.getString("last_name"),"",object.getString("id"));
                                    LoginManager.getInstance().logOut();
                                } catch (Exception e) {
                                }
                            }
                        });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "email,id,first_name,last_name,picture");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        Log.e("onCancel ", "cancel");

                    }


                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("onError ", "cancel");
                    }
                }
        );

    }




    private void GooglesignIn() {
        Log.e("insideSendAcrivit ","yes");
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

//        updateUI(account);
    }

    GoogleSignInClient mGoogleSignInClient;
    public void googleSignIn()
    {
        Log.e("insideGoogleSignIn ","googleSignin");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GooglesignIn();
    }

        public void setSocialLogins(String type)
    {
        //2n/gb5V16zYW9hVP2rk11VsTaxo=
        Log.e("setSocialLogins ",type);
        callbackManager = CallbackManager.Factory.create();
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));

        emailNoti=getSaltString()+"@gmail.com";

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                // App code
                Log.e("Result ","Success"+loginResult);
            }

            @Override
            public void onCancel()
            {
                // App code
                Log.e("Result ","cancel");
            }

            @Override
            public void onError(FacebookException exception)
            {
                // App code
                Log.e("Result ","Error"+exception.toString());
//                registerAPI(type);

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1000) {

            Log.e("inside 1000 ","insideOnActivityResult");
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else {
            Log.e("inside OnActivity ",data.toString());
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask)
    {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.e(TAG, "signInResult:account Success" + account.getEmail());
            Log.e(TAG, "signInResult:account Success" + account.getId());
            Log.e(TAG, "signInResult:account Success" + account.getDisplayName());
            Log.e(TAG, "signInResult:account Success" + account.getGivenName());
//            Log.e(TAG, "signInResult:account Success" + account.getPhotoUrl());

            socialLoginId=account.getId();

            registerAPI("google",account.getEmail(),account.getDisplayName(),account.getGivenName(),"",account.getId());
            // Signed in successfully, show authenticated UI.
//            updateUI(account);
        } catch (ApiException e)
        {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }

    String emailNoti="";
    String socialLoginId="";





    private void registerAPI(String key,String email,String fname,String lname,String phone,String socialId)
    {
        try
        {
            JSONObject object=new JSONObject();
            object.put("device_type", "android");
            object.put("device_id", device_UDID);
            object.put("device_token", "" + device_token);

            object.put("first_name", fname);
            object.put("last_name", lname);
            object.put("email", email);
            object.put("password", "123456");
            object.put("mobile","2345678736");
            object.put("picture", "");
            if (key.equalsIgnoreCase("google"))
            {
                object.put("social_unique_id", socialId);
            }
            else {
                object.put("social_unique_id", socialId);
            }

            object.put("login_by", key);

            Log.e("InputToRegisterAPI ", "" + object);

            new RetrofitService(this, this, URLHelper.REGISTER ,
                    object,
                    100, 2,"1").callService(true);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }


   /* private void registerAPI(String key,String email,String fname,String lname,String phone,String socialId)
    {
        customDialog = new CustomDialog(SignIn.this);
        customDialog.setCancelable(false);
        customDialog.show();
        JSONObject object = new JSONObject();
        try
        {
            object.put("device_type", "android");
            object.put("device_id", device_UDID);
            object.put("device_token", "" + device_token);

            object.put("first_name", fname);
            object.put("last_name", lname);
            object.put("email", email);
            object.put("password", "123456");
            object.put("mobile","2345678736");
            object.put("picture", "");
            if (key.equalsIgnoreCase("google"))
            {
                object.put("social_unique_id", socialId);
            }
            else {
                object.put("social_unique_id", socialId);
            }

            object.put("login_by", key);

            Log.e("InputToRegisterAPI ", "" + object);
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
                Log.e("SignInResponse ", response.toString());
                SharedHelper.putKey(SignIn.this, "email", email);
                SharedHelper.putKey(SignIn.this, "password", "123456");
//                signIn();

                callSignIn();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customDialog.dismiss();
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;

                if (error instanceof TimeoutError)
                {
                    // registerAPI();
                }

                if (response != null && response.data != null) {
                    utils.print("MyTest", "" + error);
                    utils.print("MyTestError", "" + error.networkResponse);
                    utils.print("MyTestError1", "" + response.statusCode);
                    try {
                        JSONObject errorObj = new JSONObject(new String(response.data));

                        if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
                            try {
//                                displayMessage(errorObj.optString("message"));
                                Toast.makeText(SignIn.this,errorObj.optString("message"), Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
//                                displayMessage(getString(R.string.something_went_wrong));
                                Toast.makeText(SignIn.this,getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                            }
                        } else if (response.statusCode == 401) {
                            try {
                                if (errorObj.optString("message").equalsIgnoreCase("invalid_token")) {
                                    //Call Refresh token
                                } else {
//                                    displayMessage(errorObj.optString("message"));
                                    Toast.makeText(SignIn.this,errorObj.optString("message"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
//                                displayMessage(getString(R.string.something_went_wrong));
                                Toast.makeText(SignIn.this,getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                            }

                        } else if (response.statusCode == 422) {

                            json = trimMessage(new String(response.data));
                            if (json != "" && json != null) {
//                                displayMessage(json);
                                Toast.makeText(SignIn.this, json, Toast.LENGTH_SHORT).show();
                            } else {
//                                displayMessage(getString(R.string.please_try_again));
                                Toast.makeText(SignIn.this, "Please try again", Toast.LENGTH_SHORT).show();
                            }

                        } else {
//                            displayMessage(getString(R.string.please_try_again));
                            Toast.makeText(SignIn.this,getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
//                        displayMessage(getString(R.string.something_went_wrong));
                        Toast.makeText(SignIn.this,getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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

    }*/




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        thisActivity = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }

        Permission.checkPermissionLocation(this, this);

//        setContentView(R.layout.activity_begin_signin);
        setContentView(R.layout.sign_in);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }

        if (Build.VERSION.SDK_INT > 15)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        callbackManager=CallbackManager.Factory.create();

        helper = new ConnectionHelper(thisActivity);
        isInternet = helper.isConnectingToInternet();

        txtemail = (EditText)findViewById(R.id.txtemail);
        ivFb = (ImageView) findViewById(R.id.ivFb);
        ivGoogle = (ImageView) findViewById(R.id.ivGoogle);
//        lnrRegister = (LinearLayout) findViewById(R.id.lnrRegister);
        tvSignup = (TextView) findViewById(R.id.tvSignup);
        rlLogin = (RelativeLayout) findViewById(R.id.rlLogin);
        lblforgotpassword = (TextView) findViewById(R.id.lblforgotpassword);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        ivEye = (ImageView) findViewById(R.id.ivEye);
        txtpassword = (EditText)findViewById(R.id.txtpassword);

        txtpassword.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

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


       // btnSignIn = (Button) findViewById(R.id.btnSignIn);
        GetToken();

        ivEye.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (txtpassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance()))
                {

                    ivEye.setImageResource(R.drawable.eye_hide);
                    txtpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    txtpassword.setSelection(txtpassword.length());
                }
                else
                {
                    ivEye.setImageResource(R.drawable.eye_visible);
                    txtpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    txtpassword.setSelection(txtpassword.length());
                }
            }
        });

        ivFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("FbCLick ","fbClick");
                emailNoti=getSaltString()+"@gmail.com";
//                registerAPI("facebook");

//                setSocialLogins("facebook");

                try {
                    AccessToken accessToken = AccessToken.getCurrentAccessToken();
                    if (accessToken!=null)
                    {
                        LoginManager.getInstance().logOut();
                    }

                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                fblogin();
            }
        });

        ivGoogle.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.e("googleClick ","googleClick");
                emailNoti=getSaltString()+"@gmail.com";

                googleSignIn();
//                setSocialLogins("google");
//                registerAPI("google");
            }
        });

        lblforgotpassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SharedHelper.putKey(thisActivity, "password", "");
                Intent mainIntent = new Intent(thisActivity, ForgetPassword.class);
                startActivity(mainIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR)
                {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Utilities.hideKeypad(SignIn.this,tvSignup);
                SharedHelper.putKey(getApplicationContext(), "from", "email");
                SharedHelper.putKey(getApplicationContext(),"email", ""+txtemail.getText().toString());
                Intent mainIntent = new Intent(getApplicationContext(), Register.class);
                startActivity(mainIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR)
                {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            }
        });

        rlLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Utilities.hideKeypad(SignIn.this,rlLogin);
                if(txtemail.getText().toString().equals("") || txtemail.getText().toString().equalsIgnoreCase(getString(R.string.sample_mail_id)))
                {

                Toast.makeText(thisActivity,"Please enter email address", Toast.LENGTH_SHORT).show();
//              displayMessage(getString(R.string.email_validation));
                }
                else if((!isValidEmail(txtemail.getText().toString())))
                {
                    Toast.makeText(thisActivity,"Please enter valid email address", Toast.LENGTH_SHORT).show();
//                    displayMessage(getString(R.string.not_valid_email));
                }
                else if(txtpassword.getText().toString().equals("") || txtpassword.getText().toString().equalsIgnoreCase (getString(R.string.password_txt)))
                {
                    Toast.makeText(thisActivity,getString(R.string.password_validation), Toast.LENGTH_SHORT).show();
//                    displayMessage(getString(R.string.passwokrd_validation));
                }
                else if (txtpassword.getText().toString().length() < 6)
                {
                    Toast.makeText(thisActivity,getString(R.string.passwd_length), Toast.LENGTH_SHORT).show();
//                    displayMessage(getString(R.string.passwd_length));
                }
                else
                {
                        SharedHelper.putKey(thisActivity,"email",txtemail.getText().toString());
                        SharedHelper.putKey(thisActivity,"password",txtpassword.getText().toString());
                        //todo call signin
                        callSignIn();
                }
            }
        });


        tvLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Utilities.hideKeypad(SignIn.this,tvLogin);
                if(txtemail.getText().toString().equals("") || txtemail.getText().toString().equalsIgnoreCase(getString(R.string.sample_mail_id)))
                {

                    Toast.makeText(thisActivity,getString(R.string.email_validation), Toast.LENGTH_SHORT).show();
//                    displayMessage(getString(R.string.email_validation));
                }
                else if((!isValidEmail(txtemail.getText().toString())))
                {
                    Toast.makeText(thisActivity,getString(R.string.not_valid_email), Toast.LENGTH_SHORT).show();
//                    displayMessage(getString(R.string.not_valid_email));
                }
                else if(txtpassword.getText().toString().equals("") || txtpassword.getText().toString().equalsIgnoreCase (getString(R.string.password_txt)))
                {
                    Toast.makeText(thisActivity,getString(R.string.password_validation), Toast.LENGTH_SHORT).show();
//                    displayMessage(getString(R.string.password_validation));
                }
                else if (txtpassword.getText().toString().length() < 6)
                {
                    Toast.makeText(thisActivity,getString(R.string.passwd_length), Toast.LENGTH_SHORT).show();
//                    displayMessage(getString(R.string.passwd_length));
                }
                else
                {
                        SharedHelper.putKey(thisActivity,"email",txtemail.getText().toString());
                        SharedHelper.putKey(thisActivity,"password",txtpassword.getText().toString());
//                        signIn();
                    callSignIn();
                }
            }
        });
    }

    protected String getSaltString()
    {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();

        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }


    public void callSignIn()
    {
        try
        {
            JSONObject object=new JSONObject();

            object.put("email", SharedHelper.getKey(thisActivity, "email"));
            object.put("password", SharedHelper.getKey(thisActivity, "password"));
            object.put("device_type", "android");
            object.put("device_id", device_UDID);
            object.put("device_token", device_token);

            Log.e("CallFareParams ",object.toString());

            new RetrofitService(this, this, URLHelper.LOGIN ,
                    object,
                    700, 2,"1").callService(true);


        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void getprofileDetail()
    {
        new RetrofitService(this, this, URLHelper.GET_USER_PROFILE ,
                500, 1,"1").callService(true);
    }

    private void signIn() {
        if (isInternet)
        {
            customDialog = new CustomDialog(thisActivity);
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();
            try {

//                object.put("grant_type", "password");
//                object.put("client_id", URLHelper.CLIENT_ID);
//                object.put("client_secret", URLHelper.CLIENT_SECRET_KEY);
//                object.put("username", SharedHelper.getKey(thisActivity, "email"));
                object.put("email", SharedHelper.getKey(thisActivity, "email"));
                object.put("password", SharedHelper.getKey(thisActivity, "password"));
//                object.put("scope", "");
                object.put("device_type", "android");
                object.put("device_id", device_UDID);
                object.put("device_token", device_token);
                utils.print("InputToLoginAPI", "" + object);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.LOGIN, object,
                    new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response)
                {
                    customDialog.dismiss();
                    utils.print("SignUpResponse", response.toString());
                    SharedHelper.putKey(thisActivity, "access_token", response.optString("access_token"));
                    SharedHelper.putKey(thisActivity, "refresh_token", response.optString("refresh_token"));
                    SharedHelper.putKey(thisActivity, "token_type", response.optString("token_type"));
                    getProfile();
                }
            }, new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    customDialog.dismiss();
                    String json = null;
                    String Message;
                    NetworkResponse response = error.networkResponse;
                    utils.print("MyTest", "" + error);
                    utils.print("Response", "" + response);
                    utils.print("MyTestError", "" + error.networkResponse);

                    if (response != null && response.data != null) {
                        try
                        {
                            JSONObject errorObj = new JSONObject(new String(response.data));

                            if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500 || response.statusCode == 401)
                            {
                                try {
//                                    displayMessage(errorObj.optString("message"));
                                    Toast.makeText(SignIn.this,errorObj.optString("message"), Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
//                                    displayMessage(getString(R.string.something_went_wrong));
                                    Toast.makeText(SignIn.this,getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                                }
                            }else if (response.statusCode == 422)
                            {
                                json = trimMessage(new String(response.data));
                                if (json != "" && json != null)
                                {
//                                    displayMessage(json);
                                    Toast.makeText(SignIn.this,json, Toast.LENGTH_SHORT).show();

                                }
                                else {
//                                    displayMessage(getString(R.string.please_try_again));
                                    Toast.makeText(SignIn.this,getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
                                }

                            } else {
//                                displayMessage(getString(R.string.please_try_again));
                                Toast.makeText(SignIn.this,getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
//                            displayMessage(getString(R.string.something_went_wrong));
                            Toast.makeText(SignIn.this,getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
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

        }else
            {
//            displayMessage(getString(R.string.something_went_wrong_net));
            Toast.makeText(SignIn.this,getString(R.string.something_went_wrong_net), Toast.LENGTH_SHORT).show();
        }

    }



    public void getProfile() {

        if (isInternet) {

            customDialog = new CustomDialog(thisActivity);
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URLHelper.GET_USER_PROFILE,
                    object, new Response.Listener<JSONObject>()
            {
                @Override
                public void onResponse(JSONObject response)
                {
                    customDialog.dismiss();
                    utils.print("GetProfile", response.toString());
                    SharedHelper.putKey(thisActivity, "id", response.optString("id"));
                    SharedHelper.putKey(thisActivity, "first_name", response.optString("first_name"));
                    SharedHelper.putKey(thisActivity, "last_name", response.optString("last_name"));
                    SharedHelper.putKey(thisActivity, "email", response.optString("email"));
                    SharedHelper.putKey(thisActivity, "picture", Utilities.getImageURL(response.optString("picture")));
                    SharedHelper.putKey(thisActivity, "gender", response.optString("gender"));
                    SharedHelper.putKey(thisActivity, "mobile", response.optString("mobile"));
                    SharedHelper.putKey(thisActivity, "wallet_balance", response.optString("wallet_balance"));
                    SharedHelper.putKey(thisActivity, "payment_mode", response.optString("payment_mode"));
                    SharedHelper.putKey(thisActivity, "currency",response.optString("currency"));
                    SharedHelper.putKey(thisActivity, "loggedIn", getString(R.string.True));
                    GoToMainActivity();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {
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
                                    Toast.makeText(SignIn.this,errorObj.optString("message"), Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
//                                    displayMessage(getString(R.string.something_went_wrong));
                                    Toast.makeText(SignIn.this,getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                                }
                            } else if (response.statusCode == 401) {
                                refreshAccessToken();
                            } else if (response.statusCode == 422) {

                                json = trimMessage(new String(response.data));
                                if (json != "" && json != null) {
//                                    displayMessage(json);
                                    Toast.makeText(SignIn.this,json, Toast.LENGTH_SHORT).show();

                                } else {
//                                    displayMessage(getString(R.string.please_try_again));
                                    Toast.makeText(SignIn.this,getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
                                }

                            }else if(response.statusCode == 503){
//                                displayMessage(getString(R.string.server_down));
                                Toast.makeText(SignIn.this,getString(R.string.server_down), Toast.LENGTH_SHORT).show();

                            } else {
//                                displayMessage(getString(R.string.please_try_again));
                                Toast.makeText(SignIn.this,getString(R.string.please_try_again), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
//                            displayMessage(getString(R.string.something_went_wrong));
                            Toast.makeText(SignIn.this,getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                        }

                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Authorization", "" + SharedHelper.getKey(thisActivity, "token_type") + " "
                            + SharedHelper.getKey(thisActivity, "access_token"));
                    utils.print("authoization",""+SharedHelper.getKey(thisActivity, "token_type") + " "
                            + SharedHelper.getKey(thisActivity, "access_token"));
                    return headers;
                }
            };

            XuberServicesApplication.getInstance().addToRequestQueue(jsonObjectRequest);
        }
        else {
//            displayMessage(getString(R.string.something_went_wrong_net));
            Toast.makeText(SignIn.this,getString(R.string.something_went_wrong_net), Toast.LENGTH_SHORT).show();
        }

    }

    public void GoToMainActivity(){
        Intent mainIntent = new Intent(thisActivity, Home.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
        thisActivity.finish();
    }

    private void refreshAccessToken() {
        if (isInternet) {
            customDialog = new CustomDialog(thisActivity);
            customDialog.setCancelable(false);
            customDialog.show();
            JSONObject object = new JSONObject();
            try {

                object.put("grant_type", "refresh_token");
                object.put("client_id", URLHelper.CLIENT_ID);
                object.put("client_secret", URLHelper.CLIENT_SECRET_KEY);
                object.put("refresh_token", SharedHelper.getKey(thisActivity, "refresh_token"));
                object.put("scope", "");

                Log.e("LoginnnTokenParams ",object.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.LOGIN,
                    object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    customDialog.dismiss();
                    utils.print("SignUpResponse ", response.toString());
                    SharedHelper.putKey(thisActivity, "access_token", response.optString("access_token"));
                    SharedHelper.putKey(thisActivity, "refresh_token", response.optString("refresh_token"));
                    SharedHelper.putKey(thisActivity, "token_type", response.optString("token_type"));
                    getProfile();


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    customDialog.dismiss();
                    String json = null;
                    String Message;
                    NetworkResponse response = error.networkResponse;
                    utils.print("MyTest", "" + error);
                    utils.print("MyTestError", "" + error.networkResponse);
                    utils.print("MyTestError1", "" + response.statusCode);

                    if (response != null && response.data != null)
                    {
                        SharedHelper.putKey(thisActivity,"loggedIn",getString(R.string.False));
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

        }else
            {
//            displayMessage(getString(R.string.something_went_wrong_net));
                Toast.makeText(SignIn.this,getString(R.string.something_went_wrong_net), Toast.LENGTH_SHORT).show();
        }

    }

    public void GoToBeginActivity(){
        finish();
      /*  Intent mainIntent = new Intent(thisActivity, BeginScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        }
        thisActivity.finish();*/
    }


    public void displayMessage(String toastString)
    {
        try
        {
        if (getCurrentFocus() != null)
        {
        Snackbar snackbar = Snackbar.make(getCurrentFocus(), toastString, Snackbar.LENGTH_SHORT);
        View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.blue));

      /*
      TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
      textView.setTextColor(ContextCompat.getColor(this, R.color.white));
      */

        snackbar.show();
        }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email)
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR)
        {
            overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
        }
        */
        finishAffinity();
    }

    public void GetToken()
    {
        try
        {
            if (!SharedHelper.getKey(thisActivity, "device_token").equals("") &&
                    SharedHelper.getKey(thisActivity, "device_token") != null)
            {
                device_token = SharedHelper.getKey(thisActivity, "device_token");
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE)
            {
             device_UDID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            }
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
//{"error":"The selected email is invalid.","status":"error"}
//{"message":"Email is not verified yet","status":"error"}
        switch (RequestCode)
        {
               case 700:
                Log.e("ResponseLogin ",response);
                try {
                    JSONObject object=new JSONObject(response);
                    Log.e("object ",object.toString());


                    if (object.getString("status").equalsIgnoreCase("error"))
                    {
                        if (object.has("message"))
                        {
                            showVerificationPopUp("login");
                        }
                        else
                        {
                            txtemail.setText("");
                            txtpassword.setText("");
                            txtemail.setFocusableInTouchMode(true);
                            txtemail.setFocusable(true);
                            txtemail.requestFocus();
                            txtpassword.setFocusable(false);

                            Toast.makeText(this, object.getString("error"), Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        JSONObject result=object.getJSONObject("result");
                        SharedHelper.putKey(thisActivity, "access_token", result.optString("token"));
                        SharedHelper.putKey(thisActivity, "refresh_token", result.optString("device_token"));
                        SharedHelper.putKey(thisActivity, "token_type", "Bearer");
                        getProfile();
                    }

//                    getprofileDetail();
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
//                    displayMessage("Something Went Wrong");
                    Toast.makeText(SignIn.this,getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

                }
                break;


            case 500:
                try
                {
                    Log.e("ResponseProfile ",response);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                break;

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
                    }
                    else {
                        if (jsonObject.has("device_token"))
                        {
                            showVerificationPopUp("social");
                        }
                        /*SharedHelper.putKey(context, "access_token", jsonObject.optString("access_token"));
                        SharedHelper.putKey(context, "refresh_token", jsonObject.optString("refresh_token"));
                        SharedHelper.putKey(context, "token_type", jsonObject.optString("token_type"));
                        getProfile();*/


                    }
                }catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                break;
        }
    }

    @Override
    public void showPermissionAlert(ArrayList<String> permissionList, int code)
    {
        Log.e( "showPermissionAlert: ","Popup" );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            Log.e( "showPermissionAlert: ","Inside" );
            requestPermissions(permissionList.toArray(new String[permissionList.size()]), code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {

            case 2:

                for (int i = 0; i < permissions.length; i++)
                {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    {
                        // Toast.makeText(this, "Permitions Allow", Toast.LENGTH_SHORT).show();
                        //  getLocation();
                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                    {
                        // checkPermissionLocation(context);
                        //   Toast.makeText(this, "Permitions Denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            default:
            {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}