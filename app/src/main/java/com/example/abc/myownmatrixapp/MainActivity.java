package com.example.abc.myownmatrixapp;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import dmax.dialog.SpotsDialog;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    TextInputEditText textInputEditText, textInputEditText1;
    FirebaseAuth firebaseAuth;
    String email, password;
    android.app.AlertDialog dialog;
    Intent intent;
    private static final int PERMISSIONS_REQUEST = 100;
    private static final String TAG = TrackingService.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dialog= new SpotsDialog.Builder()
                .setContext(this)
                .setTheme(R.style.Custom)
                .build();




        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            intent = new Intent(MainActivity.this, TrackingService.class);
            Bundle b = new Bundle();
            b.putString("Username", email);
            b.putString("password", password);
            intent.putExtras(b);
            startService(intent);
            startActivity(new Intent(this,MainPageActivity.class));
            finish();
        }
        Button button = (Button) findViewById(R.id.sign_up);
        Button button1 = (Button) findViewById(R.id.log_in);
        Button button2 = (Button) findViewById(R.id.recover);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SignUp.class);
                startActivity(intent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();

                LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                }

                int permission = ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                if (permission == PackageManager.PERMISSION_GRANTED) {
                    startTrackerService();
                } else {

//If the app doesn’t currently have access to the user’s location, then request access//

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST);
                }


            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CurrentLocationn.class);
                startActivity(intent);

            }
        });



}

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {

//If the permission has been granted...//

        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            //...then start the GPS tracking service//

            startTrackerService();
        } else {

//If the user denies the permission request, then display a toast with some more information//

            Toast.makeText(this, "Please enable location services to allow GPS tracking", Toast.LENGTH_SHORT).show();
        }
    }

//Start the TrackerService//

    private void startTrackerService() {
TextInputLayout textInputLayout=findViewById(R.id.textinputlayout);
        TextInputLayout textInputLayout1=findViewById(R.id.textinputlayout);
         textInputEditText=(TextInputEditText)findViewById(R.id.email);
        email=textInputEditText.getText().toString().trim();
        textInputEditText1=(TextInputEditText)findViewById(R.id.password);
        password=textInputEditText1.getText().toString().trim();
        if(email.equals(null) ||email.equals(""))
        {
            textInputEditText.getText().clear();
            textInputEditText1.getText().clear();
            dialog.dismiss();
            Drawable d= getResources().getDrawable(R.drawable.error);
        d.setBounds(0, 0,
                d.getIntrinsicWidth(), d.getIntrinsicHeight());

        textInputEditText.setError("email required",d);

        Toast.makeText(this,"Email can't be Empty",Toast.LENGTH_SHORT).show();
    }
       else
        if(password.equals(null) || password.equals(""))
        { dialog.dismiss();
            textInputEditText.getText().clear();
            textInputEditText1.getText().clear();
            Drawable d= getResources().getDrawable(R.drawable.error);
            d.setBounds(0, 0,
                    d.getIntrinsicWidth(), d.getIntrinsicHeight());

            textInputEditText1.setError("password required",d);
            textInputEditText1.requestFocus();

            Toast.makeText(this,"Password can't be Empty",Toast.LENGTH_SHORT).show();

        }
        else
            if(password.length()<6)
            {
                dialog.dismiss();
                textInputEditText.getText().clear();
                textInputEditText1.getText().clear();
                Drawable d= getResources().getDrawable(R.drawable.error);
                d.setBounds(0, 0,
                        d.getIntrinsicWidth(), d.getIntrinsicHeight());

                textInputEditText1.setError("password length can't be less than 6",d);
                textInputEditText1.requestFocus();

                Toast.makeText(this,"password length can't be less than 6",Toast.LENGTH_SHORT).show();
            }
        else
            if(!isEmailValid(email))
            {
                dialog.dismiss();
                textInputEditText.getText().clear();
                textInputEditText1.getText().clear();
                Drawable d= getResources().getDrawable(R.drawable.error);
                d.setBounds(0, 0,
                        d.getIntrinsicWidth(), d.getIntrinsicHeight());

                textInputEditText.setError("email invalid",d);
               textInputEditText.requestFocus();
                Toast.makeText(this,"Enter Valid email",Toast.LENGTH_SHORT).show();
            }
        else {

                loginToFirebase();

        }

    }

    public void onBackPressed() {
        // TODO Auto-generated method stub

        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
        return;
    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private void loginToFirebase() {

//Authenticate with Firebase, using the email and password we created earlier//



//Call OnCompleteListener if the user is signed in successfully//

        Log.e("username" , email+" "+ password);

        firebaseAuth.getInstance().signInWithEmailAndPassword(
                email.toLowerCase().trim(), password.trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {

//If the user has been authenticated...//

                if (task.isSuccessful()) {

//...then call requestLocationUpdates//
dialog.dismiss();
                     intent = new Intent(MainActivity.this, TrackingService.class);
                    Bundle b = new Bundle();
                    b.putString("Username", email);
                    b.putString("password", password);
                    intent.putExtras(b);
                    startService(intent);
                    Intent intent1=new Intent(MainActivity.this,MainPageActivity.class);

                    startActivity(intent1);

finish();
                } else {

//If sign in fails, then log the error//

dialog.dismiss();
                    textInputEditText1.getText().clear();
                    Drawable d= getResources().getDrawable(R.drawable.error);
                    d.setBounds(0, 0,
                            d.getIntrinsicWidth(), d.getIntrinsicHeight());

                    textInputEditText1.setError("wrong password",d);
                    textInputEditText1.requestFocus();


                    Toast.makeText(MainActivity.this,
                            "Password Incorrect", Toast.LENGTH_LONG).show();
                    Log.d(TAG, "Password Incorrect");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {



                Log.d(TAG,e.getMessage().toString());




            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
