package com.iBring_user.app.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.iBring_user.app.R;


public class CustomMarker
{
    public static Bitmap getMarkerViewMovement(Context context)
    {
        View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_marker_move, null);
        LinearLayout llLocation = (LinearLayout) customMarkerView.findViewById(R.id.llLocation);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.ivLocation);

     /*   if(address.length()>15)
        {
            tvLocation.setText(address.substring(0, 15) + "...");
        }else
        {
            tvLocation.setText(address);
        }*/


     /*if (type.equalsIgnoreCase("p"))
     {
         markerImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.car_live));
     }
     else {
         markerImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.enter_location));
     }
*/
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);

        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;

    }

    public static Bitmap getMarkerViewMovementNo(Context context)
    {

        View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_marker_move, null);
        LinearLayout llLocation = (LinearLayout) customMarkerView.findViewById(R.id.llLocation);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.ivLocation);



     /*   if(address.length()>15)
        {
            tvLocation.setText(address.substring(0, 15) + "...");
        }else
        {
            tvLocation.setText(address);
        }*/


        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    public static Bitmap getMarkerBitmapFromView11(Context context, final String address, int value)
    {

        View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_window_map, null);

        LinearLayout llLocation = (LinearLayout) customMarkerView.findViewById(R.id.llLocation);

        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.ivLocation);

        final TextView tvLocation = (TextView) customMarkerView.findViewById(R.id.tvLocation);
        Log.e( "getMarkerBitmapFrom",value+"");

        if(value==1)
        {
//       markerImageView.setRotation(180);
         markerImageView.setRotation(0);
        }

        else if(value==2)
        {
            markerImageView.setRotation(0);
        }

        if(address.length()>15)
        {
            tvLocation.setText(address.substring(0, 15) + "...");
        }
        else
        {
            tvLocation.setText(address);
        }

     /*   llLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CUSTOM_MARKER", "onClick:Hello" );
                    tvLocation.setVisibility(View.VISIBLE);
                    if(address.length()>15) {
                        tvLocation.setText(address.substring(0, 15) + "...");
                    }else{
                        tvLocation.setText(address);
                    }

            }
        });*/

//        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    public static Bitmap getMarkerBitmapBo0king(Context context, final String address, int value) {

        View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.map_window_custom, null);
        LinearLayout llLocation = (LinearLayout) customMarkerView.findViewById(R.id.llLocation);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.ivLocation);
        final TextView tvLocation = (TextView) customMarkerView.findViewById(R.id.tvLocation);

        tvLocation.setVisibility(View.GONE);

        Log.e( "getMarkerBitmap ",value+"");
        if(value==1)//todo pickup
        {
//            markerImageView.setRotation(180);
            markerImageView.setRotation(0);
            markerImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.marker_red));
        }
        else if(value==2)//todo drop
        {
            markerImageView.setRotation(0);
            markerImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.location_green));
        }
        else if(value==5)//driver's Location
        {
            markerImageView.setRotation(0);
            markerImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.car));
        }

        if(address.length()>15)
        {
            tvLocation.setText(address.substring(0, 15) + "...");
        }else
        {
            tvLocation.setText(address);
        }

     /*   llLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CUSTOM_MARKER", "onClick:Hello" );
                    tvLocation.setVisibility(View.VISIBLE);
                    if(address.length()>15) {
                        tvLocation.setText(address.substring(0, 15) + "...");
                    }else{
                        tvLocation.setText(address);
                    }

            }
        });*/

//        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }


    public static Bitmap getMarkerBitmapBo0kingLive(Context context, final String address, int value) {

        View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.map_move, null);
        LinearLayout llLocation = (LinearLayout) customMarkerView.findViewById(R.id.llLocation);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.ivLocation);
        final TextView tvLocation = (TextView) customMarkerView.findViewById(R.id.tvLocation);

        tvLocation.setVisibility(View.GONE);

        Log.e( "getMarkerBitmap ",value+"");
        if(value==1)//pickup
        {
//            markerImageView.setRotation(180);
            markerImageView.setRotation(0);
            markerImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.location_icon));
        }
        else if(value==2)//drop
        {
            markerImageView.setRotation(0);
            markerImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.location_icon));
        }
        else if(value==5)//driver's Location
        {
            markerImageView.setRotation(0);
            markerImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.car));
        }

        if(address.length()>15)
        {
            tvLocation.setText(address.substring(0, 15) + "...");
        }
        else
        {
            tvLocation.setText(address);
        }

     /*   llLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CUSTOM_MARKER", "onClick:Hello" );
                    tvLocation.setVisibility(View.VISIBLE);
                    if(address.length()>15) {
                        tvLocation.setText(address.substring(0, 15) + "...");
                    }else{
                        tvLocation.setText(address);
                    }

            }
        });*/

//        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }



    public static Bitmap getMarkerBitmapFromView(Context context, final String address, int value) {

        View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_map_window, null);
        LinearLayout llLocation = (LinearLayout) customMarkerView.findViewById(R.id.llLocation);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.ivLocation);
        final TextView tvLocation = (TextView) customMarkerView.findViewById(R.id.tvLocation);

        tvLocation.setVisibility(View.GONE);
        Log.e( "getMarkerBitma ",value+"");
        if(value==1){
//            markerImageView.setRotation(180);
            markerImageView.setRotation(0);
        }else if(value==2){
            markerImageView.setRotation(0);
        }

        if(address.length()>15)
        {
            tvLocation.setText(address.substring(0, 15) + "...");
        }else
            {
            tvLocation.setText(address);
        }

     /*   llLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CUSTOM_MARKER", "onClick:Hello" );
                    tvLocation.setVisibility(View.VISIBLE);
                    if(address.length()>15) {
                        tvLocation.setText(address.substring(0, 15) + "...");
                    }else{
                        tvLocation.setText(address);
                    }

            }
        });*/

//        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    public static Bitmap getMarkerBitmapFromViewForBothLoc(Context context, String pickup, String drop, int value) {
        View customMarkerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.custom_map_window, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.ivLocation);
        TextView tvLocation = (TextView) customMarkerView.findViewById(R.id.tvLocation);
        if(pickup.endsWith("pickup")){
            tvLocation.setText(pickup.replaceAll("pickup",""));
            markerImageView.setRotation(180);
        }else{
            tvLocation.setText(drop);
            markerImageView.setRotation(0);
        }
//        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

}

