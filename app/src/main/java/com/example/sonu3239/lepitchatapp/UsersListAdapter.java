package com.example.sonu3239.lepitchatapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sonu3239.lepitchatapp.Models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.UserViewHolder> {
    Context context;
    List<User> userlist;

    public UsersListAdapter(Context context, List<User> userlist) {
        this.context = context;
        this.userlist = userlist;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.alluserslist,parent,false);
        return  new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        final User user=userlist.get(position);
        holder.name.setText(user.getName());
        holder.status.setText(user.getStatus());
        if(!user.getThumbnail().equals("Default"))
            Picasso.get().load(user.getImage()).resize(100,100).into(holder.circleImageView);
        holder.mview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,Profile.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("status",user.getStatus());
                intent.putExtra("image",user.getThumbnail());
                intent.putExtra("uid",user.getUid());
                context.startActivity(intent);
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
        public UserViewHolder(View itemView) {
            super(itemView);
            mview=itemView;
            circleImageView=itemView.findViewById(R.id.userlist_pic);
            name=itemView.findViewById(R.id.userlist_name);
            status=itemView.findViewById(R.id.userlist_status);
        }
    }
}
