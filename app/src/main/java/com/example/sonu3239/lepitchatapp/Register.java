package com.example.sonu3239.lepitchatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sonu3239.lepitchatapp.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    Toolbar toolbar;
    MaterialEditText etusername,etmail,etpassword;
    Button createaccount;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Creating Account..");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait a while");
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etusername=findViewById(R.id.username);
        etmail=findViewById(R.id.email);
        etpassword=findViewById(R.id.password);
        createaccount=findViewById(R.id.register);
        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name,email,password;
                name=etusername.getText().toString();
                email=etmail.getText().toString();
                password=etpassword.getText().toString();
                if(TextUtils.isEmpty(name))
                    etusername.setError("Username is Required");
                else if(TextUtils.isEmpty(email))
                    etmail.setError("Email is Required");
                else if(!email.contains("@"))
                    etmail.setError("Enter Valid Email Address");
                else if(TextUtils.isEmpty(password))
                    etpassword.setError("Password is Required");
                else if(password.length()<8)
                    etpassword.setError("Password length should be minimum 8");
                else
                    signup(name,email,password);
            }
        });
    }

    private void signup(final String name,String email,String password) {
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                String token= FirebaseInstanceId.getInstance().getToken();
                                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                                User user=new User(name,"I am Using Lepit Chat App.","Default","Default",uid,token,"False");
                                databaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if(!user.isEmailVerified())
                                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    progressDialog.cancel();
                                                    Intent intent=new Intent(Register.this,Login.class);
                                                    startActivity(intent);
                                                    finish();
                                                    Toast.makeText(getApplicationContext(),"Please verify your email and login ",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    }
                                });
                                } else {
                                progressDialog.cancel();
                                Toast.makeText(Register.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            Intent intent=new Intent(Register.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

}
