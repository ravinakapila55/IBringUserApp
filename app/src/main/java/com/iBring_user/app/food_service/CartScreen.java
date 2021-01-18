package com.iBring_user.app.food_service;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.iBring_user.app.Activity.Home;
import com.iBring_user.app.Constants.URLHelper;
import com.iBring_user.app.Helper.SharedHelper;
import com.iBring_user.app.Models.MenuItems;
import com.iBring_user.app.R;
import com.iBring_user.app.Utils.GeoLocation;
import com.iBring_user.app.Utils.GooglePlacesAutocompleteAdapter;
import com.iBring_user.app.Utils.PlacesAutoCompleteAdapter;
import com.iBring_user.app.food_service.adapter.CartAdapter;
import com.iBring_user.app.paypal.PaypalMain;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartScreen extends AppCompatActivity implements RetrofitResponse
{

    @BindView(R.id.recyclerCart)
    RecyclerView recyclerCart;

    ArrayList<MenuItems> list;

    @BindView(R.id.tvPayment)
    TextView tvPayment;

    @BindView(R.id.ivEdit)
    ImageView ivEdit;

    @BindView(R.id.tvTotal)
    TextView tvTotal;

    @BindView(R.id.tvGT)
    TextView tvGT;

    @BindView(R.id.ccOrder)
    ConstraintLayout ccOrder;

    @BindView(R.id.tvAddress)
    AutoCompleteTextView tvAddress;

    String restaurrant_id="";
    String hours="";
    String minutes="";
    String fare1="";

    double latt,lng;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_screen);
        ButterKnife.bind(this);

        if (getIntent().hasExtra("list"))
        {
            list = (ArrayList<MenuItems>) getIntent().getSerializableExtra("list");
            Log.e("listSize ", list.size() + "");
            restaurrant_id=getIntent().getExtras().getString("id");
            Log.e("Restauran_id ",restaurrant_id);
            hours=getIntent().getExtras().getString("hours");
            Log.e("hours ",hours);
            minutes=getIntent().getExtras().getString("mins");
            Log.e("minutes ",minutes);
            fare1=getIntent().getExtras().getString("fare");
            Log.e("fare1 ",fare1);
        }

        if (list.size() > 0)
        {
            setAdapter();
        }
