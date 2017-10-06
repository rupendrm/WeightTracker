package com.weighttracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import com.weighttracker.data.UserDetials;
import com.weighttracker.data.WeightHistoryData;
import com.weighttracker.utils.Constants;
import com.weighttracker.utils.DatabaseHandler;

import java.util.ArrayList;

public class HomeActivity extends MenuActivity {

    private TextView mCurrentWeight = null;
    private TextView mWeightLossByWeek = null;
    private TextView mBodyMassIndex = null;
    private TextView mWeightLoss = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_home);

        mCurrentWeight = (TextView) findViewById(R.id.home_current_weight);
        mBodyMassIndex = (TextView) findViewById(R.id.home_bmi);
        mWeightLossByWeek = (TextView) findViewById(R.id.home_avg_weekly_weight_loss);
        mWeightLoss = (TextView) findViewById(R.id.home_weight_loss);

    }

    @Override
    protected void onResume() {
        super.onResume();

        updateDetails();

    }

    private void updateDetails() {

        SharedPreferences sharedpreferences = getSharedPreferences("weight_tracker", Context.MODE_PRIVATE);
        int userID = sharedpreferences.getInt("user_id", 0);

        DatabaseHandler db = new DatabaseHandler(HomeActivity.this);
        UserDetials userDetials = db.getUserDetails(userID);
        ArrayList<WeightHistoryData> mWeightHistoryData  = db.getUserWeightHistory(userID);

        Double valueheight = Double.parseDouble(userDetials.mUserHeight);
        Double currentweight = Double.parseDouble(userDetials.mUserCurrentWeight);
        Double valueheightmeters;

        valueheightmeters = valueheight / 100;
        Double bmi = (currentweight / (valueheightmeters * valueheightmeters));


        Double actualweight = Double.parseDouble(userDetials.mUserActualWeight);
        int numlenngth = mWeightHistoryData.size();
        double total = 0;

        for (int i = 0; i < numlenngth; i++) {

            Log.v("com.weighttracker", "weight " + mWeightHistoryData.get(i).weight);
            Double weight = Double.parseDouble(mWeightHistoryData.get(i).weight);
            total += (actualweight - weight);
        }

        Log.v("com.weighttracker", "actualweight " + actualweight);

        Log.v("com.weighttracker", "total " + total);

        mCurrentWeight.setText(userDetials.mUserCurrentWeight + " lbs");
        mWeightLossByWeek.setText(String.format("%.1f", (total / numlenngth)) + " lbs");
        mBodyMassIndex.setText(String.format("%.2f", bmi));
        mWeightLoss.setText(userDetials.mUserWeightLoss + " lbs");

    }
}