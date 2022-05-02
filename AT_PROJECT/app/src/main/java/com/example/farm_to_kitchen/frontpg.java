package com.example.farm_to_kitchen;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class frontpg extends AppCompatActivity {
    LinearLayout l1;
    AnimationDrawable ad;

    FirebaseAuth mAuth;

    // to check whether logged in or not
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(currentUser == null){
                    startActivity(new Intent(getApplicationContext(), login.class));
                }
                else{
                    startActivity(new Intent(getApplicationContext(),nav_home.class));
                }
            }
        }, 1300);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontpg);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        l1 = (LinearLayout) findViewById(R.id.frontpg);
    }
}
