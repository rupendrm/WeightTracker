package com.weighttracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.weighttracker.data.UserDetials;
import com.weighttracker.utils.Constants;
import com.weighttracker.utils.DatabaseHandler;

import java.util.Calendar;

public class LoginActivity extends Activity{
	
	Button mLogin, mRegister;
	EditText mUsername, mPassword;
	SharedPreferences sharedpreferences = null;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		sharedpreferences = getSharedPreferences("weight_tracker", Context.MODE_PRIVATE);
		int userID = sharedpreferences.getInt("user_id", 0);

		if(userID != 0){

			Intent loginInt = new Intent(LoginActivity.this, HomeActivity.class);
			startActivity(loginInt);
			finish();
		}

		mUsername = (EditText)findViewById(R.id.login_username);
		mPassword = (EditText)findViewById(R.id.login_password);

		mLogin = (Button)findViewById(R.id.login_login);
		mRegister = (Button)findViewById(R.id.login_register);

		mLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(!validateDetails()){
					return;
				}

				Intent loginInt = new Intent(LoginActivity.this, HomeActivity.class);
				startActivity(loginInt);
				finish();
			}
		});


		mRegister.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent loginInt = new Intent(LoginActivity.this, RegisterActivity.class);
				startActivity(loginInt);
			}
		});
	}

	private boolean validateDetails(){

		if (isEditTextEmpty(mUsername, "Please enter username")){
			return false;
		}
		else if (isEditTextEmpty(mPassword, "Please enter password")){
			return false;
		}
		else {

			DatabaseHandler db = new DatabaseHandler(LoginActivity.this);
			UserDetials userDetials = db.getUserDetails(mUsername.getText().toString().trim(), mPassword.getText().toString().trim());

			if(userDetials.mUserID == 0){
				Toast.makeText(getApplicationContext(),"Invalied username or password", Toast.LENGTH_SHORT).show();
			}
			else{

				SharedPreferences.Editor editor = sharedpreferences.edit();
				editor.putInt("user_id", userDetials.mUserID);
				editor.commit();

				return true;
			}
			return false;
		}
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



}
