package com.example.vmac.chatbotmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
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

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity
{

    private Button launchSignup;
    private Button loginBtn;
    private Button sendBtn;
    private ImageButton prevActivity;

    private boolean verifyInProgress = false,saveState= false, restoreState=false;
    private boolean CodeSent = false;

    private EditText editPhone;
    private EditText editCode;
    private String  email_string,password_string;
    private String codeSent;
    private boolean userexists = false;

    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    private DocumentReference docref;

    //timer for otp
    public int counter;
    public boolean countSet;

    public SharedPreferences s;
    public SharedPreferences.Editor e;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        //timer value set
        countSet = false;
        counter = 60;

        editPhone = findViewById(R.id.phoneNo);
        editCode =  findViewById(R.id.verifypass);

        prevActivity = (ImageButton) findViewById(R.id.backbtn);
        prevActivity.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        launchSignup = findViewById(R.id.signupBtn);
        loginBtn = findViewById(R.id.verifyBtn);

        email_string= editPhone.getText().toString();
        password_string= editCode.getText().toString();


    }



    public void login_function(View view)
    {
        mAuth.signInWithEmailAndPassword(email_string, password_string).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(LoginActivity.this,Profile_page.class);
                    intent.putExtra("email_value",email_string);

                    s= getSharedPreferences("email",MODE_PRIVATE);
                    e=s.edit();

                    String temp= email_string;
                    e.putString("text_sent",temp);e.apply();

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                    startActivity(intent);
                    finish();

                }
                else
                    Toast.makeText(LoginActivity.this, "Problem with Signing In", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void signin_function(View view)
    {
        Intent intent= new Intent(LoginActivity.this,SignupActivity.class);
        startActivity(intent);
    }


//
//       docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {


//        sendBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!countSet)
//                sendVerificationCode();
//                else{
//                    Toast.makeText(LoginActivity.this, "Please wait "+counter+" seconds before requesting for another OTP.",
//                            Toast.LENGTH_LONG).show();
//                }
//            }
//        });


//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(userexists) {
//                    verifySigninCode();
//                }
//                else{
//                    Toast.makeText(LoginActivity.this, "User does not exist with this number. Please signup.",
//                            Toast.LENGTH_LONG).show();
//                }
//            }
//        });




//    private void sendVerificationCode(){
//
//        phone = editPhone.getText().toString().trim();
//
//        if(phone.isEmpty()){
//            editPhone.setError("Phone number required.");
//            editPhone.requestFocus();
//            editPhone.performClick();
//            return;
//        }
//
//        if(phone.length()!=10){
//            editPhone.setError("Invalid phone number.");
//            editPhone.requestFocus();
//            editPhone.performClick();
//            return;
//        }
////        else {
////            phone = "+91" +phone;
////        }
//        docref = database.document("users/"+phone);
//
//        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot document) {
//                if(document.exists()) {
//                    userexists=true;
//                    Toast.makeText(LoginActivity.this, "User exists. Awaiting sms.",
//                            Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    Toast.makeText(LoginActivity.this, "User does not exist with this number. Please signup.",
//                            Toast.LENGTH_SHORT).show();
//                    userexists = false;
//                    launchSignupActivity();
//                    finish();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(LoginActivity.this, "Document fetch failed. Check your internet connection",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        if(userexists) {
//
//            verifyInProgress = true;
//            Toast.makeText(LoginActivity.this, "Verification in progress."+editPhone.getText().toString(),
//                    Toast.LENGTH_SHORT).show();
//
//            //start timer
//            startCounter();
//
//            PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                    "+91"+ phone,        // Phone number to verify
//                    60,                 // Timeout duration
//                    TimeUnit.SECONDS,   // Unit of timeout
//                    this,               // Activity (for callback binding)
//                    mCallbacks);        // OnVerificationStateChangedCallbacks
//        }
//        else{
//            return;
//        }
//    }
//
//    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        @Override
//        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
//
//        }
//
//        @Override
//        public void onVerificationFailed(@NonNull FirebaseException e) {
//
////            Toast.makeText(LoginActivity.this, "SMS failed to send.",
////                    Toast.LENGTH_LONG).show();
////
////            editPhone.requestFocus();
////            editCode.requestFocus();
//
//            if (e instanceof FirebaseAuthInvalidCredentialsException) {
//
//                Toast.makeText(LoginActivity.this, "SMS failed to send.",
//                        Toast.LENGTH_SHORT).show();
//
//            } else if (e instanceof FirebaseTooManyRequestsException) {
//                // The SMS quota for the project has been exceeded
//                // ...
//
//                Toast.makeText(LoginActivity.this, "SMS quota for the project has been exceeded.",
//                        Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        @Override
//        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//
//            CodeSent = true;
//            codeSent = s;
//            Toast.makeText(LoginActivity.this, "Verification code sent to +91"+phone,
//                    Toast.LENGTH_SHORT).show();
//
//        }
//    };
//
//    //timer function
//    public void startCounter(){
//
//        countSet = true;
//        new CountDownTimer(60000,1000) {
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//                 counter--;
//            }
//
//            @Override
//            public void onFinish() {
//                 countSet = false;
//            }
//        }.start();
//    }
//
//    private  void verifySigninCode(){
//
//        if(CodeSent){
//            String code = editCode.getText().toString().trim();
//            if(code.isEmpty())
//            {
//                editCode.setError("Enter valid verification code.");
//                editCode.requestFocus();
//                return;
//            }
//            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
//            signInWithPhoneAuthCredential(credential);
//        }
//        else{
//            Toast.makeText(LoginActivity.this, "Verification Code not processed. Please wait." ,
//                    Toast.LENGTH_SHORT).show();
//            return;
//        }
//    }
//
//
//
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
//                            launchProfileActivity();
//                        }
//                        else {
//                            Toast.makeText(getApplicationContext(), "Incorrect Verification Code", Toast.LENGTH_SHORT).show();
//                            editCode.requestFocus();
//
//                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
//                                Toast.makeText(getApplicationContext(), "Incorrect Verification Code", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//    }
//
//    private void launchProfileActivity(){
//        Intent intent = new Intent(this, Profile_page.class);
//        intent.putExtra("PhoneID", phone);
//        s= getSharedPreferences("phone1", MODE_PRIVATE);
//        e=s.edit();
//
//        String temp= phone.toString();
//        e.putString("text1", temp);e.apply();
//
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        // intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//
//        // LoginActivity.this.finish();
//
//        startActivity(intent);
//    }
//
//    private void launchSignupActivity(){
//        Intent intent = new Intent(this, SignupActivity.class);
//        startActivity(intent);
//    }

}
