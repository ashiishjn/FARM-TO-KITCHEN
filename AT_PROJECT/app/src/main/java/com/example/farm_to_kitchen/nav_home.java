package com.example.farm_to_kitchen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.example.farm_to_kitchen.databinding.NavHomeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class nav_home extends AppCompatActivity {

    NavHomeBinding binding;
    URI myUri = null;
    String key = FirebaseAuth.getInstance().getCurrentUser().getUid();
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_home);
        findViewById(R.id.drawer_layout).setVisibility(View.VISIBLE);




        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Users").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String namedb = snapshot.child("First Name").getValue(String.class)+" "+snapshot.child("Last Name").getValue(String.class);
                TextView userName = (TextView) findViewById(R.id.nav_name);
                userName.setText(namedb);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        binding = NavHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ArrayList<User> userArrayList = new ArrayList<>();
        ListAdapter listAdapter = new ListAdapter(nav_home.this,userArrayList);

        reference = FirebaseDatabase.getInstance().getReference();
        StorageReference strref = FirebaseStorage.getInstance().getReference();
        reference.child("Ads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    if(datasnapshot.child("User Id").getValue(String.class).equals(key)){}
                    else {
                        String url = datasnapshot.child("Image Url").getValue(String.class);
                        String usrid = datasnapshot.child("User Id").getValue(String.class);
                        String usrname = datasnapshot.child("Item Name").getValue(String.class);
                        String usrquantity = datasnapshot.child("Quantity").getValue(String.class);
                        String usrprice = datasnapshot.child("Item Price").getValue(String.class);
                        String itemid = datasnapshot.child("Item Id").getValue(String.class);
                        String imageString = datasnapshot.child("Image String").getValue(String.class);
                        User user2 = new User(usrname, usrprice, usrquantity, itemid, usrid, imageString);
                        userArrayList.add(user2);
                        listAdapter.notifyDataSetChanged();

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        binding.listView.setAdapter(listAdapter);
        binding.listView.setClickable(true);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) parent.getItemAtPosition(position);
                Intent i = new Intent(nav_home.this,viewmore.class);
                i.putExtra("Item Id",user.itemid);
                i.putExtra("User Id",user.userid);
                i.putExtra("Bitmap",user.imageString);
                startActivity(i);
            }
        });

    }

    public void login(MenuItem item){
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(getApplicationContext(), login.class);
        startActivity(i);
        finish();
    }
    public void newad(MenuItem item){
        Intent i = new Intent(getApplicationContext(), NewAd.class);
        startActivity(i);
        finish();
    }
    public void profile(View v)
    {
        Intent i = new Intent(getApplicationContext(), Profile.class);
        startActivity(i);
        finish();
    }
    public void gotonav(MenuItem item){
        Intent i = new Intent(getApplicationContext(), nav_home.class);
        startActivity(i);
        finish();
    }

    public void myads(MenuItem item)
    {
        Intent i = new Intent(getApplicationContext(), MyAds.class);
        startActivity(i);
        finish();
    }

    public void showdrawer(View v)
    {
        DrawerLayout navDrawer = findViewById(R.id.drawer_layout);
        // If the navigation drawer is not open then open it, if its already open then close it.
        if(!navDrawer.isDrawerOpen(GravityCompat.START)) navDrawer.openDrawer(GravityCompat.START);
        else navDrawer.closeDrawer(GravityCompat.END);
    }

}
