package com.example.sonu3239.lepitchatapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sonu3239.lepitchatapp.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Request extends Fragment {
  RecyclerView recyclerView;
   List<User> list=new ArrayList<>();
   RequestAdapter requestAdapter;
   DatabaseReference databaseReference,myref;
   String uid;
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

}
