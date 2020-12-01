package com.example.vmac.chatbotmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FrontActivity extends AppCompatActivity {

    private Button launchLogin;
    private Button launchSignup;
    private Button launchProfile;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String text="default";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);


        firebaseAuth= FirebaseAuth.getInstance();
        firebaseUser= firebaseAuth.getCurrentUser();

        if(firebaseUser != null)
        {
            //Toast.makeText(getApplicationContext(),"It has entered "+phone, Toast.LENGTH_LONG).show();

            SharedPreferences s= getSharedPreferences("phone1", MODE_PRIVATE);
            text=  s.getString("text1", "");
            Toast.makeText(this,"It has entered "+text.toString(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, Profile_page.class);
            intent.putExtra("PhoneID",text);
            startActivity(intent);
            finish();

        }

        launchLogin = (Button) findViewById(R.id.loginbtn);
        launchLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchLoginActivity();
            }
        });

        launchSignup = (Button) findViewById(R.id.signupbtn);
        launchSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchSignupActivity();
            }
        });

    }

    private void launchLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        //finish();
    }

    private void launchSignupActivity(){
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
       // finish();
    }
}
