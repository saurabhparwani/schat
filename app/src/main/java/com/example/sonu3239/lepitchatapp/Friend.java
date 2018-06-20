package com.example.sonu3239.lepitchatapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.google.firebase.database.ChildEventListener;
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
public class Friend extends Fragment {

    RecyclerView recyclerView;
    FriendsAdapter friendsAdapter;
    List<User> list=new ArrayList<>();
    DatabaseReference databaseReference,myref,databaseReference1;
    FirebaseUser firebaseUser;
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
        setHasOptionsMenu(true);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Friends");
        databaseReference1= FirebaseDatabase.getInstance().getReference().child("Users");
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
