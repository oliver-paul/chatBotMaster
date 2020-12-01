package com.example.vmac.chatbotmaster;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Profile_page extends AppCompatActivity {

    String PHONE_ID;
    Map<String, Object> USER_DETAILS;
    String firstname,lastname,dob,state;

    private FirebaseFirestore database;
    private DocumentReference docref;

    Button Bc,Bs,Bf,Bi,logout;
    TextView  name,email,age,location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        name = (TextView) findViewById(R.id.TVn);
        age = (TextView) findViewById(R.id.TVa);
        location = (TextView) findViewById(R.id.TVl);
        Bc = (Button) findViewById(R.id.buttonc);
        Bs = (Button) findViewById(R.id.buttons);
        Bf = (Button) findViewById(R.id.buttonf);
        Bi = (Button) findViewById(R.id.buttoni);
        logout= findViewById(R.id.logout_button);


//        PHONE_ID = getIntent().getExtras().getString("email_value");
//        database = FirebaseFirestore.getInstance();
//        docref = database.document("users/"+PHONE_ID);
//
//
//        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot document)
//            {
//                if(document.exists())
//                {
//                    Toast.makeText(Profile_page.this, "Welcome",
//                            Toast.LENGTH_LONG).show();
//                    name.setText("Hello, "+document.getString("firstname"));
//                    age.setText("Date of Birth : \n  "+document.getString("dob"));
//                    location.setText("Location : "+document.getString("state"));
//                }
//                else
//                {
//                    Toast.makeText(Profile_page.this, "Error displaying details.",
//                            Toast.LENGTH_LONG).show();
//
//                      refresh();
////                    Intent refresh = new Intent(this, Profile_page.class);
////                    startActivity(refresh);
////                    this.finish();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Profile_page.this, "Document fetch failed. Check your internet connection",
//                        Toast.LENGTH_LONG).show();
//            }
//        });
//
//
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        getSupportActionBar().setCustomView(R.layout.action_custom);

        Bc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChat();
            }
        });
        Bs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStats();
            }
        });
        Bf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeedback();
            }
        });
        Bi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInfo();
            }
        });


    }
        private void openChat() {
            Toast.makeText(getApplicationContext(), "Opening Chart", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,SplashActivity.class);
            startActivity(intent);

        }
    private void openStats() {
        Toast.makeText(getApplicationContext(), "Opening Stats", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,Stats.class);
        startActivity(intent);

    }
    private void openFeedback() {
        Toast.makeText(getApplicationContext(), "Opening feedback", Toast.LENGTH_SHORT).show();
        Intent Getintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/CkvESJaqVTJCfpxGA"));
        startActivity(Getintent);

    }
    private void openInfo() {
        Toast.makeText(getApplicationContext(), "Opening Info", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(getApplicationContext(),InfoActivity.class);
         startActivity(intent);
    }

    private void refresh(){
        Intent ref = new Intent(this, Profile_page.class);
        ref.putExtra("PhoneID", PHONE_ID);
                    startActivity(ref);
                    this.finish();
    }

    public  void logout(View view)
    {
        Toast.makeText(this, "Entered the logout function", Toast.LENGTH_SHORT).show();
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(Profile_page.this,FrontActivity.class));
        Toast.makeText(getApplicationContext(), "You are logged out", Toast.LENGTH_SHORT).show();
        finish();
        //return true;
    }
}
