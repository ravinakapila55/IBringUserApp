package com.iBring_user.app.courier;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.iBring_user.app.Constants.URLHelper;
import com.iBring_user.app.Helper.SharedHelper;
import com.iBring_user.app.Models.CityTYpemOdel;
import com.iBring_user.app.Models.ParcelTypeModel;
import com.iBring_user.app.R;
import com.iBring_user.app.Utils.GeoLocation;
import com.iBring_user.app.Utils.GooglePlacesAutocompleteAdapter;
import com.iBring_user.app.Utils.PlacesAutoCompleteAdapter;
import com.iBring_user.app.retrofit.RetrofitResponse;
import com.iBring_user.app.retrofit.RetrofitService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CourierAddSenderDetails extends AppCompatActivity implements RetrofitResponse, View.OnClickListener {

   public static RelativeLayout rlNext;

  public static ImageView imgMenu;

   public static AutoCompleteTextView etAddress;

  public static double latt,lng;

    Spinner spCity;
    String[] cityType = {"Select City"};

    ArrayList<CityTYpemOdel> cityList=new ArrayList<>();
    ArrayList<String> cityListName=new ArrayList<>();

  public static EditText first_name;

   public  static EditText email;

 public static EditText mobile_no;


  public void findIds()
  {
      rlNext=(RelativeLayout)findViewById(R.id.rlNext);
      imgMenu=(ImageView) findViewById(R.id.imgMenu);
      etAddress=(AutoCompleteTextView) findViewById(R.id.etAddress);
      spCity=(Spinner) findViewById(R.id.spCity);
      first_name=(EditText) findViewById(R.id.first_name);
      email=(EditText) findViewById(R.id.email);
      mobile_no=(EditText) findViewById(R.id.mobile_no);
      rlNext.setOnClickListener(this);
      imgMenu.setOnClickListener(this);


  }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courier_add_sender_detail);

        findIds();
//        ButterKnife.bind(this);

        latt=Double.parseDouble(SharedHelper.getKey(this, "current_lat"));
        lng=Double.parseDouble(SharedHelper.getKey(this, "current_lng"));

        String address= GeoLocation.getAddress(this, latt, lng);
        etAddress.setText(address);

        PlacesAutoCompleteAdapter mAdapter = new PlacesAutoCompleteAdapter
                (this, R.layout.custom_tv);
        etAddress.setAdapter(mAdapter);
        etAddress.setImeOptions(EditorInfo.IME_ACTION_DONE);
        setLoactionAdapter(etAddress);
        setSpinner();
        callCity();

    }

    public void setSpinner()
    {
        ArrayAdapter<String> adapter11 = new ArrayAdapter<String>(this, R.layout.custom_spinner, cityType);
        spCity.setAdapter(adapter11);


    }


    public void callCity()
    {
        new RetrofitService(this, this, URLHelper.GET_CITY_LIST,
                500, 1, "1").callService(true);
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
            Toast.makeText(this, "Please enter sender's name", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (first_name.getText().toString().trim().length()<3)
        {
            Toast.makeText(this, "Full Name must be 3 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (email.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please enter sender's email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if((!isValidEmail(email.getText().toString())))
        {
            Toast.makeText(this,"Please enter valid email address", Toast.LENGTH_SHORT).show();
            return false;
//                    displayMessage(getString(R.string.not_valid_email));
        }
        else if (mobile_no.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please enter sender's mobile number", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (mobile_no.getText().toString().trim().length()<10)
        {
            Toast.makeText(this, "Contact Number  must be atleast 10 digits long", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (etAddress.getText().toString().trim().isEmpty())
        {
            Toast.makeText(this, "Please choose sender's address", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (cityId.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Please choose city", Toast.LENGTH_SHORT).show();
            return false;
        }



        return true;
    }


    private void setLoactionAdapter(AutoCompleteTextView autoCompleteTextView)
    {
        autoCompleteTextView.setAdapter(new GooglePlacesAutocompleteAdapter(getApplicationContext(),
                R.layout.custom_tv));

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
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            Log.e("JSONObjectValue ",jsonObj+"");

            JSONObject res = jsonObj.getJSONObject("result");
            JSONObject geo = res.getJSONObject("geometry");
            JSONObject loc = geo.getJSONObject("location");
            latt= Double.parseDouble(loc.getString("lat"));
            lng= Double.parseDouble(loc.getString("lng"));


        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    public void onClick(View view)
    {
        switch (view.getId())
        {
        case R.id.imgMenu:
        Log.e("backClick ","backClck");
        onBackPressed();
        break;

            case R.id.rlNext:
                Log.e("createCourier  ","creationClick");

                if (checkValidations())
                {
                    Intent intent=new Intent(CourierAddSenderDetails.this,AddParcelDetails.class);
                    startActivity(intent);
                }
                break;


        }
    }

    @Override
    public void onResponse(int RequestCode, String response)
    {
        switch (RequestCode)
        {
            case 500:
                Log.e("ParcelTypeList ", response);
                try {
                    cityList.clear();
                    cityListName.clear();

                    JSONObject jsonObject=new JSONObject(response);

                    if (jsonObject.getString("status").equalsIgnoreCase("success"))
                    {
                        JSONArray result=jsonObject.getJSONArray("result");
                        if (result.length()>0)
                        {
                            cityListName.add("Select City");
                            if (result.length() > 0)
                            {
                                for (int i = 0; i < result.length(); i++)
                                {
                                    JSONObject parcelJson = result.getJSONObject(i);
                                    CityTYpemOdel city = new CityTYpemOdel();
                                    city.setIs(parcelJson.getString("id"));
                                    city.setName(parcelJson.getString("city"));
                                    city.setCity_code(parcelJson.getString("city_code"));
                                    city.setZone_id(parcelJson.getString("zone_id"));
                                    city.setPrice(parcelJson.getString("price"));

                                    cityList.add(city);
                                    cityListName.add(parcelJson.getString("city"));
                                }
                            }
                        }
                    }

                    Log.e("parcelTypeListSize ", cityList.size() + "");
                    Log.e("parcelTypeListNameSize ", cityListName.size() + "");

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, cityListName);
                    spCity.setAdapter(adapter);

                    spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                        {
                            if (position == 0)
                            {
                                cityId = "";
                                cityZone = "";
                                Log.e("cityId ", cityId);
                                Log.e("cityZone ", cityZone);
                            }
                            else
                           {
                                cityId = cityList.get(position - 1).getIs() + "";
                                cityZone = cityList.get(position - 1).getCity_code() + "";
                                Log.e("cityId ", cityId);
                                Log.e("cityZone ", cityZone);
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent)
                        {

                        }
                    });
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                break;

        }
    }



   public static String cityId="";
   public static String cityZone="";
}
