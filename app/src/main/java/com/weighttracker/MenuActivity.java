package com.weighttracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.weighttracker.data.UserDetials;
import com.weighttracker.utils.Constants;

import java.util.ArrayList;

public class MenuActivity extends Activity {

	private ImageView mTopBarMenu = null;
	private LinearLayout mDrawer = null;
	private DrawerLayout mDrawerLayout = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub

		mTopBarMenu = (ImageView) findViewById(R.id.slidingmenu_IMG_options);

			setDrawerList();

		super.onStart();
	}


	private void setDrawerList() {
		// TODO Auto-generated method stu

		mDrawer = (LinearLayout) findViewById(R.id.drawer);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mTopBarMenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				mDrawerLayout.openDrawer(mDrawer);

			}
		});

	}


	public void closeDrawer() {
		mDrawerLayout.closeDrawer(mDrawer);
	}

	/**
	 * @param v
	 * @discription on clicking on any option on topbar_img_menu will trigger this function and it will perform the corresponding
	 * action based on view id
	 */
	public void onMenuOptionClick(View v) {

		Intent intent_home = null;

		try {

			switch (v.getId()) {

				case R.id.menu_home:

					intent_home = new Intent(MenuActivity.this, HomeActivity.class);
					startActivity(intent_home);
					finish();

					break;

				case R.id.menu_update:

					intent_home = new Intent(MenuActivity.this, UpdateWeightActivity.class);
					startActivity(intent_home);
					finish();

					break;

				case R.id.menu_Settings:

					intent_home = new Intent(MenuActivity.this, SettingsActivity.class);
					startActivity(intent_home);
					finish();

					break;

				case R.id.menu_history:

					intent_home = new Intent(MenuActivity.this, HistoryActivity.class);
					startActivity(intent_home);
					finish();

					break;

				case R.id.menu_report:

					intent_home = new Intent(MenuActivity.this, ReportActivity.class);
					startActivity(intent_home);
					finish();

					break;

				case R.id.menu_logout:

					SharedPreferences sharedpreferences = getSharedPreferences("weight_tracker", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedpreferences.edit();
					editor.clear();
					editor.commit();

					intent_home = new Intent(MenuActivity.this, LoginActivity.class);
					startActivity(intent_home);
					finish();

					break;
			}

		}catch (Exception e){

		}
	}
}