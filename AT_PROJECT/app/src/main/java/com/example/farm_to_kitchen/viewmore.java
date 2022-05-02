package com.example.farm_to_kitchen;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


public class viewmore extends AppCompatActivity {

    String phone;
    String url;

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), nav_home.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewmore);
        Intent intent = getIntent();
        String itemid = intent.getStringExtra("Item Id");
        String userid = intent.getStringExtra("User Id");
        String bitmap = intent.getStringExtra("Bitmap");


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        TextView t1 = (TextView) findViewById(R.id.itemname);
        TextView t2 = (TextView) findViewById(R.id.quantity);
        TextView t3 = (TextView) findViewById(R.id.mrp);
        TextView t4 = (TextView) findViewById(R.id.seller_name);
        TextView t5 = (TextView) findViewById(R.id.seller_contact);
        ImageView img = (ImageView) findViewById(R.id.itemimage);
        reference.child("Users").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                t4.setText(snapshot.child("First Name").getValue(String.class)+" "+snapshot.child("Last Name").getValue(String.class));
                t5.setText(snapshot.child("Mobile Number").getValue(String.class));
                phone = t5.getText().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        reference.child("Ads").child(itemid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                t1.setText(snapshot.child("Item Name").getValue(String.class));
                t2.setText(snapshot.child("Quantity").getValue(String.class));
                t3.setText(snapshot.child("Item Price").getValue(String.class));
                url = snapshot.child("Image Url").getValue(String.class);
                img.setImageResource(R.drawable.ic_error_black_24dp);
                img.setImageBitmap(StringToBitMap(bitmap));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    public void call(View v) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        String temp = "tel:" + phone;
        intent.setData(Uri.parse(temp));
        startActivity(intent);
    }
    public void openWhatsApp(View view){
        PackageManager pm=getPackageManager();
        try {
            String text = "Hey! \nI saw your ad on FARM-TO-KITCHEN. I am interested in buying it. Is it still available for sale?";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=+91"+phone +"&text="+text));
            startActivity(intent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}


