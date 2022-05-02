package com.example.farm_to_kitchen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ListAdapter  extends ArrayAdapter<User > {

    public ListAdapter (Context context, ArrayList<User> userArrayList){
        super(context,R.layout.card_layout,userArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_layout,parent,false);
        }
        ImageView imageView = convertView.findViewById(R.id.itemimage);
        TextView itemname = convertView.findViewById(R.id.itemname);
        TextView item_price = convertView.findViewById(R.id.item_price);
        TextView quantity = convertView.findViewById(R.id.quantity);
        TextView userid = convertView.findViewById(R.id.userid);
        imageView.setImageResource(android.R.drawable.ic_menu_gallery);
        imageView.setImageBitmap(StringToBitMap(user.imageString));
        imageView.getLayoutParams().width = 400;
        imageView.getLayoutParams().height = 500;
        imageView.requestLayout();
        itemname.setText(user.name);
        item_price.setText(user.price);
        quantity.setText(user.quantity);
        //userid.setText(user.itemid);
        return convertView;
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
}