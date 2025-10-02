package com.fortyk.studentapp.customfonts.customradiobutton;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class KgCornerRadioButton extends androidx.appcompat.widget.AppCompatRadioButton {

    public KgCornerRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public KgCornerRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KgCornerRadioButton(Context context) {
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

