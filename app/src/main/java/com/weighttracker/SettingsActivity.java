package com.weighttracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.weighttracker.data.UserDetials;
import com.weighttracker.utils.Constants;
import com.weighttracker.utils.DatabaseHandler;

import java.util.Calendar;

public class SettingsActivity extends MenuActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener
{

    private Button mRegister;
    private EditText mUsernameTxt, mPasswordTxt, mCurrentWeightTxt, mGoalWeighTxt, mHeightTxt, mGenderTxt, mAgeTxt, mTargetDate;

    private int _day;
    private int _month;
    private int _year;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedpreferences = getSharedPreferences("weight_tracker", Context.MODE_PRIVATE);
        final int userID = sharedpreferences.getInt("user_id", 0);


        final Calendar c = Calendar.getInstance();
        _year  = c.get(Calendar.YEAR);
        _month = c.get(Calendar.MONTH);
        _day   = c.get(Calendar.DAY_OF_MONTH);

        mUsernameTxt = (EditText)findViewById(R.id.settings_username);
        mPasswordTxt = (EditText)findViewById(R.id.settings_password);
        mAgeTxt = (EditText)findViewById(R.id.settings_age);
        mGenderTxt = (EditText)findViewById(R.id.settings_gender);
        mHeightTxt = (EditText)findViewById(R.id.settings_height);
        mCurrentWeightTxt = (EditText)findViewById(R.id.settings_currentweigh);
        mGoalWeighTxt = (EditText)findViewById(R.id.settings_goalweight);
        mTargetDate = (EditText)findViewById(R.id.settings_targetdate);

        DatabaseHandler db = new DatabaseHandler(SettingsActivity.this);
        UserDetials mUserDetials = db.getUserDetails(userID);

        mUsernameTxt.setText(mUserDetials.mUserName);
        mPasswordTxt.setText(mUserDetials.mUserPassword);
        mAgeTxt.setText(mUserDetials.mUserAge);
        mGenderTxt.setText(mUserDetials.mUserGender);
        mHeightTxt.setText(mUserDetials.mUserHeight);
        mCurrentWeightTxt.setText(mUserDetials.mUserCurrentWeight);
        mGoalWeighTxt.setText(mUserDetials.mUserGoalWeight);
        mTargetDate.setText(mUserDetials.mUserTargetDate);

        mTargetDate.setOnClickListener(this);

        mRegister = (Button)findViewById(R.id.settings_update);

        mRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(validateDetails()){


                    DatabaseHandler db = new DatabaseHandler(SettingsActivity.this);
                    db.updateUserDetails(userID, mUsernameTxt.getText().toString(), mPasswordTxt.getText().toString(),
                            mAgeTxt.getText().toString(),
                            mGenderTxt.getText().toString(), mHeightTxt.getText().toString(), mCurrentWeightTxt.getText().toString(),
                            mGoalWeighTxt.getText().toString(), mTargetDate.getText().toString());

                    Toast.makeText(getApplicationContext(),"Details updated successful", Toast.LENGTH_SHORT).show();

                    Intent intent_home = new Intent(SettingsActivity.this, HomeActivity.class);
                    startActivity(intent_home);
                    finish();

                }
            }
        });

        mGenderTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CharSequence[] gender = {"Male","Female"};
                AlertDialog.Builder alert = new AlertDialog.Builder(SettingsActivity.this);
                alert.setSingleChoiceItems(gender,-1, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        mGenderTxt.setText(gender[which]);
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent_home = new Intent(SettingsActivity.this, HomeActivity.class);
        startActivity(intent_home);
        finish();

    }

    private boolean validateDetails(){

        if (isEditTextEmpty(mUsernameTxt, "Please enter username") || !validName(mUsernameTxt, "Please enter valid username")){
            return false;
        }else if (isEditTextEmpty(mPasswordTxt, "Please enter password")){
            return false;
        }else if (isEditTextEmpty(mAgeTxt, "Please enter age")){
            return false;
        }else if (isEditTextEmpty(mGenderTxt, "Please select gender")){
            return false;
        }else if (isEditTextEmpty(mHeightTxt, "Please enter height") ){
            return false;
        }else if (isEditTextEmpty(mCurrentWeightTxt,"Please enter current weight") ){
            return false;
        } else if (isEditTextEmpty(mGoalWeighTxt, "Please enter gole weight") ){
            return false;
        }else if (isEditTextEmpty(mTargetDate, "Please select target date") ){
            return false;
        }

        return true;
    }
    // Check for empty value in editext
    public boolean isEditTextEmpty(EditText editText, String message)
    {

        if(editText.getText().toString().length()>0)
            return false;
        else
        {
            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
            return true;
        }
    }


    // Check for valid number
    public boolean validName(EditText editText, String message)
    {
        String text = editText.getText().toString().trim();

        String namePattern = "^[a-zA-Z ]*$";

        if (!text.matches(namePattern))
        {
            Toast.makeText(getApplicationContext(),message, Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            return  true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        _year = year;
        _month = monthOfYear;
        _day = dayOfMonth;
        updateDisplay();
    }


    @Override
    public void onClick(View v) {

        DatePickerDialog dialog =  new DatePickerDialog(SettingsActivity.this, this, _year, _month, _day );
        dialog.show();

    }

    // updates the date in the birth date EditText
    private void updateDisplay() {

        mTargetDate.setText(new StringBuilder().append(_day).append("/").append(_month + 1).append("/").append(_year).append(""));
    }
}