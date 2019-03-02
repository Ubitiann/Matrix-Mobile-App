package com.example.abc.myownmatrixapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ReceiptListShower extends AppCompatActivity {

    private RecyclerView RC;
    private MyAdapterForReceipt AD;
    private List<RecieptItems> Rt;
    private String a, b, d;
    private byte[] imageArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_list_shower);
        showAll();
    }

    public void showAll()
    {
        RC = (RecyclerView) findViewById(R.id.RecyclerView);
        RC.setHasFixedSize(true);
        RC.setLayoutManager(new LinearLayoutManager(this));
        Rt = new ArrayList<>();
        SQLiteDatabase db = openOrCreateDatabase("studentsDatabase", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS studentsDatabase(date VARCHAR,time VARCHAR,comment VARCHAR,image BLOB);");

        Cursor c = db.rawQuery("SELECT * FROM studentsDatabase", null);
        if (c.getCount() == 0) {

            showMessage("Error", "No record found..!");
        }
        while (c.moveToNext()) {

            a = c.getString(0);
            b = c.getString(1);
            d = c.getString(2);
            imageArray = c.getBlob(3);
            RecieptItems rlist = new RecieptItems(a,b,d,imageArray);
            Rt.add(rlist);
        }
        AD = new MyAdapterForReceipt(Rt, getApplicationContext());
        RC.setAdapter(AD);
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    }

