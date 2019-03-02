package com.example.abc.myownmatrixapp;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.example.abc.myownmatrixapp.CustomAutoCompleteAdapter;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Location_getter extends AppCompatActivity {

    private final int REQUEST_CODE=100;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> mAreas;
    private Button mOK;
    private TextView mCurrentLocation;
    private AutoCompleteTextView mEnteredLocation;
    private PlacesTask mPlacesTask;
    private ParserTask mParserTask;
    private Location location; // location
    private double latitude; // latitude
    private double longitude; // longitude
    private LocationManager mLocationManager;//Location Manager
    private GoogleApiClient mGoogleApiClient;
    private AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Toast.makeText(Location_getter.this,
                            "selected place "
                                    + ((Place)adapterView.
                                    getItemAtPosition(i)).getPlaceText()
                            , Toast.LENGTH_SHORT).show();
                    //do something with the selection
                    searchScreen( ((Place)adapterView.
                            getItemAtPosition(i)).getPlaceText());
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_getter);

        //to initialize the ui element
        Init();


        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        mCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {

                LocationRequest request = new LocationRequest();

//Specify how often your app should request the device’s location//

                request.setInterval(3000);

//Get the most accurate location data available//

                request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(Location_getter.this);
                int permission = ContextCompat.checkSelfPermission(Location_getter.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

//If the app currently has access to the location permission...//

                if (permission == PackageManager.PERMISSION_GRANTED) {

//...then request location updates//

                    client.requestLocationUpdates(request, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {

//Get a reference to the database, so your app can perform read and write operations//
                            Location location = locationResult.getLastLocation();
                            if (location != null) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//Save the location data to the database//


                                Geocoder geocoder = new Geocoder(Location_getter.this, Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    Address obj = addresses.get(0);
                                    String add = obj.getAddressLine(0);
                                    add = add + "\n" + obj.getCountryName();
                                    add = add + "\n" + obj.getCountryCode();
                                    add = add + "\n" + obj.getAdminArea();
                                    add = add + "\n" + obj.getPostalCode();
                                    add = add + "\n" + obj.getSubAdminArea();
                                    add = add + "\n" + obj.getLocality();
                                    add = add + "\n" + obj.getSubThoroughfare();
                                    LatLng lat = new LatLng(obj.getLatitude(), obj.getLongitude());
                                    Intent intent = new Intent();
                                    intent.putExtra("location", obj.getAddressLine(0));
                                    intent.putExtra("coordinate", lat);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                    Log.v("IGA", "Address" + add);
                                    // Toast.makeText(this, "Address=>" + add,
                                    // Toast.LENGTH_SHORT).show();

                                    // TennisAppActivity.showDialog(add);
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();

                                }
                            }
                        }
                    }, null);

// Register the listener with the Location Manager to receive location updates


                }}});

//

    }

    /**
     * To initialize ui elements
     */
    private void Init(){
        mCurrentLocation=findViewById(R.id.current_location);
        mEnteredLocation=findViewById(R.id.get_location);
        CustomAutoCompleteAdapter adapter =  new CustomAutoCompleteAdapter(this);
        mEnteredLocation.setAdapter(adapter);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addApi(Places.GEO_DATA_API) .build();
        mGoogleApiClient.connect();
        mEnteredLocation.setOnItemClickListener(onItemClickListener);
//        mOK=findViewById(R.id.ok);

    }

    public void click (Place data){
//        Intent intent=new Intent();
//                intent.putExtra("location",data);
//                setResult(RESULT_OK,intent);
//                finish();
        Toast.makeText(this, data.getPlaceId(), Toast.LENGTH_SHORT).show();
        try{
            Places.GeoDataApi.getPlaceById(mGoogleApiClient, data.getPlaceId())
                    .setResultCallback(new ResultCallback<PlaceBuffer>() {
                        @Override
                        public void onResult(PlaceBuffer places) {
                            Toast.makeText(Location_getter.this, "hello", Toast.LENGTH_SHORT).show();
                            if (places.getStatus().isSuccess() && places.getCount() > 0) {
                                Toast.makeText(Location_getter.this, "hello", Toast.LENGTH_SHORT).show();

                                LatLng  latLngDrop = places.get(0).getLatLng();

                                Toast.makeText(Location_getter.this, "hello   "+latLngDrop.latitude+","+latLngDrop.longitude, Toast.LENGTH_SHORT).show();
                                Toast.makeText(Location_getter.this, places.get(0).getName(), Toast.LENGTH_SHORT).show();
                                Log.i("hell", "Place found: " + places.get(0).getName());

                                Log.v("latitude:", "" + latLngDrop.latitude);
                                Log.v("longitude:", "" + latLngDrop.longitude);
                                Intent intent= new Intent();
                                intent.setClass(Location_getter.this,post_load_map.class);
                                intent.putExtra("latLong",latLngDrop);
                                intent.putExtra("add",places.get(0).getName());

                                startActivityForResult(intent,REQUEST_CODE);

                            } else {
                                Log.e("hel", "Place not found");
                            }
                            places.release();
                        }
                    });
       }
        catch (Exception e){
            Log.e("NullPointer",e.getLocalizedMessage());
        }
    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Toast.makeText(this, data.getExtras().get("Lat").toString(), Toast.LENGTH_SHORT).show();
                Intent intent =new Intent();
                LatLng latLng=(LatLng)data.getExtras().get("Lat");
                String add=latLng.latitude+","+latLng.longitude;
                intent.putExtra("location",data.getExtras().get("add").toString());
                intent.putExtra("coordinate",add);
                setResult(RESULT_OK,intent);
                finish();
            }
        }
    }

    public void searchScreen(String name){
      mEnteredLocation.setText(name);
    }
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            Toast.makeText(this, strUrl, Toast.LENGTH_SHORT).show();
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("error url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyAGxlkR-d31zeC7wEDOcCCm_Du7y0xk0UM";

            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                Log.d("urlsdsds",e1.getMessage());
            }


//            // place type to be searched
//            String types = "";

            // Sensor enabled
            String sensor = "sensor=true";

            // Building the parameters to the web service
            String parameters = input+"&"+sensor+"&"+key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

            try{
                // Fetching the data from web service in background
                Log.d("urlsdsds","getting url");
                Toast.makeText(Location_getter.this, "hell", Toast.LENGTH_SHORT).show();
                data = downloadUrl(url);
            }catch(Exception e){
                Toast.makeText(Location_getter.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating mParserTask
            mParserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            mParserTask.execute(result);
        }
    }


    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("jsons",e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            mEnteredLocation.setAdapter(adapter);
        }
    }


}
