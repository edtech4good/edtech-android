package com.fortyk.studentapp.customfonts.customtextview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class ItcTextView extends AppCompatTextView {

    public ItcTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public ItcTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ItcTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "itc-avant-garde-gothic-std-demi-58f48d14eff48.otf");
            setTypeface(tf);
        }
    }
}
