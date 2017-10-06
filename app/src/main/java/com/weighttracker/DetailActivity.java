package com.weighttracker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.weighttracker.utils.Constants;

import java.io.File;
import java.io.FileInputStream;

public class DetailActivity extends Activity{

	TextView mWeight = null;
	TextView mDate = null;
	ImageView mPhoto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_details);

		mWeight = (TextView) findViewById(R.id.details_weight);
		mDate = (TextView) findViewById(R.id.details_date);
		mPhoto = (ImageView) findViewById(R.id.update_photo);

		mWeight.setText(getIntent().getStringExtra("weight"));
		mDate.setText(getIntent().getStringExtra("date"));

		String imagePath = getIntent().getStringExtra("photo");
		Log.v("com.weighttracker", "imagePath " + imagePath);

		File file = new  File(imagePath);

		if(file.exists()){

			try{

				Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
				mPhoto.setImageBitmap(bitmap);

			}catch (Exception e){}


		}
	}
}
