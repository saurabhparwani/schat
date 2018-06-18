package com.example.sonu3239.lepitchatapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sonu3239.lepitchatapp.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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
public class Friend extends Fragment {

    RecyclerView recyclerView;
    FriendsAdapter friendsAdapter;
    List<User> list=new ArrayList<>();
    DatabaseReference databaseReference,myref;
    public Friend() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_friend, container, false);
        recyclerView=view.findViewById(R.id.friendlist);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        friendsAdapter=new FriendsAdapter(this.getContext(),list);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Friends");
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
               for(DataSnapshot p:dataSnapshot.getChildren())
               {
                   String key=p.getKey();
                   myref=FirebaseDatabase.getInstance().getReference().child("Users").child(key);
                   myref.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                           User user=dataSnapshot.getValue(User.class);
                           list.add(user);
                           friendsAdapter.notifyDataSetChanged();
                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {

                       }
                   });

               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                friendsAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setAdapter(friendsAdapter);
        return view;
    }

}
