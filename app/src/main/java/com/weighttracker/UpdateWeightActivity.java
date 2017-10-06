package com.weighttracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.weighttracker.data.UserDetials;
import com.weighttracker.utils.Constants;
import com.weighttracker.utils.DatabaseHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class UpdateWeightActivity extends MenuActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener
{

    private Button mUpdateWeight;
    private EditText mCurrentWeight, mCurrentDate;
    ImageView mPhoto;

    private int _day;
    private int _month;
    private int _year;

    final static int REQUEST_CAMERA = 1;
    String imagePath = "";

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_weight);

        final Calendar c = Calendar.getInstance();
        _year  = c.get(Calendar.YEAR);
        _month = c.get(Calendar.MONTH);
        _day   = c.get(Calendar.DAY_OF_MONTH);

        mCurrentWeight = (EditText)findViewById(R.id.update_currentweigh);
        mCurrentDate = (EditText)findViewById(R.id.update_date);

        mCurrentDate.setOnClickListener(this);

        mUpdateWeight = (Button)findViewById(R.id.update_weight_details);

        mPhoto = (ImageView) findViewById(R.id.update_photo);

        mPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
            }
        });


        mUpdateWeight.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(validateDetails()){

                    SharedPreferences sharedpreferences = getSharedPreferences("weight_tracker", Context.MODE_PRIVATE);
                    final int userID = sharedpreferences.getInt("user_id", 0);

                    DatabaseHandler db = new DatabaseHandler(UpdateWeightActivity.this);
                    UserDetials mUserDetials = db.getUserDetails(userID);

                    int actualWeight = Integer.parseInt(mUserDetials.mUserActualWeight);
                    int currentWeight = Integer.parseInt(mCurrentWeight.getText().toString());

                    String userWeightLoss = (actualWeight - currentWeight) +"";

                    Log.v("com.weighttracker", "imagePath " + imagePath);

                    db.addUserWeight(userID, mCurrentWeight.getText().toString(), mCurrentDate.getText().toString(), imagePath);
                    db.updateUserWeightDetails(userID, mCurrentWeight.getText().toString(), mUserDetials.mUserCurrentWeight, userWeightLoss);

                    Toast.makeText(getApplicationContext(),"Weight details updated successfully", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(UpdateWeightActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent intent_home = new Intent(UpdateWeightActivity.this, HomeActivity.class);
        startActivity(intent_home);
        finish();

    }

    private boolean validateDetails(){

        if (isEditTextEmpty(mCurrentWeight,"Please enter current weight") ){
            return false;
        }
        else if (isEditTextEmpty(mCurrentDate, "Please select current date") ){
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


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        _year = year;
        _month = monthOfYear;
        _day = dayOfMonth;
        updateDisplay();
    }


    @Override
    public void onClick(View v) {

        DatePickerDialog dialog =  new DatePickerDialog(UpdateWeightActivity.this, this, _year, _month, _day );
        dialog.show();

    }

    // updates the date in the birth date EditText
    private void updateDisplay() {

        mCurrentDate.setText(new StringBuilder().append(_day).append("/").append(_month + 1).append("/").append(_year).append(""));
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data)
    {
        if ( requestCode == REQUEST_CAMERA) {

            if ( resultCode == RESULT_OK) {

                /*********** Load Captured Image And Data Start ****************/

                Bitmap  myBitmap = (Bitmap) data.getExtras().get("data");

                if(myBitmap != null){

                    mPhoto.setImageBitmap(myBitmap);
                    imagePath = saveToInternalStorage(myBitmap);
                }
                else {
                    Toast.makeText(this, " Picture was not taken, please try again ", Toast.LENGTH_SHORT).show();
                }


            } else if ( resultCode == RESULT_CANCELED) {

                Toast.makeText(this, " Picture was not taken, please try again ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage){

        long time = System.currentTimeMillis();

        final String log_foldername = "WeightTracker";
        final String log_filename = "photo_weight_" + time;
        final String log_filextention = ".jpg";

        File foldername = null;
        File filename = null;

        try
        {
            String root = Environment.getExternalStorageDirectory().toString();
            foldername = new File(root + "/" + log_foldername);


            if(!foldername.exists())
                foldername.mkdirs();

            filename = new File (foldername, log_filename + log_filextention);
            boolean success = false;

            // Encode the file as a PNG image.
            FileOutputStream outStream;
            try {

                outStream = new FileOutputStream(filename);
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

                outStream.flush();
                outStream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch(Exception err) {

            err.printStackTrace();

        }

        Log.v("com.weighttracker", "filename " + filename.getAbsolutePath());

        return filename.getAbsolutePath();
    }
}