package com.iBring_user.app.courier;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.clearcut.ClearcutLogger;
import com.google.android.gms.maps.model.LatLng;
import com.iBring_user.app.Activity.Home;
import com.iBring_user.app.Activity.SignIn;
import com.iBring_user.app.Constants.URLHelper;
import com.iBring_user.app.Helper.SharedHelper;
import com.iBring_user.app.R;
import com.iBring_user.app.Utils.GooglePlacesAutocompleteAdapter;
import com.iBring_user.app.Utils.LocationDistanceDuration;
import com.iBring_user.app.Utils.PlacesAutoCompleteAdapter;
import com.iBring_user.app.Utils.TrackGoogleLocation;
import com.iBring_user.app.Utils.Utilities;
import com.iBring_user.app.food_service.CartScreen;
import com.iBring_user.app.paypal.PaypalMain;
import com.iBring_user.app.retrofit.RetrofitResponse;
import com.iBring_user.app.retrofit.RetrofitService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CourierAddReceiverDetails extends AppCompatActivity implements LocationDistanceDuration,RetrofitResponse
{
    @BindView(R.id.etAddress)
  AutoCompleteTextView etAddress;

    @BindView(R.id.rlEstimatedPrice)
    RelativeLayout rlEstimatedPrice;

    double latt,lng;

    @BindView(R.id.first_name)
   EditText first_name;

    @BindView(R.id.email)
    EditText email;

    @BindView(R.id.mobile_no)
     EditText mobile_no;

    @BindView(R.id.tvNotes)
    EditText tvNotes;

    @BindView(R.id.rlSubmit)
    RelativeLayout rlSubmit;

    @BindView(R.id.tvPaymentMethodLabel)
    TextView tvPaymentMethodLabel;

    @BindView(R.id.tvPaymentMethod)
    TextView tvPaymentMethod;

    String selection="cash";
    public void callPaymentAlert()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.payment_option, null);

        RadioGroup rgGroup=(RadioGroup) dialogView.findViewById(R.id.rgGroup);
        RadioButton rbCash=(RadioButton) dialogView.findViewById(R.id.rbCash);
        RadioButton rbPayPal=(RadioButton) dialogView.findViewById(R.id.rbPayPal);
        TextView tvCancel=(TextView) dialogView.findViewById(R.id.tvCancel);
        TextView tvOk=(TextView) dialogView.findViewById(R.id.tvOk);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        int width = WindowManager.LayoutParams.WRAP_CONTENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;

        if (tvPaymentMethod.getText().toString().trim().equalsIgnoreCase("CASH"))
        {
            rbCash.setChecked(true);
            rbPayPal.setChecked(false);
        }
        else if (tvPaymentMethod.getText().toString().trim().equalsIgnoreCase("Paypal"))
        {
            rbPayPal.setChecked(true);
            rbCash.setChecked(false);
        }


        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i)
                {
                    case R.id.rbCash:

                        selection ="cash";
                        break;

                    case R.id.rbPayPal:
                        selection ="paypal";

                        break;
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                alertDialog.dismiss();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                if (selection.equalsIgnoreCase(""))
                {
                    Toast.makeText(CourierAddReceiverDetails.this, "Choose one option",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    alertDialog.cancel();
                    getPaymentSelectionInfo(selection);
                }

            }
        });
        alertDialog.show();
    }

    private void getPaymentSelectionInfo(String value)
    {
        if (value.equalsIgnoreCase("cash"))
        {
            tvPaymentMethod.setText("CASH");
        }
        else
        {
            tvPaymentMethod.setText("Paypal");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.courier_add_receiver_details);
     ButterKnife.bind(this);

        PlacesAutoCompleteAdapter mAdapter = new PlacesAutoCompleteAdapter
                (this, R.layout.custom_tv);
        etAddress.setAdapter(mAdapter);
        etAddress.setImeOptions(EditorInfo.IME_ACTION_DONE);
        setLoactionAdapter(etAddress);


        tvPaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPaymentAlert();
            }
        });

        rlSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if (checkValidations())
                {
                    if (selection.equalsIgnoreCase("CASH"))
                    {
                        //cash
                        callService();
                    }
                    else
                    {
                        if (paypalPaymentId.equalsIgnoreCase(""))
                        {
                            callConfitmPaymentPopUp();
                        }
                        else
                        {
                            callService();
                        }
                    }

                }
            }
        });
    }



    public void callConfitmPaymentPopUp()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CourierAddReceiverDetails.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.confirm_popup, null);

        TextView tvCancel=(TextView) dialogView.findViewById(R.id.tvCancel);
        TextView tvOk=(TextView) dialogView.findViewById(R.id.tvOk);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        int width = WindowManager.LayoutParams.WRAP_CONTENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                Intent it=new Intent(CourierAddReceiverDetails.this, PaypalMain.class);
                it.putExtra("fare",price);
                it.putExtra("key","courier");
                startActivityForResult(it,1400);

            }
        });

        alertDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1400)
        {
            Log.e("StringData ", "4000");

            try
            {
                paypalPaymentId = data.getSerializableExtra("payment_id").toString();
                Log.e("paypalPaymentId ", paypalPaymentId);
                tvPaymentMethod.setText("Paypal");
                callService();
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }


    MultipartBody.Part part;
    SharedHelper helper;
    String userId="";

    public void callService()
    {
        helper=new SharedHelper();
        userId = helper.getKey(CourierAddReceiverDetails.this,"id");

        HashMap<String, RequestBody> map = new HashMap<>();

        File file = new File(String.valueOf(AddParcelDetails.file));
        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        part = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

        Log.e("senderEmail ",CourierAddSenderDetails.email.getText().toString());
        Log.e("contact ",CourierAddSenderDetails.mobile_no.getText().toString());
        Log.e("first_name ",CourierAddSenderDetails.first_name.getText().toString());
        Log.e("etAddress ",CourierAddSenderDetails.etAddress.getText().toString());

        map.put("sender_name", RequestBody.create(MediaType.parse("multipart/form-data"),CourierAddSenderDetails.first_name.getText().toString().trim()));
        map.put("sender_contact", RequestBody.create(MediaType.parse("multipart/form-data"), CourierAddSenderDetails.mobile_no.getText().toString().trim()));
        map.put("sender_email", RequestBody.create(MediaType.parse("multipart/form-data"), CourierAddSenderDetails.email.getText().toString().trim()));
        map.put("sender_location", RequestBody.create(MediaType.parse("multipart/form-data"),CourierAddSenderDetails.etAddress.getText().toString().trim()));
        map.put("sender_lattitude", RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(CourierAddSenderDetails.latt)));
        map.put("sender_longitude", RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(CourierAddSenderDetails.lng)));
        map.put("receiver_name", RequestBody.create(MediaType.parse("multipart/form-data"),first_name.getText().toString().trim()));
        map.put("receiver_contact", RequestBody.create(MediaType.parse("multipart/form-data"),mobile_no.getText().toString().trim()));
        map.put("receiver_email", RequestBody.create(MediaType.parse("multipart/form-data"),email.getText().toString().trim()));
        map.put("receiver_lattitude", RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(latt)));
        map.put("receiver_longitude", RequestBody.create(MediaType.parse("multipart/form-data"),String.valueOf(lng)));
        map.put("weight", RequestBody.create(MediaType.parse("multipart/form-data"), AddParcelDetails.etWeight.getText().toString().trim()));
        map.put("parcel_type", RequestBody.create(MediaType.parse("multipart/form-data"),AddParcelDetails.parcelId));
        map.put("special_note", RequestBody.create(MediaType.parse("multipart/form-data"), tvNotes.getText().toString().trim()));
        map.put("receiver_location", RequestBody.create(MediaType.parse("multipart/form-data"), etAddress.getText().toString().trim()));
        map.put("delivery_note", RequestBody.create(MediaType.parse("multipart/form-data"),AddParcelDetails.tvNotes.getText().toString().trim()));
        map.put("is_fragile", RequestBody.create(MediaType.parse("multipart/form-data"), AddParcelDetails.isFragile));
        map.put("payment_mode", RequestBody.create(MediaType.parse("multipart/form-data"),selection));
        map.put("sender_id", RequestBody.create(MediaType.parse("multipart/form-data"),userId));
        map.put("price", RequestBody.create(MediaType.parse("multipart/form-data"),price));

        if (selection.equalsIgnoreCase("paypal"))
        {
            map.put("payment_id", RequestBody.create(MediaType.parse("multipart/form-data"),paypalPaymentId));
        }

        Log.e("ReceiverDetailsParams ",map.toString());

        new RetrofitService(CourierAddReceiverDetails.this,
                CourierAddReceiverDetails.this,
                URLHelper.SEND_COURIER_REQUEST, map, part,
                1200, 5,"1").callService(true);
    }

    private boolean isValidEmail(String email)
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public boolean checkValidations()
    {
        if (first_name.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please enter receiver's name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (first_name.getText().toString().trim().length()<3)
        {
            Toast.makeText(this, "Full Name must be 3 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (email.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please enter receiver's email address", Toast.LENGTH_SHORT).show();
            return false;
        } else if((!isValidEmail(email.getText().toString())))
        {
            Toast.makeText(this,"Please enter valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (mobile_no.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please enter receiver's mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (mobile_no.getText().toString().trim().length()<10)
        {
            Toast.makeText(this, "Contact Number  must be atleast 10 digits long", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (mobile_no.getText().toString().trim().length()<10)
        {
            Toast.makeText(this, "Contact Number  must be atleast 10 digits long", Toast.LENGTH_SHORT).show();
            return false;
        }

        else if (etAddress.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please choose receiver's address", Toast.LENGTH_SHORT).show();
            return false;
        }else if (tvNotes.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Enter some special note", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setLoactionAdapter(AutoCompleteTextView autoCompleteTextView)
    {
        autoCompleteTextView.setAdapter(new GooglePlacesAutocompleteAdapter(getApplicationContext(),R.layout.custom_tv));

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String placeId = GooglePlacesAutocompleteAdapter.resultListId.get(position).toString();
                Log.e("PlaceId ",placeId);

                final String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" +
                        placeId + "&key=AIzaSyAAgL20f3ydrQcvvl77vldDinOqPNjv0LY";
//                constant.hideKeyboard(EnterPickUp.this,view);

                new AsyncTask<Void, Void, String>()
                {
                    @Override
                    protected void onPreExecute()
                    {
                        super.onPreExecute();
//                      DialogClass.showDialog(Listing.this);
                    }
                    @Override
                    protected String doInBackground(final Void... params)
                    {
                        try
                        {
                            serviceArrive(url);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(final String result)
                    {
//                        DialogClass.logout();
                    }
                }.execute();
            }
        });

    }

    private void serviceArrive(String url1)
    {
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();

        try
        {
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

        try
        {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            Log.e("JSONObjectValue ",jsonObj+"");

            JSONObject res = jsonObj.getJSONObject("result");
            JSONObject geo = res.getJSONObject("geometry");
            JSONObject loc = geo.getJSONObject("location");

            latt= Double.parseDouble(loc.getString("lat"));
            lng= Double.parseDouble(loc.getString("lng"));

            LatLng latLng=null;
            LatLng latLng1=null;

            latLng=new LatLng(CourierAddSenderDetails.latt,CourierAddSenderDetails.lng);
            latLng1=new LatLng(latt,lng);

            Utilities.hideKeypad(CourierAddReceiverDetails.this,etAddress);

            disList.clear();
            new TrackGoogleLocation(CourierAddReceiverDetails.this,
                    CourierAddReceiverDetails.this).getEstimate(latLng, latLng1);

            rlEstimatedPrice.setVisibility(View.VISIBLE);
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public   String actualDist="";
    public   String distance="";

    ArrayList<String> disList=new ArrayList<>();
    ArrayList<String> distanceList=new ArrayList<>();

    public void callGetCourierEstimate()
    {
        try
        {
            JSONObject object=new JSONObject();

            object.put("service_id","19");
            object.put("city_code",CourierAddSenderDetails.cityZone);
//            object.put("distance",actualDist);
            object.put("distance",dist[0]);

            Log.e("CallFareParamsCourier ",object.toString());

            new RetrofitService(this, this, URLHelper.ESTIMATED_COURIER_PRICE ,
                    object,
                    700, 2,"1").callService(true);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void getResult(String distance, String duration)
    {
        Log.e("ResultConfirmDistance  ",distance);
        Log.e("ResultConfirmDuration  ",duration);

        disList.add(distance);
        actualDist=disList.get(0);
        Log.e("actualDist  ",actualDist);
        Log.e("cityZone  ",CourierAddSenderDetails.cityZone);



        dist=actualDist.split(" ");
        Log.e("distanceValuee  ",dist[0]);

        callGetCourierEstimate();
    }

    String dist[];

    @BindView(R.id.tvEstimatedPrice)
    TextView tvEstimatedPrice;

    @Override
    public void onResponse(int RequestCode, String response)
    {
        switch (RequestCode)
        {
            case 700:
            try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.e("JSONOBJECT ",jsonObject.toString());

                    if (jsonObject.getString("status").equalsIgnoreCase("success"))
                    {
                        rlEstimatedPrice.setVisibility(View.VISIBLE);
                        price=jsonObject.getString("result");
                        tvEstimatedPrice.setText("$ "+jsonObject.getString("result"));
                    }
                    else {
                        rlEstimatedPrice.setVisibility(View.GONE);
                    }




                }
            catch (Exception ex)
            {
                    ex.printStackTrace();
                }
            break;

            case 1200:
            Log.e("ResponseSendCourier ",response);
            try {
                JSONObject jsonObject=new JSONObject(response);
                Log.e("jsonObject ",jsonObject.toString());

                if (jsonObject.getString("status").equalsIgnoreCase("success"))
                {
                    Toast.makeText(this, "Courier order placed succesfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(CourierAddReceiverDetails.this, Home.class);
                    startActivity(intent);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            break;
        }
    }

    String price="";
    String paypalPaymentId="";

}
