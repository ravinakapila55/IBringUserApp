package com.iBring_user.app.Utils;

import android.animation.TypeEvaluator;

import com.google.android.gms.maps.model.LatLng;


public class RouteEvaluator implements TypeEvaluator<LatLng> {
    @Override
    public LatLng evaluate(float t, LatLng startPoint, LatLng endPoint) {
        if(startPoint!= null && endPoint !=null){
            double lat = startPoint.latitude + t * (endPoint.latitude - startPoint.latitude);
            double lng = startPoint.longitude + t * (endPoint.longitude - startPoint.longitude);
            return new LatLng(lat,lng);
        }
        return null;
    }
}
