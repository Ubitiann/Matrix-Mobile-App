package com.example.abc.myownmatrixapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends AppCompatActivity {

    private EditText mPhoneNo,mEmail,mPassword,mConfirmPassword;
    private Button mSignUpButton;

    private String mStringEmail;
    private String mConfirmPasswordString;
    private String mStringPassword;
    private String mStringPhoneNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Init();

        mSignUpButton=findViewById(R.id.sign_up);

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkCredentials();
            }
        });


    }
    private void Init(){
        mPhoneNo=findViewById(R.id.phone_number);
        mEmail=findViewById(R.id.email);
        mPassword=findViewById(R.id.password);
        mConfirmPassword=findViewById(R.id.confirm_password);

    }

    private void checkCredentials(){
        mConfirmPasswordString=mConfirmPassword.getText().toString().trim();
        mStringPassword=mPassword.getText().toString().trim();
        mStringPhoneNo=mPhoneNo.getText().toString().trim();
        mStringEmail=mEmail.getText().toString().trim();
        boolean mAllSet=true;
        if (mStringEmail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mStringEmail).matches()) {
            mEmail.setError("Enter your Email!");
            mEmail.requestFocus();
            mAllSet=false;

        }
        if(mConfirmPasswordString.isEmpty()){
            mConfirmPassword.setError("Wrong password");
            mConfirmPassword.requestFocus();
            mAllSet=false;
        }
        if (!mConfirmPasswordString.equals(mStringPassword)){
            mConfirmPassword.setError("Password Mismatch");
            mConfirmPassword.requestFocus();
            mAllSet=false;
        }
        if(mConfirmPasswordString.length()<6){
            mConfirmPassword.setError("Password too short");
            mConfirmPassword.requestFocus();
            mAllSet=false;
        }
        if(mStringPassword.isEmpty()){
            mPassword.setError("Wrong password");
            mPassword.requestFocus();
            mAllSet=false;
        }
        if(mStringPassword.length()<6){
            mPassword.setError("Password too short");
            mPassword.requestFocus();
            mAllSet=false;
        }
        if(mStringPhoneNo.isEmpty()){
            mPhoneNo.setError("Enter Phone No");
            mPhoneNo.requestFocus();
            mAllSet=false;
        }


        if(mAllSet){
            Intent intent =new Intent(this,VerificationActivity.class);
            intent.putExtra("email",mStringEmail);
            intent.putExtra("password",mStringPassword);
            intent.putExtra("phoneNo",mStringPhoneNo);
            startActivity(intent);
            finish();
        }


    }
}
