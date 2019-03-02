package com.example.abc.myownmatrixapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static  final  String DataBaseName="register.db";
    public static final String TableName="register";
    public static final String Col_1="ID";
    public static final String Col_2="Email";
    public static final String Col_3="Password";
    public static final String Col_4="LongLat";



    public DatabaseHelper(Context context) {
        super(context, DataBaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TableName + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,Email TEXT,Password TEXT,LongLat TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS " +TableName); //Drop older table if exists

        onCreate(db);

    }
}
