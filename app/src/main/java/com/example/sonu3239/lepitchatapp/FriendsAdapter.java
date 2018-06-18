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
import android.widget.Toast;

import com.example.sonu3239.lepitchatapp.Models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsAdapter extends  RecyclerView.Adapter<FriendsAdapter.UserViewHolder> {
    Context context;
    List<User> userlist;
    DatabaseReference databaseReference;

    public FriendsAdapter(Context context, List<User> userlist) {
        this.context = context;
        this.userlist = userlist;
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @NonNull
    @Override
    public FriendsAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.alluserslist,parent,false);
        return  new FriendsAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FriendsAdapter.UserViewHolder holder, int position) {
        final User user=userlist.get(position);
        holder.name.setText(user.getName());
        holder.status.setText(user.getStatus());
        if(!user.getThumbnail().equals("Default"))
            Picasso.get().load(user.getImage()).resize(100,100).into(holder.circleImageView);
        if(user.getOnline().equals("True"))
        {
            holder.imageView.setVisibility(View.VISIBLE);

        }
        holder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence options[]=new CharSequence[]{"Open Profile","Send Message"};
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Select Options");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0)
                        {
                            Intent intent=new Intent(context,Profile.class);
                            intent.putExtra("uid",user.getUid());
                            context.startActivity(intent);
                        }
                        else if(i==1)
                        {
                          Intent intent=new Intent(context,Chatuser.class);
                          intent.putExtra("uid",user.getUid());
                          intent.putExtra("name",user.getName());
                          context.startActivity(intent);
                        }
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }
    public  static  class UserViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView circleImageView;
        TextView name,status;
        View mview;
        ImageView imageView;
        public UserViewHolder(View itemView) {
            super(itemView);
            mview=itemView;
            circleImageView=itemView.findViewById(R.id.userlist_pic);
            name=itemView.findViewById(R.id.userlist_name);
            status=itemView.findViewById(R.id.userlist_status);
            imageView=itemView.findViewById(R.id.isonline);
        }
    }
}
