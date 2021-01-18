package com.iBring_user.app.paypal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iBring_user.app.R;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;

public class PaypalMain extends AppCompatActivity implements View.OnClickListener
{

    //The views
    private Button buttonPay;
    private TextView tvBookingLabel;
    private EditText editTextAmount;

    //Payment Amount
    private String paymentAmount;
    private String bookingNumber="";

    //Paypal intent request code to track onActivityResult method
    public static final int PAYPAL_REQUEST_CODE = 123;

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "credentials from developer.paypal.com";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId("AVNsN-r9pw6UG1vWAAiJkf0cUmBrqhGlpmYVI8uNWccTWcQNF048uR9uNkR8Htc0EUary2aRHG9m1qVV")
            // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Example Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paypal);

        buttonPay = (Button) findViewById(R.id.buttonPay);
        tvBookingLabel = (TextView) findViewById(R.id.tvBookingLabel);
        editTextAmount = (EditText) findViewById(R.id.editTextAmount);

        buttonPay.setOnClickListener(this);
        paymentAmount=getIntent().getExtras().getString("fare");
        key=getIntent().getExtras().getString("key");

        if (key.equalsIgnoreCase("book"))
        {
            bookingNumber=getIntent().getExtras().getString("BookingID");
            tvBookingLabel.setVisibility(View.VISIBLE);
            tvBookingLabel.setText("Booking Number:-"+bookingNumber);
        }else if (key.equalsIgnoreCase("food"))
        {
            tvBookingLabel.setVisibility(View.GONE);
        }else if (key.equalsIgnoreCase("pending"))
        {
            tvBookingLabel.setVisibility(View.GONE);
        }else if (key.equalsIgnoreCase("courier"))
        {
            tvBookingLabel.setVisibility(View.GONE);
        }
        else
        {
            tvBookingLabel.setVisibility(View.GONE);
        }

        Log.e("PaymentAmount ",paymentAmount);
        Log.e("key ",key);

       // paypalConfiguartion();

        getPayment();



    }


   // PayPalConfiguration config=null;
    public void paypalConfiguartion()
    {
        //Paypal Configuration Object
         config = new PayPalConfiguration()
                // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
                // or live (ENVIRONMENT_PRODUCTION)
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(PayPalConfig.PAYPAL_CLIENT_ID).merchantName("Test");


        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        startService(intent);
    }


    @Override
    public void onClick(View view)
    {

        switch (view.getId())
        {
            case R.id.buttonPay:
                getPayment();
                break;
        }

    }

    private void getPayment()
    {
        //Getting the amount from editText
//        paymentAmount = editTextAmount.getText().toString();


        PayPalPayment payment=null;
        if (key.equalsIgnoreCase("book"))
        {
             payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)),
                    "USD", "Fare",
                    PayPalPayment.PAYMENT_INTENT_SALE);
        }
        else
        {
             payment = new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)),
                    "USD", "Amount",
                    PayPalPayment.PAYMENT_INTENT_SALE);
        }


        //Creating a paypalpayment


        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

  /*  @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }*/

    public void callPaymentAlert()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.payment_done_success, null);

        TextView tvLabel=(TextView)dialogView.findViewById(R.id.tvLabel);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();
         alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        int width = WindowManager.LayoutParams.WRAP_CONTENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;

        if (key.equalsIgnoreCase("wallet"))
        {
            Log.e("KeyValueIf ",key);
            tvLabel.setText("Money added to wallet");
        }
        else {
            Log.e("KeyValueElse ",key);
            tvLabel.setText("Payment  Successfully  Done.");
        }

        Log.e("AlertPaymentMethod ",paymentId);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                alertDialog.dismiss();
                Intent intent=new Intent();
                intent.putExtra("payment_id",paymentId);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        },2000);
        alertDialog.show();
    }


    String paymentId="";
    String key="";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //If the result is from paypal
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("data ", data+"");
        if (requestCode == PAYPAL_REQUEST_CODE)
        {
            Log.e("requestCode ", requestCode+"");
            // If the result is OK i.e. user has not canceled the payment

            if (resultCode == Activity.RESULT_OK)
            {
                // Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                Log.e("confirm ", confirm+"");

                // if confirmation is not null
                if (confirm != null)
                {
                    try
                    {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.e("paymentExample", paymentDetails);
                        Log.e("paymentAmount", paymentAmount);

                        JSONObject jsonObject=new JSONObject(paymentDetails);
                        JSONObject response=jsonObject.getJSONObject("response");

                        paymentId=response.getString("id");

                        Log.e("PaymentId ",paymentId);


                        callPaymentAlert();


                     /*   startActivity(new Intent(this, ConfirmationActivity.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", paymentAmount));*/

                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                        Log.e("Catch ", "an extremely unlikely failure occurred: ", e);
                    }
                }
            }

            else if (resultCode == Activity.RESULT_CANCELED)
            {
                Log.e("RESULT_CANCELED ", "The user canceled.");
            }

            else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            {
                Log.e("RESULT_EXTRAS_INVALID ", "An invalid Payment or PayPalConfiguration was submitted." +
                        " Please see the docs.");
            }

            else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT)
            {
                if (resultCode == Activity.RESULT_OK)
                {
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
                }
                else if (resultCode == Activity.RESULT_CANCELED)
                {
                    Log.i("FuturePaymentExample", "The user canceled.");
                }
                else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID)
                {
                    Log.e("FuturePaymentExample", "Probably the attempt to previously start the PayPalService " +
                            "had an invalid PayPalConfiguration. Please see the docs.");
                }
            }

            else if (requestCode == REQUEST_CODE_PROFILE_SHARING)
            {
                if (resultCode == Activity.RESULT_OK)
                {
                    PayPalAuthorization auth = data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
                    if (auth != null)
                    {
                        try
                        {
                            Log.e("ProfileSharingExample", auth.toJSONObject().toString(4));
                            String authorization_code = auth.getAuthorizationCode();
                            Log.e("ProfileSharingExample", authorization_code);

                        }
                        catch (JSONException e)
                        {
                            Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
                        }
                    }
                }
                else if (resultCode == Activity.RESULT_CANCELED)
                {
                    Log.e("ProfileSharingExample", "The user canceled.");
                }
                else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID)
                {
                    Log.e("ProfileSharingExample", "Probably the attempt to previously start the " +
                            "PayPalService had an invalid PayPalConfiguration. Please see the docs.");
                }
            }
        }
    }
}
