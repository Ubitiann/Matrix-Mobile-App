package com.example.abc.myownmatrixapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class content_load_detail_page  extends AppCompatActivity {

    RecyclerView recyclerView;
    CardAdapter cardAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_load_detail_page);
        recyclerView=(RecyclerView)findViewById(R.id.recycleview);
        cardAdapter=new CardAdapter(content_load_detail_page.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(content_load_detail_page.this));
        recyclerView.setAdapter(cardAdapter);

        cardAdapter.notifyDataSetChanged();
    }

}
