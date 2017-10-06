package com.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import com.weighttracker.data.WeightHistoryData;
import com.weighttracker.utils.Constants;
import com.weighttracker.utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends MenuActivity {

    private int blankCnt = 7;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initView();
    }

    @Override
    public void onBackPressed() {

        Intent intent_home = new Intent(ReportActivity.this, HomeActivity.class);
        startActivity(intent_home);
        finish();

    }

    private void initView(){

        SharedPreferences sharedpreferences = getSharedPreferences("weight_tracker", Context.MODE_PRIVATE);
        int userID = sharedpreferences.getInt("user_id", 0);

        DatabaseHandler db = new DatabaseHandler(ReportActivity.this);
        ArrayList<WeightHistoryData> mWeightHistoryData = db.getUserWeightHistory(userID);

        if(mWeightHistoryData.size() > 0)
        {
            setGraphData(mWeightHistoryData);
        }
    }

    private void setGraphData(ArrayList<WeightHistoryData> aWeightHistoryData)
    {

        ArrayList<String> mMaxValue = new ArrayList<>();
        HorizontalBarChart barChart = (HorizontalBarChart) findViewById(R.id.reports_grapview_chart);

        List<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();

        int cnt = 0;

        for (int i = 0; i < aWeightHistoryData.size(); i++, cnt++)
        {
            blankCnt --;
            entries.add(new BarEntry(Float.parseFloat(aWeightHistoryData.get(cnt).weight), cnt));
            labels.add(aWeightHistoryData.get(cnt).date);
        }

        for (int i = 0; i < blankCnt; i++, cnt++)
        {
            entries.add(new BarEntry(Float.parseFloat("0.0"), cnt));
            labels.add("0");
        }

        BarDataSet dataset = new BarDataSet(entries, "");

        BarData data = new BarData(labels, dataset);
        dataset.setColor(getResources().getColor(R.color.blue_dark));

        barChart.setData(data);
        barChart.invalidate();

        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getAxisLeft().setDrawLabels(false);

        barChart.setVisibleXRangeMinimum(7);
        barChart.setTouchEnabled(false);
        barChart.setDescription("");

    }
}
