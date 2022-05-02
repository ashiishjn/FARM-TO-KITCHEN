package com.example.farm_to_kitchen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ListAdapter2 extends ArrayAdapter<User2> {

    public ListAdapter2(Context context, ArrayList<User2> userArrayList){
        super(context,R.layout.activity_my_ads_card_view,userArrayList);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User2 user = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_my_ads_card_view,parent,false);
        }
        ImageView imageView = convertView.findViewById(R.id.itemimage1);
        TextView itemname = convertView.findViewById(R.id.itemname1);
        TextView item_price = convertView.findViewById(R.id.item_price1);
        TextView quantity = convertView.findViewById(R.id.quantity1);
        TextView userid = convertView.findViewById(R.id.userid1);
        imageView.setImageResource(R.drawable.r12);
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