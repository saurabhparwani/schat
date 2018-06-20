package com.example.sonu3239.lepitchatapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.sonu3239.lepitchatapp.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Request extends Fragment {
  RecyclerView recyclerView;
   List<User> list=new ArrayList<>();
   RequestAdapter requestAdapter;
   DatabaseReference databaseReference,databaseReference1,myref;
   String uid;
   FirebaseUser firebaseUser;
    public Request() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_request, container, false);
        list.clear();
        recyclerView=view.findViewById(R.id.requestlist);
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("FriendRequets");
        myref=FirebaseDatabase.getInstance().getReference().child("Users");
        requestAdapter=new RequestAdapter(this.getContext(),list);
        databaseReference1=FirebaseDatabase.getInstance().getReference().child("Users");
        setHasOptionsMenu(true);
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                    if(dataSnapshot1.child("request_type").getValue().toString().equals("received"))
                    {
                        list.clear();
                       myref.child(dataSnapshot1.getKey()).addValueEventListener(new ValueEventListener() {
                          @Override
                          public void onDataChange(DataSnapshot dataSnapshot) {
                              User user=dataSnapshot.getValue(User.class);
                              list.add(user);
                              requestAdapter.notifyDataSetChanged();
                          }

                          @Override
                          public void onCancelled(DatabaseError databaseError) {

                          }
                      });
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(requestAdapter);
        return  view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.homemenu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.logouth)
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
            Date date = new Date();
            databaseReference1.child(firebaseUser.getUid()).child("online").setValue(format.format(date));
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(this.getContext(),MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        else if(item.getItemId()==R.id.settingh)
        {
            Intent intent=new Intent(this.getContext(),Accountsetting.class);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.alluserh)
        {
            Intent intent=new Intent(this.getContext(),AllUsers.class);
            startActivity(intent);
        }
        return true;
    }

}
