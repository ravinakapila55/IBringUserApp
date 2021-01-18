package com.iBring_user.app.Utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeoLocation
{
    public static String getAddress(Context context, double lat, double lng)
    {
        Geocoder geocoder;
        String address="";
        String cId="";
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat,lng, 1);
            Log.e("Addresssess ",addresses.toString());

            if (addresses.size()>0)
            {
                address = addresses.get(0).getAddressLine(0);
                cId = addresses.get(0).getLocality();
                Log.e("cId ",cId);
//                Log.e("locality ",addresses.get(0).getLocality());
            }

            else
            {
                address="";
                cId="";
                Toast.makeText(context,"Location Not Found", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    public static String getCid(Context context, double lat, double lng)
    {
        Geocoder geocoder;
        String address="";
        String cId="";
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat,lng, 1);
            Log.e("Addresssess ",addresses.toString());

            if (addresses.size()>0)
            {
                address = addresses.get(0).getAddressLine(0);
//                cId = addresses.get(0).getCountryCode();
                cId = addresses.get(0).getLocality();
                Log.e("cId ",cId);
            }

            else
            {
                address="";
                cId="";
                Toast.makeText(context,"Location Not Found", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cId;
    }


}
