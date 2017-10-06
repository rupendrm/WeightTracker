package com.weighttracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.weighttracker.data.UserDetials;
import com.weighttracker.data.WeightHistoryData;
import com.weighttracker.utils.Constants;
import com.weighttracker.utils.DatabaseHandler;

import java.util.Calendar;

public class RegisterActivity extends Activity implements View.OnClickListener, DatePickerDialog.OnDateSetListener{

	private Button mRegister;
	private EditText mUsernameTxt, mPasswordTxt, mCurrentWeightTxt, mGoalWeighTxt, mHeightTxt, mGenderTxt, mAgeTxt, mTargetDate;

	private String mCurrrentDate = "";

	private int _day;
	private int _month;
	private int _year;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		final Calendar c = Calendar.getInstance();
		_year  = c.get(Calendar.YEAR);
		_month = c.get(Calendar.MONTH);
		_day   = c.get(Calendar.DAY_OF_MONTH);

		mCurrrentDate = (new StringBuilder().append(_day).append("/").append(_month + 1).append("/").append(_year).append("")).toString();

		mUsernameTxt = (EditText)findViewById(R.id.register_username);
		mPasswordTxt = (EditText)findViewById(R.id.register_password);
		mAgeTxt = (EditText)findViewById(R.id.register_age);
		mGenderTxt = (EditText)findViewById(R.id.register_gender);
		mHeightTxt = (EditText)findViewById(R.id.register_height);
		mCurrentWeightTxt = (EditText)findViewById(R.id.register_currentweigh);
		mGoalWeighTxt = (EditText)findViewById(R.id.register_goalweight);
		mTargetDate = (EditText)findViewById(R.id.register_targetdate);

		mTargetDate.setOnClickListener(this);

		mRegister = (Button)findViewById(R.id.register_register);

		mRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(validateDetails()){


					DatabaseHandler db = new DatabaseHandler(RegisterActivity.this);
					db.addUser(mUsernameTxt.getText().toString(), mPasswordTxt.getText().toString(), mAgeTxt.getText().toString(),
							mGenderTxt.getText().toString(), mHeightTxt.getText().toString(), mCurrentWeightTxt.getText().toString(),
							mCurrentWeightTxt.getText().toString(), "0", mGoalWeighTxt.getText().toString(), mTargetDate.getText().toString(), "0");
					UserDetials userDetials = db.getUserDetails(mUsernameTxt.getText().toString(), mPasswordTxt.getText().toString());
					db.addUserWeight(userDetials.mUserID, mCurrentWeightTxt.getText().toString(), mCurrrentDate, "");

					Toast.makeText(getApplicationContext(),"Registration successful", Toast.LENGTH_SHORT).show();
					finish();

				}
			}
		});

		mGenderTxt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				final CharSequence[] gender = {"Male","Female"};
				AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
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

		DatePickerDialog dialog =  new DatePickerDialog(RegisterActivity.this, this, _year, _month, _day );
		dialog.show();

	}

	// updates the date in the birth date EditText
	private void updateDisplay() {

		mTargetDate.setText(new StringBuilder().append(_day).append("/").append(_month + 1).append("/").append(_year).append(""));
	}
}
