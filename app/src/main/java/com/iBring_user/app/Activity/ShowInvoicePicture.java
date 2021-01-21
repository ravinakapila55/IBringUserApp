package com.iBring_user.app.Activity;

/**
 * Created by CSS08 on 01-07-2017.
 */

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.iBring_user.app.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

public class ShowInvoicePicture extends AppCompatActivity implements View.OnClickListener {

    ImageView imgZoomService;

    Activity activity;
    ImageView backArrow;

    String image = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
        setContentView(R.layout.show_invoice_pic);
        findviewById();
        setOnClickListener();

    }

    private void findviewById() {
        imgZoomService = (ImageView) findViewById(R.id.imgZoomService);
        backArrow = (ImageView) findViewById(R.id.backArrow);


//        before_comment = getIntent().getExtras().getString("before_comment");
        image = getIntent().getExtras().getString("image");


        Picasso.with(activity).load(image).memoryPolicy(MemoryPolicy.NO_CACHE).placeholder(R.drawable.no_image).error(R.drawable.no_image).into(imgZoomService);


    }

    private void setOnClickListener() {
        backArrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == backArrow) {
            finish();
        }
    }

}
