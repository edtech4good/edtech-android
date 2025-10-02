package com.fortyk.studentapp.customfonts.customtextview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class KgCornerTextView extends AppCompatTextView {

    public KgCornerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public KgCornerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KgCornerTextView(Context context) {
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
