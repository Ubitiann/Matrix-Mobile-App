package com.example.abc.myownmatrixapp;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PostLoadActivity extends AppCompatActivity {
    
//    //for the origin aspect of places such as city and country
//    private PlaceAutocompleteFragment mOriginCountryComplete;
//    private String mCountryName="";
//    private PlaceAutocompleteFragment mOriginCityComplete;
//
//    //for the destination aspect of places such as city and country
//    private PlaceAutocompleteFragment mDestinationCountryComplete;
//    private String mDestinationCountryName="";
//    private PlaceAutocompleteFragment mDestinationCityComplete;

    final Calendar myCalendar = Calendar.getInstance();
    private TextView mOrigin,mDestination;
    private EditText mWeight;
    private TextView mDate,mTime;
    private String mFromPlaceCoordinate,mToPlaceCoordinate;
    private TextView mMaterialType,mTruckType;
    private ImageView mMaterialTypeImage,mTruckTypeImage;
    private TextView mLoadTypeFull,mLoadTypeAny,mLoadTypePart;
    private Button mPost;
    /**
     * this is the code for the destination activity result
     */
    private short LOCATION_RESULT_CODE_DESTINATION=500;
    /**
     * this is the code for the origin activity result
     */
    private short LOCATION_RESULT_CODE_ORIGIN=501;
    //for the changing of the color of three button
    private String clicked="";
    private short noOfButtonClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_post_load);
        Init();
        




        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
////                new DatePickerDialog(PostLoadActivity.this, date, myCalendar
////                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
////                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//                DatePickerDialog datepicker=new DatePickerDialog(PostLoadActivity.this, date, myCalendar
//                                             .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH)
//                                                ,myCalendar.get(Calendar.DAY_OF_MONTH));
//                datepicker.show();

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(PostLoadActivity.this,R.style.DialogTheme,date
                , myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                mDatePicker.setTitle("Select Date");
                mDatePicker.show();
            }
        });

        mTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(PostLoadActivity.this,R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        /**
         * this will open the getLocation activity and get the destination location
         */
        mDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivityForResult(new Intent(PostLoadActivity.this,Location_getter.class),LOCATION_RESULT_CODE_DESTINATION);
            }
        });

        /**
         * this will open the getLocation activity and get the origin location
         */
        mOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(PostLoadActivity.this,Location_getter.class),LOCATION_RESULT_CODE_ORIGIN);
            }
        });
        /**
         * for the selection of Load type from the three button placed
         */
        mLoadTypeAny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clicked.equals("Any")){
                    clicked="Any";

                    //for the checking of which button is pressed
                    checkWhichButtonIsPressed();
                    noOfButtonClicked=1;
                    mLoadTypeAny.setBackground(ContextCompat.getDrawable(getApplicationContext()
                                                                        ,R.drawable.selected_button));
                }
            }
        });

/**
 * for the selection of Load type from the three button placed
 */
        mLoadTypeFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clicked.equals("Full")){
                    clicked="Full";
                    //for the checking of which button is pressed
                    checkWhichButtonIsPressed();
                    noOfButtonClicked=2;
                    mLoadTypeFull.setBackground(ContextCompat.getDrawable(getApplicationContext()
                            ,R.drawable.selected_button));
                }
            }
        });


