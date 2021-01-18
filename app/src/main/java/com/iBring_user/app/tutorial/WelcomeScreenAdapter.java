package com.iBring_user.app.tutorial;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;
import com.iBring_user.app.Models.SliderModel;
import com.iBring_user.app.R;
import java.util.ArrayList;

public class WelcomeScreenAdapter extends PagerAdapter
{
    private LayoutInflater inflater;
    private Context context;
    ArrayList<SliderModel> list;

    Integer array[] =
    {
            R.drawable.first,
            R.drawable.second,
            R.drawable.third
    };

    public WelcomeScreenAdapter(Context context, ArrayList<SliderModel> list)
    {
        this.context = context;
        this.list=list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position)
    {
        View imageLayout = inflater.inflate(R.layout.custom_welcome, view, false);

        assert imageLayout != null;
        final ImageView slideimg = (ImageView) imageLayout.findViewById(R.id.ivSlide);
        final TextView tvTitle = (TextView) imageLayout.findViewById(R.id.tvTitle);
        final TextView tvDesc = (TextView) imageLayout.findViewById(R.id.tvDesc);

       /* Picasso.get().load(Constant.SPLASHIMAGEURL + list.get(position).getImage())
                .placeholder(R.drawable.noimageplaceholder).
                into(slideimg);*/
        tvTitle.setText(list.get(position).getTitle());
        tvDesc.setText(list.get(position).getDescription());

        for (int i = 0; i <array.length ; i++)
        {
            slideimg.setImageResource(array[position]);
        }

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader)
    {

    }

    @Override
    public Parcelable saveState()
    {
        return null;
    }

}
