package com.example.sonu3239.lepitchatapp;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sonu3239.lepitchatapp.Models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageHolder> {
    List<Message> list;
    Context mContext;
    String uid,thumb,name;
    DatabaseReference myref;

    public MessageAdapter(List<Message> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message, parent, false);
        return new MessageHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        Message message = list.get(position);
        RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        if(uid.equals(message.getFrom()))
        {
            holder.textView.setBackground(mContext.getResources().getDrawable(R.drawable.sender));
            holder.textView.setTextColor(Color.WHITE);
            holder.textView.setLayoutParams(params);
        }
        else
        {
            holder.textView.setBackground(mContext.getResources().getDrawable(R.drawable.receiver));
            holder.textView.setTextColor(Color.BLACK);
        }
        holder.textView.setText(message.getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
    class MessageHolder extends RecyclerView.ViewHolder
    {
        TextView textView;
        public MessageHolder(View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.mdata);
            }
    }


