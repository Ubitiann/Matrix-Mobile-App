package com.example.abc.myownmatrixapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class CurrentLocationn extends FragmentActivity implements OnMapReadyCallback,View.OnClickListener
{

    private GoogleMap mMap;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    Marker marker;
    LatLng latLng;
    String result;
    FloatingActionButton terrain;
    FloatingActionButton hybrid;
    FloatingActionButton satellite;
    FloatingActionButton Roadmap;
    android.location.LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_locationn);
        // Obtain the SupportMapFragment and get notified when the map is ready to be
        //    used.
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

         terrain=(FloatingActionButton) findViewById(R.id.fab_action1);
         hybrid=(FloatingActionButton) findViewById(R.id.fab_action3);
         satellite=(FloatingActionButton) findViewById(R.id.fab_action2);
        Roadmap=(FloatingActionButton) findViewById(R.id.fab_action4);



        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }

        locationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                //get the location name from latitude and longitude
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List<Address> addresses =
                            geocoder.getFromLocation(latitude, longitude, 1);
                    if(addresses != null && addresses.size()>0) {
                        result = addresses.get( 0 ).getAddressLine( 0 );
                    }
                    latLng = new LatLng(latitude, longitude);
                    if (marker != null) {
                        marker.remove();
                        marker = mMap.addMarker(new MarkerOptions().position
                                (latLng).title(result).icon(BitmapDescriptorFactory.defaultMarker
                                (BitmapDescriptorFactory.HUE_GREEN)));
                        mMap.setMaxZoomPreference(20);
                        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,
                        //  12.0f));
                    } else {
                        marker = mMap.addMarker(new MarkerOptions().position
                                (latLng).title(result).icon(BitmapDescriptorFactory.defaultMarker
                                (BitmapDescriptorFactory.HUE_GREEN)));
                        mMap.setMaxZoomPreference(20);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,
                                12.0f));
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50000,
                0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50000, 0,
                locationListener);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be
     * prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the
     * user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMaxZoomPreference(30);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setTrafficEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        terrain.setOnClickListener(this);
        hybrid.setOnClickListener(this);
        satellite.setOnClickListener(this);
        Roadmap.setOnClickListener(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        mMap.setMyLocationEnabled(true);

//        mMap.setOnMyLocationButtonClickListener(new
//                                                        GoogleMap.OnMyLocationButtonClickListener() {
//                                                            @Override
//                                                            public boolean onMyLocationButtonClick() {
//                                                                if (marker != null) {
//                                                                    marker.remove();
//                                                                    marker = mMap.addMarker(new MarkerOptions().position
//                                                                            (latLng).title(result).icon(BitmapDescriptorFactory.defaultMarker
//                                                                            (BitmapDescriptorFactory.HUE_GREEN)));
//                                                                    mMap.setMaxZoomPreference(50);
//                                                                    // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,
//                                                                    //  12.0f));
//                                                                } else {
//                                                                    marker = mMap.addMarker(new MarkerOptions().position
//                                                                            (latLng).title(result).icon(BitmapDescriptorFactory.defaultMarker
//                                                                            (BitmapDescriptorFactory.HUE_GREEN)));
//                                                                    mMap.setMaxZoomPreference(50);
//                                                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,
//                                                                            12.0f));
//                                                                }
//                                                                return false;
//                                                            }
//                                                        });
//        mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
//            @Override
//            public void onMyLocationClick(@NonNull Location location) {
//                if (marker != null) {
//                    marker.remove();
//                    marker = mMap.addMarker(new MarkerOptions().position
//                            (latLng).title(result).icon(BitmapDescriptorFactory.defaultMarker
//                            (BitmapDescriptorFactory.HUE_GREEN)));
//                    mMap.setMaxZoomPreference(30);
//                    // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,
//                    //  12.0f));
//                } else {
//                    marker = mMap.addMarker(new MarkerOptions().position
//                            (latLng).title(result).icon(BitmapDescriptorFactory.defaultMarker
//                            (BitmapDescriptorFactory.HUE_GREEN)));
//                    mMap.setMaxZoomPreference(30);
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,
//                            12.0f));
//                }
//            }
//        });
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in
        //// Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates((android.location.LocationListener)
                locationListener);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case  R.id.fab_action1:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case  R.id.fab_action3:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case  R.id.fab_action2:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case  R.id.fab_action4:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            default:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;

        }
    }
}
