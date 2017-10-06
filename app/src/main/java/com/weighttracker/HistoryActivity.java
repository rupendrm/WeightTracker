package com.weighttracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.weighttracker.data.WeightHistoryData;
import com.weighttracker.utils.Constants;
import com.weighttracker.utils.DatabaseHandler;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class HistoryActivity extends MenuActivity {

    ListView mLSTUserData;
    int position;
    ArrayList<WeightHistoryData> mWeightHistoryData = null;
    int userID = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_history);

        mLSTUserData = (ListView) findViewById(R.id.history_weight_history);

        SharedPreferences sharedpreferences = getSharedPreferences("weight_tracker", Context.MODE_PRIVATE);
        userID = sharedpreferences.getInt("user_id", 0);

        DatabaseHandler db = new DatabaseHandler(HistoryActivity.this);
        mWeightHistoryData = db.getUserWeightHistory(userID);

        mLSTUserData.setAdapter(new UserDataAdapter(this, mWeightHistoryData));
        this.registerForContextMenu(mLSTUserData);
    }

    @Override
    public void onBackPressed() {

        Intent intent_home = new Intent(HistoryActivity.this, HomeActivity.class);
        startActivity(intent_home);
        finish();

    }

    public class UserDataAdapter extends BaseAdapter
    {

        ArrayList<WeightHistoryData> listData = null;
        Context context;

        public UserDataAdapter(Context context, ArrayList<WeightHistoryData> listData) {

            this.listData = listData;
            this.context = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.history_listrow, null);
            }

            RelativeLayout parentView = (RelativeLayout) convertView.findViewById(R.id.history_parent);
            parentView.setTag(position);
            parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
                    Integer index = (Integer) view.getTag();
                    intent.putExtra("weight", mWeightHistoryData.get(index.intValue()).weight);
                    intent.putExtra("date",  mWeightHistoryData.get(index.intValue()).date);
                    intent.putExtra("photo",  mWeightHistoryData.get(index.intValue()).photo);

                    startActivity(intent);
                }
            });

            final TextView textWeight = (TextView) convertView.findViewById(R.id.history_weight);
            final TextView textDate = (TextView) convertView.findViewById(R.id.history_date);
            final ImageView photo = (ImageView) convertView.findViewById(R.id.history_photo);

            String imagePath = listData.get(position).photo;
            Log.v("com.weighttracker", "imagePath " + imagePath);

            File file = new  File(imagePath);

            if(file.exists()){

                try{

                    Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                    photo.setImageBitmap(bitmap);

                }catch (Exception e){}


            }

            final ImageButton delete = (ImageButton) convertView.findViewById(R.id.history_delete);
            delete.setTag(position);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    DatabaseHandler db = new DatabaseHandler(HistoryActivity.this);
                    Integer index = (Integer) view.getTag();
                    db.deleteUserWeight(listData.get(index.intValue()).id);

                    mWeightHistoryData.clear();
                    mWeightHistoryData = new ArrayList<WeightHistoryData>();
                    mWeightHistoryData = db.getUserWeightHistory(userID);
                    mLSTUserData.setAdapter(new UserDataAdapter(HistoryActivity.this, mWeightHistoryData));

                    notifyDataSetChanged();
                }
            });

            textWeight.setText("Weight - " + listData.get(position).weight);
            textDate.setText(listData.get(position).date);

            return convertView;
        }
    }
}