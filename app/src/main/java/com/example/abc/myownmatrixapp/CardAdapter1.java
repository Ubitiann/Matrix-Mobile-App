package com.example.abc.myownmatrixapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter1 extends RecyclerView.Adapter<CardAdapter1.CardHolder1> {


    Context context;

    public CardAdapter1(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CardHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_lay,parent,false);
        return new CardHolder1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardHolder1 holder, int position) {

    }



    @Override
    public int getItemCount() {
        return 6;
    }


    public class CardHolder1 extends RecyclerView.ViewHolder{
        public CardHolder1(View itemview)
        {
            super(itemview);
        }
    }
}
