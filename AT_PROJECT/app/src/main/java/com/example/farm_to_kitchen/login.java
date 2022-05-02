package com.example.farm_to_kitchen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class login extends AppCompatActivity {

    //Declaration
    Boolean flag = true;
    EditText username, pw;
    ImageButton showpw;
    Button signup, login;
    String b;
    ProgressDialog p;

    FirebaseAuth mAuth;

    //inbuilt oncreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        //values assignment
        setContentView(R.layout.activity_login);
        username = (EditText) findViewById(R.id.username);
        pw = (EditText) findViewById(R.id.password);
        showpw = (ImageButton) findViewById(R.id.showpw);
        signup = (Button) findViewById(R.id.signup);
        login = (Button) findViewById(R.id.login);
        p = new ProgressDialog(this);

        //show password functionality
        showpw.setBackgroundResource(R.drawable.ic_visibility_off_black_24dp);
        showpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    showpw.setBackgroundResource(R.drawable.ic_remove_red_eye_black_24dp);
                    pw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flag = false;
                } else {
                    showpw.setBackgroundResource(R.drawable.ic_visibility_off_black_24dp);
                    pw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flag = true;
                }
            }
        });
        //onclick for signup
        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), com.example.farm_to_kitchen.signup.class);
                startActivity(i);
            }
        });
        //onclick for login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = username.getText().toString();
                String password = pw.getText().toString();

                if(email.isEmpty()){
                    username.setError("Email is Empty");
                    username.requestFocus();
                }
                else if(password.isEmpty()){
                    pw.setError("Password is Empty");
                    pw.requestFocus();
                }
                else{
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), nav_home.class);
                                startActivity(i);
                                finish();
                            }
                            else{
                                Toast.makeText(login.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    public void forgot(View v)
    {
        Intent i = new Intent(getApplicationContext(), forgot.class);
        startActivity(i);
        finish();
    }
}



