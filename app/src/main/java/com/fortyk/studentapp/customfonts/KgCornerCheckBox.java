package com.fortyk.studentapp.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckBox;

public class KgCornerCheckBox extends AppCompatCheckBox {

    public KgCornerCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public KgCornerCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KgCornerCheckBox(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "kg_corner_of_the_sky_regular.ttf");
            setTypeface(tf);
        }
    }
}

