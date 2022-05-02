package com.example.farm_to_kitchen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.farm_to_kitchen.databinding.ActivityMyAdsBinding;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AlertDialog;


public class MyAds extends AppCompatActivity {
    ActivityMyAdsBinding binding;

    @Override
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(), nav_home.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads);
        binding = ActivityMyAdsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<User2> userArrayList2 = new ArrayList<>();
        ListAdapter2 listAdapter2 = new ListAdapter2(MyAds.this,userArrayList2);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String key = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        StorageReference strref = FirebaseStorage.getInstance().getReference();
        reference.child("Ads").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    if(!datasnapshot.child("User Id").getValue(String.class).matches(key)){}
                    else{
                        String usrid = datasnapshot.child("User Id").getValue(String.class);
                        String name = datasnapshot.child("Item Name").getValue(String.class);
                        String quantity = datasnapshot.child("Quantity").getValue(String.class);
                        String price = datasnapshot.child("Item Price").getValue(String.class);
                        String itemid = datasnapshot.child("Item Id").getValue(String.class);
                        String imageString = datasnapshot.child("Image String").getValue(String.class);
                        User2 user2 = new User2(name, price, quantity, itemid, usrid, imageString);
                        userArrayList2.add(user2);
                        listAdapter2.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        binding.listView1.setAdapter(listAdapter2);
        binding.listView1.setClickable(true);
        binding.listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final CharSequence[] optionsMenu = {"Delete this Ad", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MyAds.this);
                builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(optionsMenu[i].equals("Delete this Ad")){
                            User2 user = (User2) parent.getItemAtPosition(position);
                            String delitemid = user.itemid;
                            reference.child("Ads").child(delitemid).removeValue();
                            Toast.makeText(MyAds.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            Intent i1 = new Intent(getApplicationContext(), MyAds.class);
                            startActivity(i1);
                            finish();
                        }
                        else if(optionsMenu[i].equals("Cancel")){
                            dialogInterface.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
    }
}


