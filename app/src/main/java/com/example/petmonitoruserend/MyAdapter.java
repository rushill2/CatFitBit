package com.example.petmonitoruserend;

import com.robinhood.spark.SparkAdapter;

public class MyAdapter extends SparkAdapter {
    private float[] yData;

    public MyAdapter(float[] yData) {
        this.yData = yData;
    }

    @Override
    public int getCount() {
        return yData.length;
    }

    @Override
    public Object getItem(int index) {
        return yData[index];
    }

    @Override
    public float getY(int index) {
        return yData[index];
    }
}