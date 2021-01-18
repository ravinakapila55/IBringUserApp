package com.iBring_user.app.Utils;

import android.content.Context;

import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

/**
 * Created by Freeware Sys on 3/27/2017.
 */

public class ClanProButton extends AppCompatButton {


    public ClanProButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(!isInEditMode())
            applyFont(context);
    }

    private void applyFont(Context context){
        setTypeface(Typefaces.get(context, "ClanPro-NarrNews.otf"));
    }


}
