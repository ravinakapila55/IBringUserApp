package com.iBring_user.app.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.afollestad.viewpagerdots.DotsIndicator;
import com.iBring_user.app.Activity.Register;
import com.iBring_user.app.Activity.SignIn;
import com.iBring_user.app.Helper.SharedHelper;
import com.iBring_user.app.Models.SliderModel;
import com.iBring_user.app.R;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TutorialScreens extends AppCompatActivity
{
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @BindView(R.id.tvSkip)
    TextView tvSkip;

    @BindView(R.id.tvNext)
    TextView tvNext;

    @BindView(R.id.dots)
    DotsIndicator dots;

    ArrayList<SliderModel> list=new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screens);
        ButterKnife.bind(this);
        setadapter();
    }

    public void setData()
    {
        list.clear();

       /* SliderModel sliderModel = new SliderModel("0", "iBring Courier",
                getResources().getString(R.string.dummy));
        list.add(sliderModel); */

        SliderModel sliderModel = new SliderModel("0", "iBring Courier",
               "Offer Courier services to daily users.");
        list.add(sliderModel);

        sliderModel = new SliderModel("1", "iBring Transport/Taxi",
               "Offer Transport/taxi services to daily users.");
        list.add(sliderModel);

        sliderModel = new SliderModel("2", "iBring Food",
                "Offer Food services to daily users.");
        list.add(sliderModel);

    }

    private void setadapter()
    {
        setData();
        WelcomeScreenAdapter  welcomeScreenAdapter = new WelcomeScreenAdapter(this, list);
        viewpager.setAdapter(welcomeScreenAdapter);
        dots.attachViewPager(viewpager);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
            Log.e("PositionViewPager ",positionOffset+"");
            Log.e("position ",position+"");
            }

            @Override
            public void onPageSelected(int position)
            {
                Log.e("countScroll ",count+"");
                Log.e("positionScroll ",position+"");
                Log.e("listSize ",list.size()+"");
                if (position==list.size()-1)
                {
                    tvSkip.setVisibility(View.VISIBLE);
                    tvSkip.setText("Back");
                    tvNext.setText("Get Started");

                }
                else
                {
                    if (position==list.size()-2)
                    {
                        tvSkip.setText("Back");
                    }
                    else
                    {
                        tvSkip.setText("Skip");
                    }
                    tvSkip.setVisibility(View.VISIBLE);
                    tvNext.setText("Next");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

      /*
     indicator.setViewPager(viewpager);
     viewpager.setPageTransformer(false, new DefaultTransformer());

     final float density = getResources().getDisplayMetrics().density;
     indicator.setRadius(5 * density);
     */
    }

    int count=0;

    @OnClick({R.id.tvNext,R.id.tvSkip})
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tvNext:

                Log.e("CountNext ",count+"");
                if (tvNext.getText().toString().equalsIgnoreCase("Get Started"))
                {
                    Intent intent=new Intent(this, SignIn.class);
                    startActivity(intent);
                }
                else
                {
                    viewpager.arrowScroll(View.FOCUS_RIGHT);
                    count=count+1;
                }

              /*  if (count==2)
                {
                 //next screen
                    Intent intent=new Intent(this, SignIn.class);
                    startActivity(intent);
                }
                else
                {
                    viewpager.arrowScroll(View.FOCUS_RIGHT);
                    count=count+1;
                }*/

                break;

            case R.id.tvSkip:

                Log.e("CountSkip ",count+"");

                if (tvSkip.getText().toString().trim().equalsIgnoreCase("Skip"))
                {
                    Intent intent=new Intent(this, SignIn.class);
                    startActivity(intent);
                }
                else
                {
                    viewpager.arrowScroll(View.FOCUS_LEFT);
                    count=count-1;
                }

                 /*  if (count==2 || count==1)
                {
                    viewpager.arrowScroll(View.FOCUS_LEFT);
                    count=count-1;
                }
                else
                {
                    Intent intent=new Intent(this, SignIn.class);
                    startActivity(intent);
                    //next screen
                }*/

                break;
        }
    }
}
