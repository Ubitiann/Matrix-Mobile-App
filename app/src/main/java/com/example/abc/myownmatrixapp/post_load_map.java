package com.example.abc.myownmatrixapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class post_load_map extends FragmentActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private Marker mMarker;
    private GoogleMap mMap;
    private Geocoder mGeocoder;
    private String mAddress;
    private Button mSet;
    private LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_load_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
         mGeocoder = new Geocoder(post_load_map.this, Locale.getDefault());
        mSet=findViewById(R.id.ok);
        mSet.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent =new Intent();
                intent.putExtra("Lat",latLng);
                intent.putExtra("add",mAddress);
                setResult(RESULT_OK,intent);
                finish();
            }
        });



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
            mMap=googleMap;
   latLng= (LatLng) getIntent().getExtras().get("latLong");
       mMap.setMyLocationEnabled(true);
   mAddress=getIntent().getExtras().get("add").toString();
        mMarker=mMap.addMarker(new MarkerOptions().position(latLng).title(getIntent().getExtras().get("add").toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLngs) {

                try{
                    List<Address> addresses = mGeocoder.getFromLocation(latLng.latitude,latLng.longitude, 1);
                    Address obj = addresses.get(0);
                    mAddress = obj.getAddressLine(0);
                    latLng=latLngs;
                    if (mMarker != null) {
                        mMarker.remove();
                        mMarker=mMap.addMarker(new MarkerOptions().position(latLngs).title(mAddress).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs, 18.0f));}
                    else{
                        mMarker=mMap.addMarker(new MarkerOptions().position(latLngs).title(mAddress).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    }
                }
                catch (Exception e){
                    Log.e("post load map",e.getLocalizedMessage());
                }
            }

        });

    }
}
