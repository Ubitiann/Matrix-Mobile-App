package com.example.abc.myownmatrixapp;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AllTruckAdapter extends ArrayAdapter<TruckModel> {
    List<TruckModel> truck,tempItems;
    List<TruckModel> suggestions;
    Context context;
    int mResource;
    ArrayList<TruckModel> tempValues;
    public AllTruckAdapter(@NonNull Context context, int mResource, @NonNull List<TruckModel> truck) {
        super( context, mResource, truck );
        this.context=context;
        this.truck=truck;
        this.mResource=mResource;
        suggestions=new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        try {
            if (convertView == null) {
                view= LayoutInflater.from(parent.getContext()).inflate(R.layout.map_search_item,parent,false);
            }

            TextView truckID = (TextView) view.findViewById(R.id.truck_Id);
            truckID.setText(suggestions.get(position).getmUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Nullable
    @Override
    public TruckModel getItem(int position) {
        return suggestions.get(position);
    }
    @Override
    public int getCount() {
        if(suggestions!=null)
            return suggestions.size();
        else
            return 0;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @NonNull
    @Override
    public Filter getFilter() {
        return truckFilter;
    }
    private Filter truckFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            TruckModel truckModel = (TruckModel) resultValue;
            return truckModel.getmUserId();
        }
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            if (charSequence != null) {
                suggestions.clear();

                for (TruckModel truckModel:truck) {


                    if (truckModel.getmUserId().toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                        suggestions.add(truckModel);
                      //  Toast.makeText(context, truckModel.getmUserId(), Toast.LENGTH_SHORT).show();
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            tempValues = (ArrayList<TruckModel>) filterResults.values;
            if (filterResults != null && filterResults.count > 0) {
                clear();
                for (TruckModel truckObj : tempValues) {
                    add(truckObj);
                    notifyDataSetChanged();
                }
            } else {
                clear();
                notifyDataSetChanged();
            }
        }
    };
}