package com.example.sonu3239.lepitchatapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sonu3239.lepitchatapp.Models.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Chat extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference databaseReference,myref;
    private ChatAdapter chatAdapter;
    private  LinearLayoutManager linearLayoutManager;
    private List<User> list=new ArrayList<>();
    int i=0;
    FirebaseUser firebaseUser;
    public Chat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView=view.findViewById(R.id.chatlist);
        recyclerView.setHasFixedSize(true);
        setHasOptionsMenu(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        linearLayoutManager=new LinearLayoutManager(this.getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        chatAdapter=new ChatAdapter(this.getContext(),list);
        recyclerView.setAdapter(chatAdapter);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        myref=databaseReference.child("Chat").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        myref.orderByChild("time").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                chatAdapter.notifyDataSetChanged();
                for(DataSnapshot p:dataSnapshot.getChildren())
                {
                    String key=p.getKey();
                   databaseReference.child("Users").child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user=dataSnapshot.getValue(User.class);
                            list.add(user);
                            chatAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                   Log.d("User",p.getKey());

                   }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.mainmenu,menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        android.support.v7.widget.SearchView mSearchView = (android.support.v7.widget.SearchView) menuItem.getActionView();
        mSearchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("Query",query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText=newText.toLowerCase();
                ArrayList<User> newlist=new ArrayList<>();
                for(User user:list)
                {
                    String name=user.getName().toLowerCase();
                    if(name.contains(newText))
                        newlist.add(user);
                    chatAdapter.filter(newlist);
                }
                return true;
        }});
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.logout)
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss");
            Date date = new Date();
            databaseReference.child("Users").child(firebaseUser.getUid()).child("online").setValue(format.format(date));
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(this.getContext(),MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        else if(item.getItemId()==R.id.setting)
        {
            Intent intent=new Intent(this.getContext(),Accountsetting.class);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.alluser)
        {
            Intent intent=new Intent(this.getContext(),AllUsers.class);
            startActivity(intent);
        }
        return true;
    }

}
