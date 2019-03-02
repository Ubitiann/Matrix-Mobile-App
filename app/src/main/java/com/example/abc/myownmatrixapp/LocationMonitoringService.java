package com.example.abc.myownmatrixapp;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class LocationMonitoringService extends Service {

    private static final String TAG = LocationMonitoringService.class.getSimpleName();



    public static final String ACTION_LOCATION_BROADCAST = LocationMonitoringService.class.getName() + "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requestLocationUpdates();

        //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.
        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();

//Specify how often your app should request the device’s location//

        request.setInterval(3000);

//Get the most accurate location data available//

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

//If the app currently has access to the location permission...//

        if (permission == PackageManager.PERMISSION_GRANTED) {

//...then request location updates//

            client.requestLocationUpdates(request, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {




                        Location location = locationResult.getLastLocation();
                        if (location != null) {
                            Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
                            intent.putExtra(EXTRA_LATITUDE, location.getLatitude());
                            intent.putExtra(EXTRA_LONGITUDE, location.getLongitude());
                            LocalBroadcastManager.getInstance(LocationMonitoringService.this).sendBroadcast(intent);


                        }

                    }

            }, null);
        }
    }
}