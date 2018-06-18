package com.example.sonu3239.lepitchatapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sonu3239.lepitchatapp.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestHolder> {
    Context context;
    List<User> list;
    DatabaseReference databaseReference;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date = new Date();
    public RequestAdapter(Context context, List<User> list) {
        this.context = context;
        this.list = list;
        databaseReference= FirebaseDatabase.getInstance().getReference().child("FriendRequets");
    }

    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.requests,parent,false);
        return  new RequestHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestHolder holder, int position) {
        final User user=list.get(position);
        holder.textView.setText(user.getName());
        if(!user.getThumbnail().equals("Default"))
            Picasso.get().load(user.getImage()).resize(100,100).into(holder.circleImageView);
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        databaseReference.child(user.getUid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                              final DatabaseReference myref=FirebaseDatabase.getInstance().getReference().child("Friends");
                              myref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(user.getUid()).setValue(dateFormat.format(date)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                  @Override
                                  public void onComplete(@NonNull Task<Void> task) {
                                      myref.child(user.getUid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(dateFormat.format(date)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                          @Override
                                          public void onComplete(@NonNull Task<Void> task) {
                                              Toast.makeText(context,"Added As Friend",Toast.LENGTH_SHORT).show();
                                          }
                                      });
                                  }
                              }).addOnFailureListener(new OnFailureListener() {
                                  @Override
                                  public void onFailure(@NonNull Exception e) {
                                      Toast.makeText(context,"Could not added as friend",Toast.LENGTH_SHORT).show();
                                  }
                              });
                                notifyDataSetChanged();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        notifyDataSetChanged();
                    }
                });
            }
        });
        holder.ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(user.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                         databaseReference.child(user.getUid()).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                 Toast.makeText(context,"Friend Request Ignored",Toast.LENGTH_SHORT).show();
                                 notifyDataSetChanged();
                             }
                         });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class RequestHolder extends RecyclerView.ViewHolder
    {
        CircleImageView circleImageView;
        TextView textView;
        Button ignore,accept;
        View mview;
        public RequestHolder(View itemView) {
            super(itemView);
            mview=itemView;
            circleImageView=itemView.findViewById(R.id.userlist_pic);
            textView=itemView.findViewById(R.id.userlist_name);
            ignore=itemView.findViewById(R.id.Ignore);
            accept=itemView.findViewById(R.id.Accept);
        }
    }
}
