package com.example.sonu3239.lepitchatapp;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;

public class Home extends AppCompatActivity {
   android.support.v7.widget.Toolbar toolbar;
   FirebaseAuth firebaseAuth;
   TabLayout tabLayout;
   ViewPager viewPager;
   PagerAdapter pagerAdapter;
   DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth=FirebaseAuth.getInstance();
        getSupportActionBar().setTitle("S-Chat");
        tabLayout =  findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.pager);
        pagerAdapter=new PagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.logout)
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
            Date date = new Date();
            databaseReference.child(firebaseUser.getUid()).child("online").setValue(format.format(date));
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(Home.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else if(item.getItemId()==R.id.setting)
        {
            Intent intent=new Intent(Home.this,Accountsetting.class);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.alluser)
        {
            Intent intent=new Intent(Home.this,AllUsers.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseUser == null) {
          Intent intent=new Intent(Home.this,MainActivity.class);
          startActivity(intent);
          finish();
        }
        else
        {
          databaseReference.child(firebaseUser.getUid()).child("online").setValue("True");
          Log.d("Start","Home");
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Resume","Home");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Pause","Home");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseUser!=null) {
            databaseReference.child(firebaseUser.getUid()).child("online").setValue("True");
            Log.d("Stop", "Home");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("ReStart","Home");

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(firebaseUser!=null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
            Date date = new Date();
            databaseReference.child(firebaseUser.getUid()).child("online").setValue(format.format(date));
            Log.d("Destroy",format.format(date));

        }
    }
}