//
        latt=Double.parseDouble(SharedHelper.getKey(this, "current_lat"));
        lng=Double.parseDouble(SharedHelper.getKey(this, "current_lng"));

     /*   latt=31.7413754;
        lng=75.7551023;*/

        String address= GeoLocation.getAddress(this, latt, lng);
        tvAddress.setText(address);
        tvTotal.setText("$ "+fare1);
        tvGT.setText("$ "+fare1);

        PlacesAutoCompleteAdapter mAdapter = new PlacesAutoCompleteAdapter
                (this, R.layout.custom_tv);
        tvAddress.setAdapter(mAdapter);
        tvAddress.setImeOptions(EditorInfo.IME_ACTION_DONE);
        setLoactionAdapter(tvAddress);

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

    public void callConfitmPaymentPopUp()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CartScreen.this);
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
                Intent it=new Intent(CartScreen.this, PaypalMain.class);
                it.putExtra("fare",fare1);
                it.putExtra("key","food");
                startActivityForResult(it,4000);

            }
        });

        alertDialog.show();
    }

    String paypalPaymentId="";

    public boolean checkValidations()
    {
        if (selection.equalsIgnoreCase("PAYPAL"))
        {
            if (paypalPaymentId.equalsIgnoreCase(""))
            {
                Toast.makeText(this, "Make Payment by clicking on payapl", Toast.LENGTH_SHORT).show();
                return false;
            }
            return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 4000)
        {
            Log.e("StringData ", "4000");

            try {
                paypalPaymentId = data.getSerializableExtra("payment_id").toString();
                Log.e("paypalPaymentId ", paypalPaymentId);
                tvPayment.setText("Paypal");

                callPlaceOrder();


            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }
    }

    /*'location' => 'required'
                   'longitude' => 'required',
                   'latitude' => 'required',
                   'payment_mode' => 'cash,paypal',
                   'service_id' => 'required',
                   'price' => 'required',
                   'item' => 'required',
                   'payment_id' => 'optional',

                   //http://178.128.116.149/ibring/public/api/user/place-order

                   */

    public void callPlaceOrder()
    {
       try {
           JSONObject object=new JSONObject();
           object.put("location",tvAddress.getText().toString().trim());
           object.put("longitude",String.valueOf(latt));
           object.put("latitude",String.valueOf(lng));
           object.put("price",fare1);
           object.put("service_id","18");
           object.put("restaurant_id",restaurrant_id);
           object.put("hours",hours);
           object.put("minutes",minutes);

           if (selection.equalsIgnoreCase("CASH"))
           {
               object.put("payment_mode","cash");
           }
           else
           {
               object.put("payment_mode","paypal");
               object.put("payment_id",paypalPaymentId);
           }


           JSONArray jsonArray=new JSONArray();
           for (int i = 0; i <list.size() ; i++)
           {
               JSONObject jsonObject=new JSONObject();
               jsonObject.put("menu_id",list.get(i).getId());
               jsonObject.put("quantity",list.get(i).getCount());
               jsonObject.put("item_name",list.get(i).getName());
               jsonObject.put("price",list.get(i).getPrice());
               jsonArray.put(i,jsonObject);
           }

           object.put("item",jsonArray);

           Log.e("PlaceOrderParams ",object.toString());

           new RetrofitService(this, this, URLHelper.PLACED_ORDER ,
                   object, 800, 2,"1").callService(true);

       }
       catch (Exception ex)
       {
           ex.printStackTrace();
       }
    }

    int count;
    String fare="";
    public void setAdapter()
    {
        CartAdapter cartAdapter=new CartAdapter(this,list);
        recyclerCart.setLayoutManager(new LinearLayoutManager(this));
        recyclerCart.setAdapter(cartAdapter);

        cartAdapter.onItemSelectedListener(new CartAdapter.onMyClick()
        {
            @Override
            public void onAddClick(int layoutPosition, View view)
            {
                count=Integer.parseInt(list.get(layoutPosition).getCount());
                count++;
                list.get(layoutPosition).setCount(String.valueOf(count));
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void onMinusClick(int layoutPosition, View view)
            {
                if (list.get(layoutPosition).getCount().equalsIgnoreCase("0")||
                        list.get(layoutPosition).getCount().equalsIgnoreCase("1"))
                {

                }
                else
                {
                    count=Integer.parseInt(list.get(layoutPosition).getCount());
                    count-=1;
                    list.get(layoutPosition).setCount(String.valueOf(count));
                    cartAdapter. notifyDataSetChanged();
                }
            }
        });
    }

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

        if (tvPayment.getText().toString().trim().equalsIgnoreCase("CASH"))
        {
            rbCash.setChecked(true);
            rbPayPal.setChecked(false);
        }
        else if (tvPayment.getText().toString().trim().equalsIgnoreCase("Paypal"))
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
                    Toast.makeText(CartScreen.this, "Choose one option",Toast.LENGTH_SHORT).show();
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
            tvPayment.setText("CASH");
        }
        else
        {
            tvPayment.setText("Paypal");
        }
    }

    @BindView(R.id.img_back)
    ImageView img_back;

    @OnClick({R.id.img_back,R.id.tvPayment,R.id.ccOrder,R.id.ivEdit})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.img_back:
                Log.e("backClick ","backClck");
                onBackPressed();
                break;

                case R.id.tvPayment:
                    callPaymentAlert();
                break;

                case R.id.ivEdit:
                    tvAddress.setEnabled(true);
                break;

                case R.id.ccOrder:

                    if (tvAddress.getText().toString().trim().isEmpty())
                    {
                        Toast.makeText(this, "Please enter Address", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
                        if (selection.equalsIgnoreCase("CASH"))
                        {
                            //cash
                            callPlaceOrder();
                        }
                        else
                        {
                            if (paypalPaymentId.equalsIgnoreCase(""))
                            {
                                callConfitmPaymentPopUp();
                            }
                            else
                            {
                                callPlaceOrder();
                            }
                        }
                    }



                break;

        }
    }

    @Override
    public void onResponse(int RequestCode, String response)
    {
        switch (RequestCode)
        {
            case 800:

                Log.e("placedOrder ",response);

                try {
                    JSONObject jsonObject=new JSONObject(response);

                    if (jsonObject.getString("status").equalsIgnoreCase("success"))
                    {
                        Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(this, Home.class);
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
}