/**
 * for the selection of Load type from the three button placed
 */
        mLoadTypePart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!clicked.equals("Part")){
                    clicked="Part";
                    //for the checking of which button is pressed
                    checkWhichButtonIsPressed();
                    noOfButtonClicked=3;
                    mLoadTypePart.setBackground(ContextCompat.getDrawable(getApplicationContext()
                            ,R.drawable.selected_button));
                }
            }
        });

        mMaterialType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMaterial();
            }
        });


        mMaterialTypeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMaterial();
            }
        });


        mTruckType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTruck();
            }
        });


        mTruckTypeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTruck();
            }
        });

    }

    /**
     * to show the trucks in the list of the other screen
     */
    private void showTruck(){
        int ResultCode=101;

        Intent intent=new Intent(PostLoadActivity.this,TypeShow.class);
        intent.putExtra("type",1);
        startActivityForResult(intent,ResultCode);
    }


    /**
     * to show the material type  in the list of the other screen
     */
    private void showMaterial(){
        int ResultCode=100;
        Intent intent=new Intent(PostLoadActivity.this,TypeShow.class);
        intent.putExtra("type",2);
        startActivityForResult(intent,ResultCode);
    }


    /**
     * this will recieve the results of the activity that are being opened to take some information
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 100) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
               mMaterialType.setText(data.getExtras().getString("type"));
            }
        }else
        if (requestCode == 101) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                mTruckType.setText(data.getExtras().getString("type"));
            }
        }else
        if(requestCode==LOCATION_RESULT_CODE_DESTINATION){
            if (resultCode == RESULT_OK) {
                mDestination.setText(data.getExtras().getString("location"));
                mFromPlaceCoordinate=data.getExtras().getString("coordinate");
            }
        }
        else
        if(requestCode==LOCATION_RESULT_CODE_ORIGIN){
            if (resultCode == RESULT_OK) {
                mOrigin.setText(data.getExtras().getString("location"));
                mToPlaceCoordinate=data.getExtras().getString("coordinate");

            }
        }
    }

    /**
     * to check that which  button is pressed and change the background of the previous selected button
     */
    private void  checkWhichButtonIsPressed(){
        switch (noOfButtonClicked){
            case 1:
                mLoadTypeAny.setBackground(null);
                break;

            case 2:
                mLoadTypeFull.setBackground(null);
                break;

                case 3:
                mLoadTypePart.setBackground(null);
                break;


        }
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mDate.setText(sdf.format(myCalendar.getTime()));
    }

    /**
     * for the initialization of the autocomplete fragments and other components  on the screen
     */
    private void Init(){
        
        
        mPost=findViewById(R.id.post_add);
        
        mWeight=findViewById(R.id.weight);
        
        mDate=findViewById(R.id.date_picker);
        
        mTime=findViewById(R.id.time_picker);
        
        mLoadTypeAny=findViewById(R.id.any);
        
        mLoadTypeFull=findViewById(R.id.full);


        mLoadTypePart=findViewById(R.id.part);
        
        mMaterialType=findViewById(R.id.material_type);

        mTruckType=findViewById(R.id.truck_type);

        mMaterialTypeImage=findViewById(R.id.material_type_img);

        mTruckTypeImage=findViewById(R.id.truck_type_image);

        mOrigin=findViewById(R.id.origin);
        mDestination=findViewById(R.id.destination);
        //
//
//        /**
//         * for the picking of origin country
//         */
//        mOriginCountryComplete = (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.origin);
//
//        mOriginCountryComplete.setHint("Country");
//
//        AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder().setCountry("US|PK").build();
//
//
//        mOriginCountryComplete.setFilter(autocompleteFilter);
////        ((View)findViewById(R.id.place_autocomplete_search_button)).setVisibility(View.GONE);
//
//
//        mOriginCountryComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                String place1=place.getName().toString();
//                mOriginCountryComplete.setText(place1);
//                mCountryName=place1;
//                // TODO: Get info about the selected place.
//                //  Log.i(TAG, "Place: " + place.getName());
//            }
//
//            @Override
//            public void onError(Status status) {
//
//
//                // TODO: Handle the error.
//                // Log.i(TAG, "An error occurred: " + status);
//            }
//        });
//
//        /**
//         * for the picking of origin country's city
//         */
//        mOriginCityComplete = (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.origin_city);
//
//
//        mOriginCityComplete.setHint("City");
//
//        AutocompleteFilter autocompleteFilter1 = new AutocompleteFilter.Builder().setCountry("US|PK").build();
//
//        mOriginCityComplete.setFilter(autocompleteFilter1);
//
//        mOriginCityComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//
//                String place1=place.getName().toString();
//                mOriginCityComplete.setText(place1);
//
//                // TODO: Get info about the selected place.
//                //  Log.i(TAG, "Place: " + place.getName());
//            }
//
//            @Override
//            public void onError(Status status) {
//
//
//                // TODO: Handle the error.
//                // Log.i(TAG, "An error occurred: " + status);
//            }
//        });
//
//
//        /**
//         * for the picking of destination country
//         */
//        mDestinationCountryComplete = (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.destination);
//
//        mDestinationCountryComplete.setHint("Country");
//
//        AutocompleteFilter autocompleteFilter3 = new AutocompleteFilter.Builder().build();
//
//
//        mDestinationCountryComplete.setFilter(autocompleteFilter);
////        ((View)findViewById(R.id.place_autocomplete_search_button)).setVisibility(View.GONE);
//
//
//        mDestinationCountryComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                String place1=place.getName().toString();
//                mOriginCountryComplete.setText(place1);
//                mDestinationCountryName=place1;
//
//                // TODO: Get info about the selected place.
//                //  Log.i(TAG, "Place: " + place.getName());
//            }
//
//            @Override
//            public void onError(Status status) {
//
//
//                // TODO: Handle the error.
//                // Log.i(TAG, "An error occurred: " + status);
//            }
//        });
//
//        /**
//         * for the picking of origin country's city
//         */
//        mDestinationCityComplete = (PlaceAutocompleteFragment)
//                getFragmentManager().findFragmentById(R.id.desitnation_city);
//
//
//
//        mDestinationCityComplete.setHint("City");
//
//
//        AutocompleteFilter autocompleteFilter4 = new AutocompleteFilter.Builder().setCountry(mDestinationCountryName).build();
//
//
//        mDestinationCityComplete.setFilter(autocompleteFilter1);
//
//
//        mDestinationCityComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//
//                String place1=place.getName().toString();
//                mOriginCityComplete.setText(place1);
//
//                // TODO: Get info about the selected place.
//                //  Log.i(TAG, "Place: " + place.getName());
//            }
//
//            @Override
//            public void onError(Status status) {
//
//
//                // TODO: Handle the error.
//                // Log.i(TAG, "An error occurred: " + status);
//            }
//        });
    }
}

