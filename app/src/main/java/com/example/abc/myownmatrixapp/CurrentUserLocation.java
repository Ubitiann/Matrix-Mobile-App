package com.example.abc.myownmatrixapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import dmax.dialog.SpotsDialog;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CurrentUserLocation extends AppCompatActivity implements OnMapReadyCallback {


    Marker marker;
    private GoogleMap mMap;
    long num;
    AutoCompleteTextView autoCompleteTextView;
    /**
     * for the markers
     */

    private ArrayList<String> KEYS=new ArrayList<>();
    private HashMap<String, Marker> mMarkers = new HashMap<>();
    List<TruckModel> truck=new ArrayList<>();
    private boolean mAlreadyStartedService = false;
    AllTruckAdapter adapter;
    android.app.AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_user_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        dialog= new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();
        dialog.show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findTruck();
        autoCompleteTextView=(AutoCompleteTextView)findViewById(R.id.auto_map_view);

        autoCompleteTextView.setThreshold(1);


        autoCompleteTextView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TruckModel truckModel=(TruckModel)adapterView.getItemAtPosition(i);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(truckModel.getLatLng(), 18f));

            }
        } );


        Button button = (Button) findViewById(R.id.settingsss);
        Button button1 = (Button) findViewById(R.id.logout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CurrentUserLocation.this, MainActivity.class);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


                startActivity(intent);


            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.documnt:

                        Intent intent = new Intent(CurrentUserLocation.this, details.class);
                        startActivity(intent);
                        break;

                    case R.id.myload:
                        Intent intent1 = new Intent(CurrentUserLocation.this, PostLoadActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.help:
                        Intent intent3 = new Intent(CurrentUserLocation.this, CurrentUserLocation.class);
                        startActivity(intent3);
                        break;
                    case R.id.paymnt:
                        Intent intent4 = new Intent(CurrentUserLocation.this, CurrentLocationn.class);
                        startActivity(intent4);
                        break;

                    case R.id.search:
                        Intent intent5 = new Intent(CurrentUserLocation.this, LoadDetail
                                .class);
                        startActivity(intent5);
                    default:
                        return false;
                }
                return  false;
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        try{
            mMap.setMaxZoomPreference(30);
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setTrafficEnabled(true);
            mMap.setBuildingsEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);

            subscribeToUpdates();


        }catch(NullPointerException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();


        }
//        if(mMap!=null) {
//            DatabaseReference usercordinates = FirebaseDatabase.getInstance().getReference().child("Users");
//            usercordinates.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    int size = (int) dataSnapshot.getChildrenCount();
//                    Marker[] allMarkers = new Marker[size];
////                    mMap.clear();
//                    int i = 0;
//                    for (DataSnapshot s : dataSnapshot.getChildren()) {
////                    UserModel cordinatesModel = new UserModel();
//
//
//                        try {
//                            UserModel cordinatesModel = s.getValue(UserModel.class);
//
//                            //
////                            cordinatesModel.setLatitude(s.getValue(UserModel.class).getLatitude());
////                            cordinatesModel.setLongitude(s.getValue(UserModel.class).getLongitude());
//
//
//                            //convert string latitude to double
//                            Double latitude1 = cordinatesModel.getLatitude();
//                            Double longitude1 = cordinatesModel.getLongitude();
//
//
//                            LatLng latLng = new LatLng(latitude1, longitude1);
////                            mMap.clear();
//                            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//                            mMap.setTrafficEnabled(true);
//                            mMap.setBuildingsEnabled(true);
//                            mMap.getUiSettings().setZoomControlsEnabled(true);
//
//                            //lets add updated marker
//                            allMarkers[i] = mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.firing)));
//                            mMap.setMaxZoomPreference(20);
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 21.0f));
//                            i++;
//                        } catch (Exception ex) {
//                            Toast.makeText(CurrentUserLocation.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//
//        }

    }
    private void subscribeToUpdates() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                setMarker(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d(CurrentUserLocation.class.getName(), "Failed to read value.", error.toException());
            }
        });
    }

    private void findTruck()
    {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                truck.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UserModel latLng = ds.getValue( UserModel.class );

                    String userid = ds.getKey();
                    TruckModel tr=new TruckModel(  );
                    tr.setmUserId( userid );
                    tr.setmLatitude( latLng.getLatitude() );
                    tr.setmLongitude( latLng.getLongitude() );
                    tr.formLatlng();

                    Log.e( "data",  tr.getLatLng().toString());
                    truck.add( tr );

                }

                adapter = new AllTruckAdapter(CurrentUserLocation.this,R.layout.map_search_item,truck);
                autoCompleteTextView.setAdapter(adapter);

                dialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        ref.addValueEventListener( valueEventListener );


    }

    private void setMarker(DataSnapshot dataSnapshot) {
        // When a location update is received, put or update
        // its value in mMarkers, which contains all the markers
        // for locations received, so that we can build the
        // boundaries required to show them all on the map at once
        String key = dataSnapshot.getKey();
        UserModel value = dataSnapshot.getValue(UserModel.class);
        double lat = value.getLatitude();
        double lng = value.getLongitude();
        LatLng location = new LatLng(lat, lng);
        if (!mMarkers.containsKey(key)) {

            mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(key).position(location).icon(BitmapDescriptorFactory.fromResource(R.drawable.firing))));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18f));
        } else {
            mMarkers.get(key).setPosition(location);
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(17), 2000, null);
        }
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        for (Marker marker : mMarkers.values()) {
//            builder.include(marker.getPosition());
//        }
//        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.search) {
            // Handle the camera action
        } else if (id == R.id.post) {

        } else if (id == R.id.myload) {

        } else if (id == R.id.paymnt) {

        } else if (id == R.id.notif) {

        } else if (id == R.id.call) {

        }
        else if (id==R.id.documnt)
        {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}