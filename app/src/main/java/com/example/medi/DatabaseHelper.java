package com.example.medi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "medi.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "users";
    public static final String ROW_ID = "_id";
    public static final String ROW_NAME = "name";
    public static final String ROW_PHONE = "phone";
    public static final String ROW_EMAIL = "email";
    public static final String ROW_PASSWORD = "password";


    private SQLiteDatabase db;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" + ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ROW_NAME + " TEXT," + ROW_PHONE + " TEXT," + ROW_EMAIL + " TEXT," + ROW_PASSWORD + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Insert Data
    public void insertData(ContentValues values){
        db.insert(TABLE_NAME, null, values);
    }

    public boolean checkUser(String username, String password){
        String[] columns = {ROW_ID};
        SQLiteDatabase db = getReadableDatabase();
        String selection = ROW_NAME + "=?" + " and " + ROW_PASSWORD + "=?";
        String[] selectionArgs = {username,password};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count>0)
            return true;
        else
            return false;
    }

}

