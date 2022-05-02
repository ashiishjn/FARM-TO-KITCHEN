package com.example.farm_to_kitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NewAd extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database;
    byte bb[];
    String imageString="";
    EditText iname, quant, price;
    Button post;
    String item_name, quantity, item_price;

    private ImageView imageView;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), nav_home.class);
        startActivity(i);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ad);

        DatabaseReference reference = database.getInstance().getReference("Ads");


        iname = (EditText) findViewById(R.id.itemname);
        quant = (EditText) findViewById(R.id.quantity);
        price = (EditText) findViewById(R.id.price);
        imageView = (ImageView) findViewById(R.id.image_show);
        post = (Button) findViewById(R.id.post);



        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item_name = iname.getText().toString();
                String words[]=item_name.split("\\s");
                String capitalizeWord="";
                for(String w:words){
                    String first=w.substring(0,1);
                    String afterfirst=w.substring(1);
                    capitalizeWord+=first.toUpperCase()+afterfirst+" ";
                }
                item_name = capitalizeWord;
                quantity = quant.getText().toString();
                item_price = price.getText().toString();
                boolean valid_item_name = false, v_quantity= false, v_item_price= false;
                int f=0;
                //Item Name Check
                if(item_name.length()>1)
                    valid_item_name = true;
                else
                    valid_item_name = false;

                //Quantity Check
                f=0;
                if(quantity.length() > 0)
                {
                    for(int j=0;j<quantity.length();j++)
                        if(quantity.charAt(j)>='0' && quantity.charAt(j)<='9')
                        {
                            continue;
                        }
                        else
                        {
                            v_quantity = false;
                            f=1;
                            break;
                        }
                }
                else
                {
                    v_quantity = false;
                }
                if(f==0)
                    v_quantity=true;

                //Price Check
                f=0;
                if(item_price.length() > 0)
                {
                    for(int j=0;j<item_price.length();j++)
                        if(item_price.charAt(j)>='0' && item_price.charAt(j)<='9')
                        {
                            continue;
                        }
                        else
                        {
                            v_item_price = false;
                            f=1;
                            break;
                        }
                }
                else
                {
                    v_item_price = false;
                }
                if(f==0)
                    v_item_price=true;

                boolean allValid = v_item_price  && v_quantity && valid_item_name;
                if(!v_item_price)
                {
                    price.setError("Price Should be in digits");
                }
                if(!v_quantity)
                    quant.setError("Quantity should be in digits");
                if(item_name.length()==0)
                    iname.setError("This Field cannot be empty");
                if(!valid_item_name)
                    iname.setError("Name should have minimum 4 characters");
                if(allValid)
                {
                    String userkey = mAuth.getCurrentUser().getUid();
                    String itemkey = reference.push().getKey();
                    reference.child(itemkey).child("Item Name").setValue(item_name);
                    reference.child(itemkey).child("Item Price").setValue("₹"+item_price);
                    reference.child(itemkey).child("Quantity").setValue(quantity+" Kgs");
                    reference.child(itemkey).child("User Id").setValue(userkey);
                    reference.child(itemkey).child("Item Id").setValue(itemkey);
                    reference.child(itemkey).child("Image String").setValue(imageString);
                    Toast.makeText(getApplicationContext(), "Item Added Successfully", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), MyAds.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    public void takeImage(View v) {
        imageView = findViewById(R.id.image_show);
        if(checkAndRequestPermissions(NewAd.this)){
            chooseImage(NewAd.this);
        }
    }

    private void chooseImage(Context context){
        final CharSequence[] optionsMenu = {"Camera", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Camera")){
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }
    // function to check permission
    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    // Handled permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(NewAd.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(NewAd.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(NewAd.this);
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100,bytes);
                        bb = bytes.toByteArray();
                        imageString = Base64.encodeToString(bb, Base64.DEFAULT);
                        imageView.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                BitmapFactory.decodeFile(picturePath).compress(Bitmap.CompressFormat.JPEG, 100,bytes);
                                bb = bytes.toByteArray();
                                imageString = Base64.encodeToString(bb, Base64.DEFAULT);
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }


}