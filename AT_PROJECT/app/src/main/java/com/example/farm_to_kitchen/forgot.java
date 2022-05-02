package com.example.farm_to_kitchen;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class forgot extends AppCompatActivity {
    Boolean flag = true, flagc = true;
    EditText mob_no, mail, pw, cpw;
    ImageButton showpw, showcpw;
    Button signup;
    String mRequestBody, p, token;
    ProgressDialog prog;
    @Override
    public void onBackPressed() {

        Intent i = new Intent(getApplicationContext(), login.class);
        startActivity(i);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot);
        mob_no = (EditText) findViewById(R.id.mob_no);
        mail = (EditText) findViewById(R.id.mail);
        pw = (EditText) findViewById(R.id.pw);
        cpw = (EditText) findViewById(R.id.cpw);
        showpw = (ImageButton) findViewById(R.id.showpw);
        showcpw = (ImageButton) findViewById(R.id.showcpw);
        signup = (Button) findViewById(R.id.signup);
        prog = new ProgressDialog(this);

        mail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mail_black_24dp, 0, 0, 0);
        pw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_black_24dp, 0, 0, 0);
        cpw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_black_24dp, 0, 0, 0);
        mob_no.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_smartphone_black_24dp, 0, 0, 0);

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

        showcpw.setBackgroundResource(R.drawable.ic_visibility_off_black_24dp);
        showcpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagc) {
                    showcpw.setBackgroundResource(R.drawable.ic_remove_red_eye_black_24dp);
                    cpw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flagc = false;
                } else {
                    showcpw.setBackgroundResource(R.drawable.ic_visibility_off_black_24dp);
                    cpw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flagc = true;
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p = pw.getText().toString();
                String cp = cpw.getText().toString();
                String mob = mob_no.getText().toString();
                String ml = mail.getText().toString();
                boolean valid_password=false, valid_cp, v_fname, v_lname, v_mobile=false, v_mail;
                int f=0;
                if(p.length()>8)
                    valid_password = true;
                else
                    valid_password = true;
                valid_cp = (p.equals(cp));
                if(mob.length() == 10)
                {
                    for(int j=0;j<10;j++)
                        if(mob.charAt(j)>='0' && mob.charAt(j)<='9')
                        {
                            continue;
                        }
                        else
                        {
                            v_mobile = false;
                            f=1;
                            break;
                        }
                }
                else
                {
                    v_mobile = false;
                }
                if(f==0)
                    v_mobile=true;
                v_mail = !TextUtils.isEmpty(ml) && Patterns.EMAIL_ADDRESS.matcher(ml).matches();
                boolean allValid = v_mail && v_mobile && valid_cp && valid_password;
                if(!v_mail)
                {
                    mail.setError("Invalid Email");
                }
                if (!v_mobile){
                    mob_no.setError("Phone Number Should be of 10 digits");
                }
                if(!valid_password)
                    Toast.makeText(getApplicationContext(), "Password should be minimum of 8 characters", Toast.LENGTH_LONG).show();
                else if(!valid_cp)
                    Toast.makeText(getApplicationContext(), "Passwords does not match", Toast.LENGTH_LONG).show();
                if(allValid)
                {
                    Toast.makeText(getApplicationContext(), "Password change Successful", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), login.class);
                    startActivity(i);
                    finish();
                }

            }
        });
    }


}