package com.example.farm_to_kitchen;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import java.net.URI;

public class User  {
    String name, price, quantity, itemid, userid, imageString;

    public User(String name, String price, String quantity, String itemid,String userid, String imageString) {

        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.itemid = itemid;
        this.userid = userid;
        this.imageString = imageString;
    }

}