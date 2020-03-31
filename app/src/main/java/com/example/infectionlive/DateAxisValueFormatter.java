package com.example.infectionlive;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;

class DateAxisValueFormatter implements IAxisValueFormatter {
    private String[] mValues;

    SimpleDateFormat sdf = new SimpleDateFormat("MM.dd");

    public DateAxisValueFormatter(String[] values) {
        this.mValues = values; }


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        return mValues[(int) value];
    }
}