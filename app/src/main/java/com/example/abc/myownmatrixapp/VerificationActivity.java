package com.example.abc.myownmatrixapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import dmax.dialog.SpotsDialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {

        private String mEmail,mPhone,mPassword;
        private Button mAccept;
        private EditText mCode;
    private String verificationId;
    android.app.AlertDialog dialog;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        //to get the credentials that are given on the signup screen
        Bundle intent=getIntent().getExtras();
        mEmail=intent.getString("email");
        mPhone=intent.getString("phoneNo");
        mPassword=intent.getString("password");
        mAuth = FirebaseAuth.getInstance();
        //initializeing the screen elements
        mAccept=findViewById(R.id.accept);
        mCode=findViewById(R.id.code);

        //to have the code for the verified phone no
        sendVerificationCode(mPhone);

            dialog= new SpotsDialog.Builder()
                    .setContext(this)
                    .setTheme(R.style.Custom)
                    .build();

        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = mCode.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {

                    mCode.setError("Enter code...");
                    mCode.requestFocus();
                    return;
                }
                dialog= new SpotsDialog.Builder()
                        .setContext(VerificationActivity.this)
                        .setTheme(R.style.Custom1)
                        .build();
                dialog.show();
                verifyCode(code);
            }
        });



    }

    /**
     * this will intitate if the user add code by hands
     * @param code
     */
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    /**
     * this will be used to sign in by the
     * @param credential
     */
    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                mAuth.createUserWithEmailAndPassword(mEmail,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                dialog.dismiss();
                                Intent intent = new Intent(VerificationActivity.this, MainPageActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }else{
                                Toast.makeText(VerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }
                    }
                });

                        } else {
                            Toast.makeText(VerificationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendVerificationCode(String number) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallBack
        );

    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Toast.makeText(VerificationActivity.this, "code is sent", Toast.LENGTH_SHORT).show();
            verificationId = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            Toast.makeText(VerificationActivity.this, phoneAuthCredential.getSmsCode(), Toast.LENGTH_SHORT).show();

            if (code != null) {
                mCode.setText(code);
                Toast.makeText(VerificationActivity.this, "hogai verification", Toast.LENGTH_SHORT).show();
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerificationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };



    @Override
    public void onBackPressed() {
        super.onBackPressed();
            startActivity(new Intent(this,SignUp.class));
            finish();
    }
}
