package com.example.sonu3239.lepitchatapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sonu3239.lepitchatapp.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends  RecyclerView.Adapter<ChatAdapter.ChatHolder> {
    Context context;
    List<User> userlist;
    DatabaseReference databaseReference,myref;

    public ChatAdapter(Context context, List<User> userlist) {
        this.context = context;
        this.userlist = userlist;
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        myref=FirebaseDatabase.getInstance().getReference().child("Chat").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.alluserslist,parent,false);
        return  new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatHolder holder, int position) {
        final User user=userlist.get(position);
        holder.name.setText(user.getName());
        if(!user.getThumbnail().equals("Default"))
            Picasso.get().load(user.getThumbnail()).resize(100,100).placeholder(R.drawable.avatar).into(holder.circleImageView);
        myref.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String lastmessage=dataSnapshot.child("lastmessgae").getValue().toString();
                if(lastmessage.contains("https://"))
                  holder.status.setText("image/video");
                else
                    holder.status.setText(lastmessage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,Chatuser.class);
                intent.putExtra("uid",user.getUid());
                intent.putExtra("name",user.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }
    public  static  class ChatHolder extends RecyclerView.ViewHolder
    {
        CircleImageView circleImageView;
        TextView name,status;
        View mview;
        ImageView imageView;
        public ChatHolder(View itemView) {
            super(itemView);
            mview=itemView;
            circleImageView=itemView.findViewById(R.id.userlist_pic);
            name=itemView.findViewById(R.id.userlist_name);
            status=itemView.findViewById(R.id.userlist_status);
            imageView=itemView.findViewById(R.id.isonline);
        }
    }
    public void filter(ArrayList<User> newlist) {
        userlist=new ArrayList<>();
        userlist.addAll(newlist);
        notifyDataSetChanged();
    }
}
