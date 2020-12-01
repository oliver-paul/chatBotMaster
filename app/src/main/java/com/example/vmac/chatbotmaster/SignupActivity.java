package com.example.vmac.chatbotmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private ImageButton backbutton;
    private Button signupBtn;
    protected Spinner editState;
    protected EditText editFirstName, editLastName;
    protected EditText editPhone, password;
    protected TextView editDoB;

    private FirebaseFirestore database;
    private DocumentReference docref;
    private FirebaseAuth firebaseAuth;

    protected DatePickerDialog.OnDateSetListener onDateSetListener;
    protected static Map<String, Object> user;
    protected static String phoneno;
    private boolean userexists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editDoB = (TextView) findViewById(R.id.editDoB);
        backbutton = (ImageButton) findViewById(R.id.backbutton);
        editState = (Spinner) findViewById(R.id.editState);
        editFirstName = (EditText) findViewById(R.id.editFirstName);
        editLastName = (EditText) findViewById(R.id.editLastName);
        editPhone = (EditText) findViewById(R.id.editPhone);
        password = findViewById(R.id.password_sign_up);
        signupBtn = (Button) findViewById(R.id.signupBtn);

        userexists = true;

        database = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        editDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SignupActivity.this,
                        android.R.style.Theme_Material_Dialog,
                        onDateSetListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.DKGRAY));
                dialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + month + "/" + dayOfMonth + "/" + year);

                String date = dayOfMonth + "/" + month + "/" + year;
                editDoB.setText(date);
            }
        };

        editState.setFocusable(true);
        editState.setFocusableInTouchMode(true);

        String[] items = new String[]{"State", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar",
                "Chhattisgarh", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jharkhand",
                "Karnataka", "Kerala", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya",
                "Mizoram", "Nagaland", "Odisha", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu",
                "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        editState.setAdapter(adapter);

        backbutton = (ImageButton) findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(registerUser() && !userexists)
                {
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else
                    {
                    boolean a = createUser();
                    if (a)
                    {
                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else
                        {
                        Toast.makeText(SignupActivity.this, "User not Registered try again", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

//    private void launchAuthActivity(){
//        Intent intent = new Intent(SignupActivity.this, AuthActivity.class);
//
//        startActivity(intent);
//    }

    private boolean createUser()
    {
        String pass = password.getText().toString();
        String email = editPhone.getText().toString();
        final int[] flag = new int[1];

        Toast.makeText(this,pass+"  "+email,Toast.LENGTH_LONG ).show();

        firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    Toast.makeText(SignupActivity.this, "User registered", Toast.LENGTH_LONG).show();
                    flag[0] =1;

                }
                else
                    {
                    Toast.makeText(SignupActivity.this, "User not registered", Toast.LENGTH_LONG).show();
                    flag[0] =0;
                }

            }
        });
        if(flag[0] ==0)
            return  false;
        else
            return true;
    }



    private boolean registerUser()
    {

        String firstname = editFirstName.getText().toString().trim();
        String lastname = editLastName.getText().toString().trim();
        String dob = editDoB.getText().toString().trim();
        String state = editState.getSelectedItem().toString().trim();
        String phone = editPhone.getText().toString().trim();
        String pass= password.getText().toString().trim();


        if(firstname.isEmpty()){
            editFirstName.setError("First Name is required.");
            editFirstName.requestFocus();
            return false;
        }
        if(!firstname.matches("^[a-zA-Z]*$")){
            editFirstName.setError("Name can only contain letters.");
            editFirstName.requestFocus();
            return false;
        }
        if(lastname.isEmpty()){
            editLastName.setError("Last Name is required.");
            editLastName.requestFocus();
            return false;
        }
        if(!lastname.matches("^[a-zA-Z]*$")){
            editLastName.setError("Name can only contain letters.");
            editLastName.requestFocus();
            return false;
        }
        if(dob.isEmpty()){
            editDoB.setError("Date of birth is required.");
            editDoB.requestFocus();
            return false;
        }
        if(state.equals("State")){
            editState.requestFocusFromTouch();
            editState.performClick();
            return false;
        }
        if(phone.isEmpty()){
            editPhone.setError("Email Id Needed");
            editPhone.requestFocus();
            return false;
        }
        if(pass.isEmpty()){
            editPhone.setError("Email Id Needed");
            editPhone.requestFocus();
            return false;
        }



        docref = database.document("users/"+phone);

        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if(documentSnapshot.exists())
                {
                    Toast.makeText(SignupActivity.this, "User already exists with this number. Please login.",
                            Toast.LENGTH_LONG).show();
                    userexists = true;
                    launchLoginActivity();
                    finish();
                }
                else{
                    userexists = false;
                }
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(SignupActivity.this, "Document fetch failed. Check your internet connection",
                        Toast.LENGTH_LONG).show();
            }
        });

        if(userexists){
            return false;
        }
        phoneno = phone;
        user = new HashMap<>();
        user.put("firstname", editFirstName.getText().toString());
        user.put("lastname", editLastName.getText().toString());
        user.put("dob", editDoB.getText().toString());
        user.put("state", editState.getSelectedItem().toString());


//        firstname = editFirstName.getText().toString().trim();
//        lastname = editLastName.getText().toString().trim();
//        dob = editDoB.getText().toString().trim();
//        state = editState.getSelectedItem().toString().trim();
//        phone = editPhone.getText().toString().trim();
        return true;
    }

    private void launchLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
//   setContentView(R.layout.activity_s_ign_up);
}
