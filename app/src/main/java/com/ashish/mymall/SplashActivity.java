package com.ashish.mymall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SystemClock.sleep(3000);

        firebaseAuth=FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser==null){

            Intent loginintent = new Intent(SplashActivity.this, RegisterActivity.class);
            startActivity(loginintent);
            finish();
        }else {
            Intent mainintent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(mainintent);
            finish();
        }
    }
}
