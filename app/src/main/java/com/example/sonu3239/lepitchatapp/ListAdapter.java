package com.example.sonu3239.lepitchatapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.sonu3239.lepitchatapp.Models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListAdapter extends ArrayAdapter<Message> {
    List<Message> list;
    Context context;
    String uid;
    FirebaseStorage firebaseStorage;
    public ListAdapter(Context context, int resource, List<Message> list) {
        super(context, resource,list);
        this.context = context;
        this.list=list;
        uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        firebaseStorage=FirebaseStorage.getInstance();
    }
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.message, null, false);
        TextView textView=view.findViewById(R.id.mdata);
        ImageView imageView=view.findViewById(R.id.mimage);
        ImageButton videoView=view.findViewById(R.id.VideoView);
        final Message message = list.get(position);
        RelativeLayout.LayoutParams params= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        if(message.getType().equals("text")) {
            textView.setVisibility(View.VISIBLE);
            if (uid.equals(message.getFrom())) {
                textView.setBackground(context.getResources().getDrawable(R.drawable.sender));
                textView.setTextColor(Color.WHITE);
                textView.setLayoutParams(params);
            } else {
                textView.setBackground(context.getResources().getDrawable(R.drawable.receiver));
                textView.setTextColor(Color.BLACK);
            }
            textView.setText(message.getContent());
            imageView.setVisibility(View.GONE);
        }
        else if(message.getType().equals("image"))
        {
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            Picasso.get().load(message.getContent()).resize(300,300).into(imageView);
            if (uid.equals(message.getFrom())) {
                imageView.setLayoutParams(params);
            } else {

            }
        }
        else if(message.getType().equals("video"))
        {
            textView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            if (uid.equals(message.getFrom())) {
                videoView.setLayoutParams(params);
            } else {

            }
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,PlayVideo.class);
                    intent.putExtra("video",message.getContent());
                    context.startActivity(intent);
                }
            });
        }
        return view;
    }
}
