package com.example.abc.myownmatrixapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class MyAdapterForReceipt extends RecyclerView.Adapter<MyAdapterForReceipt.Viewholder> {

    SQLiteDatabase db;
    List<RecieptItems> ritem;
    Context context;

    public MyAdapterForReceipt(List<RecieptItems> ritem, Context context) {
        this.ritem = ritem;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.receipt,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        final RecieptItems rItemHold = ritem.get(position);
        holder.date.setText(rItemHold.getDate());
        holder.time.setText(rItemHold.getTime());
        holder.comment.setText(rItemHold.getComment());
        Bitmap bmp = BitmapFactory.decodeByteArray(rItemHold.getImage(),0,rItemHold.getImage().length);
        holder.imageViewMyAdapter.setImageBitmap(bmp);
    }



    @Override
    public int getItemCount() {
        return ritem.size();

    }

    public class Viewholder extends RecyclerView.ViewHolder{
        public TextView date;
        public TextView time;
        public TextView comment;
        public ImageView imageViewMyAdapter;

        public Viewholder( View itemView) {
            super(itemView);
            date   = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            comment=(TextView) itemView.findViewById(R.id.comment);
            imageViewMyAdapter=(ImageView) itemView.findViewById(R.id.imageView3);
        }
    }
}

