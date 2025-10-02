package com.fortyk.studentapp.utils;

import android.content.Context;
import android.util.AttributeSet;

import com.fortyk.studentapp.R;

import java.math.BigDecimal;

public class HorizontalProgressBar extends androidx.appcompat.widget.AppCompatImageView {

    private static final BigDecimal MAX = BigDecimal.valueOf(10000);

    public HorizontalProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setImageResource(R.drawable.progress_bar);
    }

    public void setCurrentValue(Percent percent) {
        int cliDrawableImageLevel = percent.asBigDecimal().multiply(MAX).intValue();
        setImageLevel(cliDrawableImageLevel);
    }
}
