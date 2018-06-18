package com.example.sonu3239.lepitchatapp;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sonu3239.lepitchatapp.Models.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Message> {
    List<Message> list;
    Context context;
    String uid;
    public ListAdapter(Context context, int resource, List<Message> list) {
        super(context, resource,list);
        this.context = context;
        this.list=list;
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.message, null, false);
        TextView textView=view.findViewById(R.id.mdata);
        Message message = list.get(position);
        RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        if(uid.equals(message.getFrom()))
        {
            textView.setBackground(context.getResources().getDrawable(R.drawable.sender));
            textView.setTextColor(Color.WHITE);
            textView.setLayoutParams(params);
        }
        else
        {
            textView.setBackground(context.getResources().getDrawable(R.drawable.receiver));
            textView.setTextColor(Color.BLACK);
        }textView.setText(message.getContent());
        return view;
    }
}
