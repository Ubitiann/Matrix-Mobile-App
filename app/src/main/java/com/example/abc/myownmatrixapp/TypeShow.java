package com.example.abc.myownmatrixapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TypeShow extends AppCompatActivity {

    private Bundle mIntent;
    private int mType;
    private DatabaseReference databaseReference;


    private ListView mCandidatesListView;
    private ArrayAdapter<String> mCandidateAdapter;
    private List<String> candidates;
    private ChildEventListener mChildEventListener;


        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_show);

        mCandidatesListView = findViewById(R.id.list);
        mIntent=getIntent().getExtras();

        mType=mIntent.getInt("type");

        switch(mType){
            case 2:
                databaseReference=FirebaseDatabase.getInstance().getReference().child("material");
                break;

            case 1:
                databaseReference=FirebaseDatabase.getInstance().getReference().child("truck_type");
                break;
        }


        candidates = new ArrayList<String>();
        List<String> candidatesNames = new ArrayList<String>();
        mCandidateAdapter = new ArrayAdapter<String>(TypeShow.this,R.layout.type_item,R.id.list_item_textView,candidates);

        attachListener();





    }

    /**
     * to inflate the list from the firebase
     */
    private void attachListener() {
        mCandidatesListView.setAdapter(mCandidateAdapter);




        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    if (dataSnapshot == null) {
                        return;
                    }

                    String name=dataSnapshot.getValue().toString();
                    candidates.add(name);


                        mCandidateAdapter.notifyDataSetChanged();
                    mCandidatesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            Toast.makeText(TypeShow.this, candidates.get(i), Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent();
                            intent.putExtra("type",candidates.get(i));
                            setResult(RESULT_OK,intent);
                            finish();

                        }
                    });

                }
                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            };
        }
        databaseReference.addChildEventListener(mChildEventListener);

    }
}
