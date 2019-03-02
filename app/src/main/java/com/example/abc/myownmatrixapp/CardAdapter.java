package com.example.abc.myownmatrixapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardHolder> {

    Context context;

    public CardAdapter(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public CardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card,parent,false);
        return new CardHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class CardHolder extends RecyclerView.ViewHolder{
        public CardHolder(View itemview)
        {
            super(itemview);
        }
    }
}
