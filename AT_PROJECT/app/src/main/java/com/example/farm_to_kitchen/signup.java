package com.example.farm_to_kitchen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class signup extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;

    Boolean flag = true, flagc = true;
    EditText fname, lname, mob_no, mail, pw, cpw;
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
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        DatabaseReference reference = database.getInstance().getReference("Users");

        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        mob_no = (EditText) findViewById(R.id.mob_no);
        mail = (EditText) findViewById(R.id.mail);
        pw = (EditText) findViewById(R.id.pw);
        cpw = (EditText) findViewById(R.id.cpw);
        showpw = (ImageButton) findViewById(R.id.showpw);
        showcpw = (ImageButton) findViewById(R.id.showcpw);
        signup = (Button) findViewById(R.id.signup);
        prog = new ProgressDialog(this);


        mail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mail_black_24dp, 0, 0, 0);
        fname.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_perm_identity_black_24dp, 0, 0, 0);
        lname.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_perm_identity_black_24dp, 0, 0, 0);
        pw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_black_24dp, 0, 0, 0);
        cpw.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_black_24dp, 0, 0, 0);
        mob_no.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_smartphone_black_24dp, 0, 0, 0);

        mob_no.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 10) {
                    mob_no.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_smartphone_black_24dp, 0, R.drawable.ic_error_black_24dp, 0);
                } else {
                    mob_no.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_smartphone_black_24dp, 0, 0, 0);
                }
            }
        });

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
                String fn = fname.getText().toString();
                String ln = lname.getText().toString();
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
                v_fname = (fn.length()>0);
                v_lname = (ln.length()>0);
                v_mail = !TextUtils.isEmpty(ml) && Patterns.EMAIL_ADDRESS.matcher(ml).matches();
                boolean allValid = v_mail && v_mobile && v_fname && v_lname && valid_cp && valid_password;
                if(!v_mail)
                {
                    mail.setError("Invalid Email");
                }
                if (!v_mobile){
                    mob_no.setError("Phone Number Should be of 10 digits");
                }
                if(!v_fname)
                    fname.setError("This Field cannot be empty");
                if(!v_lname)
                    lname.setError("This Field cannot be empty");
                if(!valid_password)
                    Toast.makeText(getApplicationContext(), "Password should be minimum of 8 characters", Toast.LENGTH_LONG).show();
                else if(!valid_cp)
                    Toast.makeText(getApplicationContext(), "Passwords does not match", Toast.LENGTH_LONG).show();
                if(allValid)
                {
                    mAuth.createUserWithEmailAndPassword(ml,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Signup Successful", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), login.class);
                                startActivity(i);
                                finish();
                                String key = mAuth.getUid();
                                reference.child(key).child("First Name").setValue(fn);
                                reference.child(key).child("Last Name").setValue(ln);
                                reference.child(key).child("Mobile Number").setValue(mob);
                                reference.child(key).child("Mail Id").setValue(ml);
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Error: "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }





}