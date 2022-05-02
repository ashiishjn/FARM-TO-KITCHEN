package com.example.farm_to_kitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String key = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(key);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String namedb = snapshot.child("First Name").getValue(String.class)+" "+snapshot.child("Last Name").getValue(String.class);
                String mailIddb = snapshot.child("Mail Id").getValue(String.class);
                String numberdb = snapshot.child("Mobile Number").getValue(String.class);
                TextView name = findViewById(R.id.user_name);
                TextView mailId = findViewById(R.id.user_mailid);
                TextView number = findViewById(R.id.user_number);
                name.setText(namedb);
                mailId.setText(mailIddb);
                number.setText(numberdb);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void click(View v)
    {
        Intent i = new Intent(getApplicationContext(), nav_home.class);
        startActivity(i);
        finish();
    }
    @Override
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(), nav_home.class);
        startActivity(i);
        finish();
    }
}