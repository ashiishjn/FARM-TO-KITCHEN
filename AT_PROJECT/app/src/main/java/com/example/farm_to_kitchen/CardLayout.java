package com.example.farm_to_kitchen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class CardLayout extends AppCompatActivity {
    EditText iname, quant, price, number, description;
    Button post;
    String item_name, quantity, item_price, w_number, item_desc;

    @Override
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(), nav_home.class);
        startActivity(i);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_layout);
    }
}