package com.fortyk.studentapp.utils;

import java.math.BigDecimal;

public class Percent {
    public static final Percent PERCENT_0 = new Percent(0);
    public static final Percent PERCENT_10 = new Percent(10);
    public static final Percent PERCENT_20 = new Percent(20);
    public static final Percent PERCENT_30 = new Percent(30);
    public static final Percent PERCENT_40 = new Percent(40);
    public static final Percent PERCENT_50 = new Percent(50);
    public static final Percent PERCENT_60 = new Percent(60);
    public static final Percent PERCENT_70 = new Percent(70);
    public static final Percent PERCENT_80 = new Percent(80);
    public static final Percent PERCENT_90 = new Percent(90);
    public static final Percent PERCENT_100 = new Percent(100);

    public static final BigDecimal DIVISOR_PERCENT = new BigDecimal(100);

    private final double value;

    public Percent(double value) {
        if(value < 0 || value > 100){
            throw new IllegalArgumentException("Percentage value must be in <0;100> range");
        }
        this.value = value;
    }

    public double asIntValue() {
        return value;
    }

    public BigDecimal asBigDecimal() {
        return new BigDecimal(value).divide(DIVISOR_PERCENT);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Percent{");
        sb.append("value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
