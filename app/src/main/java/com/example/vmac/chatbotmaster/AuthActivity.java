package com.example.vmac.chatbotmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AuthActivity extends SignupActivity {

    private static final String TAG = "AuthActivity";
    EditText editPhone, editCode;
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    private DocumentReference docref;
    private String codeSent;
    private ImageButton backbutton;
    private boolean userexists = false;
    private String phone;

    private boolean verifyInProgress = false,saveState= false, restoreState=false;
    private boolean CodeSent = false;

    //timer for otp
    public int counter;
    public boolean countSet;

//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        if(verifyInProgress){
//            saveState = true;
//        }
//        else{
//            saveState = false;
//        }
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//
//        if(saveState){
//            restoreState = true;
//        }
//        else{
//            restoreState = false;
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if(restoreState){
//            sendVerificationCode();
//        }
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        database = FirebaseFirestore.getInstance();

        editPhone = (EditText) findViewById(R.id.phoneNo);
        editCode = (EditText) findViewById(R.id.verifypass);

        mAuth = FirebaseAuth.getInstance();

        //timer value set
        countSet = false;
        counter = 60;

        findViewById(R.id.sendBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!countSet)
                    sendVerificationCode();
                else{
                    Toast.makeText(AuthActivity.this, "Please wait "+counter+" seconds before requesting for another OTP.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.verifyBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifySignupCode();
            }
        });

        backbutton = (ImageButton) findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

    }

    private void sendVerificationCode(){

        phone = editPhone.getText().toString().trim();

        if(phone.isEmpty()){
            editPhone.setError("Phone Number is required.");
            editPhone.requestFocus();
            return;
        }
        if(phone.length() != 10){
            editPhone.setError("Please enter a valid number.");
            editPhone.requestFocus();
            return;
        }

        docref = database.document("users/"+phoneno);

        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    Toast.makeText(AuthActivity.this, "User already exists with this number. Please login.",
                            Toast.LENGTH_LONG).show();
                    userexists = true;
                    finish();
                }
                else{
                    userexists = false;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AuthActivity.this, "Document fetch failed. Check your internet connection",
                        Toast.LENGTH_LONG).show();
            }
        });

        if(!userexists) {

//            verifyInProgress = true;
            Toast.makeText(AuthActivity.this, "Verification in progress.",
                    Toast.LENGTH_LONG).show();

            //start timer
            startCounter();

            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91"+phone,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
        }
        else{
            return;
        }
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

//            Toast.makeText(AuthActivity.this, "Invalid Credentials",
//                    Toast.LENGTH_LONG).show();
//
//            editPhone.requestFocus();
//            editCode.requestFocus();

            if (e instanceof FirebaseAuthInvalidCredentialsException) {

                Toast.makeText(AuthActivity.this, "SMS failed to send.",
                        Toast.LENGTH_LONG).show();

            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...

                Toast.makeText(AuthActivity.this, "SMS quota for the project has been exceeded.",
                        Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            CodeSent = true;
            codeSent = s;
            Toast.makeText(AuthActivity.this, "Verification code sent to +91"+phone, Toast.LENGTH_LONG).show();
        }
    };

    //timer function
    public void startCounter(){

        countSet = true;
        new CountDownTimer(60000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                counter--;
            }

            @Override
            public void onFinish() {
                counter= 60;
                countSet = false;
            }
        }.start();
    }

    private void verifySignupCode(){

        if(CodeSent) {
            String code = editCode.getText().toString().trim();
            if (code.isEmpty()) {
                editCode.setError("Enter valid verification code.");
                editCode.requestFocus();
                return;
            }
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
            signInWithPhoneAuthCredential(credential);
        }
        else{
            Toast.makeText(AuthActivity.this, "Verification Code not processed. Please Wait." ,
                    Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AuthActivity.this, "Registration Successful.", Toast.LENGTH_LONG).show();
                            addUser();
                            launchProfileActivity();

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Incorrect Verification Code", Toast.LENGTH_LONG).show();
                            editCode.requestFocus();
                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                Toast.makeText(getApplicationContext(), "Incorrect Verification Code", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }


    private void addUser(){

        docref = database.document("users/"+phone);

        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    Toast.makeText(AuthActivity.this, "User already exists with this number. Please login.",
                            Toast.LENGTH_LONG).show();
                    userexists = true;
                }
                else{
                    docref.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "User added with ID: "+phone);
                            Toast.makeText(AuthActivity.this, "User Created.",
                                    Toast.LENGTH_LONG).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "User addition Failed", e);
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AuthActivity.this, "Document fetch failed. Check your internet connection",
                        Toast.LENGTH_LONG).show();
            }
        });
    }





    private void launchProfileActivity(){

        Intent intent = new Intent(this, Profile_page.class);
        intent.putExtra("PhoneID", phone );
        startActivity(intent);
        finish();
    }

}
