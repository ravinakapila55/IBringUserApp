/*
package com.iBring_user.app;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.app.blackpatch.R;
import com.app.blackpatch.booking.modals.BookingData;
import com.app.blackpatch.networking.Injector;
import com.app.blackpatch.networking.InterfaceApi;
import com.app.blackpatch.ui.BaseActivity;
import com.app.blackpatch.ui.HomeUserActivity;
import com.app.blackpatch.utils.CommonMethod;
import com.app.blackpatch.utils.GeneralResponse;
import com.app.blackpatch.utils.SharedPrefUtil;
import com.bumptech.glide.Glide;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentUserActivity extends BaseActivity implements Callback<ResponseBody> {

    //Here's the Paypal Sandbox account info.
    //User: jorge220386@gmail.com
    //Pswrd: Miblackpatch22

    private static final String TAG = "paymentExample";

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "credentials from developer.paypal.com";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;


    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId("AbdjTyd1XhBUE0-sVIDDRHDRgJTTTvSr82Q8-we8DDmgNlRxJkBxnPJkeXZ-JiDPUrJWpzhrq-wgJDZ0")
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Example Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));


    BookingData mObj;
    String bookingId;
    String userId;
    SharedPrefUtil helper;

    @BindViews({R.id.txt_name,R.id.txt_device_name,R.id.txt_location,R.id.txt_total})
    List<TextView> mTextViews;


    @BindView(R.id.txt_header)
    TextView txtHeader;

    @BindView(R.id.img_user)
    ImageView imgUser;
    InterfaceApi api;

    int amount=0;
    @Override
    protected int getContentId() {
        return R.layout.payment_user_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = Injector.provideApi();
        txtHeader.setText("Payment");

        helper = SharedPrefUtil.getInstance();
        userId = helper.getString(SharedPrefUtil.USER_ID);
        if(getIntent().getStringExtra("from").equals("home")) {
            mObj = (BookingData) getIntent().getSerializableExtra("mObj");
            bookingId = mObj.getId();
            setData();
        }

    }

    private void setData() {
        mTextViews.get(0).setText(""+mObj.getUsername());
        mTextViews.get(1).setText("Device Name "+mObj.getModelName());
        mTextViews.get(2).setText(""+mObj.getTechAddress());

        amount=mObj.getTotalEstimate()/2;
       // mTextViews.get(3).setText("$ "+mObj.getTotalEstimate());
        mTextViews.get(3).setText("$ "+amount);

        Glide.with(this).load(mObj.getProfilePic()).placeholder(R.drawable.ic_dummy_profile) //placeholder
                .error(R.drawable.ic_dummy_profile) //error
                .into(imgUser);



    }

    @OnClick(R.id.img_back_btn)
    public void backClicked(){
        finish();
    }

    @OnClick(R.id.btn_pay_now)
    public void payClicked()
    {


        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

        */
/*
         * See getStuffToBuy(..) for examples of some available payment options.
         *//*


        Intent intent = new Intent(PaymentUserActivity.this, com.paypal.android.sdk.payments.PaymentActivity.class);

        // send the same configuration for restart resiliency
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, thingToBuy);

        startActivityForResult(intent, REQUEST_CODE_PAYMENT);

    }


    private PayPalPayment getThingToBuy(String paymentIntent) {

        return new PayPalPayment(new BigDecimal("0.01"), "USD", "sample item",
                paymentIntent);
    }


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i("Pament", confirm.toJSONObject().toString(4));
                        Log.i("PaymentOther", confirm.getPayment().toJSONObject().toString(4));
                        */
/**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         *//*

                        // displayResultText("PaymentConfirmation info received from PayPal");

hitAccetAPi();
                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);

//                        sendAuthorizationToServer(auth);
//                        displayResultText("Future Payment code received from PayPal");

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("ProfileSharingExample", authorization_code);

//                        sendAuthorizationToServer(auth);
//                        displayResultText("Profile Sharing code received from PayPal");

                    } catch (JSONException e) {
                        Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("ProfileSharingExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "ProfileSharingExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void hitAccetAPi() {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("bookingId",mObj.getId());
        hashMap.put("technicianId",mObj.getTechnicianId());
        hashMap.put("amount",String.valueOf(amount));
        hashMap.put("currency_code","USD");
        hashMap.put("transition_id","PAY-18X32451H0459092JKO7KFUI");
        hashMap.put("status","approved");
        hashMap.put("create_time","2020-03-10 11:35:19");
        hashMap.put("userId",userId);

        CommonMethod.showProgress(this);
        api.acceptTechnicain(hashMap).enqueue(this);

    }


    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        GeneralResponse generalResponse = new GeneralResponse(response);
        CommonMethod.hideProgress();
        try {

            JSONObject jsonObject = generalResponse.getResponse();

            if (jsonObject.getString(("status")).equalsIgnoreCase("true")||jsonObject.getString(("status")).equalsIgnoreCase("200")) {
                CommonMethod.showToast(this, jsonObject.getString(("message")));
               // setResult(Activity.RESULT_OK);
               // finish();
Intent it = new Intent(PaymentUserActivity.this, HomeUserActivity.class);
startActivity(it);
finish();
            } else {

                CommonMethod.showToast(this, jsonObject.getString("message"));
            }
        } catch (JSONException e) {

        }

    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {

    }
}
*/
