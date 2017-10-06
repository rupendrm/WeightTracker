package com.weighttracker.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.weighttracker.data.UserDetials;
import com.weighttracker.data.WeightHistoryData;

import java.util.ArrayList;


public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "weighttracket";

    private static final String TABLE_USERS = "contacts";
    private static final String TABLE_WEIGHT = "weights";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "username";
    private static final String KEY_PASS = "password";
    private static final String KEY_AGE = "age";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_HEIGHT = "height";

    private static final String KEY_ACTUAL_WEIGHT = "actual_weight";
    private static final String KEY_CURRENT_WEIGHT = "current_weight";
    private static final String KEY_PREVIOUS_WEIGHT = "previous_weight";
    private static final String KEY_GOAL_WEIGHT = "goal_weight";

    private static final String KEY_TARGET_DATE = "target_date";
    private static final String KEY_WEIGHT_LOSS = "weight_loss";


    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_DATE = "date";
    private static final String KEY_PHOTO = "photo";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USERS + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_NAME + " VARCHAR(200), " +
                KEY_PASS + " VARCHAR(200), " +
                KEY_AGE + " VARCHAR(200), " +
                KEY_GENDER + " VARCHAR(200), " +
                KEY_HEIGHT + " VARCHAR(200), " +
                KEY_ACTUAL_WEIGHT + " VARCHAR(200), " +
                KEY_CURRENT_WEIGHT + " VARCHAR(200), " +
                KEY_PREVIOUS_WEIGHT + " VARCHAR(200), " +
                KEY_GOAL_WEIGHT + " VARCHAR(200), " +
                KEY_TARGET_DATE + " VARCHAR(200), " +
                KEY_WEIGHT_LOSS + " VARCHAR(200)" + ")";

        String CREATE_USER_WEIGHT = "CREATE TABLE " + TABLE_WEIGHT + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_USER_ID + " INTEGER, " +
                KEY_WEIGHT + " VARCHAR(200), " +
                KEY_DATE + " VARCHAR(200), " +
                KEY_PHOTO + " VARCHAR(200)" + ")";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_USER_WEIGHT);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEIGHT);

        // Create tables again
        onCreate(db);
    }

    // code to add the new contact
    public void addUser(String mUserName, String mUserPassword, String mUserAge, String mUserGender, String mUserHeight,
                        String mUserActualWeight, String mUserCurrentWeight, String mUserPreviousWeight, String mUserGoalWeight,
                        String mUserTargetDate, String mUserWeightLoss) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, mUserName);
        values.put(KEY_PASS, mUserPassword);
        values.put(KEY_AGE, mUserAge);
        values.put(KEY_GENDER, mUserGender);
        values.put(KEY_HEIGHT, mUserHeight);
        values.put(KEY_ACTUAL_WEIGHT, mUserActualWeight);
        values.put(KEY_CURRENT_WEIGHT, mUserCurrentWeight);
        values.put(KEY_PREVIOUS_WEIGHT, mUserPreviousWeight);
        values.put(KEY_GOAL_WEIGHT, mUserGoalWeight);
        values.put(KEY_TARGET_DATE, mUserTargetDate);
        values.put(KEY_WEIGHT_LOSS, mUserWeightLoss);

        // Inserting Row
        db.insert(TABLE_USERS, null, values);

        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection

    }

    // code to get all contacts in a list view
    public UserDetials getUserDetails(int userId) {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS+ " WHERE " + KEY_ID + " = " + userId ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        UserDetials mUserDetials = new UserDetials();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            mUserDetials.mUserID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            mUserDetials.mUserName = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            mUserDetials.mUserPassword = cursor.getString(cursor.getColumnIndex(KEY_PASS));
            mUserDetials.mUserAge = cursor.getString(cursor.getColumnIndex(KEY_AGE));
            mUserDetials.mUserGender = cursor.getString(cursor.getColumnIndex(KEY_GENDER));
            mUserDetials.mUserHeight = cursor.getString(cursor.getColumnIndex(KEY_HEIGHT));
            mUserDetials.mUserActualWeight = cursor.getString(cursor.getColumnIndex(KEY_ACTUAL_WEIGHT));
            mUserDetials.mUserCurrentWeight = cursor.getString(cursor.getColumnIndex(KEY_CURRENT_WEIGHT));
            mUserDetials.mUserPreviousWeight = cursor.getString(cursor.getColumnIndex(KEY_PREVIOUS_WEIGHT));
            mUserDetials.mUserGoalWeight = cursor.getString(cursor.getColumnIndex(KEY_GOAL_WEIGHT));
            mUserDetials.mUserTargetDate = cursor.getString(cursor.getColumnIndex(KEY_TARGET_DATE));
            mUserDetials.mUserWeightLoss = cursor.getString(cursor.getColumnIndex(KEY_WEIGHT_LOSS));
        }

        return mUserDetials;
    }

    // code to get all contacts in a list view
    public UserDetials getUserDetails(String aUserName, String aPassword) {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS+ " WHERE " + KEY_NAME + " = '" + aUserName + "' AND " + KEY_PASS + " = '" + aPassword + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        UserDetials mUserDetials = new UserDetials();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            mUserDetials.mUserID = cursor.getInt(cursor.getColumnIndex(KEY_ID));
            mUserDetials.mUserName = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            mUserDetials.mUserPassword = cursor.getString(cursor.getColumnIndex(KEY_PASS));
            mUserDetials.mUserAge = cursor.getString(cursor.getColumnIndex(KEY_AGE));
            mUserDetials.mUserGender = cursor.getString(cursor.getColumnIndex(KEY_GENDER));
            mUserDetials.mUserHeight = cursor.getString(cursor.getColumnIndex(KEY_HEIGHT));
            mUserDetials.mUserActualWeight = cursor.getString(cursor.getColumnIndex(KEY_ACTUAL_WEIGHT));
            mUserDetials.mUserCurrentWeight = cursor.getString(cursor.getColumnIndex(KEY_CURRENT_WEIGHT));
            mUserDetials.mUserPreviousWeight = cursor.getString(cursor.getColumnIndex(KEY_PREVIOUS_WEIGHT));
            mUserDetials.mUserGoalWeight = cursor.getString(cursor.getColumnIndex(KEY_GOAL_WEIGHT));
            mUserDetials.mUserTargetDate = cursor.getString(cursor.getColumnIndex(KEY_TARGET_DATE));
            mUserDetials.mUserWeightLoss = cursor.getString(cursor.getColumnIndex(KEY_WEIGHT_LOSS));
        }

        return mUserDetials;
    }

    // code to update the single contact
    public void updateUserDetails(int mUserID, String mUserName, String mUserPassword, String mUserAge,
                                  String mUserGender, String mUserHeight,
                                  String mUserCurrentWeight, String mUserGoalWeight,
                                  String mUserTargetDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, mUserName);
        values.put(KEY_PASS, mUserPassword);
        values.put(KEY_AGE, mUserAge);
        values.put(KEY_GENDER, mUserGender);
        values.put(KEY_HEIGHT, mUserHeight);
        values.put(KEY_CURRENT_WEIGHT, mUserCurrentWeight);
        values.put(KEY_GOAL_WEIGHT, mUserGoalWeight);
        values.put(KEY_TARGET_DATE, mUserTargetDate);

        // updating row
        db.update(TABLE_USERS, values, KEY_ID + " = ?", new String[] { String.valueOf( mUserID) });
    }

    // code to update the single contact
    public void updateUserWeightDetails(int mUserID, String mUserCurrentWeight, String mUserPreviousWeight, String mUserWeightLoss) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

       values.put(KEY_CURRENT_WEIGHT, mUserCurrentWeight);
        values.put(KEY_PREVIOUS_WEIGHT, mUserPreviousWeight);
        values.put(KEY_WEIGHT_LOSS, mUserWeightLoss);

        // updating row
        db.update(TABLE_USERS, values, KEY_ID + " = ?", new String[] { String.valueOf( mUserID) });
    }


    // code to add the new contact
    public void addUserWeight(int mUserID, String aWeight, String aDate, String aPhoto) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, mUserID);
        values.put(KEY_WEIGHT, aWeight);
        values.put(KEY_DATE, aDate);
        values.put(KEY_PHOTO, aPhoto);

        // Inserting Row
        db.insert(TABLE_WEIGHT, null, values);

        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection


    }

    // code to get all contacts in a list view
    public  ArrayList<WeightHistoryData> getUserWeightHistory(int aUserID) {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WEIGHT + " WHERE " + KEY_USER_ID + " = " + aUserID;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<WeightHistoryData> mWeightHistoryData = new ArrayList<>();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {

                WeightHistoryData weightHistoryData = new WeightHistoryData();
                weightHistoryData.id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                weightHistoryData.weight = cursor.getString(cursor.getColumnIndex(KEY_WEIGHT));
                weightHistoryData.date =  cursor.getString(cursor.getColumnIndex(KEY_DATE));
                weightHistoryData.photo =  cursor.getString(cursor.getColumnIndex(KEY_PHOTO));

                // Adding contact to list
                mWeightHistoryData.add(weightHistoryData);

            } while (cursor.moveToNext());
        }

        return mWeightHistoryData;
    }

    // Deleting single contact
    public void deleteUserWeight(int aWeightID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WEIGHT, KEY_ID + " = ?", new String[] { String.valueOf(aWeightID) });
        db.close();
    }
}